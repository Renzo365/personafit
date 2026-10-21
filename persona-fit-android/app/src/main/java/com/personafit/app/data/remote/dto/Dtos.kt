package com.personafit.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Generic API Envelope
data class ApiResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?
)

// ==================== AUTH DTOs ====================
data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class AuthData(
    @SerializedName("user") val user: UserDto,
    @SerializedName("token") val token: String
)

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("has_completed_onboarding") val hasCompletedOnboarding: Boolean,
    @SerializedName("persona") val persona: String?
)

// ==================== ASSESSMENT DTOs ====================
data class LimitationDto(
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("affected_body_part") val affectedBodyPart: String?
)

data class AssessmentSubmitRequest(
    @SerializedName("gender") val gender: String,
    @SerializedName("age") val age: Int,
    @SerializedName("height_cm") val heightCm: Float,
    @SerializedName("weight_kg") val weightKg: Float,
    @SerializedName("target_weight_kg") val targetWeightKg: Float?,
    @SerializedName("activity_level") val activityLevel: String = "moderate",
    @SerializedName("fitness_goal") val fitnessGoal: String,
    @SerializedName("available_days") val availableDays: List<String> = emptyList(),
    @SerializedName("training_experience") val trainingExperience: String = "intermediate",
    @SerializedName("exercise_type") val exerciseType: String = "compound",
    @SerializedName("workout_location") val workoutLocation: String = "gym",
    @SerializedName("joint_health_status") val jointHealthStatus: String = "No Limitations",
    @SerializedName("joint_limitations") val jointLimitations: List<String> = emptyList(),
    @SerializedName("time_available") val timeAvailable: String = "medium",
    @SerializedName("location") val location: String = "gym",
    @SerializedName("experience") val experience: String = "intermediate",
    @SerializedName("limitation_ids") val limitationIds: List<Long> = emptyList()
)

data class AssessmentSummaryData(
    @SerializedName("persona") val persona: PersonaMetaDto,
    @SerializedName("nutrition") val nutrition: NutritionTargetDto,
    @SerializedName("active_limitations") val activeLimitations: List<LimitationDto>,
    @SerializedName("profile") val profile: ProfileSummaryDto? = null
)

data class ProfileSummaryDto(
    @SerializedName("workout_location") val workoutLocation: String? = null,
    @SerializedName("exercise_type") val exerciseType: String? = null,
    @SerializedName("available_days") val availableDays: List<String>? = null,
    @SerializedName("training_experience") val trainingExperience: String? = null,
    @SerializedName("joint_health_status") val jointHealthStatus: String? = null,
    @SerializedName("joint_limitations") val jointLimitations: List<String>? = null,
    @SerializedName("weight_kg") val weightKg: Float? = null,
    @SerializedName("height_cm") val heightCm: Float? = null,
    @SerializedName("fitness_goal") val fitnessGoal: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("persona_archetype") val personaArchetype: String? = null
)

data class PersonaMetaDto(
    @SerializedName("archetype") val archetype: String,
    @SerializedName("title") val title: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("description") val description: String,
    @SerializedName("recommended_days_per_week") val recommendedDays: Int,
    @SerializedName("target_session_minutes") val targetSessionMinutes: Int
)

data class NutritionTargetDto(
    @SerializedName("bmr") val bmr: Float,
    @SerializedName("tdee") val tdee: Float,
    @SerializedName("daily_calories") val dailyCalories: Int,
    @SerializedName("protein_grams") val proteinGrams: Int,
    @SerializedName("carbs_grams") val carbsGrams: Int,
    @SerializedName("fat_grams") val fatGrams: Int,
    @SerializedName("water_ml_target") val waterMlTarget: Int
)

// ==================== WORKOUT DTOs ====================
data class TailoredPlanData(
    @SerializedName("plan_id") val planId: Long?,
    @SerializedName("plan_name") val planName: String,
    @SerializedName("focus_area") val focusArea: String,
    @SerializedName("estimated_duration_minutes") val estimatedDurationMinutes: Int,
    @SerializedName("persona_archetype") val personaArchetype: String,
    @SerializedName("total_exercises") val totalExercises: Int,
    @SerializedName("substitutions_applied_count") val substitutionsAppliedCount: Int,
    @SerializedName("substitutions_applied") val substitutionsApplied: List<SubstitutionMetaDto>,
    @SerializedName("exercises") val exercises: List<PrescribedExerciseDto>,
    @SerializedName("is_rest_day") val isRestDay: Boolean? = false,
    @SerializedName("rest_day_message") val restDayMessage: String? = null,
    @SerializedName("next_workout_day") val nextWorkoutDay: String? = null,
    @SerializedName("available_days") val availableDays: List<String>? = null
)

data class SubstitutionMetaDto(
    @SerializedName("original") val original: String,
    @SerializedName("substituted_with") val substitutedWith: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("triggered_by") val triggeredBy: String
)

