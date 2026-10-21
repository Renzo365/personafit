package com.personafit.app.presentation.onboarding.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.common.SecondaryButton
import com.personafit.app.presentation.onboarding.OnboardingUiState
import com.personafit.app.presentation.onboarding.OnboardingViewModel
import com.personafit.app.presentation.theme.*

@Composable
fun Step3InjuryScreen(
    viewModel: OnboardingViewModel
) {
    val jointHealthStatus by viewModel.jointHealthStatus.collectAsState()
    val jointLimitations by viewModel.jointLimitations.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val stepError by viewModel.stepError.collectAsState()

    val hasNoLimitations = jointHealthStatus == "No Limitations" && jointLimitations.isEmpty()

    val predefinedLimitations = remember {
        listOf(
            LimitationOption(
                key = "Lower Back",
                title = "Lower Back Pain / Lumbar Disc Vulnerability",
                description = "History or symptoms involving the lower back. May require avoiding excessive spinal loading or unsupported spinal flexion."
            ),
            LimitationOption(
                key = "Knee",
                title = "Knee Pain / Meniscus / Patellofemoral",
                description = "Knee-related discomfort or previous injury. May require modified knee angles and reduced-impact movements."
            ),
            LimitationOption(
                key = "Shoulder",
                title = "Shoulder Impingement / Rotator Cuff",
                description = "Shoulder-related discomfort. May require avoiding painful overhead or high-load shoulder movements."
            ),
            LimitationOption(
                key = "Wrist",
                title = "Wrist Pain / Carpal Tunnel / Tendinitis",
                description = "Wrist or hand discomfort. May require modified wrist positions or alternative exercises."
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Step 2 of 2: Joint Health & Injury Prevention",
            style = MaterialTheme.typography.titleMedium.copy(
                color = ElectricMint,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = "Select any areas that may require exercise modifications.",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        // Safety Information Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            color = SafeGreen.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = SafeGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "PersonaFit Safety Rules",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = SafeGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Exercises that may aggravate the selected limitation will be replaced with alternative exercises based on PersonaFit's safety rules.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "PersonaFit is not a substitute for professional medical advice.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }

        // Error message banner if any
        if (stepError != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
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

        // NO JOINT PAIN OPTION (Mutually Exclusive)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { viewModel.selectNoLimitations() },
            shape = RoundedCornerShape(12.dp),
            color = if (hasNoLimitations) SurfaceElevatedDark else SurfaceCardDark,
            border = BorderStroke(1.5.dp, if (hasNoLimitations) ElectricMint else BorderDark)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (hasNoLimitations) ElectricMint else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "I have NO joint pain or physical limitations",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = if (hasNoLimitations) TextPrimary else TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Full unrestricted exercise selection.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (hasNoLimitations) ElectricMint else TextMuted
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // INJURY / LIMITATION OPTIONS
        Text(
            text = "SELECT ALL AREAS THAT MAY APPLY",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricMint,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            predefinedLimitations.forEach { option ->
                val isSelected = jointLimitations.contains(option.key)
                LimitationCheckboxCard(
                    title = option.title,
                    description = option.description,
                    isSelected = isSelected,
                    onToggle = { viewModel.toggleJointLimitation(option.key) }
                )
            }
        }

        // Error message from API if any
        if (uiState is OnboardingUiState.Error) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(8.dp),
                color = WarningRed.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, WarningRed)
            ) {
                Text(
                    text = (uiState as OnboardingUiState.Error).message,
                    color = WarningRed,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Actions
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
                text = "Finish Assessment →",
                onClick = { viewModel.submitAssessment() },
                isLoading = uiState is OnboardingUiState.Loading,
                modifier = Modifier.weight(1.8f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

private data class LimitationOption(
    val key: String,
    val title: String,
    val description: String
)

@Composable
private fun LimitationCheckboxCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) SurfaceElevatedDark else SurfaceCardDark,
        border = BorderStroke(1.5.dp, if (isSelected) ElectricMint else BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = ElectricMint,
                    checkmarkColor = TextOnAccent,
                    uncheckedColor = TextMuted
                ),
                modifier = Modifier.padding(top = 2.dp)
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
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}
