<?php

namespace App\Services;

use App\Models\Exercise;
use App\Models\ExerciseContraindication;
use App\Models\User;
use App\Models\WorkoutPlan;
use App\Models\WorkoutPlanExercise;

class InjuryPrescriptionEngine
{
    /**
     * Inspect a Workout Plan and apply rule-based injury contraindication,
     * environment, exercise type, and BMI safety filtering.
     * Returns an array of exercises with safe substitutions and safety flags.
     */
    public function filterAndPrescribePlan(WorkoutPlan $plan, User $user): array
    {
        $profile = $user->profile;
        $workoutLocation = strtolower($profile?->workout_location ?? 'gym');
        $exercisePreference = strtolower($profile?->exercise_type ?? 'compound');
        $trainingExperience = strtolower($profile?->training_experience ?? 'intermediate');
        
        // Calculate user BMI if height and weight are provided
        $heightCm = (float) ($profile?->height_cm ?? 0);
        $weightKg = (float) ($profile?->weight_kg ?? 0);
        $bmi = ($heightCm > 0 && $weightKg > 0) ? round($weightKg / (($heightCm / 100) ** 2), 1) : null;

        $userLimitationIds = $user->limitations()->pluck('physical_limitations.id')->toArray();
        $planExercises = $plan->exercises()->with('exercise')->get();

        $processedExercises = [];
        $appliedSubstitutions = [];

        foreach ($planExercises as $planItem) {
            $currentExercise = $planItem->exercise;
            $originalExercise = $currentExercise;
            $substituted = false;
            $subReason = null;
            $subFlag = null;

            // 0. RETIRED / INACTIVE EXERCISE SAFEGUARD
            // If the exercise in the plan was archived/deactivated, substitute with an active movement
            if (!$currentExercise->is_active) {
                $activeReplacement = $this->findActiveCatalogReplacement($currentExercise, $workoutLocation, $userLimitationIds);
                if ($activeReplacement && $activeReplacement->id !== $currentExercise->id) {
                    $currentExercise = $activeReplacement;
                    $substituted = true;
                    $subReason = 'Upgraded from legacy archive to current active catalog movement';
                    $subFlag = 'Catalog Modernization';
                }
            }

            // 1. RULE-BASED INJURY CONTRAINDICATION SCREENING
            $contraindication = ExerciseContraindication::where('exercise_id', $currentExercise->id)
                ->whereIn('limitation_id', $userLimitationIds)
                ->with(['limitation', 'alternativeExercise'])
                ->first();

            if ($contraindication && $contraindication->alternativeExercise && $contraindication->alternativeExercise->is_active) {
                $currentExercise = $contraindication->alternativeExercise;
                $substituted = true;
                $subReason = $contraindication->risk_mechanism;
                $subFlag = $contraindication->limitation->name;
            } elseif ($contraindication) {
                // If no active direct alternative was mapped, find an active injury-safe alternative
                $safeAlt = $this->findSafeAlternativeForMuscle($currentExercise, $userLimitationIds, $workoutLocation);
                if ($safeAlt && $safeAlt->id !== $currentExercise->id) {
                    $currentExercise = $safeAlt;
                    $substituted = true;
                    $subReason = $contraindication->risk_mechanism ?: 'Injury safeguard substitution for ' . $contraindication->limitation->name;
                    $subFlag = $contraindication->limitation->name;
                }
            }

            // 2. RULE-BASED ENVIRONMENT ADAPTATION (HOME VS GYM)
            $isNotHomeCompatible = ($workoutLocation === 'home') && (
                $currentExercise->workout_environment === 'gym' ||
                !in_array($currentExercise->equipment_type, ['dumbbell', 'bodyweight', 'bands'])
            );

            if ($isNotHomeCompatible) {
                $homeAlt = $this->findHomeAlternative($currentExercise, $userLimitationIds, $exercisePreference);
                if ($homeAlt && $homeAlt->id !== $currentExercise->id) {
                    $currentExercise = $homeAlt;
                    $substituted = true;
                    $subReason = $subReason ?: 'Adapted for Home Workout environment (bodyweight/dumbbells)';
                    $subFlag = $subFlag ?: 'Home Equipment Adaptation';
                }
            }

            // 3. RULE-BASED EXERCISE TYPE PREFERENCE (ISOLATION VS COMPOUND)
            if ($exercisePreference === 'isolation' && ($currentExercise->exercise_type === 'compound' || $this->isHeavyCompound($currentExercise))) {
                $isolationAlt = $this->findIsolationAlternative($currentExercise, $userLimitationIds, $workoutLocation);
                if ($isolationAlt && $isolationAlt->id !== $currentExercise->id) {
                    $currentExercise = $isolationAlt;
                    $substituted = true;
                    $subReason = $subReason ?: 'Target muscle isolation preference';
                    $subFlag = $subFlag ?: 'Isolation Movement Selection';
                }
            }

            // 4. RULE-BASED BMI & JOINT COMPRESSION SAFEGUARD (BMI >= 30)
            if ($bmi !== null && $bmi >= 30.0) {
                $bmiRegression = $this->findBmiRegression($currentExercise, $workoutLocation, $userLimitationIds);
                if ($bmiRegression && $bmiRegression->id !== $currentExercise->id) {
                    $currentExercise = $bmiRegression;
                    $substituted = true;
                    $subReason = 'Adapted to low-impact variation to minimize joint compression and patellofemoral shear (BMI ' . $bmi . ')';
                    $subFlag = 'Joint Compression / High BMI Safeguard';
                }
            }

            // 5. RULE-BASED EXPERIENCE LEVEL REGRESSION (Beginner avoiding Advanced movements)
            if ($trainingExperience === 'beginner' && $currentExercise->difficulty === 'advanced') {
                $beginnerRegression = $this->findDifficultyRegression($currentExercise, $workoutLocation, $userLimitationIds);
                if ($beginnerRegression && $beginnerRegression->id !== $currentExercise->id) {
                    $currentExercise = $beginnerRegression;
                    $substituted = true;
                    $subReason = 'Regressed from advanced skill requirement to beginner-friendly movement';
                    $subFlag = 'Experience Level Adaptation';
                }
            }

            if ($substituted && $originalExercise->id !== $currentExercise->id) {
                $appliedSubstitutions[] = [
                    'original' => $originalExercise->name,
                    'substituted_with' => $currentExercise->name,
                    'reason' => $subReason,
                    'triggered_by' => $subFlag,
                ];
            }

            $processedExercises[] = [
                'plan_exercise_id' => $planItem->id,
                'order' => $planItem->order_in_routine,
                'exercise' => $currentExercise,
                'target_sets' => $planItem->target_sets,
                'target_reps_min' => $planItem->target_reps_min,
                'target_reps_max' => $planItem->target_reps_max,
                'rest_seconds' => $planItem->rest_seconds,
                'is_substituted' => $substituted,
                'original_exercise' => ($substituted && $originalExercise->id !== $currentExercise->id) ? [
                    'id' => $originalExercise->id,
                    'name' => $originalExercise->name,
                ] : null,
                'substitution_reason' => $subReason,
                'limitation_flag' => $subFlag,
            ];
        }

        return [
            'plan_id' => $plan->id,
            'plan_name' => $plan->name,
            'focus_area' => $plan->focus_area,
            'estimated_duration_minutes' => $plan->estimated_duration_minutes,
            'persona_archetype' => $plan->persona_archetype,
            'total_exercises' => count($processedExercises),
            'substitutions_applied_count' => count($appliedSubstitutions),
            'substitutions_applied' => $appliedSubstitutions,
            'exercises' => $processedExercises,
        ];
    }

