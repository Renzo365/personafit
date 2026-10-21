package com.personafit.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personafit.app.data.remote.dto.LimitationDto
import com.personafit.app.data.remote.dto.UpdatePreferencesRequest
import com.personafit.app.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class SettingsUiState {
    object Loading : SettingsUiState()
    object Idle : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    // Working state for preferences
    val workoutLocation = MutableStateFlow("gym") // "gym", "home"
    val exerciseType = MutableStateFlow("compound") // "compound", "isolation"
    val availableDays = MutableStateFlow<Set<String>>(setOf("Monday", "Wednesday", "Friday"))
    val jointHealthStatus = MutableStateFlow("No Limitations") // "No Limitations", "Has Limitations"
    val jointLimitations = MutableStateFlow<Set<String>>(emptySet())

    // Saved (checkpoint) state to allow cancellation
    private var savedWorkoutLocation = "gym"
    private var savedExerciseType = "compound"
    private var savedAvailableDays = setOf("Monday", "Wednesday", "Friday")
    private var savedJointHealthStatus = "No Limitations"
    private var savedJointLimitations = emptySet<String>()

    // Account & Profile
    val userName = MutableStateFlow("")
    val userEmail = MutableStateFlow("")
    val personaArchetype = MutableStateFlow("Active Archetype")

    // Limitations master catalog
    private val _availableLimitations = MutableStateFlow<List<LimitationDto>>(emptyList())
    val availableLimitations: StateFlow<List<LimitationDto>> = _availableLimitations.asStateFlow()

    // App Preferences
    val notificationsEnabled: StateFlow<Boolean> = settingsRepository.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // Dialog & Feedback UI states
    val isUpdating = MutableStateFlow(false)
    val snackbarMessage = MutableStateFlow<String?>(null)

    val showConfirmDialog = MutableStateFlow(false)
    val confirmDialogTitle = MutableStateFlow("Update Your Workout Plan?")
    val confirmDialogMessage = MutableStateFlow("")

    val showChangePasswordDialog = MutableStateFlow(false)
    val showAboutDialog = MutableStateFlow(false)
    val showJointHealthDialog = MutableStateFlow(false)

    init {
        loadSettings()
    }

    fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            settingsRepository.getSettings().fold(
                onSuccess = { data ->
                    userName.value = data.user.name
                    userEmail.value = data.user.email

                    data.profile?.let { prof ->
                        val loc = prof.workoutLocation ?: "gym"
                        val type = prof.exerciseType ?: "compound"
                        val days = prof.availableDays?.toSet() ?: setOf("Monday", "Wednesday", "Friday")
                        val status = prof.jointHealthStatus ?: "No Limitations"
                        val limits = prof.jointLimitations?.toSet() ?: emptySet()

                        workoutLocation.value = loc
                        exerciseType.value = type
                        availableDays.value = days
                        jointHealthStatus.value = status
                        jointLimitations.value = limits

                        savedWorkoutLocation = loc
                        savedExerciseType = type
                        savedAvailableDays = days
                        savedJointHealthStatus = status
                        savedJointLimitations = limits

                        personaArchetype.value = data.user.persona ?: "Active Archetype"
                    }

                    _availableLimitations.value = data.availableLimitations
                    _uiState.value = SettingsUiState.Idle
                },
                onFailure = { error ->
                    _uiState.value = SettingsUiState.Error(error.message ?: "Failed to load settings.")
                }
            )
        }
    }

    // --- Workout Environment switching ---
    fun selectWorkoutLocation(newLocation: String) {
        if (workoutLocation.value == newLocation) return
        workoutLocation.value = newLocation

        confirmDialogTitle.value = "Update Your Workout Plan?"
        confirmDialogMessage.value = "Your workout environment has changed. PersonaFit will update your upcoming workouts to match your new environment while keeping your completed workout history."
        showConfirmDialog.value = true
    }

    // --- Exercise Type switching ---
    fun selectExerciseType(newType: String) {
        if (exerciseType.value == newType) return
        exerciseType.value = newType

        confirmDialogTitle.value = "Update Your Workout Plan?"
        confirmDialogMessage.value = "Your exercise preference has changed. PersonaFit will update your upcoming workouts based on your new preference while keeping your completed workout history."
        showConfirmDialog.value = true
    }

    // --- Schedule editing ---
    fun toggleAvailableDay(day: String) {
        val current = availableDays.value.toMutableSet()
        if (current.contains(day)) {
            if (current.size > 1) { // Require at least one workout day
                current.remove(day)
            } else {
                snackbarMessage.value = "Please keep at least one available workout day."
                return
            }
        } else {
            current.add(day)
        }
        availableDays.value = current
    }

    fun promptSaveSchedule() {
        if (availableDays.value == savedAvailableDays) {
            snackbarMessage.value = "No changes made to workout schedule."
            return
        }
        confirmDialogTitle.value = "Update Your Workout Schedule?"
        confirmDialogMessage.value = "Your workout schedule has changed. PersonaFit will update your upcoming workouts based on your new availability while keeping your completed workout history."
        showConfirmDialog.value = true
    }

    // --- Joint Health editing ---
    fun selectNoLimitations() {
        jointHealthStatus.value = "No Limitations"
        jointLimitations.value = emptySet()
    }

    fun toggleJointLimitation(limitationName: String) {
        val current = jointLimitations.value.toMutableSet()
        if (current.contains(limitationName)) {
            current.remove(limitationName)
        } else {
            current.add(limitationName)
        }
        jointLimitations.value = current

        if (current.isEmpty()) {
            jointHealthStatus.value = "No Limitations"
        } else {
            jointHealthStatus.value = "Has Limitations"
        }
    }

    fun promptSaveJointHealth() {
        showJointHealthDialog.value = false
        if (jointHealthStatus.value == savedJointHealthStatus && jointLimitations.value == savedJointLimitations) {
            return
        }
        confirmDialogTitle.value = "Update Joint Health Rules?"
        confirmDialogMessage.value = "Your joint health limitations have changed. PersonaFit will update your upcoming workouts using safe orthopedic rules while keeping your completed workout history."
        showConfirmDialog.value = true
    }

    // --- Confirmation Dialog Actions ---
    fun confirmPendingUpdate() {
        showConfirmDialog.value = false
        viewModelScope.launch {
            isUpdating.value = true

            // Match limitation names to IDs if available
            val matchedIds = _availableLimitations.value
                .filter { item ->
                    jointLimitations.value.any { name ->
                        item.name.contains(name, ignoreCase = true) ||
                        (item.affectedBodyPart?.contains(name, ignoreCase = true) == true) ||
                        item.code.contains(name.replace(" ", "_"), ignoreCase = true)
                    }
                }
                .map { it.id }

            val request = UpdatePreferencesRequest(
                workoutLocation = workoutLocation.value,
                exerciseType = exerciseType.value,
                availableDays = availableDays.value.toList(),
                jointHealthStatus = jointHealthStatus.value,
                jointLimitations = jointLimitations.value.toList(),
                limitationIds = matchedIds
            )

            settingsRepository.updatePreferences(request).fold(
                onSuccess = { msg ->
                    savedWorkoutLocation = workoutLocation.value
                    savedExerciseType = exerciseType.value
                    savedAvailableDays = availableDays.value
                    savedJointHealthStatus = jointHealthStatus.value
                    savedJointLimitations = jointLimitations.value

                    snackbarMessage.value = "Your upcoming workout plan has been updated."
                    isUpdating.value = false
                },
                onFailure = { error ->
                    // Revert to saved
                    cancelPendingUpdate()
                    snackbarMessage.value = error.message ?: "Failed to update preferences."
                    isUpdating.value = false
                }
            )
        }
    }

    fun cancelPendingUpdate() {
        showConfirmDialog.value = false
        workoutLocation.value = savedWorkoutLocation
        exerciseType.value = savedExerciseType
        availableDays.value = savedAvailableDays
        jointHealthStatus.value = savedJointHealthStatus
        jointLimitations.value = savedJointLimitations
    }

    // --- App Settings ---
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
            snackbarMessage.value = if (enabled) "Daily workout notifications enabled." else "Notifications disabled."
        }
    }

    // --- Account / Password ---
    fun changePassword(oldPass: String, newPass: String, confirmPass: String) {
        if (oldPass.isBlank() || newPass.isBlank()) {
            snackbarMessage.value = "Please fill in all password fields."
            return
        }
        if (newPass.length < 6) {
            snackbarMessage.value = "New password must be at least 6 characters."
            return
        }
        if (newPass != confirmPass) {
            snackbarMessage.value = "New passwords do not match."
            return
        }

        viewModelScope.launch {
            isUpdating.value = true
            settingsRepository.changePassword(oldPass, newPass).fold(
                onSuccess = { msg ->
                    showChangePasswordDialog.value = false
                    snackbarMessage.value = msg
                    isUpdating.value = false
                },
                onFailure = { error ->
                    snackbarMessage.value = error.message ?: "Failed to change password."
                    isUpdating.value = false
                }
            )
        }
    }

    fun clearSnackbar() {
        snackbarMessage.value = null
    }

    class Factory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(settingsRepository) as T
        }
    }
}
