package com.personafit.app.presentation.onboarding.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.presentation.common.PersonaInputField
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.onboarding.OnboardingViewModel
import com.personafit.app.presentation.theme.*

@Composable
fun Step1BiometricsScreen(
    viewModel: OnboardingViewModel
) {
    val gender by viewModel.gender.collectAsState()
    val age by viewModel.age.collectAsState()
    val heightCm by viewModel.heightCm.collectAsState()
    val weightKg by viewModel.weightKg.collectAsState()
    val targetWeightKg by viewModel.targetWeightKg.collectAsState()
    val fitnessGoal by viewModel.fitnessGoal.collectAsState()
    val exerciseType by viewModel.exerciseType.collectAsState()
    val workoutLocation by viewModel.workoutLocation.collectAsState()
    val availableDays by viewModel.availableDays.collectAsState()
    val trainingExperience by viewModel.trainingExperience.collectAsState()
    val stepError by viewModel.stepError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Step 1 of 2: Biometrics, Goal & Workout Preferences",
            style = MaterialTheme.typography.titleMedium.copy(
                color = ElectricMint,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = "Your metabolic baseline, workout style, and weekly availability",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 20.dp)
        )

        // Error banner
        if (stepError != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                color = WarningRed.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, WarningRed)
            ) {
                Text(
                    text = stepError!!,
                    color = WarningRed,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Biological Sex
        Text(
            text = "Biological Sex (For Mifflin-St Jeor Equation)",
            style = MaterialTheme.typography.labelLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SexOptionButton(
                label = "Male",
                isSelected = gender == "male",
                onClick = { viewModel.gender.value = "male" },
                modifier = Modifier.weight(1f)
            )
            SexOptionButton(
                label = "Female",
                isSelected = gender == "female",
                onClick = { viewModel.gender.value = "female" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Age
        PersonaInputField(
            value = age,
            onValueChange = { viewModel.age.value = it },
            label = "Age (Years)",
            placeholder = "e.g. 24",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Height & Weight Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PersonaInputField(
                value = heightCm,
                onValueChange = { viewModel.heightCm.value = it },
                label = "Height (cm)",
                placeholder = "e.g. 175",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
            PersonaInputField(
                value = weightKg,
                onValueChange = { viewModel.weightKg.value = it },
                label = "Weight (kg)",
                placeholder = "e.g. 70",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Target Goal Weight (Optional)
        PersonaInputField(
            value = targetWeightKg,
            onValueChange = { viewModel.targetWeightKg.value = it },
            label = "Target Goal Weight (kg) - Optional",
            placeholder = "e.g. 68",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Fitness Goal
        Text(
            text = "Primary Fitness Goal",
            style = MaterialTheme.typography.labelLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val goalOptions = listOf(
            "fat_loss" to ("Fat Loss" to "Caloric Deficit -20% for fat reduction"),
            "maintenance" to ("Maintenance" to "Caloric Equilibrium for recomposition & mobility"),
            "muscle_gain" to ("Muscle Hypertrophy" to "Caloric Surplus +10% for muscle growth")
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            goalOptions.forEach { (key, pair) ->
                SelectableCard(
                    title = pair.first,
                    description = pair.second,
                    isSelected = fitnessGoal == key,
                    onClick = { viewModel.fitnessGoal.value = key }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Exercise Type
        Text(
            text = "Exercise Type Preference",
            style = MaterialTheme.typography.labelLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val exerciseTypeOptions = listOf(
            "compound" to ("Compound Exercise" to "Multi-joint compound lifts (Squat, Press, Row, Deadlift) for maximal efficiency"),
            "isolation" to ("Isolation Exercise" to "Targeted single-joint focus (Curls, Extensions, Raises) for specific muscle isolation")
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            exerciseTypeOptions.forEach { (key, pair) ->
                SelectableCard(
                    title = pair.first,
                    description = pair.second,
                    isSelected = exerciseType == key,
                    onClick = { viewModel.exerciseType.value = key }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Workout Location
        Text(
            text = "Workout Location",
            style = MaterialTheme.typography.labelLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val locationOptions = listOf(
            "gym" to ("Commercial Gym" to "Full access to barbells, dumbbells, cables, and machines"),
            "home" to ("Home Workout" to "Home routines with dumbbells, resistance bands, or bodyweight")
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            locationOptions.forEach { (key, pair) ->
                SelectableCard(
                    title = pair.first,
                    description = pair.second,
                    isSelected = workoutLocation == key,
                    onClick = { viewModel.workoutLocation.value = key }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

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
                DaySelectionCard(
                    dayName = day,
                    isSelected = isSelected,
                    onClick = { viewModel.toggleAvailableDay(day) }
                )
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

        val experienceOptions = listOf(
            "beginner" to ("Beginner (< 6 months)" to "Learning foundational movement patterns, tempo, and joint safety"),
            "intermediate" to ("Intermediate (6 months - 2 years)" to "Familiar with compound movements and consistent progressive overload"),
            "advanced" to ("Advanced (2+ years)" to "High training age, advanced technique, and volume periodization")
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            experienceOptions.forEach { (key, pair) ->
                SelectableCard(
                    title = pair.first,
                    description = pair.second,
                    isSelected = trainingExperience == key,
                    onClick = { viewModel.trainingExperience.value = key }
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Next Button to Step 2
        PrimaryButton(
            text = "Next: Joint Health →",
            onClick = { viewModel.nextStep() }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DaySelectionCard(
    dayName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                    onCheckedChange = { onClick() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = ElectricMint,
                        checkmarkColor = TextOnAccent,
                        uncheckedColor = TextMuted
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = dayName,
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
                        text = "Workout Day",
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

@Composable
fun SelectableCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = ElectricMint,
                    unselectedColor = TextMuted
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun SexOptionButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) ElectricMint else SurfaceCardDark,
        border = BorderStroke(1.5.dp, if (isSelected) ElectricMint else BorderDark)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = if (isSelected) TextOnAccent else TextSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            )
        }
    }
}
