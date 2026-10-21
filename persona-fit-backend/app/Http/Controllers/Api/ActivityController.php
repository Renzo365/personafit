<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\StepLog;
use Carbon\Carbon;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class ActivityController extends Controller
{
    /**
     * Sync daily step count from Android Hardware Sensor.
     */
    public function syncSteps(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'step_count' => ['required', 'integer', 'min:0'],
            'log_date' => ['nullable', 'date'],
        ]);

        $user = $request->user()->load('profile');
        $date = $validated['log_date'] ?? Carbon::today()->toDateString();
        $stepCount = $validated['step_count'];

        // Stride Length estimation: Height (cm) * 0.415
        $heightCm = $user->profile ? $user->profile->height_cm : 170.0;
        $strideLengthMeters = ($heightCm * 0.415) / 100;
        $distanceKm = round(($stepCount * $strideLengthMeters) / 1000, 2);

        // Approximate energy expenditure: 0.04 kcal per step
        $caloriesBurned = round($stepCount * 0.04, 1);

        $stepLog = StepLog::updateOrCreate(
            [
                'user_id' => $user->id,
                'log_date' => $date,
            ],
            [
                'step_count' => $stepCount,
                'distance_km' => $distanceKm,
                'calories_burned' => $caloriesBurned,
            ]
        );

        return response()->json([
            'status' => 'success',
            'message' => 'Steps synchronized successfully.',
            'data' => $stepLog,
        ]);
    }

    /**
     * Get step tracking summary for today and past 7 days.
     */
    public function getStepsSummary(Request $request): JsonResponse
    {
        $user = $request->user();
        $today = Carbon::today()->toDateString();
        $sevenDaysAgo = Carbon::today()->subDays(6)->toDateString();

        $todayLog = StepLog::where('user_id', $user->id)
            ->whereDate('log_date', $today)
            ->first();

        $weeklyLogs = StepLog::where('user_id', $user->id)
            ->whereDate('log_date', '>=', $sevenDaysAgo)
            ->whereDate('log_date', '<=', $today)
            ->orderBy('log_date')
            ->get();

        $avgSteps = $weeklyLogs->count() > 0 ? (int) round($weeklyLogs->avg('step_count')) : 0;

        return response()->json([
            'status' => 'success',
            'data' => [
                'today' => $todayLog ?? [
                    'log_date' => $today,
                    'step_count' => 0,
                    'distance_km' => 0,
                    'calories_burned' => 0,
                ],
                'weekly_average_steps' => $avgSteps,
                'history_7_days' => $weeklyLogs,
            ],
        ]);
    }
}
