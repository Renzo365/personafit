package com.personafit.app.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personafit.app.data.remote.dto.ProgressDashboardData
import com.personafit.app.data.remote.dto.StepSummaryData
import com.personafit.app.data.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProgressUiState {
    object Idle : ProgressUiState()
    object Loading : ProgressUiState()
    data class Success(val dashboard: ProgressDashboardData, val stepSummary: StepSummaryData) : ProgressUiState()
    data class Error(val message: String) : ProgressUiState()
}

class ProgressViewModel(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProgressUiState>(ProgressUiState.Idle)
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    private val _dashboardData = MutableStateFlow<ProgressDashboardData?>(null)
    val dashboardData: StateFlow<ProgressDashboardData?> = _dashboardData.asStateFlow()

    private val _stepSummaryData = MutableStateFlow<StepSummaryData?>(null)
    val stepSummaryData: StateFlow<StepSummaryData?> = _stepSummaryData.asStateFlow()

    init {
        loadProgressData()
    }

    fun loadProgressData() {
        viewModelScope.launch {
            _uiState.value = ProgressUiState.Loading

            val dashRes = activityRepository.getProgressDashboard()
            val stepsRes = activityRepository.getStepsSummary()

            if (dashRes.isSuccess && stepsRes.isSuccess) {
                val dash = dashRes.getOrNull()!!
                val steps = stepsRes.getOrNull()!!
                _dashboardData.value = dash
                _stepSummaryData.value = steps
                _uiState.value = ProgressUiState.Success(dash, steps)
            } else {
                _uiState.value = ProgressUiState.Error("Failed to load progress analytics.")
            }
        }
    }

    class Factory(private val activityRepository: ActivityRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProgressViewModel(activityRepository) as T
        }
    }
}
