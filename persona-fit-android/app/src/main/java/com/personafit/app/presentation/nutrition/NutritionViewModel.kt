package com.personafit.app.presentation.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personafit.app.data.local.SessionManager
import com.personafit.app.data.remote.dto.NutritionSummaryData
import com.personafit.app.data.repository.NutritionRepository
import com.personafit.app.notification.PersonaFitNotificationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

sealed class NutritionUiState {
    object Idle : NutritionUiState()
    object Loading : NutritionUiState()
    data class Success(val data: NutritionSummaryData) : NutritionUiState()
    data class Error(val message: String) : NutritionUiState()
}

sealed class MealSaveFeedback {
    data class Success(val message: String) : MealSaveFeedback()
    data class Failure(val message: String) : MealSaveFeedback()
}

class NutritionViewModel(
    private val nutritionRepository: NutritionRepository,
    private val sessionManager: SessionManager? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<NutritionUiState>(NutritionUiState.Idle)
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    private val _nutritionSummary = MutableStateFlow<NutritionSummaryData?>(null)
    val nutritionSummary: StateFlow<NutritionSummaryData?> = _nutritionSummary.asStateFlow()

    private val _saveFeedback = MutableStateFlow<MealSaveFeedback?>(null)
    val saveFeedback: StateFlow<MealSaveFeedback?> = _saveFeedback.asStateFlow()

    init {
        loadTodayNutrition()
    }

    fun loadTodayNutrition() {
        viewModelScope.launch {
            _uiState.value = NutritionUiState.Loading
            val result = nutritionRepository.getTodayNutrition()
            result.fold(
                onSuccess = { summary ->
                    _nutritionSummary.value = summary
                    _uiState.value = NutritionUiState.Success(summary)
                },
                onFailure = { error ->
                    _uiState.value = NutritionUiState.Error(error.message ?: "Failed to load nutrition.")
                }
            )
        }
    }

    fun logMeal(mealType: String, foodName: String, calories: Int, protein: Float, carbs: Float, fat: Float) {
        viewModelScope.launch {
            nutritionRepository.logMeal(mealType, foodName, calories, protein, carbs, fat).fold(
                onSuccess = {
                    loadTodayNutrition()
                    val soundEnabled = sessionManager?.notifMealEnabled?.firstOrNull() ?: true
                    if (soundEnabled) {
                        PersonaFitNotificationManager.playMealConfirmationChime()
                    }
                    val formattedType = mealType.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    _saveFeedback.value = MealSaveFeedback.Success("Logged $formattedType: $foodName")
                },
                onFailure = { error ->
                    _saveFeedback.value = MealSaveFeedback.Failure(error.message ?: "Failed to save meal.")
                }
            )
        }
    }

    fun clearFeedback() {
        _saveFeedback.value = null
    }

    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            nutritionRepository.logWater(amountMl).fold(
                onSuccess = {
                    loadTodayNutrition()
                },
                onFailure = {
                    // Retry or handle
                }
            )
        }
    }

    class Factory(
        private val nutritionRepository: NutritionRepository,
        private val sessionManager: SessionManager? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NutritionViewModel(nutritionRepository, sessionManager) as T
        }
    }
}
