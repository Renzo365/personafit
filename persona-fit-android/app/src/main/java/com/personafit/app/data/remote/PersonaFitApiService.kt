package com.personafit.app.data.remote

import com.personafit.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PersonaFitApiService {

    // ==================== AUTH ====================
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthData>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthData>>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET("auth/me")
    suspend fun getMe(): Response<ApiResponse<UserDto>>

    // ==================== ASSESSMENT & LIMITATIONS ====================
    @GET("assessment/limitations")
    suspend fun getLimitations(): Response<ApiResponse<List<LimitationDto>>>

    @POST("assessment/submit")
    suspend fun submitAssessment(@Body request: AssessmentSubmitRequest): Response<ApiResponse<AssessmentSummaryData>>

    @GET("assessment/summary")
    suspend fun getAssessmentSummary(): Response<ApiResponse<AssessmentSummaryData>>

    // ==================== WORKOUTS & CONTRAINDICATIONS ====================
    @GET("workouts/today")
    suspend fun getTodayWorkout(): Response<ApiResponse<TailoredPlanData>>

    @GET("workouts/exercises/{exerciseId}")
    suspend fun getExercise(@Path("exerciseId") exerciseId: Long): Response<ApiResponse<ExerciseDetailDto>>

    @GET("workouts/alternatives/{exerciseId}")
    suspend fun getSafeAlternatives(@Path("exerciseId") exerciseId: Long): Response<ApiResponse<SafeAlternativesData>>

    @POST("workouts/start")
    suspend fun startWorkout(@Body request: StartWorkoutRequest): Response<ApiResponse<StartWorkoutResponse>>

    @POST("workouts/log-set")
    suspend fun logSet(@Body request: LogSetRequest): Response<ApiResponse<Unit>>

    @POST("workouts/complete")
    suspend fun completeWorkout(@Body request: CompleteWorkoutRequest): Response<ApiResponse<CompleteWorkoutResponse>>

    // ==================== NUTRITION & WATER ====================
    @GET("nutrition/today")
    suspend fun getTodayNutrition(): Response<ApiResponse<NutritionSummaryData>>

    @POST("nutrition/log-meal")
    suspend fun logMeal(@Body request: LogMealRequest): Response<ApiResponse<MealLogDto>>

    @POST("nutrition/log-water")
    suspend fun logWater(@Body request: LogWaterRequest): Response<ApiResponse<LogWaterResponse>>

    // ==================== ACTIVITY & SENSORS ====================
    @POST("activity/sync-steps")
    suspend fun syncSteps(@Body request: SyncStepsRequest): Response<ApiResponse<StepLogDto>>

    @GET("activity/steps-summary")
    suspend fun getStepsSummary(): Response<ApiResponse<StepSummaryData>>

    // ==================== PROGRESS & ANALYTICS ====================
    @GET("progress/dashboard")
    suspend fun getDashboardAnalytics(): Response<ApiResponse<ProgressDashboardData>>

    // ==================== SETTINGS & PREFERENCES ====================
    @GET("settings")
    suspend fun getSettings(): Response<ApiResponse<SettingsData>>

    @POST("settings/preferences")
    suspend fun updatePreferences(@Body request: UpdatePreferencesRequest): Response<ApiResponse<UpdatePreferencesResponse>>

    @POST("settings/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ApiResponse<Unit>>
}
