package com.personafit.app.presentation.workout

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.data.remote.dto.ExerciseDetailDto
import com.personafit.app.data.remote.dto.LimitationDto
import com.personafit.app.data.remote.dto.ProfileSummaryDto
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*
import kotlin.math.PI
import kotlin.math.sin

private enum class GuidanceTab(val title: String, val mode: AnimationMode) {
    PROPER_FORM("Proper Form", AnimationMode.PROPER_FORM),
    COMMON_MISTAKES("Common Mistakes", AnimationMode.COMMON_MISTAKES),
    INJURY_PREVENTION("Injury Prevention", AnimationMode.INJURY_PREVENTION)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: Long,
    viewModel: WorkoutViewModel,
    onStartWorkoutSession: (Long?, String, Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val detailedExercise by viewModel.detailedExercise.collectAsState()
    val isLoading by viewModel.isLoadingExerciseDetail.collectAsState()
    val todayPlan by viewModel.todayPlan.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val userLimitations by viewModel.userLimitations.collectAsState()

    var selectedTab by remember { mutableStateOf(GuidanceTab.PROPER_FORM) }

    // Collapsible states for additional clickable details
    var isMuscleExpExpanded by remember { mutableStateOf(false) }
    var isEffectivenessExpanded by remember { mutableStateOf(false) }
    var isBenefitsExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(exerciseId) {
        viewModel.loadExerciseDetail(exerciseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .clickable { onNavigateBack() }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSlateBg
                )
            )
        },
        containerColor = DarkSlateBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && detailedExercise == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ElectricMint)
                }
            } else if (detailedExercise != null) {
                val exercise = detailedExercise!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ==================== 1. TOP HEADER: TITLE & BADGES ====================
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Pill Badges Row: [CHEST] [ISOLATION] [GYM]
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Target Muscle Badge
                        CategoryPillBadge(text = exercise.targetMuscle.uppercase())

                        // Exercise Type Badge (Compound / Isolation)
                        val exType = exercise.exerciseType ?: "Compound"
                        CategoryPillBadge(text = exType.uppercase())

                        // Location Badge (Home / Gym)
                        val env = exercise.workoutEnvironment ?: "Gym"
                        CategoryPillBadge(text = env.uppercase())
                    }

                    // ==================== INJURY CONTRAINDICATION ALERT CARD ====================
                    if (exercise.isContraindicated == true) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = WarningRed.copy(alpha = 0.15f),
                            border = BorderStroke(1.5.dp, WarningRed.copy(alpha = 0.8f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Injury Warning",
                                        tint = WarningRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "INJURY CONTRAINDICATION DETECTED",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = WarningRed,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = exercise.contraindicationWarning ?: "This movement places high mechanical stress on your reported physical limitation.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                                if (exercise.suggestedAlternativeId != null && !exercise.suggestedAlternativeName.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.loadExerciseDetail(exercise.suggestedAlternativeId)
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        color = SafeGreen.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.6f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Shield,
                                                    contentDescription = null,
                                                    tint = SafeGreen,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Safe Alternative: ${exercise.suggestedAlternativeName}",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = SafeGreen,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = "Switch",
                                                tint = SafeGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ==================== 1. STEP-BY-STEP GUIDANCE ====================
                    SectionHeaderLabel(number = "1", title = "STEP-BY-STEP GUIDANCE")
                    Card1ProperFormOverview(exercise = exercise)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Primary Action Button placed immediately below guidance
                    PrimaryButton(
                        text = "Start Workout Session ⚡",
                        onClick = {
                            val planId = todayPlan?.planId
                            val sessionName = todayPlan?.planName ?: "${exercise.name} Focus"
                            onStartWorkoutSession(planId, sessionName, exercise.id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ==================== 2. MUSCLE TARGET ====================
                    SectionHeaderLabel(number = "2", title = "MUSCLE TARGET")
                    Card2MuscleTarget(exercise = exercise)

                    Spacer(modifier = Modifier.height(16.dp))

                    // ==================== 3. PERSONALIZED SAFE BASELINE ====================
                    SectionHeaderLabel(number = "3", title = "SAFE BASELINE & RECOMMENDATIONS")
                    Card3PersonalizedSafeBaseline(
                        exercise = exercise,
                        userProfile = userProfile,
                        userLimitations = userLimitations
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ==================== 4. INTERACTIVE EXERCISE DEMONSTRATIONS ====================
                    SectionHeaderLabel(number = "4", title = "INTERACTIVE EXERCISE DEMONSTRATIONS")
                    Card4GuidanceTabsSection(
                        exercise = exercise,
                        selectedTab = selectedTab,
                        onTabSelect = { selectedTab = it },
                        onStartSession = {
                            val planId = todayPlan?.planId
                            val sessionName = todayPlan?.planName ?: "${exercise.name} Focus"
                            onStartWorkoutSession(planId, sessionName, exercise.id)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ==================== 5. ADDITIONAL CLICKABLE / COLLAPSIBLE DETAILS ====================
                    SectionHeaderLabel(number = "5", title = "ADDITIONAL INFORMATION")
                    Card5CollapsibleDetailsSection(
                        exercise = exercise,
                        isMuscleExpExpanded = isMuscleExpExpanded,
                        onToggleMuscleExp = { isMuscleExpExpanded = !isMuscleExpExpanded },
                        isEffectivenessExpanded = isEffectivenessExpanded,
                        onToggleEffectiveness = { isEffectivenessExpanded = !isEffectivenessExpanded },
                        isBenefitsExpanded = isBenefitsExpanded,
                        onToggleBenefits = { isBenefitsExpanded = !isBenefitsExpanded }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ==================== 6. BREATHING METHODS & TIPS ====================
                    SectionHeaderLabel(number = "6", title = "BREATHING METHODS & TIPS")
                    Card6BreathingMethodsAndTips(exercise = exercise)

                    Spacer(modifier = Modifier.height(32.dp))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Unable to load exercise details.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Go Back",
                        onClick = onNavigateBack
                    )
                }
            }
        }
    }
}

// ==================== SECTION HEADER LABEL ====================

@Composable
private fun SectionHeaderLabel(number: String, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = NeonCyan.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f)),
            modifier = Modifier.size(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                color = ElectricMint,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
        )
    }
}

// ==================== PILL BADGE HELPER ====================

