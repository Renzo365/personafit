package com.personafit.app.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.data.remote.dto.AssessmentSummaryData
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*

@Composable
fun AssessmentSummaryScreen(
    viewModel: OnboardingViewModel,
    onNavigateToDashboard: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadSummary()
    }

    val summaryData by viewModel.summaryData.collectAsState()
    val workoutLocationState by viewModel.workoutLocation.collectAsState()
    val exerciseTypeState by viewModel.exerciseType.collectAsState()
    val availableDaysState by viewModel.availableDays.collectAsState()
    val trainingExperienceState by viewModel.trainingExperience.collectAsState()
    val jointHealthStatusState by viewModel.jointHealthStatus.collectAsState()
    val jointLimitationsState by viewModel.jointLimitations.collectAsState()

    val profile = summaryData?.profile

    // Dynamic Workout
    val rawWorkout = profile?.workoutLocation ?: workoutLocationState
    val workoutDisplay = when (rawWorkout.lowercase()) {
        "home" -> "Home Workout"
        "gym" -> "Gym Workout"
        else -> if (rawWorkout.isNotBlank()) "${rawWorkout.replaceFirstChar { it.uppercase() }} Workout" else "Gym Workout"
    }

    // Dynamic Focus
    val rawFocus = profile?.exerciseType ?: exerciseTypeState
    val focusDisplay = when (rawFocus.lowercase()) {
        "compound" -> "Compound Exercise"
        "isolation" -> "Isolation Exercise"
        else -> if (rawFocus.isNotBlank()) "${rawFocus.replaceFirstChar { it.uppercase() }} Exercise" else "Compound Exercise"
    }

    // Dynamic Schedule
    val rawDays = (profile?.availableDays ?: availableDaysState.toList()).filter { it.isNotBlank() }
    val dayOrder = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val sortedDays = dayOrder.filter { day -> rawDays.any { it.equals(day, ignoreCase = true) } }
    val finalDays = if (sortedDays.isNotEmpty()) sortedDays else rawDays
    val scheduleDisplay = if (finalDays.isNotEmpty()) {
        if (finalDays.size <= 2) {
            finalDays.joinToString(", ")
        } else {
            finalDays.map { it.take(3) }.joinToString(", ")
        }
    } else {
        "Mon, Wed, Fri"
    }

    // Dynamic Level
    val rawLevel = profile?.trainingExperience ?: trainingExperienceState
    val levelDisplay = when (rawLevel.lowercase()) {
        "beginner" -> "Beginner"
        "intermediate" -> "Intermediate"
        "advanced" -> "Advanced"
        else -> rawLevel.replaceFirstChar { it.uppercase() }
    }

    // Dynamic Safety
    val rawSafetyStatus = profile?.jointHealthStatus ?: jointHealthStatusState
    val rawLimitations = profile?.jointLimitations ?: jointLimitationsState.toList()
    val activeLimitationNames = summaryData?.activeLimitations?.map { it.name } ?: emptyList()
    val combinedLimitations = (rawLimitations + activeLimitationNames).distinct().filter { it.isNotBlank() }

    val hasLimitations = rawSafetyStatus.equals("Has Limitations", ignoreCase = true) || combinedLimitations.isNotEmpty()
    val safetyDisplay = if (!hasLimitations) {
        "No Limitations"
    } else {
        combinedLimitations.joinToString(", ")
    }

    Scaffold(
        containerColor = DarkSlateBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Celebration Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(ElectricMint, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TextOnAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = "✨ Your Personalized Plan Is Ready!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = "Great job! Your PersonaFit plan has been created based on your goals, schedule, experience, and workout preferences.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 1. "Your Plan" Summary Card (Dynamic User Assessment Data)
                YourPlanSummaryCard(
                    workout = workoutDisplay,
                    focus = focusDisplay,
                    schedule = scheduleDisplay,
                    level = levelDisplay,
                    safety = safetyDisplay,
                    hasLimitations = hasLimitations
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Motivational Message Banner
                MotivationalMessageCard(
                    message = "Your plan is built around your schedule. Stay consistent and let PersonaFit guide your progress."
                )

                summaryData?.let { summary ->
                    Spacer(modifier = Modifier.height(20.dp))

                    // 3. Persona Archetype Card
                    PersonaArchetypeCard(summary = summary)

                    Spacer(modifier = Modifier.height(20.dp))

                    // 4. Nutrition & Macro Split Card (Mifflin-St Jeor)
                    NutritionPrescriptionCard(summary = summary)

                    Spacer(modifier = Modifier.height(20.dp))

                    // 5. Active Injury Safeguards Card
                    InjurySafeguardCard(summary = summary)
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Main CTA Button
                PrimaryButton(
                    text = "View My Personalized Plan →",
                    onClick = onNavigateToDashboard
                )

                Spacer(modifier = Modifier.height(32.dp))

                // "What happens next?" Section
                WhatHappensNextSection()

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun YourPlanSummaryCard(
    workout: String,
    focus: String,
    schedule: String,
    level: String,
    safety: String,
    hasLimitations: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.5.dp, ElectricMint.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "YOUR PLAN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ElectricMint.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, ElectricMint.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Tailored",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PlanSummaryItem(label = "Workout", value = workout, icon = Icons.Default.FitnessCenter)
            HorizontalDivider(color = BorderDark, modifier = Modifier.padding(vertical = 10.dp))
            PlanSummaryItem(label = "Focus", value = focus, icon = Icons.Default.Tune)
            HorizontalDivider(color = BorderDark, modifier = Modifier.padding(vertical = 10.dp))
            PlanSummaryItem(label = "Schedule", value = schedule, icon = Icons.Default.CalendarMonth)
            HorizontalDivider(color = BorderDark, modifier = Modifier.padding(vertical = 10.dp))
            PlanSummaryItem(label = "Level", value = level, icon = Icons.Default.TrendingUp)
            HorizontalDivider(color = BorderDark, modifier = Modifier.padding(vertical = 10.dp))
            PlanSummaryItem(
                label = "Safety",
                value = safety,
                icon = Icons.Default.HealthAndSafety,
                valueColor = if (hasLimitations) CoralAccent else SafeGreen
            )
        }
    }
}

@Composable
private fun PlanSummaryItem(
    label: String,
    value: String,
    icon: ImageVector,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = valueColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun MotivationalMessageCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceElevatedDark,
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(NeonCyan.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            )
        }
    }
}