    /**
     * Get safe alternative exercises when a user requests a live swap during workout.
     */
    public function getSafeAlternatives(int $exerciseId, User $user): array
    {
        $exercise = Exercise::findOrFail($exerciseId);
        $userLimitationIds = $user->limitations()->pluck('physical_limitations.id')->toArray();

        // 1. Check direct mapped alternatives in contraindication rules
        $mappedAlternativeIds = ExerciseContraindication::where('exercise_id', $exerciseId)
            ->whereNotNull('alternative_exercise_id')
            ->pluck('alternative_exercise_id')
            ->toArray();

        // 2. Find other active exercises for the same target muscle group
        $sameMuscleExercises = Exercise::active()
            ->where('target_muscle', $exercise->target_muscle)
            ->where('id', '!=', $exerciseId)
            ->get();

        $safeAlternatives = [];

        foreach ($sameMuscleExercises as $candidate) {
            // Ensure the candidate itself is not contraindicated for the user
            $isCandidateUnsafe = ExerciseContraindication::where('exercise_id', $candidate->id)
                ->whereIn('limitation_id', $userLimitationIds)
                ->exists();

            if (!$isCandidateUnsafe) {
                $isDirectRecommendation = in_array($candidate->id, $mappedAlternativeIds);

                $safeAlternatives[] = [
                    'id' => $candidate->id,
                    'name' => $candidate->name,
                    'description' => $candidate->description,
                    'target_muscle' => $candidate->target_muscle,
                    'exercise_type' => $candidate->exercise_type,
                    'workout_environment' => $candidate->workout_environment,
                    'equipment_type' => $candidate->equipment_type,
                    'difficulty' => $candidate->difficulty,
                    'instructions' => $candidate->instructions,
                    'injury_prevention_cues' => $candidate->injury_prevention_cues,
                    'is_top_recommendation' => $isDirectRecommendation,
                ];
            }
        }

        // Sort so top recommendations appear first
        usort($safeAlternatives, fn($a, $b) => $b['is_top_recommendation'] <=> $a['is_top_recommendation']);

        return [
            'original_exercise' => [
                'id' => $exercise->id,
                'name' => $exercise->name,
                'description' => $exercise->description,
                'target_muscle' => $exercise->target_muscle,
                'exercise_type' => $exercise->exercise_type,
                'workout_environment' => $exercise->workout_environment,
            ],
            'safe_alternatives_count' => count($safeAlternatives),
            'safe_alternatives' => $safeAlternatives,
        ];
    }

