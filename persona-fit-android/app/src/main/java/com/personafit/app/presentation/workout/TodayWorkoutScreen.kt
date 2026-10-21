package com.personafit.app.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.data.remote.dto.PrescribedExerciseDto
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayWorkoutScreen(
    viewModel: WorkoutViewModel,
    onStartWorkout: (Long?, String) -> Unit,
    onNavigateToExerciseDetail: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val plan by viewModel.todayPlan.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Today's Prescribed Routine",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadTodayWorkout() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = ElectricMint
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSlateBg
                )
            )
        },
        containerColor = DarkSlateBg,
        bottomBar = {
            plan?.let { p ->
                if (p.isRestDay != true && p.planId != null) {
                    Surface(
                        color = DarkSlateBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        PrimaryButton(
                            text = "Start Workout Session ⚡",
                            onClick = { onStartWorkout(p.planId, p.planName) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState is WorkoutUiState.Loading && plan == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ElectricMint)
                }
            } else if (plan != null) {
                val currentPlan = plan!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp)
                ) {
                    if (currentPlan.isRestDay == true) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceCardDark,
                                border = BorderStroke(1.dp, BorderDark)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ElectricMint.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "ACTIVE RECOVERY & REST",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = ElectricMint,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 10.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = currentPlan.planName,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = TextPrimary
                                        )
                                    )

                                    Text(
                                        text = currentPlan.restDayMessage ?: "Today is a designated rest and recovery day based on your weekly schedule.",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                                        modifier = Modifier.padding(top = 6.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SurfaceElevatedDark,
                                        border = BorderStroke(1.dp, BorderDark),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = "Your Weekly Schedule: ${currentPlan.availableDays?.joinToString(", ") ?: "Custom"}",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = ElectricMint,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            if (!currentPlan.nextWorkoutDay.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Next Workout Day: ${currentPlan.nextWorkoutDay}",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "RECOVERY PROTOCOL",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = NeonCyan,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "• Hydration: Maintain fluid and electrolyte balance\n• Joint Decompression: Light stretching, walking, foam rolling\n• Restorative Sleep: Target 7–9 hours for muscular protein synthesis",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            lineHeight = 20.sp
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        // Header Overview Card
                        item {
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
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ElectricMint.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = currentPlan.personaArchetype.replace('_', ' ').uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = ElectricMint,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 10.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = null,
                                            tint = TextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "~${currentPlan.estimatedDurationMinutes} mins",
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = currentPlan.planName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )

                                Text(
                                    text = "Focus: ${currentPlan.focusArea} • ${currentPlan.totalExercises} Exercises",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                if (currentPlan.substitutionsAppliedCount > 0) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SafeGreen.copy(alpha = 0.15f),
                                        border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Shield,
                                                contentDescription = null,
                                                tint = SafeGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${currentPlan.substitutionsAppliedCount} exercises substituted to protect your joint profile.",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = SafeGreen,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }
                                }
                            }
                        }

                        // Section Heading
                        item {
                            Text(
                                text = "PRESCRIBED MOVEMENTS",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = ElectricMint,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                ),
                                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                            )
                        }

                        // Exercises List
                        itemsIndexed(currentPlan.exercises) { index, exerciseItem ->
                            ExercisePrescriptionRow(
                                item = exerciseItem,
                                order = index + 1,
                                onViewDetails = {
                                    viewModel.loadExerciseDetail(exerciseItem.exercise.id)
                                    onNavigateToExerciseDetail(exerciseItem.exercise.id)
                                }
                            )
                        }
                    }
                }
            } else if (uiState is WorkoutUiState.Error) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = WarningRed,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = (uiState as WorkoutUiState.Error).message,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Retry",
                        onClick = { viewModel.loadTodayWorkout() }
                    )
                }
            }
        }
    }
}

@Composable
fun ExercisePrescriptionRow(
    item: PrescribedExerciseDto,
    order: Int,
    onViewDetails: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetails() },
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise Number
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(SurfaceElevatedDark, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$order",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = ElectricMint
                    )
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.exercise.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    if (item.isSubstituted) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = SafeGreen.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "SAFE SWAP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SafeGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.targetSets} sets × ${item.targetRepsMin}-${item.targetRepsMax}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CoralAccent
                        )
                    )
                    Text(text = "•", color = TextMuted)
                    Text(
                        text = item.exercise.targetMuscle,
                        style = MaterialTheme.typography.bodySmall.copy(color = NeonCyan, fontWeight = FontWeight.SemiBold)
                    )
                    Text(text = "•", color = TextMuted)
                    Text(
                        text = "Rest ${item.restSeconds}s",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            IconButton(onClick = onViewDetails) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "View Guide",
                    tint = TextSecondary
                )
            }
        }
    }
}
