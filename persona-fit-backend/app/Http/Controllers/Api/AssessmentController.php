<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\PhysicalLimitation;
use App\Models\UserProfile;
use App\Services\NutritionCalculatorService;
use App\Services\PersonaClassificationService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class AssessmentController extends Controller
{
    public function __construct(
        protected NutritionCalculatorService $nutritionCalculator,
        protected PersonaClassificationService $personaClassifier
    ) {}

    /**
     * Get master list of selectable physical limitations and injury flags.
     */
    public function getLimitations(): JsonResponse
    {
        $limitations = PhysicalLimitation::all();

        return response()->json([
            'status' => 'success',
            'data' => $limitations,
        ]);
    }

    /**
     * Process onboarding assessment: Biometrics + Persona Quiz + Injury Screening.
     */
    public function submitAssessment(Request $request): JsonResponse
    {
        // Normalize incoming keys for full frontend compatibility
        $input = $request->all();
        if (isset($input['activity_level'])) {
            $input['activity_level'] = match ($input['activity_level']) {
                'lightly_active', 'light' => 'light',
                'moderately_active', 'moderate' => 'moderate',
                'very_active', 'super_active', 'extremely_active' => 'very_active',
                default => 'sedentary',
            };
        }
        if (isset($input['time_available'])) {
            $input['time_available'] = match ($input['time_available']) {
                'moderate', 'medium' => 'medium',
                'long' => 'long',
                default => 'short',
            };
        }
        if (isset($input['location'])) {
            $input['location'] = match ($input['location']) {
                'home', 'dumbbells' => 'home',
                'outdoor' => 'outdoor',
                default => 'gym',
            };
        }
        if (isset($input['fitness_goal'])) {
            $input['fitness_goal'] = match ($input['fitness_goal']) {
                'muscle_gain', 'hypertrophy' => 'muscle_gain',
                'maintenance', 'endurance', 'general_health' => 'maintenance',
                default => 'fat_loss',
            };
        }

        // Assessment v2 field normalizations
        if (isset($input['workout_location'])) {
            $input['location'] = match ($input['workout_location']) {
                'home', 'Home Workout' => 'home',
                default => 'gym',
            };
            $input['workout_location'] = $input['location'];
        }
        if (isset($input['training_experience'])) {
            $input['experience'] = match (strtolower(trim($input['training_experience']))) {
                'beginner', 'beginner (< 6 months)' => 'beginner',
                'advanced', 'advanced (2+ years)' => 'advanced',
                default => 'intermediate',
            };
            $input['training_experience'] = $input['experience'];
        }
        if (!isset($input['time_available'])) {
            $input['time_available'] = 'medium';
        }
        if (!isset($input['location'])) {
            $input['location'] = 'gym';
        }
        if (!isset($input['experience'])) {
            $input['experience'] = 'intermediate';
        }
        if (!isset($input['activity_level'])) {
            $input['activity_level'] = 'moderate';
        }

        $request->merge($input);

        $validated = $request->validate([
            // 1. Biometrics & Core Goals
            'gender' => ['required', 'in:male,female'],
            'age' => ['required', 'integer', 'min:14', 'max:90'],
            'height_cm' => ['required', 'numeric', 'min:100', 'max:250'],
            'weight_kg' => ['required', 'numeric', 'min:30', 'max:300'],
            'target_weight_kg' => ['nullable', 'numeric', 'min:30', 'max:300'],
            'activity_level' => ['nullable', 'in:sedentary,light,moderate,very_active'],
            'fitness_goal' => ['required', 'in:fat_loss,maintenance,muscle_gain'],

            // 2. Workout Preferences (Assessment v2)
            'available_days' => ['nullable', 'array'],
            'available_days.*' => ['string'],
            'training_experience' => ['nullable', 'string'],
            'exercise_type' => ['nullable', 'string'],
            'workout_location' => ['nullable', 'string'],

            // Legacy fallbacks
            'time_available' => ['nullable', 'in:short,medium,long'],
            'location' => ['nullable', 'in:gym,home,outdoor'],
            'experience' => ['nullable', 'in:beginner,intermediate,advanced'],

            // 3. Physical Limitations & Joint Health
            'joint_health_status' => ['nullable', 'string'],
            'joint_limitations' => ['nullable', 'array'],
            'joint_limitations.*' => ['string'],
            'limitation_ids' => ['nullable', 'array'],
            'limitation_ids.*' => ['integer', 'exists:physical_limitations,id'],
        ]);

        $user = $request->user();

        // Standardize available days
        $availableDays = $validated['available_days'] ?? ['Monday', 'Wednesday', 'Friday'];
        if (empty($availableDays)) {
            $availableDays = ['Monday', 'Wednesday', 'Friday'];
        }

        // 1. Classify Persona Archetype
        $personaArchetype = $this->personaClassifier->classifyPersona([
            'time_available' => $validated['time_available'] ?? 'medium',
            'location' => $validated['location'] ?? ($validated['workout_location'] ?? 'gym'),
            'experience' => $validated['experience'] ?? ($validated['training_experience'] ?? 'intermediate'),
            'daily_lifestyle' => $validated['activity_level'] ?? 'moderate',
        ]);

        // 2. Calculate Metabolic & Nutrition Targets (Mifflin-St Jeor)
        $nutrition = $this->nutritionCalculator->generateFullProfile(
            $validated['gender'],
            $validated['age'],
            (float) $validated['height_cm'],
            (float) $validated['weight_kg'],
            $validated['activity_level'] ?? 'moderate',
            $validated['fitness_goal']
        );

        $jointHealthStatus = $validated['joint_health_status'] ?? 'No Limitations';
        $jointLimitations = $validated['joint_limitations'] ?? [];

        // 3. Store / Update UserProfile with Assessment v2 fields
        $profile = UserProfile::updateOrCreate(
            ['user_id' => $user->id],
            [
                'gender' => $validated['gender'],
                'age' => $validated['age'],
                'height_cm' => $validated['height_cm'],
                'weight_kg' => $validated['weight_kg'],
                'target_weight_kg' => $validated['target_weight_kg'] ?? null,
                'activity_level' => $validated['activity_level'] ?? 'moderate',
                'fitness_goal' => $validated['fitness_goal'],
                'available_days' => $availableDays,
                'training_experience' => $validated['training_experience'] ?? 'intermediate',
                'exercise_type' => $validated['exercise_type'] ?? 'compound',
                'workout_location' => $validated['workout_location'] ?? 'gym',
                'joint_health_status' => $jointHealthStatus,
                'joint_limitations' => $jointLimitations,
                'persona_archetype' => $personaArchetype,
                'daily_calories' => $nutrition['daily_calories'],
                'protein_grams' => $nutrition['protein_grams'],
                'carbs_grams' => $nutrition['carbs_grams'],
                'fat_grams' => $nutrition['fat_grams'],
                'water_ml_target' => $nutrition['water_ml_target'],
            ]
        );

        // 4. Sync User Limitations with Rule-Based Mapping
        $limitationIds = $validated['limitation_ids'] ?? [];
        if ($jointHealthStatus === 'No Limitations') {
            $limitationIds = [];
        } else {
            // Map joint limitation strings to physical limitation IDs
            if (!empty($jointLimitations)) {
                $mappedIds = PhysicalLimitation::where(function ($q) use ($jointLimitations) {
                    foreach ($jointLimitations as $item) {
                        $term = trim($item);
                        $q->orWhere('name', 'LIKE', "%{$term}%")
                          ->orWhere('affected_body_part', 'LIKE', "%{$term}%")
                          ->orWhere('code', 'LIKE', "%{$term}%");
                    }
                })->pluck('id')->toArray();
                $limitationIds = array_unique(array_merge($limitationIds, $mappedIds));
            }
        }
        $user->limitations()->sync($limitationIds);

        // 5. Build Summary Response
        $personaMeta = $this->personaClassifier->getPersonaMetadata($personaArchetype);
        $activeLimitations = $user->limitations()->get([
            'physical_limitations.id',
            'physical_limitations.code',
            'physical_limitations.name',
            'physical_limitations.affected_body_part',
        ]);

        return response()->json([
            'status' => 'success',
            'message' => 'Assessment completed successfully. Your customized routine is ready.',
            'data' => [
                'persona' => array_merge(['archetype' => $personaArchetype], $personaMeta),
                'nutrition' => $nutrition,
                'active_limitations' => $activeLimitations,
                'profile' => $profile,
            ],
        ]);
    }

    /**
     * Get user's current assessment summary.
     */
    public function getAssessmentSummary(Request $request): JsonResponse
    {
        $user = $request->user()->load(['profile', 'limitations']);

        if (!$user->profile) {
            return response()->json([
                'status' => 'error',
                'message' => 'User has not completed the onboarding assessment.',
            ], 404);
        }

        $personaMeta = $this->personaClassifier->getPersonaMetadata($user->profile->persona_archetype);

        $nutrition = $this->nutritionCalculator->generateFullProfile(
            $user->profile->gender ?? 'male',
            $user->profile->age ?? 25,
            (float) ($user->profile->height_cm ?? 175),
            (float) ($user->profile->weight_kg ?? 70),
            $user->profile->activity_level ?? 'moderate',
            $user->profile->fitness_goal ?? 'maintenance'
        );

        return response()->json([
            'status' => 'success',
            'data' => [
                'persona' => array_merge(['archetype' => $user->profile->persona_archetype], $personaMeta),
                'nutrition' => $nutrition,
                'profile' => $user->profile,
                'active_limitations' => $user->limitations,
            ],
        ]);
    }
}
