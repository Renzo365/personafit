package com.personafit.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
    }

    val authToken: Flow<String?> = context.dataStore.data.map { it[KEY_AUTH_TOKEN] }
    val userId: Flow<Long?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val userName: Flow<String?> = context.dataStore.data.map { it[KEY_USER_NAME] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[KEY_USER_EMAIL] }
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data.map { it[KEY_HAS_COMPLETED_ONBOARDING] ?: false }
    val personaArchetype: Flow<String?> = context.dataStore.data.map { it[KEY_PERSONA_ARCHETYPE] }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIFICATIONS_ENABLED] ?: true }

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
