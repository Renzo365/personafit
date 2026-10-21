package com.personafit.app.data.repository

import com.personafit.app.data.local.SessionManager
import com.personafit.app.data.remote.PersonaFitApiService
import com.personafit.app.data.remote.dto.AssessmentSubmitRequest
import com.personafit.app.data.remote.dto.AssessmentSummaryData
import com.personafit.app.data.remote.dto.LimitationDto

class AssessmentRepository(
    private val apiService: PersonaFitApiService,
    private val sessionManager: SessionManager
) {
    suspend fun getLimitations(): Result<List<LimitationDto>> {
        return try {
            val response = apiService.getLimitations()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to load limitations list."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitAssessment(request: AssessmentSubmitRequest): Result<AssessmentSummaryData> {
        return try {
            val response = apiService.submitAssessment(request)
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                sessionManager.setOnboardingCompleted(true, data.persona.archetype)
                Result.success(data)
            } else {
                val errorMsg = com.personafit.app.data.remote.ErrorUtils.parseError(response.errorBody(), "Failed to submit assessment.")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSummary(): Result<AssessmentSummaryData> {
        return try {
            val response = apiService.getAssessmentSummary()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to fetch assessment summary."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