@Composable
private fun CategoryPillBadge(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceElevatedDark.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.55f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// ==================== CARD 1: PROPER FORM OVERVIEW ====================

@Composable
private fun Card1ProperFormOverview(exercise: ExerciseDetailDto) {
    val steps = remember(exercise.id, exercise.name) {
        get5NumberedStepsForExercise(exercise)
    }
    val properForm = remember(exercise.id) {
        getProperFormDetails(exercise)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Starting Position Cue Header
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceElevatedDark,
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Adjust,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "STARTING POSITION: ${properForm.startingPosition}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column (~42%): Looping Character Animation (Proper Form Mode)
                Box(
                    modifier = Modifier
                        .weight(0.42f)
                        .height(210.dp)
                ) {
                    ExerciseAnimationView(
                        exerciseName = exercise.name,
                        targetMuscle = exercise.targetMuscle,
                        imageUrl = exercise.imageUrl,
                        properFormAsset = exercise.properFormAsset ?: exercise.imageUrl,
                        modifier = Modifier.fillMaxSize(),
                        heightDp = 210,
                        mode = AnimationMode.PROPER_FORM,
                        compact = true
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Right Column (~58%): 5 Numbered Instructions
                Column(
                    modifier = Modifier.weight(0.58f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    steps.forEachIndexed { index, step ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${index + 1}. ",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== CARD 2: MUSCLE TARGET ====================

@Composable
private fun Card2MuscleTarget(exercise: ExerciseDetailDto) {
    AnatomyHighlightView(exercise = exercise)
}

// ==================== CARD 3: PERSONALIZED SAFE BASELINE ====================

@Composable
private fun Card3PersonalizedSafeBaseline(
    exercise: ExerciseDetailDto,
    userProfile: ProfileSummaryDto?,
    userLimitations: List<LimitationDto>
) {
    val baseline = remember(exercise.id, userProfile, userLimitations) {
        getPersonalizedBaseline(exercise, userProfile, userLimitations)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CautionAmber.copy(alpha = 0.10f),
        border = BorderStroke(1.dp, CautionAmber.copy(alpha = 0.50f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Warning Icon + Recommendation Title + Bodyweight
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = CautionAmber,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Recommended for ${baseline.userLevel} Levels",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CautionAmber,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = "(Based on ${baseline.bwDisplay})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Starting Load Row
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceElevatedDark.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STARTING LOAD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = baseline.startingLoad,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = ElectricMint,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Metrics Grid (Sets / Reps / Rest)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaselineMetricPill(
                    label = "Target Sets",
                    value = "${baseline.sets} Sets",
                    modifier = Modifier.weight(1f)
                )
                BaselineMetricPill(
                    label = "Target Reps",
                    value = baseline.repsOrDuration,
                    modifier = Modifier.weight(1.2f)
                )
                BaselineMetricPill(
                    label = "Rest",
                    value = "${baseline.restSeconds}s",
                    modifier = Modifier.weight(0.8f)
                )
            }

            // Injury Sensitivity Alert Check
            if (baseline.hasInjuryConflict) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = WarningRed.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, WarningRed.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = WarningRed,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 1.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Limitation Alert: ${baseline.conflictReason}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = WarningRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            if (baseline.saferAlternative.isNotBlank()) {
                                Text(
                                    text = "Safer Alternative: ${baseline.saferAlternative}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            if (!exercise.personalizedSafetyNotes.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    exercise.personalizedSafetyNotes.forEach { note ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = note,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Medical Disclaimer
            Text(
                text = "* General safety baseline based on conservative training standards; not a medical diagnosis.",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 10.sp,
                    lineHeight = 13.sp
                )
            )
        }
    }
}

@Composable
private fun BaselineMetricPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 2.dp)
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium.copy(
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

data class ParsedMistakeItem(
    val bodyPart: String,
    val fault: String,
    val fix: String
)

private fun parseCommonMistakes(rawText: String?): List<ParsedMistakeItem> {
    if (rawText.isNullOrBlank()) return emptyList()
    val lines = rawText.lines()
        .map { it.trim().removePrefix("•").trim() }
        .filter { it.isNotBlank() }

    return lines.mapNotNull { line ->
        val bodyPartMatch = Regex("\\[(.*?)\\]").find(line)
        val bodyPart = bodyPartMatch?.groupValues?.get(1)?.trim() ?: "Technique"
        val remaining = line.replace(Regex("^\\[.*?\\]\\s*"), "")

        val parts = remaining.split("|")
        val faultPart = parts.getOrNull(0)?.trim() ?: ""
        val fixPart = parts.getOrNull(1)?.trim() ?: ""

        val fault = faultPart.removePrefix("Mistake:").trim()
        val fix = fixPart.removePrefix("Fix:").trim()

        if (fault.isNotBlank()) {
            ParsedMistakeItem(bodyPart = bodyPart, fault = fault, fix = fix)
        } else {
            ParsedMistakeItem(bodyPart = "Technique", fault = line, fix = "Perform with controlled tempo and strict alignment.")
        }
    }
}

data class ParsedPreventionData(
    val highRiskJoints: String?,
    val safetyPrecautions: String?,
    val regressionsAndStopSigns: String?
)

private fun parseInjuryPreventionCues(rawText: String?): ParsedPreventionData {
    if (rawText.isNullOrBlank()) return ParsedPreventionData(null, null, null)
    var joints: String? = null
    var precautions: String? = null
    var regressions: String? = null

    val lines = rawText.lines()
        .map { it.trim().removePrefix("•").trim() }
        .filter { it.isNotBlank() }

    for (line in lines) {
        when {
            line.contains("[High-Risk Joints]", ignoreCase = true) -> {
                joints = line.replace(Regex("(?i)\\[High-Risk Joints\\]\\s*"), "").trim()
            }
            line.contains("[Safety Precautions]", ignoreCase = true) -> {
                precautions = line.replace(Regex("(?i)\\[Safety Precautions\\]\\s*"), "").trim()
            }
            line.contains("[Regressions & Stop Signs]", ignoreCase = true) || line.contains("[Regressions]", ignoreCase = true) -> {
                regressions = line.replace(Regex("(?i)\\[(Regressions & Stop Signs|Regressions)\\]\\s*"), "").trim()
            }
            else -> {
                if (precautions == null) {
                    precautions = line
                } else {
                    precautions += "\n$line"
                }
            }
        }
    }
    return ParsedPreventionData(joints, precautions, regressions)
}

// ==================== CARD 4: CLICKABLE GUIDANCE TABS WITH INDEPENDENT ANIMATIONS ====================

@Composable
private fun Card4GuidanceTabsSection(
    exercise: ExerciseDetailDto,
    selectedTab: GuidanceTab,
    onTabSelect: (GuidanceTab) -> Unit,
    onStartSession: () -> Unit
) {
    val context = LocalContext.current
    val currentResolution = remember(exercise.name, exercise.imageUrl, exercise.properFormAsset, exercise.commonMistakesAsset, exercise.injuryPreventionAsset, selectedTab) {
        ExerciseAssetResolver.resolveAsset(
            context = context,
            imageUrl = exercise.imageUrl,
            exerciseName = exercise.name,
            mode = selectedTab.mode,
            properFormAsset = exercise.properFormAsset,
            commonMistakesAsset = exercise.commonMistakesAsset,
            injuryPreventionAsset = exercise.injuryPreventionAsset
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Tab Header Selectors
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = SurfaceElevatedDark,
            border = BorderStroke(1.dp, BorderDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GuidanceTab.values().forEach { tab ->
                    val isSelected = tab == selectedTab
                    val tabColor = when (tab) {
                        GuidanceTab.PROPER_FORM -> ElectricMint
                        GuidanceTab.COMMON_MISTAKES -> CoralAccent
                        GuidanceTab.INJURY_PREVENTION -> Color(0xFF00E5FF)
                    }
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clickable { onTabSelect(tab) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) tabColor.copy(alpha = 0.16f) else Color.Transparent,
                        border = if (isSelected) BorderStroke(1.2.dp, tabColor) else null
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSelected) tabColor else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Content Container: Prominent Interactive Animation View + Structured Tab Cards
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceCardDark,
            border = BorderStroke(
                if (selectedTab == GuidanceTab.PROPER_FORM) 1.5.dp else 2.dp,
                when (selectedTab) {
                    GuidanceTab.PROPER_FORM -> NeonCyan.copy(alpha = 0.5f)
                    GuidanceTab.COMMON_MISTAKES -> CoralAccent
                    GuidanceTab.INJURY_PREVENTION -> Color(0xFF00E5FF)
                }
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // 1. Full-Width Tab Demonstration View (Height 235dp with interactive overlays)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(235.dp)
                ) {
                    ExerciseAnimationView(
                        exerciseName = exercise.name,
                        targetMuscle = exercise.targetMuscle,
                        imageUrl = exercise.imageUrl,
                        properFormAsset = exercise.properFormAsset ?: exercise.imageUrl,
                        commonMistakesAsset = exercise.commonMistakesAsset,
                        injuryPreventionAsset = exercise.injuryPreventionAsset,
                        modifier = Modifier.fillMaxSize(),
                        heightDp = 235,
                        mode = selectedTab.mode,
                        compact = false
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Tab-Specific Content (Below Visual Demonstration)
                when (selectedTab) {
                    GuidanceTab.PROPER_FORM -> {
                        ProperFormGuidanceContent(exercise = exercise)
                    }
                    GuidanceTab.COMMON_MISTAKES -> {
                        CommonMistakesGuidanceContent(
                            exercise = exercise,
                            isDedicatedVariant = currentResolution.isDedicatedVariant
                        )
                    }
                    GuidanceTab.INJURY_PREVENTION -> {
                        InjuryPreventionGuidanceContent(
                            exercise = exercise,
                            isDedicatedVariant = currentResolution.isDedicatedVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // PRIMARY ACTION BUTTON: Start Workout Session ⚡
                PrimaryButton(
                    text = "Start Workout Session ⚡",
                    onClick = onStartSession,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ProperFormGuidanceContent(exercise: ExerciseDetailDto) {
    val properForm = remember(exercise.id) { getProperFormDetails(exercise) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Biomechanical Setup & Movement Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceElevatedDark,
            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Adjust,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "STARTING POSITION & CUES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
                Text(
                    text = "• Position: ${properForm.startingPosition}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )
                Text(
                    text = "• Execution: ${properForm.movementCues}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )
                Text(
                    text = "• Optimal Cadence: ${properForm.tempo}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = ElectricMint,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        // Form Checkpoints Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceElevatedDark,
            border = BorderStroke(1.dp, BorderDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "KEY FORM CHECKPOINTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                )
                listOf(
                    "Posture Alignment: Maintain neutral spine and engaged pelvic floor.",
                    "Controlled Tempo: 2s eccentric lowering, 1s peak contraction hold.",
                    "Full Active Range: Full extension and flexion without hyperextending joints."
                ).forEach { checkpoint ->
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ElectricMint,
                            modifier = Modifier.size(14.dp).padding(top = 1.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = checkpoint,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommonMistakesGuidanceContent(
    exercise: ExerciseDetailDto,
    isDedicatedVariant: Boolean
) {
    val parsedMistakes = remember(exercise.commonMistakes) {
        parseCommonMistakes(exercise.commonMistakes)
    }
    val fallbackMistakes = remember(exercise.id) { getMistakesDetails(exercise) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Status & Mode Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = CoralAccent.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, CoralAccent.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = CoralAccent,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "FORM BREAKDOWN & FAULTS TO AVOID",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CoralAccent,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = SurfaceElevatedDark,
                border = BorderStroke(1.dp, if (isDedicatedVariant) SafeGreen.copy(alpha = 0.5f) else BorderDark)
            ) {
                Text(
                    text = if (isDedicatedVariant) "✓ Dedicated Asset" else "⚠️ Annotated Fault Overlay",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isDedicatedVariant) SafeGreen else CautionAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        // Structured Mistake vs Correction Cards
        if (parsedMistakes.isNotEmpty()) {
            parsedMistakes.forEach { item ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceElevatedDark,
                    border = BorderStroke(1.dp, CoralAccent.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = CoralAccent.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, CoralAccent.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = item.bodyPart.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CoralAccent,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 9.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = CoralAccent,
                                modifier = Modifier.size(14.dp).padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Mistake / Fault:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CoralAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = item.fault,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ElectricMint,
                                modifier = Modifier.size(14.dp).padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Biomechanical Fix:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = item.fix,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            fallbackMistakes.forEach { m ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceElevatedDark,
                    border = BorderStroke(1.dp, CoralAccent.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = CoralAccent,
                                modifier = Modifier.size(14.dp).padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Mistake: ${m.mistake}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CoralAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = "Why wrong: ${m.whyWrong}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ElectricMint,
                                modifier = Modifier.size(14.dp).padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Correction: ${m.correction}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = ElectricMint,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InjuryPreventionGuidanceContent(
    exercise: ExerciseDetailDto,
    isDedicatedVariant: Boolean
) {
    val parsed = remember(exercise.injuryPreventionCues) {
        parseInjuryPreventionCues(exercise.injuryPreventionCues)
    }
    val fallback = remember(exercise.id) { getInjuryPreventionDetails(exercise) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Status & Mode Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF00E5FF).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "JOINT PROTECTION & INJURY PREVENTION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = SurfaceElevatedDark,
                border = BorderStroke(1.dp, if (isDedicatedVariant) SafeGreen.copy(alpha = 0.5f) else BorderDark)
            ) {
                Text(
                    text = if (isDedicatedVariant) "✓ Dedicated Asset" else "🛡️ Anatomical Shield Active",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isDedicatedVariant) SafeGreen else Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        // Card 1: High-Risk Joints
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceElevatedDark,
            border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "HIGH-RISK JOINTS & ANATOMY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
                Text(
                    text = parsed.highRiskJoints ?: fallback.equipmentSetup,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }

        // Card 2: Biomechanical Safety & Alignment Rules
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceElevatedDark,
            border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SafeGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SAFETY PRECAUTIONS & ALIGNMENT RULES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SafeGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
                Text(
                    text = parsed.safetyPrecautions ?: fallback.warmUp,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }

        // Card 3: When to Stop & Regression Alternatives
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceElevatedDark,
            border = BorderStroke(1.dp, WarningRed.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = WarningRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PAIN STOP SIGNS & REGRESSION ALTERNATIVES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WarningRed,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
                Text(
                    text = parsed.regressionsAndStopSigns ?: fallback.warningSigns,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}

// ==================== CARD 5: ADDITIONAL CLICKABLE / COLLAPSIBLE DETAILS ====================

@Composable
private fun Card5CollapsibleDetailsSection(
    exercise: ExerciseDetailDto,
    isMuscleExpExpanded: Boolean,
    onToggleMuscleExp: () -> Unit,
    isEffectivenessExpanded: Boolean,
    onToggleEffectiveness: () -> Unit,
    isBenefitsExpanded: Boolean,
    onToggleBenefits: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Collapsible 1: Muscle Explanation
        CollapsibleDetailCard(
            title = "Muscle Explanation",
            icon = Icons.Default.FitnessCenter,
            isExpanded = isMuscleExpExpanded,
            onToggle = onToggleMuscleExp
        ) {
            Text(
                text = exercise.muscleExplanation?.takeIf { it.isNotBlank() } ?: getMuscleExplanation(exercise),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            )
        }

        // Collapsible 2: Exercise Effectiveness
        CollapsibleDetailCard(
            title = "Exercise Effectiveness",
            icon = Icons.Default.TrendingUp,
            isExpanded = isEffectivenessExpanded,
            onToggle = onToggleEffectiveness
        ) {
            Text(
                text = exercise.recommendationRules?.takeIf { it.isNotBlank() } ?: getExerciseEffectiveness(exercise),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            )
        }

        // Collapsible 3: Benefits
        CollapsibleDetailCard(
            title = "Benefits",
            icon = Icons.Default.Stars,
            isExpanded = isBenefitsExpanded,
            onToggle = onToggleBenefits
        ) {
            Text(
                text = exercise.benefits?.takeIf { it.isNotBlank() } ?: getBenefits(exercise),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

@Composable
private fun CollapsibleDetailCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    content()
                }
            }
        }
    }
}

// ==================== CARD 6: BREATHING METHODS & TIPS ====================

@Composable
private fun Card6BreathingMethodsAndTips(exercise: ExerciseDetailDto) {
    val breathing = remember(exercise.name, exercise.targetMuscle) {
        getBreathingGuide(exercise)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Breathing Wave Animation Canvas
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(SurfaceElevatedDark, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderDark, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                BreathingAnimationCanvas(modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Right: Breathing Instructions
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Breathing Methods & Tips",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                )

                if (!exercise.breathingTips.isNullOrBlank()) {
                    val lines = exercise.breathingTips.lines()
                        .map { it.trim().removePrefix("•").trim() }
                        .filter { it.isNotBlank() }
                    lines.forEach { line ->
                        val parts = line.split(":", limit = 2)
                        if (parts.size == 2) {
                            Text(
                                text = "• ${parts[0].trim()}: ${parts[1].trim()}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (parts[0].contains("Inhale", ignoreCase = true)) NeonCyan else if (parts[0].contains("Exhale", ignoreCase = true)) ElectricMint else TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            )
                        } else {
                            Text(
                                text = "• $line",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            )
                        }
                    }
                } else {
                    Text(
                        text = "• Inhale: ${breathing.inhalePhase}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NeonCyan,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    )

                    Text(
                        text = "• Exhale: ${breathing.exhalePhase}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ElectricMint,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    )

                    Text(
                        text = "• Bracing: ${breathing.bracingAdvice}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    )
                }
            }
        }
    }
}

// ==================== CANVAS: MUSCLE TORSO GRAPHIC ====================

@Composable
private fun MuscleTorsoCanvas(
    targetMuscle: String,
    secondaryMuscles: String?,
    modifier: Modifier = Modifier
) {
    val m = targetMuscle.lowercase()
    val isChest = m.contains("chest") || m.contains("pec")
    val isBack = m.contains("back") || m.contains("lat") || m.contains("trap")
    val isShoulder = m.contains("shoulder") || m.contains("delt")
    val isArms = m.contains("arm") || m.contains("bicep") || m.contains("tricep")
    val isLegs = m.contains("quad") || m.contains("leg") || m.contains("glute") || m.contains("hamstring") || m.contains("calf")
    val isCore = m.contains("ab") || m.contains("core") || m.contains("waist")

    val activeColor = NeonCyan
    val activeBorder = ElectricMint
    val inactiveFill = Color(0xFF1E2835)
    val inactiveBorder = Color(0xFF33445C)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f

        // 1. Head / Neck
        drawCircle(
            color = inactiveFill,
            radius = w * 0.12f,
            center = Offset(cx, h * 0.12f)
        )
        drawCircle(
            color = inactiveBorder,
            radius = w * 0.12f,
            center = Offset(cx, h * 0.12f),
            style = Stroke(width = 1.5f)
        )

        // 2. Deltoids (Shoulders)
        val deltFill = if (isShoulder) activeColor else inactiveFill
        val deltStroke = if (isShoulder) activeBorder else inactiveBorder

        // Left Deltoid
        drawRoundRect(
            color = deltFill,
            topLeft = Offset(cx - w * 0.44f, h * 0.22f),
            size = Size(w * 0.18f, h * 0.20f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = deltStroke,
            topLeft = Offset(cx - w * 0.44f, h * 0.22f),
            size = Size(w * 0.18f, h * 0.20f),
            cornerRadius = CornerRadius(6f, 6f),
            style = Stroke(width = if (isShoulder) 2.5f else 1.2f)
        )

        // Right Deltoid
        drawRoundRect(
            color = deltFill,
            topLeft = Offset(cx + w * 0.26f, h * 0.22f),
            size = Size(w * 0.18f, h * 0.20f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = deltStroke,
            topLeft = Offset(cx + w * 0.26f, h * 0.22f),
            size = Size(w * 0.18f, h * 0.20f),
            cornerRadius = CornerRadius(6f, 6f),
            style = Stroke(width = if (isShoulder) 2.5f else 1.2f)
        )

        // 3. Pectorals (Chest Plates) - Highlighted in cyan for Chest exercises
        val pecFill = if (isChest) activeColor else inactiveFill
        val pecStroke = if (isChest) activeBorder else inactiveBorder

        // Left Pec
        val leftPecPath = Path().apply {
            moveTo(cx - 2f, h * 0.24f)
            lineTo(cx - w * 0.25f, h * 0.25f)
            lineTo(cx - w * 0.22f, h * 0.42f)
            lineTo(cx - 2f, h * 0.44f)
            close()
        }
        drawPath(path = leftPecPath, color = pecFill)
        drawPath(path = leftPecPath, color = pecStroke, style = Stroke(width = if (isChest) 2.5f else 1.2f))

        // Right Pec
        val rightPecPath = Path().apply {
            moveTo(cx + 2f, h * 0.24f)
            lineTo(cx + w * 0.25f, h * 0.25f)
            lineTo(cx + w * 0.22f, h * 0.42f)
            lineTo(cx + 2f, h * 0.44f)
            close()
        }
        drawPath(path = rightPecPath, color = pecFill)
        drawPath(path = rightPecPath, color = pecStroke, style = Stroke(width = if (isChest) 2.5f else 1.2f))

        // 4. Arms (Biceps / Triceps)
        val armFill = if (isArms) activeColor else inactiveFill
        val armStroke = if (isArms) activeBorder else inactiveBorder
        drawRoundRect(
            color = armFill,
            topLeft = Offset(cx - w * 0.46f, h * 0.44f),
            size = Size(w * 0.14f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = armStroke,
            topLeft = Offset(cx - w * 0.46f, h * 0.44f),
            size = Size(w * 0.14f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = if (isArms) 2.5f else 1f)
        )
        drawRoundRect(
            color = armFill,
            topLeft = Offset(cx + w * 0.32f, h * 0.44f),
            size = Size(w * 0.14f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = armStroke,
            topLeft = Offset(cx + w * 0.32f, h * 0.44f),
            size = Size(w * 0.14f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = if (isArms) 2.5f else 1f)
        )

        // 5. Abdominals (6-pack grid)
        val coreFill = if (isCore) activeColor else inactiveFill
        val coreStroke = if (isCore) activeBorder else inactiveBorder
        for (row in 0..2) {
            val yTop = h * (0.47f + row * 0.09f)
            // Left block
            drawRoundRect(
                color = coreFill,
                topLeft = Offset(cx - w * 0.14f, yTop),
                size = Size(w * 0.12f, h * 0.07f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = coreStroke,
                topLeft = Offset(cx - w * 0.14f, yTop),
                size = Size(w * 0.12f, h * 0.07f),
                cornerRadius = CornerRadius(3f, 3f),
                style = Stroke(width = if (isCore) 2f else 1f)
            )
            // Right block
            drawRoundRect(
                color = coreFill,
                topLeft = Offset(cx + w * 0.02f, yTop),
                size = Size(w * 0.12f, h * 0.07f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = coreStroke,
                topLeft = Offset(cx + w * 0.02f, yTop),
                size = Size(w * 0.12f, h * 0.07f),
                cornerRadius = CornerRadius(3f, 3f),
                style = Stroke(width = if (isCore) 2f else 1f)
            )
        }

        // 6. Lats (Back flare behind waist)
        val latFill = if (isBack) activeColor else inactiveFill
        val latStroke = if (isBack) activeBorder else inactiveBorder
        val leftLat = Path().apply {
            moveTo(cx - w * 0.25f, h * 0.42f)
            lineTo(cx - w * 0.32f, h * 0.54f)
            lineTo(cx - w * 0.18f, h * 0.65f)
            close()
        }
        drawPath(path = leftLat, color = latFill)
        drawPath(path = leftLat, color = latStroke, style = Stroke(width = if (isBack) 2.5f else 1f))

        val rightLat = Path().apply {
            moveTo(cx + w * 0.25f, h * 0.42f)
            lineTo(cx + w * 0.32f, h * 0.54f)
            lineTo(cx + w * 0.18f, h * 0.65f)
            close()
        }
        drawPath(path = rightLat, color = latFill)
        drawPath(path = rightLat, color = latStroke, style = Stroke(width = if (isBack) 2.5f else 1f))

        // 7. Lower Body / Quads
        val legFill = if (isLegs) activeColor else inactiveFill
        val legStroke = if (isLegs) activeBorder else inactiveBorder
        drawRoundRect(
            color = legFill,
            topLeft = Offset(cx - w * 0.22f, h * 0.76f),
            size = Size(w * 0.19f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = legStroke,
            topLeft = Offset(cx - w * 0.22f, h * 0.76f),
            size = Size(w * 0.19f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = if (isLegs) 2.5f else 1f)
        )
        drawRoundRect(
            color = legFill,
            topLeft = Offset(cx + w * 0.03f, h * 0.76f),
            size = Size(w * 0.19f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = legStroke,
            topLeft = Offset(cx + w * 0.03f, h * 0.76f),
            size = Size(w * 0.19f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = if (isLegs) 2.5f else 1f)
        )
    }
}

// ==================== CANVAS: BREATHING WAVE ANIMATION ====================

@Composable
private fun BreathingAnimationCanvas(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "BreathingCycle")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathScale"
    )

    Canvas(modifier = modifier) {
        val cx = size.width * 0.5f
        val cy = size.height * 0.5f
        val baseRadius = size.width * 0.35f

        // Outer Expanding Ripple
        drawCircle(
            color = NeonCyan.copy(alpha = 0.15f * breathScale),
            radius = baseRadius * breathScale * 1.25f,
            center = Offset(cx, cy)
        )

        // Mid Ring
        drawCircle(
            color = ElectricMint.copy(alpha = 0.35f * breathScale),
            radius = baseRadius * breathScale,
            center = Offset(cx, cy)
        )

        // Center Lung Core
        drawCircle(
            color = NeonCyan,
            radius = baseRadius * 0.45f,
            center = Offset(cx, cy)
        )
    }
}

// ==================== DATA RESOLVERS & HELPER FUNCTIONS ====================

private fun get5NumberedStepsForExercise(exercise: ExerciseDetailDto): List<String> {
    // 1. If exercise instructions contains lines or numbered steps from CMS, parse them dynamically!
    val rawInstructions = exercise.instructions
    if (!rawInstructions.isNullOrBlank()) {
        val lines = rawInstructions.lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { line ->
                line.replace(Regex("""^(?:(?:Step\s*\d+[:.-]?)|(?:\d+[:.)-]))\s*"""), "").trim()
            }
            .filter { it.isNotBlank() }
        if (lines.size in 3..7) {
            return lines
        }
    }

    val name = exercise.name.lowercase()
    return when {
        name.contains("fly") || name.contains("pec") -> listOf(
            "Set Height (high / mid-chest)",
            "Assume Stance (split or even)",
            "Engage Core & neutral spine",
            "Controlled Movement (arms slightly bent)",
            "Peak Squeeze at center"
        )
        name.contains("chest press") || name.contains("bench press") -> listOf(
            "Adjust Seat Height (handles level with mid-chest)",
            "Retract Scapulae & plant feet firmly",
            "Brace Abdominal Core",
            "Press Forward smoothly without locking elbows",
            "Controlled 3-Second Return"
        )
        name.contains("push-up") || name.contains("pushup") -> listOf(
            "Hands Outside Shoulders (fingers forward)",
            "High Plank Alignment (neutral head-to-heel line)",
            "Engage Glutes & Core",
            "Lower Chest to 1 inch above floor",
            "Drive Floor Away under control"
        )
        name.contains("row") -> listOf(
            "Adjust Chest Pad & Grip Handles",
            "Sit Tall with spine neutral",
            "Initiate Pull with Elbows Back",
            "Squeeze Scapulae Together firmly",
            "Resist Weight slowly on return"
        )
        name.contains("pulldown") || name.contains("pull-up") -> listOf(
            "Grip Bar (slightly wider than shoulder width)",
            "Lock Thighs Snugly under support pad",
            "Depress Scapulae & pull to collarbone",
            "Hold Peak Lat Contraction for 1s",
            "Control Full Overhead Extension"
        )
        name.contains("squat") || name.contains("leg press") -> listOf(
            "Feet Shoulder-Width Apart (toes flared 15°)",
            "Brace Midsection & lift chest",
            "Hinge at Hips & break at knees",
            "Descend until thighs reach parallel",
            "Drive Upwards evenly through whole foot"
        )
        name.contains("wall sit") -> listOf(
            "Back Flush Against sturdy wall",
            "Walk Feet 2 Feet Out from wall",
            "Slide Down to 90° Knee Angle",
            "Knees Aligned directly above ankles",
            "Breathe Continuously throughout hold"
        )
        name.contains("dead bug") -> listOf(
            "Lie Flat on back on comfortable mat",
            "Knees at 90° Tabletop & arms toward ceiling",
            "Press Lumbar Spine completely flat to floor",
            "Extend Opposite Arm & Leg slowly",
            "Return to Center without lumbar arching"
        )
        name.contains("curl") -> listOf(
            "Stand Tall with shoulders depressed",
            "Pin Elbows close to ribcage",
            "Curl Load Upward squeezing biceps",
            "Pause for 1s at peak contraction",
            "Lower Load smoothly over 3 seconds"
        )
        name.contains("shoulder press") || name.contains("overhead") -> listOf(
            "Set Handles or Weights at collarbone level",
            "Tuck Elbows 45° forward in scapular plane",
            "Lock Ribcage & brace glutes",
            "Press Directly Overhead without arching back",
            "Lower Smoothly back to clavicle"
        )
        else -> listOf(
            "Adopt Athletic Stance & establish footing",
            "Align Joints & engage primary stabilizers",
            "Initiate Movement with deliberate control",
            "Squeeze Target Muscles at peak range",
            "Return Slowly through full controlled path"
        )
    }
}

private fun getPrimaryMuscleName(exercise: ExerciseDetailDto): String {
    val m = exercise.targetMuscle.lowercase()
    val n = exercise.name.lowercase()
    return when {
        m.contains("chest") -> if (n.contains("fly")) "Pectoralis Major (Sternal & Clavicular), Minor" else "Pectoralis Major, Pectoralis Minor"
        m.contains("back") -> "Latissimus Dorsi, Rhomboids, Trapezius"
        m.contains("quad") -> "Quadriceps Femoris (Rectus Femoris, Vasti)"
        m.contains("glute") -> "Gluteus Maximus, Gluteus Medius"
        m.contains("hamstring") -> "Biceps Femoris, Semitendinosus"
        m.contains("calf") -> "Gastrocnemius, Soleus"
        m.contains("shoulder") -> "Deltoids (Anterior, Lateral & Posterior)"
        m.contains("arm") -> if (n.contains("tricep") || n.contains("dip")) "Triceps Brachii" else "Biceps Brachii, Brachialis"
        m.contains("ab") || m.contains("core") || m.contains("waist") -> "Rectus Abdominis, Transverse Abdominis"
        else -> exercise.targetMuscle
    }
}

private fun getFallbackSecondaryMuscles(exercise: ExerciseDetailDto): String {
    val m = exercise.targetMuscle.lowercase()
    return when {
        m.contains("chest") -> "Anterior Deltoids, Triceps Brachii"
        m.contains("back") -> "Biceps Brachii, Posterior Deltoids, Forearms"
        m.contains("quad") -> "Gluteus Maximus, Hamstrings, Core Stabilizers"
        m.contains("glute") -> "Hamstrings, Erector Spinae"
        m.contains("shoulder") -> "Triceps Brachii, Upper Trapezius"
        m.contains("arm") -> "Forearms, Anterior Deltoids"
        else -> "Core Stabilizers, Joint Fixators"
    }
}

private fun getFocusArea(exercise: ExerciseDetailDto): String {
    val n = exercise.name.lowercase()
    val m = exercise.targetMuscle.lowercase()
    return when {
        n.contains("fly") -> "Inner and Outer Chest Adduction"
        n.contains("chest press") || n.contains("bench") -> "Horizontal Pressing Strength & Mass"
        n.contains("push-up") -> "Chest & Core Anti-Extension Control"
        n.contains("row") -> "Mid-Back Thickness & Scapular Retraction"
        n.contains("pulldown") || n.contains("pull-up") -> "Lat Width & Vertical Pulling Power"
        n.contains("squat") -> "Bilateral Knee Extension & Quad Hypertrophy"
        n.contains("wall sit") -> "Isometric Quad Endurance & Joint Stability"
        n.contains("dead bug") -> "Deep Core Anti-Extension Stability"
        n.contains("curl") -> "Bicep Peak & Elbow Flexion Strength"
        n.contains("shoulder press") -> "Vertical Shoulder Drive & Overhead Lockout"
        n.contains("lateral raise") -> "Lateral Deltoid Width & Shoulder Contour"
        else -> "${exercise.targetMuscle} Structural Balance"
    }
}

private data class BaselineParameters(
    val userLevel: String,
    val bwDisplay: String,
    val startingLoad: String,
    val sets: Int,
    val repsOrDuration: String,
    val restSeconds: Int,
    val hasInjuryConflict: Boolean,
    val conflictReason: String,
    val saferAlternative: String
)

private fun getPersonalizedBaseline(
    exercise: ExerciseDetailDto,
    profile: ProfileSummaryDto?,
    limitations: List<LimitationDto>
): BaselineParameters {
    val level = profile?.trainingExperience?.replaceFirstChar { it.uppercase() } ?: "Intermediate"
    val weightKg = profile?.weightKg ?: 70f
    val weightLbs = (weightKg * 2.20462f).toInt()
    val bwDisplay = "$weightLbs lbs BW"

    val isBodyweight = exercise.workoutEnvironment?.contains("home", ignoreCase = true) == true ||
            exercise.equipmentType.contains("body", ignoreCase = true) ||
            exercise.requiredEquipment?.contains("body", ignoreCase = true) == true

    val isIsometric = exercise.name.contains("wall sit", ignoreCase = true) ||
            exercise.name.contains("plank", ignoreCase = true)

    // Starting load: prioritize personalized load computed by backend, then safeStartingLoad from CMS, then client default
    val startingLoad = exercise.personalizedStartingLoad
        ?: exercise.safeStartingLoad
        ?: if (isBodyweight) {
            when {
                level.contains("begin", ignoreCase = true) -> "Bodyweight (Assisted/Incline)"
                level.contains("adv", ignoreCase = true) -> "Bodyweight (Full Range)"
                else -> "Bodyweight"
            }
        } else {
            when {
                exercise.name.contains("fly", ignoreCase = true) -> "10 – 15 kg (22 – 33 lbs)"
                exercise.name.contains("press", ignoreCase = true) -> "20 – 30 kg (45 – 65 lbs)"
                exercise.name.contains("row", ignoreCase = true) -> "25 – 35 kg (55 – 75 lbs)"
                exercise.name.contains("pulldown", ignoreCase = true) -> "25 – 35 kg (55 – 75 lbs)"
                exercise.name.contains("squat", ignoreCase = true) -> "30 – 40 kg (65 – 90 lbs)"
                exercise.name.contains("curl", ignoreCase = true) -> "6 – 10 kg (15 – 22 lbs)"
                else -> "Conservative Load (RPE 6)"
            }
        }

    val sets = exercise.personalizedSets
        ?: exercise.defaultSets
        ?: (if (level.contains("begin", ignoreCase = true)) 3 else 4)

    val reps = exercise.personalizedReps
        ?: exercise.defaultReps
        ?: (if (isIsometric) "20 – 45s Hold" else "10 – 12 Reps")

    val rest = exercise.personalizedRestSeconds
        ?: exercise.defaultRestSeconds
        ?: (if (exercise.exerciseType?.contains("compound", ignoreCase = true) == true) 90 else 60)

    // Limitation conflict check: prioritize server contraindication results, fallback to client rules
    val limCodes = limitations.map { it.code.lowercase() }
    val exName = exercise.name.lowercase()
    var conflict = exercise.isContraindicated == true
    var reason = exercise.contraindicationWarning ?: ""
    var alt = exercise.suggestedAlternativeName ?: exercise.saferAlternativesText ?: ""

    if (!conflict) {
        if (limCodes.any { it.contains("knee") } && (exName.contains("squat") || exName.contains("wall sit") || exName.contains("lunge"))) {
            conflict = true
            reason = "Knee sensitivity detected. Limit knee flexion angle to <70° or use glute bridges."
            if (alt.isBlank()) alt = "Glute Bridges or Machine Leg Curls"
        } else if (limCodes.any { it.contains("shoulder") } && (exName.contains("press") || exName.contains("fly") || exName.contains("overhead"))) {
            conflict = true
            reason = "Shoulder impingement risk. Restrict range of motion so elbows stay forward of chest plane."
            if (alt.isBlank()) alt = "Chest-Supported Neutral Row"
        } else if (limCodes.any { it.contains("back") } && (exName.contains("deadlift") || exName.contains("row"))) {
            conflict = true
            reason = "Lumbar sensitivity detected. Maintain chest support and avoid spinal rocking."
            if (alt.isBlank()) alt = "Bird Dog or Floor Prone Cobra"
        }
    }

    return BaselineParameters(
        userLevel = level,
        bwDisplay = bwDisplay,
        startingLoad = startingLoad,
        sets = sets,
        repsOrDuration = reps,
        restSeconds = rest,
        hasInjuryConflict = conflict,
        conflictReason = reason,
        saferAlternative = alt
    )
}

private data class ProperFormDetails(
    val startingPosition: String,
    val movementCues: String,
    val tempo: String
)

private fun getProperFormDetails(exercise: ExerciseDetailDto): ProperFormDetails {
    val rawInstructions = exercise.instructions
    var customStartingPos: String? = null
    if (!rawInstructions.isNullOrBlank()) {
        val firstLine = rawInstructions.lines().firstOrNull { it.isNotBlank() }
        if (firstLine != null && (firstLine.contains("start", ignoreCase = true) || firstLine.contains("position", ignoreCase = true) || firstLine.contains("stand", ignoreCase = true) || firstLine.contains("lie", ignoreCase = true) || firstLine.contains("sit", ignoreCase = true))) {
            customStartingPos = firstLine.replace(Regex("""^(?:(?:Step\s*\d+[:.-]?)|(?:\d+[:.)-]))\s*"""), "").trim()
        }
    }

    val n = exercise.name.lowercase()
    val defaultDetails = when {
        n.contains("fly") || n.contains("pec") -> ProperFormDetails(
            startingPosition = "Handles aligned with mid-chest, upright torso against backrest, feet planted firmly.",
            movementCues = "Bring handles together in a wide hugging arc keeping a slight 15° bend in elbows. Squeeze pectorals at center.",
            tempo = "3s smooth stretch • 1s peak squeeze • 1s return"
        )
        n.contains("chest press") -> ProperFormDetails(
            startingPosition = "Handles level with mid-chest, scapulae retracted, upper back and glutes pressed into pad.",
            movementCues = "Press handles forward smoothly in scapular plane without forcefully snapping elbows into hyperextension.",
            tempo = "3s eccentric lowering • 1s press"
        )
        n.contains("wall sit") -> ProperFormDetails(
            startingPosition = "Back flat against wall, feet shoulder-width and 2 feet out from wall.",
            movementCues = "Slide down until thighs are parallel to ground (90° knee angle). Keep shins vertical over ankles.",
            tempo = "Continuous static tension • 30–45s hold"
        )
        n.contains("dead bug") -> ProperFormDetails(
            startingPosition = "Lie flat on back, arms raised toward ceiling, knees bent at 90° tabletop over hips.",
            movementCues = "Press lumbar spine completely flat into floor. Extend opposite arm and leg simultaneously with control.",
            tempo = "2s extension • 1s pause • 2s return"
        )
        else -> ProperFormDetails(
            startingPosition = "Establish neutral spine alignment and firmly anchored footing or seating.",
            movementCues = "Articulate strictly through intended joint path, keeping tension on prime movers.",
            tempo = "3s lowering • 1s contraction"
        )
    }

    return if (customStartingPos != null) {
        defaultDetails.copy(startingPosition = customStartingPos)
    } else {
        defaultDetails
    }
}

private data class MistakeItem(
    val mistake: String,
    val whyWrong: String,
    val correction: String
)

private fun getMistakesDetails(exercise: ExerciseDetailDto): List<MistakeItem> {
    val n = exercise.name.lowercase()
    return when {
        n.contains("fly") || n.contains("pec") -> listOf(
            MistakeItem(
                mistake = "Overextending Arms Past Torso Plane",
                whyWrong = "Forces anterior shoulder capsule into excessive horizontal extension and causes impingement.",
                correction = "Set range limiters so handles stop in front of chest plane on the stretch."
            ),
            MistakeItem(
                mistake = "Locking Elbows Completely Straight",
                whyWrong = "Transfers mechanical stress directly onto the bicep tendon and elbow joint.",
                correction = "Maintain a soft 15° bend in both elbows throughout the entire fly movement."
            ),
            MistakeItem(
                mistake = "Shrugging Shoulders Toward Ears",
                whyWrong = "Overactivates upper trapezius and compromises scapular depression.",
                correction = "Keep shoulder blades depressed and retracted into backrest."
            )
        )
        n.contains("chest press") || n.contains("push-up") -> listOf(
            MistakeItem(
                mistake = "90° Lateral Elbow Flare",
                whyWrong = "Pinches supraspinatus tendon against acromion bone under loaded pressing.",
                correction = "Tuck elbows at 45° to 60° forward in the natural scapular plane."
            ),
            MistakeItem(
                mistake = "Violent Elbow Lockout",
                whyWrong = "Hyperextends olecranon bursa and removes muscular tension from chest.",
                correction = "Stop 1 inch short of full extension to keep continuous pectoral tension."
            )
        )
        n.contains("squat") || n.contains("wall sit") -> listOf(
            MistakeItem(
                mistake = "Knee Valgus (Inward Knee Cave)",
                whyWrong = "Places uneven rotational shear stress across the medial collateral ligament (MCL).",
                correction = "Actively push knees outward in line with second and third toes."
            ),
            MistakeItem(
                mistake = "Lifting Heels Off Floor",
                whyWrong = "Overloads patellofemoral cartilage and destabilizes base of support.",
                correction = "Distribute bodyweight evenly across whole foot and heels."
            )
        )
        n.contains("dead bug") -> listOf(
            MistakeItem(
                mistake = "Lumbar Spine Arching Off Floor",
                whyWrong = "Transfers core load onto lumbar vertebrae and deep spinal erectors.",
                correction = "Posteriorly tilt pelvis and press lower back completely flat to mat."
            ),
            MistakeItem(
                mistake = "Moving Limbs Too Rapidly",
                whyWrong = "Replaces abdominal stabilization with momentum.",
                correction = "Move with slow, deliberate 3-second cadence."
            )
        )
        else -> listOf(
            MistakeItem(
                mistake = "Using Ballistic Momentum",
                whyWrong = "Reduces target muscle tension and increases tendon injury risk.",
                correction = "Control the eccentric lowering phase for full 3 seconds."
            ),
            MistakeItem(
                mistake = "Collapsing Joint Neutrality",
                whyWrong = "Exposes connective tissues to shear forces.",
                correction = "Maintain neutral spine and aligned joints throughout all reps."
            )
        )
    }
}

private data class PreventionDetails(
    val warmUp: String,
    val equipmentSetup: String,
    val warningSigns: String
)

private fun getInjuryPreventionDetails(exercise: ExerciseDetailDto): PreventionDetails {
    val n = exercise.name.lowercase()
    return when {
        n.contains("fly") || n.contains("pec") -> PreventionDetails(
            warmUp = "Arm circles, band pull-aparts, and 1 light warmup set at 30% load.",
            equipmentSetup = "Seat height adjusted so handles travel directly across nipple line. Set range limiter pin to prevent excessive horizontal abduction.",
            warningSigns = "Sharp pinching at front of shoulder capsule or deep bicep tendon irritation."
        )
        n.contains("chest press") -> PreventionDetails(
            warmUp = "Thoracic extensions, scapular wall slides, and dynamic chest openers.",
            equipmentSetup = "Adjust handles across lower chest with wrists stacked straight over elbows.",
            warningSigns = "Stabbing front shoulder pain or elbow joint clicking with ache."
        )
        n.contains("wall sit") || n.contains("squat") -> PreventionDetails(
            warmUp = "Leg swings, bodyweight glute bridges, and dynamic ankle mobility drills.",
            equipmentSetup = "Ensure non-slip floor surface. Position feet roughly 2 feet away from wall.",
            warningSigns = "Sharp pain under or around kneecap or sudden knee joint instability."
        )
        n.contains("dead bug") -> PreventionDetails(
            warmUp = "Pelvic tilts on mat, cat-cow spine articulation, and diaphragmatic breathing.",
            equipmentSetup = "Comfortable exercise mat with head resting flat and neck relaxed.",
            warningSigns = "Lower-back pinching or neck tension tightness."
        )
        else -> PreventionDetails(
            warmUp = "5 minutes dynamic multi-joint mobility matching target muscle group.",
            equipmentSetup = "Inspect pins, collars, and alignment pegs before loading resistance.",
            warningSigns = "Sharp joint pinching, numbness, or acute asymmetric strain."
        )
    }
}

private fun getMuscleExplanation(exercise: ExerciseDetailDto): String {
    val n = exercise.name.lowercase()
    val m = exercise.targetMuscle.lowercase()
    return when {
        n.contains("fly") ->
            "During the Cable Pec Fly, the Pectoralis Major acts as the prime mover through horizontal adduction, bringing the arms toward the midline. The anterior deltoids assist in arm flexion while the biceps brachii (short head) and coracobrachialis stabilize the glenohumeral joint. The abdominal core maintains isometric stability against cable resistance."
        n.contains("chest press") ->
            "The Chest Press heavily recruits the sternal and clavicular heads of the Pectoralis Major for pressing drive. The anterior deltoids assist in shoulder flexion, while the Triceps Brachii extend the elbow joint through the second half of the press. Scapular stabilizers (rhomboids and serratus anterior) keep the shoulder blades anchored."
        n.contains("wall sit") ->
            "The Static Wall Sit demands intense isometric contraction from the Quadriceps (rectus femoris, vastus lateralis, medialis, and intermedius) to prevent knee flexion. The Gluteus Maximus, Hamstrings, and Adductors act as secondary synergists, while the soleus and gastrocnemius stabilize the ankles."
        n.contains("dead bug") ->
            "The Dead Bug targets the deep core stabilizers, predominantly the Transverse Abdominis and Rectus Abdominis, through anti-extension. When limbs extend away from the torso, the core must produce equal resisting torque to keep the pelvis and lumbar spine anchored to the floor."
        m.contains("back") ->
            "Back pulling movements engage the Latissimus Dorsi to drive elbows down and back, the Rhomboids and Middle Trapezius to retract shoulder blades, and the Biceps Brachii to flex the elbows under control."
        else ->
            "This exercise coordinates prime mover contraction with secondary stabilizer engagement, promoting balanced joint articulation and symmetrical muscular development."
    }
}

private fun getExerciseEffectiveness(exercise: ExerciseDetailDto): String {
    val n = exercise.name.lowercase()
    return when {
        n.contains("fly") ->
            "Designed to maximize continuous tension on the pectoral muscle fibers across both deep stretch and peak contraction without overloading the elbow extensors. Excellent for isolated chest hypertrophy and muscular control."
        n.contains("chest press") ->
            "Highly effective for developing horizontal pushing power, upper body structural balance, and chest/triceps muscle volume in a machine-guided, joint-stabilized path."
        n.contains("wall sit") ->
            "Builds isometric muscular endurance in the quadriceps, improves patellofemoral tendon load tolerance, and reinforces upright postural endurance."
        n.contains("dead bug") ->
            "One of the gold-standard movements for developing rotational core resilience, Lumbar-Pelvic control, and coordinating contralateral limb mechanics without spinal compression."
        else ->
            "Develops functional strength, muscle endurance, and movement control through evidence-informed kinetic pathways."
    }
}

private fun getBenefits(exercise: ExerciseDetailDto): String {
    val n = exercise.name.lowercase()
    return when {
        n.contains("fly") ->
            "• Isolated pectoral tension without triceps fatigue\n• Enhanced mind-muscle connection across full horizontal adduction\n• Safe machine-guided path reduces stabilizer burnout\n• Promotes chest width and muscular symmetry"
        n.contains("chest press") ->
            "• Safe progression toward heavy pressing without requiring a spotter\n• Symmetrical load distribution across both arms\n• Builds foundational pressing strength for daily pushing tasks\n• Reduced rotational shear compared to free weights"
        n.contains("wall sit") ->
            "• Zero equipment required; can be performed anywhere\n• Safe isometric quad loading with zero knee joint movement\n• Reinforces neutral spine posture and core engagement\n• Improves mental focus and muscular stamina"
        n.contains("dead bug") ->
            "• Relieves lower-back strain by training deep abdominal bracing\n• Teaches independent hip and shoulder mobility with a stable spine\n• Zero compressive spinal loading\n• Enhances athletic coordination across contralateral chains"
        else ->
            "• Enhances joint longevity and connective tissue resilience\n• Improves functional strength for daily physical tasks\n• Supports long-term postural health and athletic symmetry"
    }
}

private data class BreathingGuide(
    val inhalePhase: String,
    val exhalePhase: String,
    val bracingAdvice: String
)

private fun getBreathingGuide(exercise: ExerciseDetailDto): BreathingGuide {
    val n = exercise.name.lowercase()
    return when {
        n.contains("fly") || n.contains("pec") -> BreathingGuide(
            inhalePhase = "Inhale through your nose over 3 seconds as arms stretch open, expanding your chest wall.",
            exhalePhase = "Exhale forcefully through pursed lips as you bring handles together in peak contraction.",
            bracingAdvice = "Keep core gently braced throughout. Avoid holding your breath (Valsalva) on isolation movements."
        )
        n.contains("chest press") || n.contains("push-up") -> BreathingGuide(
            inhalePhase = "Inhale steadily as you lower the weight or your body toward chest level.",
            exhalePhase = "Exhale smoothly as you drive handles or floor away during concentric effort.",
            bracingAdvice = "Maintain abdominal tension to keep upper back firmly supported against back pad."
        )
        n.contains("wall sit") || n.contains("plank") -> BreathingGuide(
            inhalePhase = "Inhale deeply into your diaphragm through your nose for 3 seconds.",
            exhalePhase = "Exhale smoothly through your mouth for 3 seconds in a continuous rhythm.",
            bracingAdvice = "Do NOT hold your breath during isometric holds; steady breathing prevents sharp blood pressure spikes."
        )
        n.contains("dead bug") -> BreathingGuide(
            inhalePhase = "Inhale through nose as limbs return to center tabletop position.",
            exhalePhase = "Exhale completely through mouth as opposite arm and leg extend, tightening abs.",
            bracingAdvice = "Use the exhale to hollow the abdominal wall and pin the lumbar spine flat to the floor."
        )
        n.contains("squat") -> BreathingGuide(
            inhalePhase = "Take a deep diaphragmatic breath into your belly and brace core before descending.",
            exhalePhase = "Exhale through pursed lips as you drive upward past the sticking point.",
            bracingAdvice = "Create 360° intra-abdominal pressure at the top before initiating each repetition."
        )
        else -> BreathingGuide(
            inhalePhase = "Inhale during the eccentric (lowering / lengthening) phase.",
            exhalePhase = "Exhale during the concentric (pushing / pulling / effort) phase.",
            bracingAdvice = "Maintain steady, rhythmic breathing without breath-holding to ensure stable intra-thoracic pressure."
        )
    }
}
