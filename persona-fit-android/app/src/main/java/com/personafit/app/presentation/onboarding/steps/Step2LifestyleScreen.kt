package com.personafit.app.presentation.onboarding.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.common.SecondaryButton
import com.personafit.app.presentation.onboarding.OnboardingViewModel
import com.personafit.app.presentation.theme.*

@Composable
fun Step2LifestyleScreen(
    viewModel: OnboardingViewModel
) {
    val availableDays by                                                        viewModel.availableDays.collectAsState()
    val trainingExperience by viewModel.trainingExperience.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Step 2: Lifestyle & Availability",
            style = MaterialTheme.typography.titleMedium.copy(
                color = ElectricMint,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = "Determines your tailored routine frequency and experience tier",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 20.dp)
        )

        // ==================== NEW SECTION 1: WEEKLY WORKOUT AVAILABILITY ====================
        Text(
            text = "Weekly Workout Availability",
            style = MaterialTheme.typography.titleSmall.copy(
                color = ElectricMint,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
        Text(
            text = "Select the days you are available to workout.",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        val daysOfWeek = listOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            daysOfWeek.forEach { day ->
                val isSelected = availableDays.contains(day)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleAvailableDay(day) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) SurfaceElevatedDark else SurfaceCardDark,
                    border = BorderStroke(1.5.dp, if (isSelected) ElectricMint else BorderDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { viewModel.toggleAvailableDay(day) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = ElectricMint,
                                    checkmarkColor = TextOnAccent,
                                    uncheckedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = day,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        }

                        if (isSelected) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ElectricMint.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, ElectricMint.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "Workout Day ✓",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ==================== NEW SECTION 2: STRENGTH TRAINING EXPERIENCE ====================
        Text(
            text = "Strength Training Experience",
            style = MaterialTheme.typography.titleSmall.copy(
                color = ElectricMint,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
        Text(
            text = "Your familiarity with structured resistance training",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        val expOptions = listOf(
            "beginner" to ("Beginner (< 6 months)" to "Focus on motor patterns, posture, and foundational strength"),
            "intermediate" to ("Intermediate (6 months - 2 years)" to "Progressive overload, volume periodization"),
            "advanced" to ("Advanced (2+ years)" to "Specialized splits, high-intensity techniques")
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            expOptions.forEach { (key, pair) ->
                val isSelected = trainingExperience == key
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.trainingExperience.value = key
                            viewModel.experience.value = key
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) SurfaceElevatedDark else SurfaceCardDark,
                    border = BorderStroke(1.5.dp, if (isSelected) ElectricMint else BorderDark)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                viewModel.trainingExperience.value = key
                                viewModel.experience.value = key
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = ElectricMint,
                                unselectedColor = TextMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = pair.first,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = pair.second,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isSelected) NeonCyan else TextSecondary
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = "← Back",
                onClick = { viewModel.previousStep() },
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = "Next: Joint Health →",
                onClick = { viewModel.nextStep() },
                modifier = Modifier.weight(1.5f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
