package com.personafit.app.presentation.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.data.remote.dto.ExerciseDetailDto
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.common.SecondaryButton
import com.personafit.app.presentation.theme.*

private enum class WorkoutGuideModalType {
    PROPER_FORM,
    COMMON_INJURIES,
    INJURY_PREVENTION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    viewModel: WorkoutViewModel,
    onNavigateToDashboard: () -> Unit,
    onNavigateToExerciseDetail: (Long) -> Unit = {}
) {
    val activeExercises by viewModel.activeExercises.collectAsState()
    val currentIndex by viewModel.currentExerciseIndex.collectAsState()
    val completedIds by viewModel.completedExerciseIds.collectAsState()
    val elapsedSeconds by viewModel.elapsedSessionSeconds.collectAsState()
    val restRemaining by viewModel.restSecondsRemaining.collectAsState()
    val isRestRunning by viewModel.isRestTimerRunning.collectAsState()
    val swapAlternatives by viewModel.swapAlternatives.collectAsState()
    val isFetchingAlts by viewModel.isFetchingAlternatives.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var showSwapSheet by remember { mutableStateOf(false) }
    var showFinishConfirmDialog by remember { mutableStateOf(false) }
    var activeGuideModal by remember { mutableStateOf<WorkoutGuideModalType?>(null) }

    // Live Exercise Swap Bottom Sheet
    if (showSwapSheet && activeExercises.isNotEmpty() && currentIndex in activeExercises.indices) {
        val currentEx = activeExercises[currentIndex]
        ExerciseSwapBottomSheet(
            originalExerciseName = currentEx.exerciseDetail.name,
            alternatives = swapAlternatives,
            isLoading = isFetchingAlts,
            onSelectAlternative = { newAlt: ExerciseDetailDto ->
                viewModel.performExerciseSwap(currentIndex, newAlt)
                showSwapSheet = false
            },
            onDismiss = { showSwapSheet = false }
        )
    }

    // Modal Guide Sheet (Proper Form / Common Injuries / Injury Prevention without losing timer)
    if (activeGuideModal != null && activeExercises.isNotEmpty() && currentIndex in activeExercises.indices) {
        val currentEx = activeExercises[currentIndex]
        ModalBottomSheet(
            onDismissRequest = { activeGuideModal = null },
            containerColor = SurfaceCardDark,
            dragHandle = { BottomSheetDefaults.DragHandle(color = BorderDark) }
        ) {
            WorkoutGuideSheetContent(
                type = activeGuideModal!!,
                exercise = currentEx.exerciseDetail,
                onDismiss = { activeGuideModal = null },
                onNavigateToExerciseDetail = { exId ->
                    activeGuideModal = null
                    onNavigateToExerciseDetail(exId)
                }
            )
        }
    }

    // Workout Completion Celebration Dialog
    if (uiState is WorkoutUiState.Completed) {
        WorkoutCompletionDialog(
            response = (uiState as WorkoutUiState.Completed).response,
            onReturnToDashboard = onNavigateToDashboard
        )
    }

    // Finish Confirmation Dialog
    if (showFinishConfirmDialog) {
        val totalCount = activeExercises.size
        val currentCompletedCount = (completedIds + (activeExercises.getOrNull(currentIndex)?.exerciseDetail?.id?.let { setOf(it) } ?: emptySet())).size
        val completedDisplay = currentCompletedCount.coerceAtMost(totalCount)

        AlertDialog(
            onDismissRequest = { showFinishConfirmDialog = false },
            title = {
                Text(
                    text = "Complete Workout Session?",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "You have completed $completedDisplay of $totalCount exercises.",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Elapsed Time: ${formatTime(elapsedSeconds)}\nSave your workout metrics to your progress profile?",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showFinishConfirmDialog = false
                        viewModel.completeWorkout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricMint)
                ) {
                    Text("Complete & Save", color = TextOnAccent, fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishConfirmDialog = false }) {
                    Text("Continue Workout", color = TextSecondary)
                }
            },
            containerColor = SurfaceCardDark
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(SafeGreen, shape = CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE WORKOUT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.2.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            // 1. Live Workout Timer
                            Text(
                                text = formatTime(elapsedSeconds),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                            )
                        }

                        // 6. Finish Workout Button
                        Button(
                            onClick = { showFinishConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CoralAccent),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Finish", color = Color.White, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSlateBg)
            )
        },
        containerColor = DarkSlateBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (activeExercises.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState is WorkoutUiState.Error) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (uiState as WorkoutUiState.Error).message,
                                color = WarningRed,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            PrimaryButton(
                                text = "Retry Starting Workout",
                                onClick = { viewModel.startWorkoutSession(null, "Workout Session") }
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = ElectricMint)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading active workout session...",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                val currentEx = activeExercises[currentIndex.coerceIn(activeExercises.indices)]
                val totalExercises = activeExercises.size
                val currentExId = currentEx.exerciseDetail.id
                val isCurrentMarkedDone = completedIds.contains(currentExId)
                val completedCount = completedIds.size
                val remainingCount = (totalExercises - completedCount).coerceAtLeast(0)
                val progressFraction = (completedCount.toFloat() / totalExercises.toFloat()).coerceIn(0f, 1f)
                val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "progress_anim")
                val progressPercent = (progressFraction * 100).toInt()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    // 2. Current Exercise Header & Target Muscles + Swap Button
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceCardDark,
                        border = BorderStroke(1.dp, BorderDark)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ElectricMint.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "EXERCISE ${currentIndex + 1} OF $totalExercises",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = ElectricMint,
                                                fontWeight = FontWeight.ExtraBold,
                                                letterSpacing = 1.sp,
                                                fontSize = 10.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    if (isCurrentMarkedDone) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = SafeGreen.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "COMPLETED ✓",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = SafeGreen,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 10.sp
                                                ),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Guide Button
                                    OutlinedButton(
                                        onClick = {
                                            onNavigateToExerciseDetail(currentEx.exerciseDetail.id)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, ElectricMint),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = ElectricMint,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Guide",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = ElectricMint,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }

                                    // 7. Safe Exercise Swap Button
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.loadSwapAlternatives(currentEx.exerciseDetail.id)
                                            showSwapSheet = true
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, NeonCyan),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.SwapHoriz,
                                            contentDescription = null,
                                            tint = NeonCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Swap",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = NeonCyan,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentEx.exerciseDetail.name,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = NeonCyan.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = currentEx.exerciseDetail.targetMuscle.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = NeonCyan,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Text(text = "•", color = TextMuted)

                                Text(
                                    text = currentEx.exerciseDetail.equipmentType,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )

                                Text(text = "•", color = TextMuted)

                                Text(
                                    text = "${currentEx.targetSets} sets × ${currentEx.targetRepsMin}-${currentEx.targetRepsMax} reps",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Large Exercise Biomechanical Animation
                    ExerciseAnimationView(
                        exerciseName = currentEx.exerciseDetail.name,
                        targetMuscle = currentEx.exerciseDetail.targetMuscle,
                        imageUrl = currentEx.exerciseDetail.imageUrl,
                        heightDp = 280,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Three Quick Guide Buttons (Proper Form, Common Injuries, Injury Prevention)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { activeGuideModal = WorkoutGuideModalType.PROPER_FORM },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, ElectricMint.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceCardDark),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = ElectricMint,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Proper Form",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricMint,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }

                        OutlinedButton(
                            onClick = { activeGuideModal = WorkoutGuideModalType.COMMON_INJURIES },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, CautionAmber.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceCardDark),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = CautionAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Injuries",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CautionAmber,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }

                        OutlinedButton(
                            onClick = { activeGuideModal = WorkoutGuideModalType.INJURY_PREVENTION },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceCardDark),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = SafeGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Prevention",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SafeGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 5. Automatic Progress Tracking Card (REPLACED Log Sets Table completely!)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceCardDark,
                        border = BorderStroke(1.dp, BorderDark)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SESSION PROGRESS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Text(
                                    text = "Progress: $progressPercent%",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = NeonCyan,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Animated Progress Bar
                            LinearProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = ElectricMint,
                                trackColor = SurfaceElevatedDark
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Metrics Grid: Completed | Remaining | Current
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Current",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                    )
                                    Text(
                                        text = "Exercise ${currentIndex + 1} of $totalExercises",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Completed",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                    )
                                    Text(
                                        text = "$completedCount of $totalExercises",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = SafeGreen,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Remaining",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                    )
                                    Text(
                                        text = "$remainingCount to go",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Rest timer trigger button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Prescribed Rest: ${currentEx.restSeconds}s",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )

                                TextButton(
                                    onClick = { viewModel.startRestTimer(currentEx.restSeconds) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = ElectricMint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Start Rest Interval",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = ElectricMint,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 5. Workout Progression Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (currentIndex > 0) {
                            SecondaryButton(
                                text = "← Previous",
                                onClick = { viewModel.currentExerciseIndex.value -= 1 },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (currentIndex < totalExercises - 1) {
                            // Primary Next Exercise Button
                            PrimaryButton(
                                text = "Next Exercise →",
                                onClick = { viewModel.advanceToNextExercise() },
                                modifier = Modifier.weight(if (currentIndex > 0) 1.5f else 1f)
                            )
                        } else {
                            // Finish Session Button
                            PrimaryButton(
                                text = "Finish Session 🎉",
                                onClick = { showFinishConfirmDialog = true },
                                modifier = Modifier.weight(1f),
                                containerColor = SafeGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            // 3ft-Glanceable Rest Countdown Timer Overlay
            AnimatedVisibility(
                visible = isRestRunning,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceElevatedDark,
                    border = BorderStroke(2.dp, ElectricMint),
                    shadowElevation = 16.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "REST INTERVAL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricMint,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.5.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = formatTime(restRemaining),
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { viewModel.add30SecondsToRest() },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardDark)
                            ) {
                                Text("+30s", color = TextPrimary, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel.skipRestTimer() },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricMint)
                            ) {
                                Text("Skip Rest", color = TextOnAccent, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Bottom sheet content for viewing Proper Form, Common Injuries, or Injury Prevention
 * without disrupting the live workout timer or state.
 */
@Composable
private fun WorkoutGuideSheetContent(
    type: WorkoutGuideModalType,
    exercise: ExerciseDetailDto,
    onDismiss: () -> Unit,
    onNavigateToExerciseDetail: (Long) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = when (type) {
                        WorkoutGuideModalType.PROPER_FORM -> "Proper Form Guide"
                        WorkoutGuideModalType.COMMON_INJURIES -> "Common Injuries & Risks"
                        WorkoutGuideModalType.INJURY_PREVENTION -> "Injury Prevention Cues"
                    },
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = when (type) {
                            WorkoutGuideModalType.PROPER_FORM -> ElectricMint
                            WorkoutGuideModalType.COMMON_INJURIES -> CautionAmber
                            WorkoutGuideModalType.INJURY_PREVENTION -> SafeGreen
                        }
                    )
                )
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            }

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = TextSecondary
                )
            }
        }

        // Looping Exercise Animation for Live Guide
        ExerciseAnimationView(
            exerciseName = exercise.name,
            targetMuscle = exercise.targetMuscle,
            imageUrl = exercise.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            heightDp = 180,
            mode = when (type) {
                WorkoutGuideModalType.PROPER_FORM -> AnimationMode.PROPER_FORM
                WorkoutGuideModalType.COMMON_INJURIES -> AnimationMode.COMMON_MISTAKES
                WorkoutGuideModalType.INJURY_PREVENTION -> AnimationMode.INJURY_PREVENTION
            },
            compact = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        when (type) {
            WorkoutGuideModalType.PROPER_FORM -> {
                Text(
                    text = "EXECUTION & POSTURE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = exercise.instructions,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, lineHeight = 22.sp)
                )

                if (!exercise.commonMistakes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "MISTAKES TO AVOID",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CautionAmber,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = exercise.commonMistakes,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, lineHeight = 22.sp)
                    )
                }
            }

            WorkoutGuideModalType.COMMON_INJURIES -> {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CautionAmber.copy(alpha = 0.10f),
                    border = BorderStroke(1.dp, CautionAmber.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Potential Risks for ${exercise.name}:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = CautionAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (!exercise.commonMistakes.isNullOrBlank()) {
                                "Main Risk Factors:\n${exercise.commonMistakes}\n\nStop Warning Signs: Sharp localized joint pinching, burning, or loss of control."
                            } else {
                                "Main Risk Factors: Moving with ballistic momentum, uncontrolled eccentric lowering, or letting joints collapse under load.\n\nStop Warning Signs: Immediate sharp pain or persistent joint clicking with pain."
                            },
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, lineHeight = 20.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Medical Disclaimer: PersonaFit guidance is educational. If acute or sharp pain occurs, stop immediately and consult a healthcare professional.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                )
            }

            WorkoutGuideModalType.INJURY_PREVENTION -> {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SafeGreen.copy(alpha = 0.10f),
                    border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Safety & Injury Prevention Cues:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SafeGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val cues = exercise.injuryPreventionCues?.takeIf { it.isNotBlank() }
                            ?: "• Control tempo: 2-3 seconds lowering, 1 second pause\n• Keep 2-3 reps in reserve (avoid failure)\n• Maintain steady breathing throughout all repetitions"
                        Text(
                            text = cues,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, lineHeight = 20.sp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = {
                onDismiss()
                onNavigateToExerciseDetail(exercise.id)
            },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, NeonCyan),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Open Full Exercise Guide 📖",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        PrimaryButton(
            text = "Got It",
            onClick = onDismiss
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun formatTime(totalSeconds: Int): String {
    val mins = totalSeconds / 60
    val secs = totalSeconds % 60
    return String.format("%02d:%02d", mins, secs)
}
