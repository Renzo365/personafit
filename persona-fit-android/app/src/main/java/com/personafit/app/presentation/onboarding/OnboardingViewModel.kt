package com.personafit.app.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personafit.app.data.remote.dto.AssessmentSubmitRequest
import com.personafit.app.data.remote.dto.AssessmentSummaryData
import com.personafit.app.data.remote.dto.LimitationDto
import com.personafit.app.data.repository.AssessmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OnboardingUiState {
    object Idle : OnboardingUiState()
    object Loading : OnboardingUiState()
    data class Submitted(val summary: AssessmentSummaryData) : OnboardingUiState()
    data class Error(val message: String) : OnboardingUiState()
}

class OnboardingViewModel(
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {

    // Current Step Index:
    // 0 = Step 1 of 2: Biometrics, Goal & Workout Preferences
    // 1 = Step 2 of 2: Joint Health & Injury Prevention
    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    // Step 1: Biometrics & Goals
    var gender = MutableStateFlow("male")
    var age = MutableStateFlow("")
    var heightCm = MutableStateFlow("")
    var weightKg = MutableStateFlow("")
    var targetWeightKg = MutableStateFlow("")
    var fitnessGoal = MutableStateFlow("fat_loss")
    var activityLevel = MutableStateFlow("moderate")

    // Step 1: Workout Preferences
    var exerciseType = MutableStateFlow("compound") // "compound", "isolation"
    var workoutLocation = MutableStateFlow("gym") // "gym", "home"
    var availableDays = MutableStateFlow<Set<String>>(setOf("Monday", "Wednesday", "Friday"))
    var trainingExperience = MutableStateFlow("intermediate") // "beginner", "intermediate", "advanced"

    // Backward-compatibility state
    var timeAvailable = MutableStateFlow("medium")
    var location = MutableStateFlow("gym")
    var experience = MutableStateFlow("intermediate")

    // Step 2: Joint Health & Injury Prevention
    var jointHealthStatus = MutableStateFlow("No Limitations") // "No Limitations", "Has Limitations"
    var jointLimitations = MutableStateFlow<Set<String>>(emptySet()) // "Lower Back", "Knee", "Shoulder", "Wrist"

    private val _availableLimitations = MutableStateFlow<List<LimitationDto>>(emptyList())
    val availableLimitations: StateFlow<List<LimitationDto>> = _availableLimitations.asStateFlow()

    private val _selectedLimitationIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedLimitationIds: StateFlow<Set<Long>> = _selectedLimitationIds.asStateFlow()

    // Overall UI State
    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    // Validation Error Message
    private val _stepError = MutableStateFlow<String?>(null)
    val stepError: StateFlow<String?> = _stepError.asStateFlow()

    // Cached Summary upon success
    private val _summaryData = MutableStateFlow<AssessmentSummaryData?>(null)
    val summaryData: StateFlow<AssessmentSummaryData?> = _summaryData.asStateFlow()

    init {
        loadLimitations()
    }

    fun loadLimitations() {
        viewModelScope.launch {
            assessmentRepository.getLimitations().fold(
                onSuccess = { list -> _availableLimitations.value = list },
                onFailure = { /* Fail gracefully or retry */ }
            )
        }
    }

    fun loadSummary() {
        if (_summaryData.value != null) return
        viewModelScope.launch {
            assessmentRepository.getSummary().fold(
                onSuccess = { summary ->
                    _summaryData.value = summary
                    summary.profile?.let { prof ->
                        prof.workoutLocation?.let { workoutLocation.value = it }
                        prof.exerciseType?.let { exerciseType.value = it }
                        prof.availableDays?.let { if (it.isNotEmpty()) availableDays.value = it.toSet() }
                        prof.trainingExperience?.let { trainingExperience.value = it }
                        prof.jointHealthStatus?.let { jointHealthStatus.value = it }
                        prof.jointLimitations?.let { jointLimitations.value = it.toSet() }
                    }
                    _uiState.value = OnboardingUiState.Submitted(summary)
                },
                onFailure = { /* Fail gracefully */ }
            )
        }
    }

    fun toggleAvailableDay(day: String) {
        val current = availableDays.value.toMutableSet()
        if (current.contains(day)) {
            current.remove(day)
        } else {
            current.add(day)
        }
        availableDays.value = current
    }

    fun selectNoLimitations() {
        jointHealthStatus.value = "No Limitations"
        jointLimitations.value = emptySet()
        _selectedLimitationIds.value = emptySet()
    }

    fun toggleJointLimitation(limitationName: String) {
        val currentNames = jointLimitations.value.toMutableSet()
        if (currentNames.contains(limitationName)) {
            currentNames.remove(limitationName)
        } else {
            currentNames.add(limitationName)
        }
        jointLimitations.value = currentNames

        if (currentNames.isEmpty()) {
            jointHealthStatus.value = "No Limitations"
        } else {
            jointHealthStatus.value = "Has Limitations"
        }

        // Map to backend limitation IDs if available
        val matchedIds = _availableLimitations.value
            .filter { item ->
                currentNames.any { name ->
                    item.name.contains(name, ignoreCase = true) ||
                    (item.affectedBodyPart?.contains(name, ignoreCase = true) == true) ||
                    item.code.contains(name.replace(" ", "_"), ignoreCase = true)
                }
            }
            .map { it.id }
            .toSet()
        _selectedLimitationIds.value = matchedIds
    }

    fun nextStep(): Boolean {
        _stepError.value = null
        when (_currentStep.value) {
            0 -> {
                // Validate Step 1 of 2
                val ageInt = age.value.toIntOrNull()
                val heightF = heightCm.value.toFloatOrNull()
                val weightF = weightKg.value.toFloatOrNull()

                if (ageInt == null || ageInt < 15 || ageInt > 100) {
                    _stepError.value = "Please enter a valid age (15 - 100)."
                    return false
                }
                if (heightF == null || heightF < 100 || heightF > 250) {
                    _stepError.value = "Please enter a valid height (100 - 250 cm)."
                    return false
                }
                if (weightF == null || weightF < 30 || weightF > 300) {
                    _stepError.value = "Please enter a valid weight (30 - 300 kg)."
                    return false
                }
                if (availableDays.value.isEmpty()) {
                    _stepError.value = "Please select at least one workout day for your weekly availability."
                    return false
                }

                _currentStep.value = 1
                return true
            }
            1 -> {
                submitAssessment()
                return true
            }
        }
        return false
    }

    fun previousStep() {
        _stepError.value = null
        if (_currentStep.value > 0) {
            _currentStep.value -= 1
        }
    }

    fun submitAssessment() {
        _stepError.value = null
        if (jointHealthStatus.value != "No Limitations" && jointLimitations.value.isEmpty()) {
            _stepError.value = "Please select either 'No Limitations' or at least one joint limitation."
            return
        }

        viewModelScope.launch {
            _uiState.value = OnboardingUiState.Loading

            val request = AssessmentSubmitRequest(
                gender = gender.value,
                age = age.value.toIntOrNull() ?: 25,
                heightCm = heightCm.value.toFloatOrNull() ?: 175f,
                weightKg = weightKg.value.toFloatOrNull() ?: 70f,
                targetWeightKg = targetWeightKg.value.toFloatOrNull(),
                activityLevel = activityLevel.value,
                fitnessGoal = fitnessGoal.value,
                availableDays = availableDays.value.toList(),
                trainingExperience = trainingExperience.value,
                exerciseType = exerciseType.value,
                workoutLocation = workoutLocation.value,
                jointHealthStatus = jointHealthStatus.value,
                jointLimitations = jointLimitations.value.toList(),
                timeAvailable = "medium",
                location = workoutLocation.value,
                experience = trainingExperience.value,
                limitationIds = _selectedLimitationIds.value.toList()
            )

            val result = assessmentRepository.submitAssessment(request)
            result.fold(
                onSuccess = { summary ->
                    _summaryData.value = summary
                    _uiState.value = OnboardingUiState.Submitted(summary)
                },
                onFailure = { error ->
                    _uiState.value = OnboardingUiState.Error(error.message ?: "Failed to generate prescription.")
                }
            )
        }
    }

    class Factory(private val assessmentRepository: AssessmentRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OnboardingViewModel(assessmentRepository) as T
        }
    }
}