    /**
     * Get today's tailored plan for a user according to their weekly availability schedule.
     */
    public function getTodayTailoredPlan(User $user): ?array
    {
        $profile = $user->profile;
        $persona = $profile ? $profile->persona_archetype : 'busy_pro';

        $dayOrder = [
            'Monday' => 1,
            'Tuesday' => 2,
            'Wednesday' => 3,
            'Thursday' => 4,
            'Friday' => 5,
            'Saturday' => 6,
            'Sunday' => 7,
        ];

        // Retrieve available days from user profile
        $availableDays = $profile?->available_days;
        if (!is_array($availableDays) || empty($availableDays)) {
            $availableDays = ['Monday', 'Wednesday', 'Friday'];
        }

        // Standardize capitalization and sort by Monday -> Sunday
        $cleanedDays = [];
        foreach ($availableDays as $d) {
            $normalized = ucfirst(strtolower(trim($d)));
            if (isset($dayOrder[$normalized]) && !in_array($normalized, $cleanedDays)) {
                $cleanedDays[] = $normalized;
            }
        }
        usort($cleanedDays, fn($a, $b) => $dayOrder[$a] <=> $dayOrder[$b]);
        if (empty($cleanedDays)) {
            $cleanedDays = ['Monday', 'Wednesday', 'Friday'];
        }

        // Today's day name in English (e.g. "Tuesday")
        $todayName = date('l');
        $todayIndex = array_search($todayName, $cleanedDays);

        // RULE: The system must NEVER schedule a workout on a day that the user did not select.
        if ($todayIndex === false) {
            // Today is a designated Rest Day!
            $currentDayNum = (int) date('N');
            $nextWorkoutDay = null;
            foreach ($cleanedDays as $candidate) {
                if ($dayOrder[$candidate] > $currentDayNum) {
                    $nextWorkoutDay = $candidate;
                    break;
                }
            }
            if (!$nextWorkoutDay) {
                $nextWorkoutDay = $cleanedDays[0];
            }

            return [
                'plan_id' => null,
                'is_rest_day' => true,
                'plan_name' => 'Rest & Active Recovery',
                'focus_area' => 'Restoration, Joint Decompression & Hydration',
                'estimated_duration_minutes' => 0,
                'persona_archetype' => $persona,
                'total_exercises' => 0,
                'substitutions_applied_count' => 0,
                'substitutions_applied' => [],
                'exercises' => [],
                'available_days' => $cleanedDays,
                'today_day_name' => $todayName,
                'next_workout_day' => $nextWorkoutDay,
                'rest_day_message' => "Today is a designated rest day based on your weekly schedule. Your next workout session is on {$nextWorkoutDay}.",
            ];
        }

        // If today IS an available workout day:
        $workoutDayNumber = $todayIndex + 1;
        $totalWorkoutDays = count($cleanedDays);

        // Get template plans for this persona
        $personaPlans = WorkoutPlan::where('persona_archetype', $persona)
            ->where('is_template', true)
            ->orderBy('day_of_week')
            ->get();

        if ($personaPlans->isEmpty()) {
            $personaPlans = WorkoutPlan::where('is_template', true)
                ->orderBy('day_of_week')
                ->get();
        }

        if ($personaPlans->isEmpty()) {
            return null;
        }

        // Map Workout Day Number to the available routine templates
        $selectedTemplate = $personaPlans[$todayIndex % $personaPlans->count()];

        $prescribed = $this->filterAndPrescribePlan($selectedTemplate, $user);
        $prescribed['is_rest_day'] = false;
        $prescribed['workout_day_number'] = $workoutDayNumber;
        $prescribed['total_workout_days'] = $totalWorkoutDays;
        $prescribed['day_name'] = $todayName;
        $prescribed['available_days'] = $cleanedDays;
        $prescribed['plan_name'] = "{$selectedTemplate->name} (Workout Day {$workoutDayNumber} of {$totalWorkoutDays})";

        return $prescribed;
    }

