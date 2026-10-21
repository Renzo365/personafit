package com.personafit.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personafit.app.data.local.SessionManager
import com.personafit.app.data.remote.dto.NutritionSummaryData
import com.personafit.app.data.remote.dto.StepLogDto
import com.personafit.app.data.remote.dto.TailoredPlanData
import com.personafit.app.data.repository.ActivityRepository
import com.personafit.app.data.repository.NutritionRepository
import com.personafit.app.data.repository.WorkoutRepository
import com.personafit.app.data.sensor.StepSensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val sessionManager: SessionManager,
    private val workoutRepository: WorkoutRepository,
    private val nutritionRepository: NutritionRepository,
    private val activityRepository: ActivityRepository,
    private val stepSensorManager: StepSensorManager
) : ViewModel() {

    val userName = sessionManager.userName
    val personaArchetype = sessionManager.personaArchetype
    val liveSensorSteps = stepSensorManager.liveSteps

    private val _todayWorkout = MutableStateFlow<TailoredPlanData?>(null)
    val todayWorkout: StateFlow<TailoredPlanData?> = _todayWorkout.asStateFlow()

    private val _nutritionSummary = MutableStateFlow<NutritionSummaryData?>(null)
    val nutritionSummary: StateFlow<NutritionSummaryData?> = _nutritionSummary.asStateFlow()

    private val _todayStepLog = MutableStateFlow<StepLogDto?>(null)
    val todayStepLog: StateFlow<StepLogDto?> = _todayStepLog.asStateFlow()

    val isLoading = MutableStateFlow(false)

    init {
        stepSensorManager.startListening()
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            isLoading.value = true

            // 1. Fetch Today's Workout
            workoutRepository.getTodayWorkout().fold(
                onSuccess = { plan -> _todayWorkout.value = plan },
                onFailure = { /* Fail gracefully */ }
            )

            // 2. Fetch Nutrition Summary
            nutritionRepository.getTodayNutrition().fold(
                onSuccess = { nut -> _nutritionSummary.value = nut },
                onFailure = { /* Fail gracefully */ }
            )

            // 3. Fetch Activity Summary
            activityRepository.getStepsSummary().fold(
                onSuccess = { act -> _todayStepLog.value = act.today },
                onFailure = { /* Fail gracefully */ }
            )

            isLoading.value = false
        }
    }

    fun quickAddWater(amountMl: Int) {
        viewModelScope.launch {
            nutritionRepository.logWater(amountMl).fold(
                onSuccess = {
                    loadDashboardData()
                },
                onFailure = { }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stepSensorManager.stopListening()
    }

    class Factory(
        private val sessionManager: SessionManager,
        private val workoutRepository: WorkoutRepository,
        private val nutritionRepository: NutritionRepository,
        private val activityRepository: ActivityRepository,
        private val stepSensorManager: StepSensorManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(
                sessionManager,
                workoutRepository,
                nutritionRepository,
                activityRepository,
                stepSensorManager
            ) as T
        }
    }
}
