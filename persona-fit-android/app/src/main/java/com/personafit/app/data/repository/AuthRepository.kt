package com.personafit.app.data.repository

import com.personafit.app.data.local.SessionManager
import com.personafit.app.data.remote.ErrorUtils
import com.personafit.app.data.remote.PersonaFitApiService
import com.personafit.app.data.remote.dto.AuthData
import com.personafit.app.data.remote.dto.LoginRequest
import com.personafit.app.data.remote.dto.RegisterRequest
import com.personafit.app.data.remote.dto.UserDto
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiService: PersonaFitApiService,
    private val sessionManager: SessionManager
) {
    val authToken: Flow<String?> = sessionManager.authToken
    val hasCompletedOnboarding: Flow<Boolean> = sessionManager.hasCompletedOnboarding
    val userName: Flow<String?> = sessionManager.userName

    suspend fun register(name: String, email: String, password: String): Result<AuthData> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                sessionManager.saveAuthToken(data.token)
                sessionManager.saveUserData(
                    id = data.user.id,
                    name = data.user.name,
                    email = data.user.email,
                    hasCompletedOnboarding = data.user.hasCompletedOnboarding,
                    persona = data.user.persona
                )
                Result.success(data)
            } else {
                val errorMsg = ErrorUtils.parseError(response.errorBody(), "Registration failed.")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthData> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                sessionManager.saveAuthToken(data.token)
                sessionManager.saveUserData(
                    id = data.user.id,
                    name = data.user.name,
                    email = data.user.email,
                    hasCompletedOnboarding = data.user.hasCompletedOnboarding,
                    persona = data.user.persona
                )
                Result.success(data)
            } else {
                val errorMsg = ErrorUtils.parseError(response.errorBody(), "Incorrect email or password. Please try again.")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            apiService.logout()
            sessionManager.clearSession()
            Result.success(Unit)
        } catch (e: Exception) {
            // Even if network fails, clear local session
            sessionManager.clearSession()
            Result.success(Unit)
        }
    }

    suspend fun getMe(): Result<UserDto> {
        return try {
            val response = apiService.getMe()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to fetch profile."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