    /**
     * Find a safe home alternative (bodyweight/dumbbell) for an exercise.
     */
    protected function findHomeAlternative(Exercise $exercise, array $userLimitationIds, string $preference = 'compound'): ?Exercise
    {
        $candidateQuery = Exercise::active()
            ->where('target_muscle', $exercise->target_muscle)
            ->where(function ($q) {
                $q->whereIn('workout_environment', ['home', 'both'])
                  ->orWhereIn('equipment_type', ['dumbbell', 'bodyweight', 'bands']);
            })
            ->where('id', '!=', $exercise->id);

        if (!empty($userLimitationIds)) {
            $contraindicatedIds = ExerciseContraindication::whereIn('limitation_id', $userLimitationIds)
                ->pluck('exercise_id')
                ->toArray();
            $candidateQuery->whereNotIn('id', $contraindicatedIds);
        }

        $candidates = $candidateQuery->get();
        if ($candidates->isEmpty()) {
            return null;
        }

        if ($preference === 'isolation') {
            $isolation = $candidates->first(fn($c) => $c->exercise_type === 'isolation' || !$this->isHeavyCompound($c));
            if ($isolation) return $isolation;
        } elseif ($preference === 'compound') {
            $compound = $candidates->first(fn($c) => $c->exercise_type === 'compound' || $this->isHeavyCompound($c));
            if ($compound) return $compound;
        }

        return $candidates->first();
    }

    /**
     * Find a safe isolation alternative for an exercise.
     */
    protected function findIsolationAlternative(Exercise $exercise, array $userLimitationIds, string $location = 'gym'): ?Exercise
    {
        $allowedEquipment = ($location === 'home') ? ['dumbbell', 'bodyweight', 'bands'] : ['dumbbell', 'bodyweight', 'cable', 'machine', 'barbell', 'bands'];

        $candidateQuery = Exercise::active()
            ->where('target_muscle', $exercise->target_muscle)
            ->whereIn('equipment_type', $allowedEquipment)
            ->where('id', '!=', $exercise->id);

        if ($location === 'home') {
            $candidateQuery->whereIn('workout_environment', ['home', 'both']);
        }

        if (!empty($userLimitationIds)) {
            $contraindicatedIds = ExerciseContraindication::whereIn('limitation_id', $userLimitationIds)
                ->pluck('exercise_id')
                ->toArray();
            $candidateQuery->whereNotIn('id', $contraindicatedIds);
        }

        $candidates = $candidateQuery->get();
        return $candidates->first(fn($c) => $c->exercise_type === 'isolation' || !$this->isHeavyCompound($c)) ?? null;
    }

    /**
     * Find an active replacement for an archived legacy exercise.
     */
    protected function findActiveCatalogReplacement(Exercise $exercise, string $location, array $userLimitationIds): ?Exercise
    {
        $query = Exercise::active()
            ->where('target_muscle', $exercise->target_muscle)
            ->where('id', '!=', $exercise->id);

        if ($location === 'home') {
            $query->whereIn('workout_environment', ['home', 'both']);
        }

        if (!empty($userLimitationIds)) {
            $contraindicatedIds = ExerciseContraindication::whereIn('limitation_id', $userLimitationIds)
                ->pluck('exercise_id')
                ->toArray();
            $query->whereNotIn('id', $contraindicatedIds);
        }

        return $query->first();
    }

