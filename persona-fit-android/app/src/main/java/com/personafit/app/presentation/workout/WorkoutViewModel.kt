package com.personafit.app.presentation.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personafit.app.data.remote.dto.*
import com.personafit.app.data.repository.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ActiveSetLog(
    val setNumber: Int,
    val weightKg: String = "",
    val repsCompleted: String = "",
    val isCompleted: Boolean = false
)

data class ActiveExerciseState(
    val exerciseDetail: ExerciseDetailDto,
    val targetSets: Int,
    val targetRepsMin: Int,
    val targetRepsMax: Int,
    val restSeconds: Int,
    val isSubstituted: Boolean,
    val substitutionReason: String?,
    val sets: MutableList<ActiveSetLog>
)

sealed class WorkoutUiState {
    object Idle : WorkoutUiState()
    object Loading : WorkoutUiState()
    data class PlanLoaded(val plan: TailoredPlanData) : WorkoutUiState()
    data class InProgress(val logId: Long, val sessionName: String) : WorkoutUiState()
    data class Completed(val response: CompleteWorkoutResponse) : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
}

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Idle)
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    // Today's Prescribed Plan
    private val _todayPlan = MutableStateFlow<TailoredPlanData?>(null)
    val todayPlan: StateFlow<TailoredPlanData?> = _todayPlan.asStateFlow()

    // Active Session State
    var activeWorkoutLogId = MutableStateFlow<Long?>(null)
    var activeSessionName = MutableStateFlow("")
    var activeExercises = MutableStateFlow<List<ActiveExerciseState>>(emptyList())
    var currentExerciseIndex = MutableStateFlow(0)

    // Elapsed Session Timer (Seconds)
    var elapsedSessionSeconds = MutableStateFlow(0)
    private var sessionTimerJob: Job? = null

    // Rest Countdown Timer (Seconds)
    var restSecondsRemaining = MutableStateFlow(0)
    var totalRestSeconds = MutableStateFlow(60)
    var isRestTimerRunning = MutableStateFlow(false)
    private var restTimerJob: Job? = null

    // Swap Alternatives State
    private val _swapAlternatives = MutableStateFlow<List<ExerciseDetailDto>>(emptyList())
    val swapAlternatives: StateFlow<List<ExerciseDetailDto>> = _swapAlternatives.asStateFlow()
    val isFetchingAlternatives = MutableStateFlow(false)

    // Selected Exercise for Details Dialog (legacy/quick ref)
    var selectedExerciseForDetails = MutableStateFlow<ExerciseDetailDto?>(null)

    // Exercise Progress Tracking
    var completedExerciseIds = MutableStateFlow<Set<Long>>(emptySet())

    // Dedicated Exercise Detail Screen State
    private val _detailedExercise = MutableStateFlow<ExerciseDetailDto?>(null)
    val detailedExercise: StateFlow<ExerciseDetailDto?> = _detailedExercise.asStateFlow()
    val isLoadingExerciseDetail = MutableStateFlow(false)
    val userProfile = MutableStateFlow<ProfileSummaryDto?>(null)
    val userLimitations = MutableStateFlow<List<LimitationDto>>(emptyList())

    init {
        loadTodayWorkout()
    }

    fun loadTodayWorkout() {
        viewModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            val result = workoutRepository.getTodayWorkout()
            result.fold(
                onSuccess = { plan ->
                    _todayPlan.value = plan
                    _uiState.value = WorkoutUiState.PlanLoaded(plan)
                },
                onFailure = { error ->
                    _uiState.value = WorkoutUiState.Error(error.message ?: "Failed to load workout.")
                }
            )
        }
    }

    fun startWorkoutSession(planId: Long?, sessionName: String, initialExerciseId: Long? = null) {
        viewModelScope.launch {
            // If session is already in progress, switch to the requested exercise without resetting session
            if (activeWorkoutLogId.value != null && activeExercises.value.isNotEmpty()) {
                if (initialExerciseId != null) {
                    val foundIdx = activeExercises.value.indexOfFirst { it.exerciseDetail.id == initialExerciseId }
                    if (foundIdx >= 0) {
                        currentExerciseIndex.value = foundIdx
                        return@launch
                    }
                } else {
                    return@launch
                }
            }

            _uiState.value = WorkoutUiState.Loading
            completedExerciseIds.value = emptySet()

            // 1. Ensure plan is loaded
            var plan = _todayPlan.value
            if (plan == null) {
                workoutRepository.getTodayWorkout().onSuccess {
                    _todayPlan.value = it
                    plan = it
                }
            }

            val targetPlanId = planId?.takeIf { it > 0L } ?: plan?.planId
            val result = workoutRepository.startWorkout(targetPlanId, sessionName)
            result.fold(
                onSuccess = { res ->
                    activeWorkoutLogId.value = res.id
                    activeSessionName.value = sessionName

                    // Initialize active exercises from today's plan
                    val currentPlan = plan ?: _todayPlan.value
                    if (currentPlan != null && currentPlan.exercises.isNotEmpty()) {
                        activeExercises.value = currentPlan.exercises.map { pe ->
                            val setsList = (1..pe.targetSets).map { setNum ->
                                ActiveSetLog(setNumber = setNum, repsCompleted = pe.targetRepsMin.toString())
                            }.toMutableList()

                            ActiveExerciseState(
                                exerciseDetail = pe.exercise,
                                targetSets = pe.targetSets,
                                targetRepsMin = pe.targetRepsMin,
                                targetRepsMax = pe.targetRepsMax,
                                restSeconds = pe.restSeconds,
                                isSubstituted = pe.isSubstituted,
                                substitutionReason = pe.substitutionReason,
                                sets = setsList
                            )
                        }
                    } else if (_detailedExercise.value != null) {
                        val ex = _detailedExercise.value!!
                        activeExercises.value = listOf(
                            ActiveExerciseState(
                                exerciseDetail = ex,
                                targetSets = 3,
                                targetRepsMin = 8,
                                targetRepsMax = 12,
                                restSeconds = 60,
                                isSubstituted = false,
                                substitutionReason = null,
                                sets = (1..3).map { ActiveSetLog(setNumber = it, repsCompleted = "10") }.toMutableList()
                            )
                        )
                    }

                    if (initialExerciseId != null) {
                        val foundIdx = activeExercises.value.indexOfFirst { it.exerciseDetail.id == initialExerciseId }
                        currentExerciseIndex.value = if (foundIdx >= 0) foundIdx else 0
                    } else {
                        currentExerciseIndex.value = 0
                    }

                    _uiState.value = WorkoutUiState.InProgress(res.id, sessionName)
                    startSessionTimer()
                },
                onFailure = { error ->
                    // Fallback to local session if network error so user can still workout
                    val currentPlan = plan ?: _todayPlan.value
                    if (currentPlan != null && currentPlan.exercises.isNotEmpty()) {
                        activeWorkoutLogId.value = 1L
                        activeSessionName.value = sessionName
                        activeExercises.value = currentPlan.exercises.map { pe ->
                            val setsList = (1..pe.targetSets).map { setNum ->
                                ActiveSetLog(setNumber = setNum, repsCompleted = pe.targetRepsMin.toString())
                            }.toMutableList()

                            ActiveExerciseState(
                                exerciseDetail = pe.exercise,
                                targetSets = pe.targetSets,
                                targetRepsMin = pe.targetRepsMin,
                                targetRepsMax = pe.targetRepsMax,
                                restSeconds = pe.restSeconds,
                                isSubstituted = pe.isSubstituted,
                                substitutionReason = pe.substitutionReason,
                                sets = setsList
                            )
                        }

                        if (initialExerciseId != null) {
                            val foundIdx = activeExercises.value.indexOfFirst { it.exerciseDetail.id == initialExerciseId }
                            currentExerciseIndex.value = if (foundIdx >= 0) foundIdx else 0
                        } else {
                            currentExerciseIndex.value = 0
                        }

                        _uiState.value = WorkoutUiState.InProgress(1L, sessionName)
                        startSessionTimer()
                    } else if (_detailedExercise.value != null) {
                        val ex = _detailedExercise.value!!
                        activeWorkoutLogId.value = 1L
                        activeSessionName.value = sessionName
                        activeExercises.value = listOf(
                            ActiveExerciseState(
                                exerciseDetail = ex,
                                targetSets = 3,
                                targetRepsMin = 8,
                                targetRepsMax = 12,
                                restSeconds = 60,
                                isSubstituted = false,
                                substitutionReason = null,
                                sets = (1..3).map { ActiveSetLog(setNumber = it, repsCompleted = "10") }.toMutableList()
                            )
                        )
                        currentExerciseIndex.value = 0
                        _uiState.value = WorkoutUiState.InProgress(1L, sessionName)
                        startSessionTimer()
                    } else {
                        _uiState.value = WorkoutUiState.Error(error.message ?: "Failed to start workout.")
                    }
                }
            )
        }
    }

    fun advanceToNextExercise() {
        val list = activeExercises.value
        val idx = currentExerciseIndex.value
        if (idx in list.indices) {
            val exId = list[idx].exerciseDetail.id
            completedExerciseIds.value = completedExerciseIds.value + exId
            list[idx].sets.forEachIndexed { sIdx, set ->
                list[idx].sets[sIdx] = set.copy(isCompleted = true)
            }
        }
        if (idx < list.size - 1) {
            currentExerciseIndex.value = idx + 1
        }
    }

    fun markCurrentExerciseCompleted() {
        val list = activeExercises.value
        val idx = currentExerciseIndex.value
        if (idx in list.indices) {
            val exId = list[idx].exerciseDetail.id
            completedExerciseIds.value = completedExerciseIds.value + exId
        }
    }

    fun loadExerciseDetail(exerciseId: Long) {
        if (userProfile.value == null) {
            viewModelScope.launch {
                workoutRepository.getAssessmentSummary().onSuccess { summary ->
                    userProfile.value = summary.profile
                    userLimitations.value = summary.activeLimitations
                }
            }
        }

        if (_detailedExercise.value?.id != exerciseId) {
            val inPlan = _todayPlan.value?.exercises?.find { it.exercise.id == exerciseId }?.exercise
            if (inPlan != null) {
                _detailedExercise.value = inPlan
            } else {
                val inActive = activeExercises.value.find { it.exerciseDetail.id == exerciseId }?.exerciseDetail
                if (inActive != null) {
                    _detailedExercise.value = inActive
                } else {
                    _detailedExercise.value = null
                }
            }
        }

        viewModelScope.launch {
            if (_detailedExercise.value == null || _detailedExercise.value?.id != exerciseId) {
                isLoadingExerciseDetail.value = true
            }
            workoutRepository.getExercise(exerciseId).fold(
                onSuccess = {
                    _detailedExercise.value = it
                    isLoadingExerciseDetail.value = false
                },
                onFailure = {
                    isLoadingExerciseDetail.value = false
                }
            )
        }
    }

    fun setDetailedExercise(exercise: ExerciseDetailDto) {
        _detailedExercise.value = exercise
    }

    private fun startSessionTimer() {
        sessionTimerJob?.cancel()
        elapsedSessionSeconds.value = 0
        sessionTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                elapsedSessionSeconds.value += 1
            }
        }
    }

    fun updateSetInput(exerciseIndex: Int, setIndex: Int, weight: String? = null, reps: String? = null) {
        val list = activeExercises.value.toMutableList()
        if (exerciseIndex in list.indices) {
            val ex = list[exerciseIndex]
            if (setIndex in ex.sets.indices) {
                val currentSet = ex.sets[setIndex]
                ex.sets[setIndex] = currentSet.copy(
                    weightKg = weight ?: currentSet.weightKg,
                    repsCompleted = reps ?: currentSet.repsCompleted
                )
                activeExercises.value = list
            }
        }
    }

    fun toggleSetComplete(exerciseIndex: Int, setIndex: Int) {
        val list = activeExercises.value.toMutableList()
        if (exerciseIndex in list.indices) {
            val ex = list[exerciseIndex]
            if (setIndex in ex.sets.indices) {
                val currentSet = ex.sets[setIndex]
                val newCompleted = !currentSet.isCompleted
                ex.sets[setIndex] = currentSet.copy(isCompleted = newCompleted)
                activeExercises.value = list

                if (newCompleted) {
                    val wt = currentSet.weightKg.toFloatOrNull() ?: 0f
                    val rp = currentSet.repsCompleted.toIntOrNull() ?: ex.targetRepsMin
                    logSetCompleted(exerciseIndex, setIndex, wt, rp)
                }
            }
        }
    }

    fun logSetCompleted(exerciseIndex: Int, setIndex: Int, weightKg: Float, reps: Int) {
        val logId = activeWorkoutLogId.value ?: return
        val exercises = activeExercises.value.toMutableList()
        val currentEx = exercises[exerciseIndex]
        val setItem = currentEx.sets[setIndex]

        viewModelScope.launch {
            workoutRepository.logSet(
                workoutLogId = logId,
                exerciseId = currentEx.exerciseDetail.id,
                setNumber = setItem.setNumber,
                weightKg = weightKg,
                repsCompleted = reps
            )

            // Trigger Rest Timer
            startRestTimer(currentEx.restSeconds)
        }
    }

    fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        totalRestSeconds.value = seconds
        restSecondsRemaining.value = seconds
        isRestTimerRunning.value = true

        restTimerJob = viewModelScope.launch {
            while (restSecondsRemaining.value > 0) {
                delay(1000)
                restSecondsRemaining.value -= 1
            }
            isRestTimerRunning.value = false
        }
    }

    fun add30SecondsToRest() {
        restSecondsRemaining.value += 30
        totalRestSeconds.value += 30
    }

    fun skipRestTimer() {
        restTimerJob?.cancel()
        restSecondsRemaining.value = 0
        isRestTimerRunning.value = false
    }

    fun loadSwapAlternatives(exerciseId: Long) {
        viewModelScope.launch {
            isFetchingAlternatives.value = true
            workoutRepository.getSafeAlternatives(exerciseId).fold(
                onSuccess = { data ->
                    _swapAlternatives.value = data.alternatives
                    isFetchingAlternatives.value = false
                },
                onFailure = {
                    isFetchingAlternatives.value = false
                }
            )
        }
    }

    fun performExerciseSwap(exerciseIndex: Int, newExercise: ExerciseDetailDto) {
        val exercises = activeExercises.value.toMutableList()
        val oldState = exercises[exerciseIndex]

        exercises[exerciseIndex] = oldState.copy(
            exerciseDetail = newExercise,
            isSubstituted = true,
            substitutionReason = "Live user swap during session"
        )
        activeExercises.value = exercises
        _swapAlternatives.value = emptyList()
    }

    fun completeWorkout(notes: String? = null) {
        val logId = activeWorkoutLogId.value
        sessionTimerJob?.cancel()
        restTimerJob?.cancel()

        val list = activeExercises.value
        val idx = currentExerciseIndex.value
        if (idx in list.indices) {
            val exId = list[idx].exerciseDetail.id
            completedExerciseIds.value = completedExerciseIds.value + exId
        }

        val completedIds = completedExerciseIds.value.toList()
        val totalCompleted = if (completedIds.isNotEmpty()) completedIds.size else list.size

        if (logId == null) {
            // Local fallback completion
            val totalVol = list.flatMap { it.sets }.filter { it.isCompleted }.sumOf {
                ((it.weightKg.toFloatOrNull() ?: 0f) * (it.repsCompleted.toIntOrNull() ?: 0)).toDouble()
            }.toFloat()

            _uiState.value = WorkoutUiState.Completed(
                CompleteWorkoutResponse(
                    workoutLogId = 1L,
                    sessionName = activeSessionName.value.ifBlank { "Workout Session" },
                    durationMinutes = (elapsedSessionSeconds.value / 60f),
                    exercisesCompleted = totalCompleted,
                    totalVolumeKg = totalVol
                )
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            val result = workoutRepository.completeWorkout(
                workoutLogId = logId,
                totalDurationSeconds = elapsedSessionSeconds.value,
                completedExerciseIds = completedIds,
                totalExercisesCompleted = totalCompleted,
                notes = notes
            )
            result.fold(
                onSuccess = { res ->
                    _uiState.value = WorkoutUiState.Completed(res)
                },
                onFailure = { error ->
                    // Fallback to local completed response so user is never stuck
                    val totalVol = list.flatMap { it.sets }.filter { it.isCompleted }.sumOf {
                        ((it.weightKg.toFloatOrNull() ?: 0f) * (it.repsCompleted.toIntOrNull() ?: 0)).toDouble()
                    }.toFloat()

                    _uiState.value = WorkoutUiState.Completed(
                        CompleteWorkoutResponse(
                            workoutLogId = logId,
                            sessionName = activeSessionName.value.ifBlank { "Workout Session" },
                            durationMinutes = (elapsedSessionSeconds.value / 60f),
                            exercisesCompleted = totalCompleted,
                            totalVolumeKg = totalVol
                        )
                    )
                }
            )
        }
    }

    class Factory(private val workoutRepository: WorkoutRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WorkoutViewModel(workoutRepository) as T
        }
    }
}