data class PrescribedExerciseDto(
    @SerializedName("plan_exercise_id") val planExerciseId: Long,
    @SerializedName("order") val order: Int,
    @SerializedName("exercise") val exercise: ExerciseDetailDto,
    @SerializedName("target_sets") val targetSets: Int,
    @SerializedName("target_reps_min") val targetRepsMin: Int,
    @SerializedName("target_reps_max") val targetRepsMax: Int,
    @SerializedName("rest_seconds") val restSeconds: Int,
    @SerializedName("is_substituted") val isSubstituted: Boolean,
    @SerializedName("substitution_reason") val substitutionReason: String?,
    @SerializedName("limitation_flag") val limitationFlag: String?
)

data class ExerciseDetailDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("target_muscle") val targetMuscle: String,
    @SerializedName("exercise_type") val exerciseType: String? = null,
    @SerializedName("workout_environment") val workoutEnvironment: String? = null,
    @SerializedName("secondary_muscles") val secondaryMuscles: String? = null,
    @SerializedName("equipment_type") val equipmentType: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("instructions") val instructions: String,
    @SerializedName("common_mistakes") val commonMistakes: String?,
    @SerializedName("injury_prevention_cues") val injuryPreventionCues: String?,
    @SerializedName("required_equipment") val requiredEquipment: String? = null,
    @SerializedName("common_injuries") val commonInjuries: String? = null,
    @SerializedName("safer_alternatives_text") val saferAlternativesText: String? = null,
    @SerializedName("recommendation_rules") val recommendationRules: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = true,
    @SerializedName("image_url") val imageUrl: String?,
    // Guide System Fields
    @SerializedName("proper_form_asset") val properFormAsset: String? = null,
    @SerializedName("common_mistakes_asset") val commonMistakesAsset: String? = null,
    @SerializedName("injury_prevention_asset") val injuryPreventionAsset: String? = null,
    @SerializedName("anatomy_view") val anatomyView: String? = "dual",
    @SerializedName("primary_muscle_regions") val primaryMuscleRegions: List<String>? = emptyList(),
    @SerializedName("secondary_muscle_regions") val secondaryMuscleRegions: List<String>? = emptyList(),
    @SerializedName("muscle_mapping_confidence") val muscleMappingConfidence: String? = "high",
    @SerializedName("default_sets") val defaultSets: Int? = null,
    @SerializedName("default_reps") val defaultReps: String? = null,
    @SerializedName("safe_starting_load") val safeStartingLoad: String? = null,
    @SerializedName("default_rest_seconds") val defaultRestSeconds: Int? = null,
    @SerializedName("breathing_tips") val breathingTips: String? = null,
    @SerializedName("benefits") val benefits: String? = null,
    @SerializedName("muscle_explanation") val muscleExplanation: String? = null,
    // Personalized & Injury Contraindication Fields
    @SerializedName("is_contraindicated") val isContraindicated: Boolean? = false,
    @SerializedName("contraindication_warning") val contraindicationWarning: String? = null,
    @SerializedName("suggested_alternative_id") val suggestedAlternativeId: Long? = null,
    @SerializedName("suggested_alternative_name") val suggestedAlternativeName: String? = null,
    @SerializedName("personalized_starting_load") val personalizedStartingLoad: String? = null,
    @SerializedName("personalized_sets") val personalizedSets: Int? = null,
    @SerializedName("personalized_reps") val personalizedReps: String? = null,
    @SerializedName("personalized_rest_seconds") val personalizedRestSeconds: Int? = null,
    @SerializedName("personalized_safety_notes") val personalizedSafetyNotes: List<String>? = null
)

data class SafeAlternativesData(
    @SerializedName("original_exercise") val originalExercise: ExerciseSummaryDto,
    @SerializedName("safe_alternatives_count") val count: Int,
    @SerializedName("safe_alternatives") val alternatives: List<ExerciseDetailDto>
)

data class ExerciseSummaryDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("target_muscle") val targetMuscle: String
)

data class StartWorkoutRequest(
    @SerializedName("workout_plan_id") val workoutPlanId: Long?,
    @SerializedName("session_name") val sessionName: String
)

data class StartWorkoutResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("session_name") val sessionName: String,
    @SerializedName("started_at") val startedAt: String
)

data class LogSetRequest(
    @SerializedName("workout_log_id") val workoutLogId: Long,
    @SerializedName("exercise_id") val exerciseId: Long,
    @SerializedName("set_number") val setNumber: Int,
    @SerializedName("weight_kg") val weightKg: Float,
    @SerializedName("reps_completed") val repsCompleted: Int
)

data class CompleteWorkoutRequest(
    @SerializedName("workout_log_id") val workoutLogId: Long,
    @SerializedName("total_duration_seconds") val totalDurationSeconds: Int,
    @SerializedName("completed_exercise_ids") val completedExerciseIds: List<Long>? = null,
    @SerializedName("total_exercises_completed") val totalExercisesCompleted: Int? = null,
    @SerializedName("notes") val notes: String? = null
)

