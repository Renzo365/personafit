<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\StepLog;
use App\Models\UserProfile;
use App\Models\WorkoutLog;
use Carbon\Carbon;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class ProgressController extends Controller
{
    /**
     * Get 30-day analytics dashboard (BMI, Weight, Streaks, Volume).
     */
    public function getDashboardAnalytics(Request $request): JsonResponse
    {
        $user = $request->user()->load('profile');
        $profile = $user->profile;

        // 1. BMI Calculation
        $heightM = $profile ? ($profile->height_cm / 100) : 1.70;
        $weightKg = $profile ? $profile->weight_kg : 70.0;
        $bmi = round($weightKg / ($heightM * $heightM), 1);

        $bmiCategory = match (true) {
            $bmi < 18.5 => 'Underweight',
            $bmi <= 24.9 => 'Normal weight',
            $bmi <= 29.9 => 'Overweight',
            default => 'Obesity',
        };

        // 2. Workout Metrics
        $totalWorkouts = WorkoutLog::where('user_id', $user->id)
            ->where('status', 'completed')
            ->count();

        $totalVolumeKg = WorkoutLog::where('user_id', $user->id)
            ->where('status', 'completed')
            ->sum('total_volume_kg');

        // 3. Weekly Consistency (past 7 days adherence)
        $startOfWeek = Carbon::now()->startOfWeek();
        $endOfWeek = Carbon::now()->endOfWeek();

        $completedThisWeek = WorkoutLog::where('user_id', $user->id)
            ->where('status', 'completed')
            ->whereBetween('completed_at', [$startOfWeek, $endOfWeek])
            ->count();

        $targetDays = 3; // Standard 3-day split default
        $adherencePercentage = min(100, round(($completedThisWeek / $targetDays) * 100, 0));

        // 4. Current Streak Calculation (Consecutive active days)
        $streak = $this->calculateCurrentStreak($user->id);

        return response()->json([
            'status' => 'success',
            'data' => [
                'bmi' => [
                    'score' => $bmi,
                    'category' => $bmiCategory,
                    'current_weight_kg' => $weightKg,
                    'target_weight_kg' => $profile ? $profile->target_weight_kg : null,
                ],
                'workouts' => [
                    'total_completed' => $totalWorkouts,
                    'total_volume_kg' => round($totalVolumeKg, 1),
                    'this_week_completed' => $completedThisWeek,
                    'adherence_percentage' => $adherencePercentage,
                ],
                'streak_days' => $streak,
            ],
        ]);
    }

    /**
     * Quick-update user's current weight.
     */
    public function updateWeight(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'weight_kg' => ['required', 'numeric', 'min:30', 'max:300'],
        ]);

        $user = $request->user();

        if ($user->profile) {
            $user->profile->update([
                'weight_kg' => $validated['weight_kg'],
            ]);
        }

        return response()->json([
            'status' => 'success',
            'message' => 'Weight updated successfully.',
            'data' => [
                'current_weight_kg' => $validated['weight_kg'],
            ],
        ]);
    }

    /**
     * Calculate consecutive days with activity (workout or steps > 1000).
     */
    private function calculateCurrentStreak(int $userId): int
    {
        $streak = 0;
        $currentCheckDate = Carbon::today();

        for ($i = 0; $i < 30; $i++) {
            $dateStr = $currentCheckDate->toDateString();

            $hasWorkout = WorkoutLog::where('user_id', $userId)
                ->where('status', 'completed')
                ->whereDate('completed_at', $dateStr)
                ->exists();

            $hasSteps = StepLog::where('user_id', $userId)
                ->where('log_date', $dateStr)
                ->where('step_count', '>=', 1000)
                ->exists();

            if ($hasWorkout || $hasSteps) {
                $streak++;
                $currentCheckDate->subDay();
            } else {
                // If today has no activity yet, check if yesterday was active
                if ($i === 0) {
                    $currentCheckDate->subDay();
                    continue;
                }
                break;
            }
        }

        return $streak;
    }
}