@Composable
fun WhatHappensNextSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "What happens next?",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        NextStepItem(
            title = "Workout Plan",
            description = "Follow the exercises scheduled for your available days.",
            icon = Icons.Default.FitnessCenter,
            accentColor = ElectricMint
        )

        Spacer(modifier = Modifier.height(10.dp))

        NextStepItem(
            title = "Meal Plan",
            description = "View your recommended meals and nutrition targets.",
            icon = Icons.Default.Restaurant,
            accentColor = NeonCyan
        )

        Spacer(modifier = Modifier.height(10.dp))

        NextStepItem(
            title = "Injury Prevention",
            description = "Learn proper exercise form and view safer alternatives when needed.",
            icon = Icons.Default.Shield,
            accentColor = SafeGreen
        )
    }
}

@Composable
private fun NextStepItem(
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(accentColor.copy(alpha = 0.12f), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                )
            }
        }
    }
}

@Composable
fun PersonaArchetypeCard(summary: AssessmentSummaryData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.5.dp, BorderHighlight)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ASSIGNED PERSONA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ElectricMint
                ) {
                    Text(
                        text = "${summary.persona.recommendedDays} Days/Wk",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextOnAccent,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = summary.persona.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )

            Text(
                text = summary.persona.description,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceElevatedDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Session Duration",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Text(
                        text = "${summary.persona.targetSessionMinutes} Minutes",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionPrescriptionCard(summary: AssessmentSummaryData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = CoralAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DAILY ENERGY TARGET",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Text(
                    text = "${summary.nutrition.dailyCalories} kcal",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Macro gram pill badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroPill(
                    label = "Protein",
                    amount = "${summary.nutrition.proteinGrams}g",
                    color = ElectricMint,
                    modifier = Modifier.weight(1f)
                )
                MacroPill(
                    label = "Carbs",
                    amount = "${summary.nutrition.carbsGrams}g",
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                MacroPill(
                    label = "Fats",
                    amount = "${summary.nutrition.fatGrams}g",
                    color = CoralAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hydration Target
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Daily Hydration Goal",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
                Text(
                    text = "${summary.nutrition.waterMlTarget} ml",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun MacroPill(
    label: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = SurfaceElevatedDark,
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )
        }
    }
}

@Composable
fun InjurySafeguardCard(summary: AssessmentSummaryData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = SafeGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "INJURY PREVENTION MATRIX",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (summary.activeLimitations.isNotEmpty()) {
                Text(
                    text = "${summary.activeLimitations.size} Joint Protection Rules Applied",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SafeGreen
                    )
                )
                Text(
                    text = "Contraindicated movements will be dynamically swapped with orthopedic safe variations.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    modifier = Modifier.padding(top = 2.dp)
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SafeGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "No joint restrictions active. Full workout catalog enabled.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        }
    }
}
