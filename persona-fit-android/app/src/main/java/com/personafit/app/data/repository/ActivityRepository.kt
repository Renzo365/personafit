package com.personafit.app.data.repository

import com.personafit.app.data.local.dao.StepCacheDao
import com.personafit.app.data.local.entity.CachedStepLog
import com.personafit.app.data.remote.PersonaFitApiService
import com.personafit.app.data.remote.dto.ProgressDashboardData
import com.personafit.app.data.remote.dto.StepLogDto
import com.personafit.app.data.remote.dto.StepSummaryData
import com.personafit.app.data.remote.dto.SyncStepsRequest

class ActivityRepository(
    private val apiService: PersonaFitApiService,
    private val stepCacheDao: StepCacheDao
) {
    suspend fun syncSteps(stepCount: Int, date: String? = null): Result<StepLogDto> {
        return try {
            val response = apiService.syncSteps(SyncStepsRequest(stepCount, date))
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                stepCacheDao.insertOrUpdateStepLog(
                    CachedStepLog(
                        logDate = data.logDate,
                        stepCount = data.stepCount,
                        distanceKm = data.distanceKm,
                        caloriesBurned = data.caloriesBurned,
                        isSynced = true
                    )
                )
                Result.success(data)
            } else {
                Result.failure(Exception("Failed to sync step count."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStepsSummary(): Result<StepSummaryData> {
        return try {
            val response = apiService.getStepsSummary()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to load step history."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProgressDashboard(): Result<ProgressDashboardData> {
        return try {
            val response = apiService.getDashboardAnalytics()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to load progress dashboard."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
