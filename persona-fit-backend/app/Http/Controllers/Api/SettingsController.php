<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\PhysicalLimitation;
use App\Models\UserProfile;
use App\Services\PersonaClassificationService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class SettingsController extends Controller
{
    public function __construct(
        protected PersonaClassificationService $personaClassifier
    ) {}

    /**
     * Get current user settings, profile preferences, and available limitation options.
     */
    public function getSettings(Request $request): JsonResponse
    {
        $user = $request->user()->load(['profile', 'limitations']);

        $availableLimitations = PhysicalLimitation::all(['id', 'code', 'name', 'description', 'affected_body_part']);

        return response()->json([
            'status' => 'success',
            'data' => [
                'user' => [
                    'id' => $user->id,
                    'name' => $user->name,
                    'email' => $user->email,
                ],
                'profile' => $user->profile,
                'limitations' => $user->limitations,
                'available_limitations' => $availableLimitations,
            ],
        ]);
    }

    /**
     * Update workout preferences, schedule, and joint health limitations.
     * Updates ONLY future/upcoming workout recommendations while preserving completed history.
     */
    public function updatePreferences(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'workout_location' => ['required', 'string', 'in:home,gym'],
            'exercise_type' => ['required', 'string', 'in:compound,isolation'],
            'available_days' => ['required', 'array', 'min:1'],
            'available_days.*' => ['string'],
            'joint_health_status' => ['required', 'string', 'in:No Limitations,Has Limitations'],
            'joint_limitations' => ['nullable', 'array'],
            'joint_limitations.*' => ['string'],
            'limitation_ids' => ['nullable', 'array'],
            'limitation_ids.*' => ['integer', 'exists:physical_limitations,id'],
        ]);

        $user = $request->user();
        $profile = $user->profile;

        if (!$profile) {
            return response()->json([
                'status' => 'error',
                'message' => 'User profile not found. Please complete the assessment first.',
            ], 404);
        }

        $availableDays = $validated['available_days'];
        $jointHealthStatus = $validated['joint_health_status'];
        $jointLimitations = ($jointHealthStatus === 'No Limitations') ? [] : ($validated['joint_limitations'] ?? []);

        // Recalculate persona archetype based on updated preferences
        $personaArchetype = $this->personaClassifier->classifyPersona([
            'time_available' => 'medium',
            'location' => $validated['workout_location'],
            'experience' => $profile->training_experience ?? 'intermediate',
            'daily_lifestyle' => $profile->activity_level ?? 'moderate',
        ]);

        // 1. Update UserProfile
        $profile->update([
            'workout_location' => $validated['workout_location'],
            'exercise_type' => $validated['exercise_type'],
            'available_days' => $availableDays,
            'joint_health_status' => $jointHealthStatus,
            'joint_limitations' => $jointLimitations,
            'persona_archetype' => $personaArchetype,
        ]);

        // 2. Sync Physical Limitations
        $limitationIds = $validated['limitation_ids'] ?? [];
        if ($jointHealthStatus === 'No Limitations') {
            $limitationIds = [];
        } else {
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

        return response()->json([
            'status' => 'success',
            'message' => 'Preferences updated successfully. Your upcoming workout plan has been updated.',
            'data' => [
                'profile' => $profile->fresh(),
                'limitations' => $user->limitations()->get(),
            ],
        ]);
    }

    /**
     * Change user account password securely.
     */
    public function changePassword(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'current_password' => ['required', 'string'],
            'new_password' => ['required', 'string', 'min:6'],
        ], [
            'current_password.required' => 'Please enter your current password.',
            'new_password.required' => 'Please enter a new password.',
            'new_password.min' => 'New password must be at least 6 characters.',
        ]);

        $user = $request->user();

        if (!Hash::check($validated['current_password'], $user->password)) {
            throw ValidationException::withMessages([
                'current_password' => ['Your current password does not match our records.'],
            ]);
        }

        $user->update([
            'password' => Hash::make($validated['new_password']),
        ]);

        return response()->json([
            'status' => 'success',
            'message' => 'Password updated successfully.',
        ]);
    }
}
