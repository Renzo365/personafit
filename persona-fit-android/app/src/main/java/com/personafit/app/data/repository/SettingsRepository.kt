package com.personafit.app.data.repository

import android.content.Context
import com.personafit.app.data.local.SessionManager
import com.personafit.app.data.remote.PersonaFitApiService
import com.personafit.app.data.remote.dto.*
import com.personafit.app.notification.NotificationScheduler
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val apiService: PersonaFitApiService,
    private val sessionManager: SessionManager
) {
    val notificationsEnabled: Flow<Boolean> = sessionManager.notificationsEnabled

    // 4 Notification Feature Toggles
    val notifWorkoutEnabled: Flow<Boolean> = sessionManager.notifWorkoutEnabled
    val notifMealEnabled: Flow<Boolean> = sessionManager.notifMealEnabled
    val notifProgressEnabled: Flow<Boolean> = sessionManager.notifProgressEnabled
    val notifRecoveryEnabled: Flow<Boolean> = sessionManager.notifRecoveryEnabled

    // Schedule Times
    val workoutTimeHour: Flow<Int> = sessionManager.workoutTimeHour
    val workoutTimeMinute: Flow<Int> = sessionManager.workoutTimeMinute
    val progressDay: Flow<String> = sessionManager.progressDay
    val progressTimeHour: Flow<Int> = sessionManager.progressTimeHour
    val progressTimeMinute: Flow<Int> = sessionManager.progressTimeMinute
    val availableDays: Flow<Set<String>> = sessionManager.availableDays

    suspend fun setAvailableDays(days: Set<String>) {
        sessionManager.setAvailableDays(days)
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        sessionManager.setNotificationsEnabled(enabled)
    }

    suspend fun setWorkoutNotificationEnabled(enabled: Boolean) {
        sessionManager.setWorkoutNotificationEnabled(enabled)
    }

    suspend fun setMealNotificationEnabled(enabled: Boolean) {
        sessionManager.setMealNotificationEnabled(enabled)
    }

    suspend fun setProgressNotificationEnabled(enabled: Boolean) {
        sessionManager.setProgressNotificationEnabled(enabled)
    }

    suspend fun setRecoveryNotificationEnabled(enabled: Boolean) {
        sessionManager.setRecoveryNotificationEnabled(enabled)
    }

    suspend fun setWorkoutTime(hour: Int, minute: Int) {
        sessionManager.setWorkoutTime(hour, minute)
    }

    suspend fun setProgressSchedule(day: String, hour: Int, minute: Int) {
        sessionManager.setProgressSchedule(day, hour, minute)
    }

    suspend fun rescheduleNotifications(context: Context, availableDays: Set<String>) {
        NotificationScheduler.rescheduleAll(context, sessionManager, availableDays)
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