data class CompleteWorkoutResponse(
    @SerializedName("workout_log_id") val workoutLogId: Long,
    @SerializedName("session_name") val sessionName: String,
    @SerializedName("duration_minutes") val durationMinutes: Float,
    @SerializedName("exercises_completed") val exercisesCompleted: Int,
    @SerializedName("total_volume_kg") val totalVolumeKg: Float
)

// ==================== NUTRITION & WATER DTOs ====================
data class NutritionSummaryData(
    @SerializedName("date") val date: String,
    @SerializedName("calories") val calories: CalorieSummaryDto,
    @SerializedName("macros") val macros: MacrosSummaryDto,
    @SerializedName("water") val water: WaterSummaryDto,
    @SerializedName("meals") val meals: List<MealLogDto>
)

data class CalorieSummaryDto(
    @SerializedName("target") val target: Int,
    @SerializedName("consumed") val consumed: Int,
    @SerializedName("remaining") val remaining: Int,
    @SerializedName("percentage") val percentage: Float
)

data class MacrosSummaryDto(
    @SerializedName("protein") val protein: MacroItemDto,
    @SerializedName("carbs") val carbs: MacroItemDto,
    @SerializedName("fat") val fat: MacroItemDto
)

data class MacroItemDto(
    @SerializedName("target_g") val targetGrams: Int,
    @SerializedName("consumed_g") val consumedGrams: Float,
    @SerializedName("remaining_g") val remainingGrams: Float
)

data class WaterSummaryDto(
    @SerializedName("target_ml") val targetMl: Int,
    @SerializedName("consumed_ml") val consumedMl: Int,
    @SerializedName("remaining_ml") val remainingMl: Int,
    @SerializedName("percentage") val percentage: Float
)

data class MealLogDto(
    @SerializedName("id") val id: Long,
    @SerializedName("meal_type") val mealType: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("calories") val calories: Int,
    @SerializedName("protein_grams") val proteinGrams: Float,
    @SerializedName("carbs_grams") val carbsGrams: Float,
    @SerializedName("fat_grams") val fatGrams: Float
)

data class LogMealRequest(
    @SerializedName("meal_type") val mealType: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("calories") val calories: Int,
    @SerializedName("protein_grams") val proteinGrams: Float,
    @SerializedName("carbs_grams") val carbsGrams: Float,
    @SerializedName("fat_grams") val fatGrams: Float
)

data class LogWaterRequest(
    @SerializedName("amount_ml") val amountMl: Int
)

data class LogWaterResponse(
    @SerializedName("total_water_today_ml") val totalWaterTodayMl: Int
)

// ==================== ACTIVITY & SENSOR DTOs ====================
data class SyncStepsRequest(
    @SerializedName("step_count") val stepCount: Int,
    @SerializedName("log_date") val logDate: String? = null
)

data class StepSummaryData(
    @SerializedName("today") val today: StepLogDto,
    @SerializedName("weekly_average_steps") val weeklyAverageSteps: Int,
    @SerializedName("history_7_days") val history7Days: List<StepLogDto>
)

data class StepLogDto(
    @SerializedName("log_date") val logDate: String,
    @SerializedName("step_count") val stepCount: Int,
    @SerializedName("distance_km") val distanceKm: Float,
    @SerializedName("calories_burned") val caloriesBurned: Float
)

// ==================== PROGRESS & ANALYTICS DTOs ====================
data class ProgressDashboardData(
    @SerializedName("bmi") val bmi: BmiDataDto,
    @SerializedName("workouts") val workouts: WorkoutStatsDto,
    @SerializedName("streak_days") val streakDays: Int
)

data class BmiDataDto(
    @SerializedName("score") val score: Float,
    @SerializedName("category") val category: String,
    @SerializedName("current_weight_kg") val currentWeightKg: Float,
    @SerializedName("target_weight_kg") val targetWeightKg: Float?
)

data class WorkoutStatsDto(
    @SerializedName("total_completed") val totalCompleted: Int,
    @SerializedName("total_volume_kg") val totalVolumeKg: Float,
    @SerializedName("this_week_completed") val thisWeekCompleted: Int,
    @SerializedName("adherence_percentage") val adherencePercentage: Float
)

// ==================== SETTINGS DTOs ====================
data class SettingsData(
    @SerializedName("user") val user: UserDto,
    @SerializedName("profile") val profile: ProfileSummaryDto?,
    @SerializedName("limitations") val limitations: List<LimitationDto>,
    @SerializedName("available_limitations") val availableLimitations: List<LimitationDto>
)

data class UpdatePreferencesRequest(
    @SerializedName("workout_location") val workoutLocation: String,
    @SerializedName("exercise_type") val exerciseType: String,
    @SerializedName("available_days") val availableDays: List<String>,
    @SerializedName("joint_health_status") val jointHealthStatus: String,
    @SerializedName("joint_limitations") val jointLimitations: List<String> = emptyList(),
    @SerializedName("limitation_ids") val limitationIds: List<Long> = emptyList()
)

data class UpdatePreferencesResponse(
    @SerializedName("profile") val profile: ProfileSummaryDto?,
    @SerializedName("limitations") val limitations: List<LimitationDto>
)

data class ChangePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String
)
