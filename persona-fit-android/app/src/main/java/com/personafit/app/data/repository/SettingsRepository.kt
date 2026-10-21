package com.personafit.app.data.repository

import com.personafit.app.data.local.SessionManager
import com.personafit.app.data.remote.PersonaFitApiService
import com.personafit.app.data.remote.dto.*
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val apiService: PersonaFitApiService,
    private val sessionManager: SessionManager
) {
    val notificationsEnabled: Flow<Boolean> = sessionManager.notificationsEnabled

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        sessionManager.setNotificationsEnabled(enabled)
    }

    suspend fun getSettings(): Result<SettingsData> {
        return try {
            val response = apiService.getSettings()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load settings."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePreferences(request: UpdatePreferencesRequest): Result<String> {
        return try {
            val response = apiService.updatePreferences(request)
            if (response.isSuccessful) {
                val msg = response.body()?.message ?: "Your upcoming workout plan has been updated."
                Result.success(msg)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to update preferences."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<String> {
        return try {
            val response = apiService.changePassword(ChangePasswordRequest(currentPassword, newPassword))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Password updated successfully.")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Current password does not match."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
