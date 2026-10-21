package com.personafit.app.presentation.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personafit.app.data.remote.dto.NutritionSummaryData
import com.personafit.app.data.repository.NutritionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NutritionUiState {
    object Idle : NutritionUiState()
    object Loading : NutritionUiState()
    data class Success(val data: NutritionSummaryData) : NutritionUiState()
    data class Error(val message: String) : NutritionUiState()
}

class NutritionViewModel(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NutritionUiState>(NutritionUiState.Idle)
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    private val _nutritionSummary = MutableStateFlow<NutritionSummaryData?>(null)
    val nutritionSummary: StateFlow<NutritionSummaryData?> = _nutritionSummary.asStateFlow()

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
                },
                onFailure = {
                    // Retry or handle
                }
            )
        }
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

    class Factory(private val nutritionRepository: NutritionRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NutritionViewModel(nutritionRepository) as T
        }
    }
}
