package com.personafit.app.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.personafit.app.data.remote.dto.ExerciseDetailDto
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*

@Composable
fun ExerciseDetailDialog(
    exercise: ExerciseDetailDto,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceCardDark,
            border = BorderStroke(1.dp, BorderDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ElectricMint.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = exercise.targetMuscle,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SurfaceElevatedDark
                            ) {
                                Text(
                                    text = exercise.equipmentType.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderDark)
                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Looping Exercise Animation Guide based on actual assets
                    ExerciseAnimationView(
                        exerciseName = exercise.name,
                        targetMuscle = exercise.targetMuscle,
                        imageUrl = exercise.imageUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        heightDp = 210,
                        mode = AnimationMode.PROPER_FORM,
                        compact = false
                    )

                    // Biomechanical Execution Instructions
                    Text(
                        text = "EXECUTION & POSTURE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = exercise.instructions,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                    )

                    // Injury Prevention Cues
                    if (!exercise.injuryPreventionCues.isNullOrBlank()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = SafeGreen.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.3f))
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = SafeGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Injury Prevention Cues",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = SafeGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = exercise.injuryPreventionCues,
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                                    )
                                }
                            }
                        }
                    }

                    // Common Biomechanical Mistakes
                    if (!exercise.commonMistakes.isNullOrBlank()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = CautionAmber.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, CautionAmber.copy(alpha = 0.3f))
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = CautionAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Common Mistakes to Avoid",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = CautionAmber,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = exercise.commonMistakes,
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                PrimaryButton(
                    text = "Got It",
                    onClick = onDismiss
                )
            }
        }
    }
}
