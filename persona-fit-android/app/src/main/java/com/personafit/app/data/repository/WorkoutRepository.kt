package com.personafit.app.data.repository

import com.personafit.app.data.local.dao.WorkoutCacheDao
import com.personafit.app.data.remote.PersonaFitApiService
import com.personafit.app.data.remote.dto.*

class WorkoutRepository(
    private val apiService: PersonaFitApiService,
    private val workoutCacheDao: WorkoutCacheDao
) {
    suspend fun getTodayWorkout(): Result<TailoredPlanData> {
        return try {
            val response = apiService.getTodayWorkout()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to load today's workout plan."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExercise(exerciseId: Long): Result<ExerciseDetailDto> {
        return try {
            val response = apiService.getExercise(exerciseId)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to load exercise details."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSafeAlternatives(exerciseId: Long): Result<SafeAlternativesData> {
        return try {
            val response = apiService.getSafeAlternatives(exerciseId)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to load safe exercise alternatives."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAssessmentSummary(): Result<AssessmentSummaryData> {
        return try {
            val response = apiService.getAssessmentSummary()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to load user assessment profile."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startWorkout(planId: Long?, sessionName: String): Result<StartWorkoutResponse> {
        return try {
            val response = apiService.startWorkout(StartWorkoutRequest(planId, sessionName))
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to start workout session."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logSet(workoutLogId: Long, exerciseId: Long, setNumber: Int, weightKg: Float, repsCompleted: Int): Result<Unit> {
        return try {
            val response = apiService.logSet(LogSetRequest(workoutLogId, exerciseId, setNumber, weightKg, repsCompleted))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to log set."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun completeWorkout(
        workoutLogId: Long,
        totalDurationSeconds: Int,
        completedExerciseIds: List<Long>? = null,
        totalExercisesCompleted: Int? = null,
        notes: String? = null
    ): Result<CompleteWorkoutResponse> {
        return try {
            val request = CompleteWorkoutRequest(
                workoutLogId = workoutLogId,
                totalDurationSeconds = totalDurationSeconds,
                completedExerciseIds = completedExerciseIds,
                totalExercisesCompleted = totalExercisesCompleted,
                notes = notes
            )
            val response = apiService.completeWorkout(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to complete workout session."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