    /**
     * Find a safe general alternative for a muscle group avoiding specified user limitations.
     */
    protected function findSafeAlternativeForMuscle(Exercise $exercise, array $userLimitationIds, string $location): ?Exercise
    {
        $query = Exercise::active()
            ->where('target_muscle', $exercise->target_muscle)
            ->where('id', '!=', $exercise->id);

        if ($location === 'home') {
            $query->whereIn('workout_environment', ['home', 'both']);
        }

        if (!empty($userLimitationIds)) {
            $contraindicatedIds = ExerciseContraindication::whereIn('limitation_id', $userLimitationIds)
                ->pluck('exercise_id')
                ->toArray();
            $query->whereNotIn('id', $contraindicatedIds);
        }

        return $query->first();
    }

    /**
     * Find a regression for individuals with BMI >= 30 to alleviate deep joint shear.
     */
    protected function findBmiRegression(Exercise $exercise, string $location, array $userLimitationIds): ?Exercise
    {
        $name = strtolower($exercise->name);

        // High knee shear / deep squats -> Box Squats or Leg Press
        if (str_contains($name, 'squat') || str_contains($name, 'lunge') || str_contains($name, 'split squat')) {
            if ($name === 'chair / box squats' || $name === 'leg press machine') {
                return null; // Already safe
            }
            if ($location === 'home') {
                return Exercise::active()->where('name', 'Chair / Box Squats')->first() 
                    ?: Exercise::active()->where('name', 'Glute Bridges')->first();
            } else {
                return Exercise::active()->where('name', 'Leg Press Machine')->first() 
                    ?: Exercise::active()->where('name', 'Goblet Squat (Dumbbell/Kettlebell)')->first();
            }
        }

        // Heavy floor push-ups / plyometric push-ups -> Incline / Knee Push-ups or Chest Press Machine
        if (str_contains($name, 'push-up') || str_contains($name, 'push up')) {
            if ($name === 'incline / knee push-ups' || $name === 'chest press machine') {
                return null;
            }
            if ($location === 'home') {
                return Exercise::active()->where('name', 'Incline / Knee Push-ups')->first();
            } else {
                return Exercise::active()->where('name', 'Chest Press Machine')->first()
                    ?: Exercise::active()->where('name', 'Incline / Knee Push-ups')->first();
            }
        }

        // Dips / Pull-ups -> supported regressions
        if (str_contains($name, 'dip')) {
            return Exercise::active()->where('name', 'Standing Dumbbell Tricep Overhead Extension')->first()
                ?: Exercise::active()->where('name', 'Incline / Knee Push-ups')->first();
        }

        if (str_contains($name, 'pull-up') || str_contains($name, 'chin-up')) {
            if ($location === 'gym') {
                return Exercise::active()->where('name', 'Lat Pulldown Machine')->first();
            } else {
                return Exercise::active()->where('name', 'Doorframe / Towel Rows')->first();
            }
        }

        return null;
    }

    /**
     * Regress advanced movements for beginners.
     */
    protected function findDifficultyRegression(Exercise $exercise, string $location, array $userLimitationIds): ?Exercise
    {
        $candidateQuery = Exercise::active()
            ->where('target_muscle', $exercise->target_muscle)
            ->whereIn('difficulty', ['beginner', 'intermediate'])
            ->where('id', '!=', $exercise->id);

        if ($location === 'home') {
            $candidateQuery->whereIn('workout_environment', ['home', 'both']);
        }

        if (!empty($userLimitationIds)) {
            $contraindicatedIds = ExerciseContraindication::whereIn('limitation_id', $userLimitationIds)
                ->pluck('exercise_id')
                ->toArray();
            $candidateQuery->whereNotIn('id', $contraindicatedIds);
        }

        return $candidateQuery->first();
    }

    /**
     * Determine if an exercise is considered a multi-joint / heavy compound movement.
     */
    protected function isHeavyCompound(Exercise $exercise): bool
    {
        $compoundKeywords = ['bench', 'squat', 'deadlift', 'press', 'row', 'lunge', 'push-up', 'pull'];
        $name = strtolower($exercise->name);
        foreach ($compoundKeywords as $kw) {
            if (str_contains($name, $kw) && !str_contains($name, 'curl') && !str_contains($name, 'lateral raise') && !str_contains($name, 'fly') && !str_contains($name, 'pushdown') && !str_contains($name, 'wall sit')) {
                return true;
            }
        }
        return false;
    }
}
