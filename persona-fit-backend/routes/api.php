<?php

use App\Http\Controllers\Api\ActivityController;
use App\Http\Controllers\Api\AssessmentController;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\NutritionController;
use App\Http\Controllers\Api\ProgressController;
use App\Http\Controllers\Api\SettingsController;
use App\Http\Controllers\Api\WorkoutController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| PersonaFit REST API Routes
|--------------------------------------------------------------------------
*/

// ==================== 1. AUTHENTICATION (PUBLIC) ====================
Route::prefix('auth')->group(function () {
    Route::post('/register', [AuthController::class, 'register']);
    Route::post('/login', [AuthController::class, 'login']);

    // Authenticated Auth Endpoints
    Route::middleware('auth:sanctum')->group(function () {
        Route::post('/logout', [AuthController::class, 'logout']);
        Route::get('/me', [AuthController::class, 'me']);
    });
});

// Master limitations list (accessible publicly or authenticated)
Route::get('/assessment/limitations', [AssessmentController::class, 'getLimitations']);

// ==================== 2. PROTECTED MOBILE ENDPOINTS ====================
Route::middleware('auth:sanctum')->group(function () {

    // --- Onboarding & Assessment ---
    Route::prefix('assessment')->group(function () {
        Route::post('/submit', [AssessmentController::class, 'submitAssessment']);
        Route::get('/summary', [AssessmentController::class, 'getAssessmentSummary']);
    });

    // --- Workouts & Injury Prevention Engine ---
    Route::prefix('workouts')->group(function () {
        Route::get('/today', [WorkoutController::class, 'getTodayWorkout']);
        Route::get('/exercises/{exerciseId}', [WorkoutController::class, 'getExercise']);
        Route::get('/alternatives/{exerciseId}', [WorkoutController::class, 'getAlternatives']);
        Route::post('/start', [WorkoutController::class, 'startWorkout']);
        Route::post('/log-set', [WorkoutController::class, 'logSet']);
        Route::post('/complete', [WorkoutController::class, 'completeWorkout']);
        Route::get('/history', [WorkoutController::class, 'getHistory']);
    });

    // --- Nutrition & Hydration Tracker ---
    Route::prefix('nutrition')->group(function () {
        Route::get('/today', [NutritionController::class, 'getTodaySummary']);
        Route::get('/foods', [NutritionController::class, 'getFoodDatabase']);
        Route::post('/log-meal', [NutritionController::class, 'logMeal']);
        Route::post('/log-water', [NutritionController::class, 'logWater']);
    });

    // --- Native Sensor Steps & Activity ---
    Route::prefix('activity')->group(function () {
        Route::post('/sync-steps', [ActivityController::class, 'syncSteps']);
        Route::get('/steps-summary', [ActivityController::class, 'getStepsSummary']);
    });

    // --- Progress & Analytics ---
    Route::prefix('progress')->group(function () {
        Route::get('/dashboard', [ProgressController::class, 'getDashboardAnalytics']);
        Route::post('/weight', [ProgressController::class, 'updateWeight']);
    });

    // --- User Settings & Preferences ---
    Route::prefix('settings')->group(function () {
        Route::get('/', [SettingsController::class, 'getSettings']);
        Route::post('/preferences', [SettingsController::class, 'updatePreferences']);
        Route::post('/change-password', [SettingsController::class, 'changePassword']);
    });
});
