<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Exercise;
use App\Models\WorkoutLog;
use App\Models\WorkoutLogItem;
use App\Services\InjuryPrescriptionEngine;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class WorkoutController extends Controller
{
    public function __construct(
        protected InjuryPrescriptionEngine $injuryEngine
    ) {}

    /**
     * Get today's tailored, injury-screened workout routine.
     */
    public function getTodayWorkout(Request $request): JsonResponse
    {
        $user = $request->user();

        $tailoredPlan = $this->injuryEngine->getTodayTailoredPlan($user);

        if (!$tailoredPlan) {
            return response()->json([
                'status' => 'error',
                'message' => 'No workout plan available. Please complete onboarding first.',
            ], 404);
        }

        return response()->json([
            'status' => 'success',
            'data' => $tailoredPlan,
        ]);
    }

    /**
     * Get safe alternative exercises for live swapping during a workout session.
     */
    public function getAlternatives(Request $request, int $exerciseId): JsonResponse
    {
        $user = $request->user();

        $alternativesData = $this->injuryEngine->getSafeAlternatives($exerciseId, $user);

        return response()->json([
            'status' => 'success',
            'data' => $alternativesData,
        ]);
    }

    /**
     * Get single exercise details for the dedicated Exercise Detail Screen.
     */
    public function getExercise(Request $request, int $exerciseId): JsonResponse
    {
        $exercise = Exercise::with(['contraindications.limitation', 'contraindications.alternativeExercise'])->find($exerciseId);
        if (!$exercise) {
            return response()->json([
                'status' => 'error',
                'message' => 'Exercise not found',
            ], 404);
        }

        $user = $request->user();
        $isContraindicated = false;
        $contraindicationWarning = null;
        $suggestedAlternativeId = null;
        $suggestedAlternativeName = null;
        $personalizedStartingLoad = $exercise->safe_starting_load;
        $personalizedSets = $exercise->default_sets ?: 3;
        $personalizedReps = $exercise->default_reps ?: 10;
        $personalizedRest = $exercise->default_rest_seconds ?: 60;
        $safetyNotes = [];

        if ($user) {
            $profile = $user->profile;
            $experience = strtolower($profile?->training_experience ?? 'intermediate');
            $goal = strtolower($profile?->fitness_goal ?? 'general_fitness');
            $heightCm = (float) ($profile?->height_cm ?? 0);
            $weightKg = (float) ($profile?->weight_kg ?? 0);
            $bmi = ($heightCm > 0 && $weightKg > 0) ? round($weightKg / (($heightCm / 100) ** 2), 1) : null;

            // 1. Check Contraindications for user's limitations
            $userLimitationIds = $user->limitations()->pluck('physical_limitations.id')->toArray();
            if (!empty($userLimitationIds)) {
                $contra = $exercise->contraindications->first(fn($c) => in_array($c->limitation_id, $userLimitationIds));
                if ($contra) {
                    $isContraindicated = true;
                    $limitationName = $contra->limitation?->name ?? 'Reported Limitation';
                    $contraindicationWarning = "Contraindicated for {$limitationName}: " . ($contra->risk_mechanism ?: 'Joint strain risk');
                    if ($contra->alternativeExercise && $contra->alternativeExercise->is_active) {
                        $suggestedAlternativeId = $contra->alternativeExercise->id;
                        $suggestedAlternativeName = $contra->alternativeExercise->name;
                    } else {
                        // Find safe alternative for same muscle
                        $safeAlts = $this->injuryEngine->getSafeAlternatives($exercise->id, $user);
                        if (!empty($safeAlts['safe_alternatives'])) {
                            $suggestedAlternativeId = $safeAlts['safe_alternatives'][0]['id'];
                            $suggestedAlternativeName = $safeAlts['safe_alternatives'][0]['name'];
                        }
                    }
                    $safetyNotes[] = "Clinical Alert: Not recommended with active {$limitationName}. Safer alternative substitution advised.";
                }
            }

            // 2. Training Experience Safeguards
            if ($experience === 'beginner') {
                if ($exercise->difficulty === 'advanced') {
                    $safetyNotes[] = "Advanced Skill Notice: As a beginner, master regressed movement patterns before loading this exercise.";
                    $personalizedSets = min($personalizedSets, 3);
                    $personalizedReps = max($personalizedReps, 12);
                    $personalizedRest = max($personalizedRest, 75);
                }
                if ($personalizedStartingLoad && (str_contains(strtolower($personalizedStartingLoad), 'kg') || str_contains(strtolower($personalizedStartingLoad), 'lbs'))) {
                    $personalizedStartingLoad = "Conservative Baseline (Form Mastery)";
                }
            } elseif ($experience === 'advanced') {
                $personalizedSets = max($personalizedSets, 4);
            }

            // 3. Goal Calibration
            if (str_contains($goal, 'strength')) {
                $personalizedSets = max($personalizedSets, 4);
                $personalizedReps = min($personalizedReps, 6);
                $personalizedRest = max($personalizedRest, 120);
            } elseif (str_contains($goal, 'fat_loss') || str_contains($goal, 'endurance')) {
                $personalizedReps = max($personalizedReps, 15);
                $personalizedRest = min($personalizedRest, 45);
            }

            // 4. BMI joint compression check (BMI is NOT the sole determinant of safe weight)
            if ($bmi !== null && $bmi >= 30.0) {
                $safetyNotes[] = "Joint Decompression: Maintain controlled cadence to minimize axial/patellar compressive stress (BMI {$bmi}).";
            }
        }

        return response()->json([
            'status' => 'success',
            'data' => [
                'id' => $exercise->id,
                'name' => $exercise->name,
                'description' => $exercise->description,
                'target_muscle' => $exercise->target_muscle,
                'secondary_muscles' => $exercise->secondary_muscles,
                'exercise_type' => $exercise->exercise_type,
                'workout_environment' => $exercise->workout_environment,
                'equipment_type' => $exercise->equipment_type,
                'difficulty' => $exercise->difficulty,
                'instructions' => $exercise->instructions,
                'common_mistakes' => $exercise->common_mistakes,
                'injury_prevention_cues' => $exercise->injury_prevention_cues,
                'required_equipment' => $exercise->required_equipment ?: ucfirst($exercise->equipment_type),
                'common_injuries' => $exercise->common_injuries,
                'safer_alternatives_text' => $exercise->safer_alternatives_text,
                'recommendation_rules' => $exercise->recommendation_rules,
                'is_active' => (bool) $exercise->is_active,
                'image_url' => $exercise->image_url,
                // Guide System Fields
                'proper_form_asset' => $exercise->proper_form_asset ?: $exercise->image_url,
                'common_mistakes_asset' => $exercise->common_mistakes_asset,
                'injury_prevention_asset' => $exercise->injury_prevention_asset,
                'anatomy_view' => $exercise->anatomy_view ?: 'dual',
                'primary_muscle_regions' => $exercise->primary_muscle_regions ?: [],
                'secondary_muscle_regions' => $exercise->secondary_muscle_regions ?: [],
                'muscle_mapping_confidence' => $exercise->muscle_mapping_confidence ?: 'high',
                'default_sets' => $exercise->default_sets,
                'default_reps' => $exercise->default_reps,
                'safe_starting_load' => $exercise->safe_starting_load,
                'default_rest_seconds' => $exercise->default_rest_seconds,
                'breathing_tips' => $exercise->breathing_tips,
                'benefits' => $exercise->benefits,
                'muscle_explanation' => $exercise->muscle_explanation,
                // Personalized / Contraindication Screen Fields
                'is_contraindicated' => $isContraindicated,
                'contraindication_warning' => $contraindicationWarning,
                'suggested_alternative_id' => $suggestedAlternativeId,
                'suggested_alternative_name' => $suggestedAlternativeName,
                'personalized_starting_load' => $personalizedStartingLoad,
                'personalized_sets' => $personalizedSets,
                'personalized_reps' => $personalizedReps,
                'personalized_rest_seconds' => $personalizedRest,
                'personalized_safety_notes' => $safetyNotes,
            ],
        ]);
    }

    /**
     * Initialize a new live workout session.
     */
    public function startWorkout(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'workout_plan_id' => ['nullable', 'exists:workout_plans,id'],
            'session_name' => ['nullable', 'string', 'max:150'],
        ]);

        $user = $request->user();

        $log = WorkoutLog::create([
            'user_id' => $user->id,
            'workout_plan_id' => $validated['workout_plan_id'] ?? null,
            'session_name' => $validated['session_name'] ?? 'Workout Session',
            'started_at' => now(),
            'status' => 'in_progress',
        ]);

        return response()->json([
            'status' => 'success',
            'message' => 'Workout session started.',
            'data' => $log,
        ], 201);
    }

    /**
     * Record a completed set for an exercise.
     */
    public function logSet(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'workout_log_id' => ['required', 'exists:workout_logs,id'],
            'exercise_id' => ['required', 'exists:exercises,id'],
            'set_number' => ['required', 'integer', 'min:1'],
            'weight_kg' => ['required', 'numeric', 'min:0'],
            'reps_completed' => ['required', 'integer', 'min:1'],
        ]);

        $item = WorkoutLogItem::updateOrCreate(
            [
                'workout_log_id' => $validated['workout_log_id'],
                'exercise_id' => $validated['exercise_id'],
                'set_number' => $validated['set_number'],
            ],
            [
                'weight_kg' => $validated['weight_kg'],
                'reps_completed' => $validated['reps_completed'],
                'is_completed' => true,
            ]
        );

        return response()->json([
            'status' => 'success',
            'message' => 'Set recorded.',
            'data' => $item,
        ]);
    }

    /**
     * Finalize and complete the workout session.
     */
    public function completeWorkout(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'workout_log_id' => ['required', 'exists:workout_logs,id'],
            'total_duration_seconds' => ['required', 'integer', 'min:0'],
            'completed_exercise_ids' => ['nullable', 'array'],
            'completed_exercise_ids.*' => ['integer', 'exists:exercises,id'],
            'total_exercises_completed' => ['nullable', 'integer', 'min:0'],
            'notes' => ['nullable', 'string'],
        ]);

        $log = WorkoutLog::with('items')->findOrFail($validated['workout_log_id']);

        // Check if already completed to prevent accidental duplicate session records
        if ($log->status === 'completed') {
            return response()->json([
                'status' => 'success',
                'message' => 'Workout session was already completed.',
                'data' => [
                    'workout_log_id' => $log->id,
                    'session_name' => $log->session_name,
                    'duration_minutes' => round($log->total_duration_seconds / 60, 1),
                    'exercises_completed' => $log->total_exercises_completed,
                    'total_volume_kg' => $log->total_volume_kg,
                    'completed_at' => $log->completed_at,
                ],
            ]);
        }

        // If completed_exercise_ids are provided from simplified session tracking
        $completedExerciseIds = $validated['completed_exercise_ids'] ?? [];
        if (!empty($completedExerciseIds)) {
            foreach ($completedExerciseIds as $exId) {
                WorkoutLogItem::firstOrCreate([
                    'workout_log_id' => $log->id,
                    'exercise_id' => $exId,
                    'set_number' => 1,
                ], [
                    'weight_kg' => 0,
                    'reps_completed' => 10,
                    'is_completed' => true,
                ]);
            }
            $log->load('items');
        }

        // Calculate aggregate statistics
        $recordedUniqueExercises = $log->items->pluck('exercise_id')->unique()->count();
        $totalExercisesCompleted = $validated['total_exercises_completed'] 
            ?? max($recordedUniqueExercises, count($completedExerciseIds));
        $totalVolume = $log->items->sum(fn($i) => $i->weight_kg * $i->reps_completed);

        $log->update([
            'completed_at' => now(),
            'total_duration_seconds' => $validated['total_duration_seconds'],
            'status' => 'completed',
            'total_exercises_completed' => $totalExercisesCompleted,
            'total_volume_kg' => $totalVolume,
            'notes' => $validated['notes'] ?? null,
        ]);

        return response()->json([
            'status' => 'success',
            'message' => 'Workout completed! Great effort.',
            'data' => [
                'workout_log_id' => $log->id,
                'session_name' => $log->session_name,
                'duration_minutes' => round($log->total_duration_seconds / 60, 1),
                'exercises_completed' => $totalExercisesCompleted,
                'total_volume_kg' => $totalVolume,
                'completed_at' => $log->completed_at,
            ],
        ]);
    }

    /**
     * Get user workout history (paginated).
     */
    public function getHistory(Request $request): JsonResponse
    {
        $logs = WorkoutLog::where('user_id', $request->user()->id)
            ->where('status', 'completed')
            ->with(['items.exercise'])
            ->orderByDesc('completed_at')
            ->paginate(15);

        return response()->json([
            'status' => 'success',
            'data' => $logs,
        ]);
    }
}
