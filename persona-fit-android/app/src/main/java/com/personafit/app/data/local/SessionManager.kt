package com.personafit.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "personafit_session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USER_ID = longPreferencesKey("user_id")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        private val KEY_PERSONA_ARCHETYPE = stringPreferencesKey("persona_archetype")
        private val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")

        // Specific Notification Categories (4 Features Only)
        private val KEY_NOTIF_WORKOUT_ENABLED = booleanPreferencesKey("notif_workout_enabled")
        private val KEY_NOTIF_MEAL_ENABLED = booleanPreferencesKey("notif_meal_enabled")
        private val KEY_NOTIF_PROGRESS_ENABLED = booleanPreferencesKey("notif_progress_enabled")
        private val KEY_NOTIF_RECOVERY_ENABLED = booleanPreferencesKey("notif_recovery_enabled")

        // Schedule Times
        private val KEY_WORKOUT_TIME_HOUR = intPreferencesKey("workout_time_hour")
        private val KEY_WORKOUT_TIME_MINUTE = intPreferencesKey("workout_time_minute")
        private val KEY_PROGRESS_DAY = stringPreferencesKey("progress_day")
        private val KEY_PROGRESS_TIME_HOUR = intPreferencesKey("progress_time_hour")
        private val KEY_PROGRESS_TIME_MINUTE = intPreferencesKey("progress_time_minute")
        private val KEY_AVAILABLE_DAYS = stringSetPreferencesKey("available_days")

        // Duplicate Guards
        private val KEY_LAST_WORKOUT_DATE = stringPreferencesKey("last_workout_date")
        private val KEY_LAST_RECOVERY_DATE = stringPreferencesKey("last_recovery_date")
        private val KEY_LAST_PROGRESS_WEEK = stringPreferencesKey("last_progress_week")
    }

    val authToken: Flow<String?> = context.dataStore.data.map { it[KEY_AUTH_TOKEN] }
    val userId: Flow<Long?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val userName: Flow<String?> = context.dataStore.data.map { it[KEY_USER_NAME] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[KEY_USER_EMAIL] }
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data.map { it[KEY_HAS_COMPLETED_ONBOARDING] ?: false }
    val personaArchetype: Flow<String?> = context.dataStore.data.map { it[KEY_PERSONA_ARCHETYPE] }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIFICATIONS_ENABLED] ?: true }

    // 4 Notification Feature Toggles
    val notifWorkoutEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIF_WORKOUT_ENABLED] ?: true }
    val notifMealEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIF_MEAL_ENABLED] ?: true }
    val notifProgressEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIF_PROGRESS_ENABLED] ?: true }
    val notifRecoveryEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIF_RECOVERY_ENABLED] ?: true }

    // Workout & Progress Times
    val workoutTimeHour: Flow<Int> = context.dataStore.data.map { it[KEY_WORKOUT_TIME_HOUR] ?: 8 }
    val workoutTimeMinute: Flow<Int> = context.dataStore.data.map { it[KEY_WORKOUT_TIME_MINUTE] ?: 0 }
    val progressDay: Flow<String> = context.dataStore.data.map { it[KEY_PROGRESS_DAY] ?: "Sunday" }
    val progressTimeHour: Flow<Int> = context.dataStore.data.map { it[KEY_PROGRESS_TIME_HOUR] ?: 18 }
    val progressTimeMinute: Flow<Int> = context.dataStore.data.map { it[KEY_PROGRESS_TIME_MINUTE] ?: 0 }
    val availableDays: Flow<Set<String>> = context.dataStore.data.map {
        it[KEY_AVAILABLE_DAYS] ?: setOf("Monday", "Wednesday", "Friday")
    }

    suspend fun setWorkoutNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIF_WORKOUT_ENABLED] = enabled }
    }

    suspend fun setMealNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIF_MEAL_ENABLED] = enabled }
    }

    suspend fun setProgressNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIF_PROGRESS_ENABLED] = enabled }
    }

    suspend fun setRecoveryNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIF_RECOVERY_ENABLED] = enabled }
    }

    suspend fun setWorkoutTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[KEY_WORKOUT_TIME_HOUR] = hour
            it[KEY_WORKOUT_TIME_MINUTE] = minute
        }
    }

    suspend fun setProgressSchedule(day: String, hour: Int, minute: Int) {
        context.dataStore.edit {
            it[KEY_PROGRESS_DAY] = day
            it[KEY_PROGRESS_TIME_HOUR] = hour
            it[KEY_PROGRESS_TIME_MINUTE] = minute
        }
    }

    suspend fun setAvailableDays(days: Set<String>) {
        context.dataStore.edit { it[KEY_AVAILABLE_DAYS] = days }
    }

    suspend fun getAvailableDays(): Set<String> {
        return availableDays.firstOrNull() ?: setOf("Monday", "Wednesday", "Friday")
    }

    suspend fun getLastWorkoutDate(): String? {
        return context.dataStore.data.firstOrNull()?.get(KEY_LAST_WORKOUT_DATE)
    }

    suspend fun setLastWorkoutDate(dateStr: String) {
        context.dataStore.edit { it[KEY_LAST_WORKOUT_DATE] = dateStr }
    }

    suspend fun getLastRecoveryDate(): String? {
        return context.dataStore.data.firstOrNull()?.get(KEY_LAST_RECOVERY_DATE)
    }

    suspend fun setLastRecoveryDate(dateStr: String) {
        context.dataStore.edit { it[KEY_LAST_RECOVERY_DATE] = dateStr }
    }

    suspend fun getLastProgressWeek(): String? {
        return context.dataStore.data.firstOrNull()?.get(KEY_LAST_PROGRESS_WEEK)
    }

    suspend fun setLastProgressWeek(weekStr: String) {
        context.dataStore.edit { it[KEY_LAST_PROGRESS_WEEK] = weekStr }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
        }
    }

    suspend fun saveUserData(
        id: Long,
        name: String,
        email: String,
        hasCompletedOnboarding: Boolean,
        persona: String?
    ) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = id
            preferences[KEY_USER_NAME] = name
            preferences[KEY_USER_EMAIL] = email
            preferences[KEY_HAS_COMPLETED_ONBOARDING] = hasCompletedOnboarding
            if (persona != null) {
                preferences[KEY_PERSONA_ARCHETYPE] = persona
            }
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean, persona: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_HAS_COMPLETED_ONBOARDING] = completed
            preferences[KEY_PERSONA_ARCHETYPE] = persona
        }
    }

    suspend fun getSyncToken(): String? {
        return context.dataStore.data.firstOrNull()?.get(KEY_AUTH_TOKEN)
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
