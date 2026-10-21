package com.personafit.app.presentation.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.R
import com.personafit.app.data.remote.dto.ExerciseDetailDto

/**
 * Available anatomy perspective modes.
 */
enum class AnatomyViewMode(val label: String) {
    DUAL("Dual View"),
    ANTERIOR("Anterior (Front)"),
    POSTERIOR("Posterior (Back)")
}

/**
 * Data structure representing a muscle target card in the carousel.
 */
data class MuscleTargetItem(
    val regionKey: String,
    val displayName: String,
    val anatomicalRole: String,
    val isPrimary: Boolean,
    val biomechanicalFunction: String
)

/**
 * Reusable dynamic anatomy component rendering the permanent 3-template base anatomy
 * with synchronized transparent target muscle highlights, view toggling, and multi-muscle cards.
 */
@Composable
fun AnatomyHighlightView(
    exercise: ExerciseDetailDto,
    modifier: Modifier = Modifier
) {
    // 1. Resolve primary & secondary regions
    val primaryRegions = remember(exercise.id, exercise.primaryMuscleRegions) {
        if (!exercise.primaryMuscleRegions.isNullOrEmpty()) {
            exercise.primaryMuscleRegions
        } else {
            fallbackDerivePrimaryRegions(exercise.targetMuscle)
        }
    }

    val secondaryRegions = remember(exercise.id, exercise.secondaryMuscleRegions, exercise.secondaryMuscles) {
        if (!exercise.secondaryMuscleRegions.isNullOrEmpty()) {
            exercise.secondaryMuscleRegions
        } else {
            fallbackDeriveSecondaryRegions(exercise.secondaryMuscles ?: "")
        }
    }

    // Default view mode from exercise data
    val defaultViewMode = remember(exercise.id, exercise.anatomyView) {
        when (exercise.anatomyView?.lowercase()) {
            "anterior" -> AnatomyViewMode.ANTERIOR
            "posterior" -> AnatomyViewMode.POSTERIOR
            else -> AnatomyViewMode.DUAL
        }
    }

    var selectedViewMode by remember(exercise.id) { mutableStateOf(defaultViewMode) }
    var focusedRegion by remember(exercise.id) { mutableStateOf<String?>(null) }

    // Pulsing highlight alpha for primary muscles
    val infiniteTransition = rememberInfiniteTransition(label = "AnatomyPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.78f,
        targetValue = 0.98f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    // Animated zoom & translation for view switching
    val targetScale by animateFloatAsState(
        targetValue = when (selectedViewMode) {
            AnatomyViewMode.DUAL -> 1.0f
            AnatomyViewMode.ANTERIOR -> 1.78f
            AnatomyViewMode.POSTERIOR -> 1.78f
        },
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "AnatomyScale"
    )

    val targetTransX by animateFloatAsState(
        targetValue = when (selectedViewMode) {
            AnatomyViewMode.DUAL -> 0f
            AnatomyViewMode.ANTERIOR -> 0.22f
            AnatomyViewMode.POSTERIOR -> -0.22f
        },
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "AnatomyTransX"
    )

    // Assemble list of all muscle items for cards
    val muscleItems = remember(primaryRegions, secondaryRegions) {
        val items = mutableListOf<MuscleTargetItem>()
        primaryRegions.forEach { r ->
            items.add(
                MuscleTargetItem(
                    regionKey = r,
                    displayName = formatRegionDisplayName(r),
                    anatomicalRole = "Primary Mover",
                    isPrimary = true,
                    biomechanicalFunction = getBiomechanicalFunction(r, true)
                )
            )
        }
        secondaryRegions.forEach { r ->
            if (items.none { it.regionKey == r }) {
                items.add(
                    MuscleTargetItem(
                        regionKey = r,
                        displayName = formatRegionDisplayName(r),
                        anatomicalRole = "Synergist / Stabilizer",
                        isPrimary = false,
                        biomechanicalFunction = getBiomechanicalFunction(r, false)
                    )
                )
            }
        }
        items
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF10161C),
        border = BorderStroke(1.dp, Color(0xFF1E2835))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Badge & Confidence
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Color(0xFF00FF88), CircleShape)
                    )
                    Text(
                        text = "ANATOMICAL TARGET MATRIX",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF00E5FF),
                            letterSpacing = 1.sp,
                            fontSize = 11.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF00FF88).copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Color(0xFF00FF88).copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00FF88),
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = "VERIFIED HIGHLIGHT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00FF88),
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }

            // View Selector Tabs: [ Dual | Front | Back ]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0A0E12), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF1A222B), RoundedCornerShape(10.dp))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AnatomyViewMode.values().forEach { mode ->
                    val isSelected = selectedViewMode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF17202A) else Color.Transparent)
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFF00E5FF).copy(alpha = 0.45f) else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedViewMode = mode }
                            .padding(vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF8899A6),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // Main Anatomy Canvas Viewport (883:655 Aspect Ratio matching permanent reference template)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(883f / 655f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0A0E13))
                    .border(1.dp, Color(0xFF1C2530), RoundedCornerShape(14.dp))
                    .clipToBounds(),
                contentAlignment = Alignment.Center
            ) {
                // Layer 1: Base unhighlighted anatomy template (from permanent reference)
                Image(
                    painter = painterResource(id = R.drawable.anatomy_template_clean),
                    contentDescription = "Anatomy Reference Model",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = targetScale
                            scaleY = targetScale
                            translationX = targetTransX * size.width
                        },
                    contentScale = ContentScale.Fit
                )

                // Layer 2: Secondary Muscle Overlays (Cyan tint)
                secondaryRegions.forEach { region ->
                    val maskRes = getMaskDrawableResource(region)
                    if (maskRes != 0) {
                        val isFocused = focusedRegion == region
                        Image(
                            painter = painterResource(id = maskRes),
                            contentDescription = region,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = targetScale
                                    scaleY = targetScale
                                    translationX = targetTransX * size.width
                                },
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                Color(0xFF00E5FF).copy(alpha = if (isFocused) 0.95f else 0.82f),
                                BlendMode.SrcAtop
                            )
                        )
                    }
                }

                // Layer 3: Primary Muscle Overlays (Electric Mint / Template Red tint with pulse)
                primaryRegions.forEach { region ->
                    val maskRes = getMaskDrawableResource(region)
                    if (maskRes != 0) {
                        val isFocused = focusedRegion == null || focusedRegion == region
                        Image(
                            painter = painterResource(id = maskRes),
                            contentDescription = region,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = targetScale
                                    scaleY = targetScale
                                    translationX = targetTransX * size.width
                                },
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                Color(0xFF00FF88).copy(alpha = if (isFocused) pulseAlpha else 0.5f),
                                BlendMode.SrcAtop
                            )
                        )
                    }
                }

                // Bottom Overlay Indicator: Primary vs Secondary Legend
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(Color(0xFF080C10).copy(alpha = 0.88f), RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFF1E2835), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF00FF88), CircleShape)
                        )
                        Text(
                            text = "Primary",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF00FF88),
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF00E5FF), CircleShape)
                        )
                        Text(
                            text = "Secondary",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF00E5FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }

            // Swipeable / Scrollable Target Muscle Cards Carousel
            if (muscleItems.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TARGET MUSCLE ENGAGEMENT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8899A6),
                                fontSize = 10.sp,
                                letterSpacing = 0.8.sp
                            )
                        )
                        Text(
                            text = "${muscleItems.size} Muscular Zones",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF00E5FF),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        muscleItems.forEach { item ->
                            val isFocused = focusedRegion == item.regionKey
                            val accentColor = if (item.isPrimary) Color(0xFF00FF88) else Color(0xFF00E5FF)

                            Surface(
                                modifier = Modifier
                                    .width(180.dp)
                                    .clickable {
                                        focusedRegion = if (focusedRegion == item.regionKey) null else item.regionKey
                                        // Auto-switch view if needed
                                        val isPosteriorRegion = isPosteriorMuscle(item.regionKey)
                                        if (isPosteriorRegion && selectedViewMode == AnatomyViewMode.ANTERIOR) {
                                            selectedViewMode = AnatomyViewMode.POSTERIOR
                                        } else if (!isPosteriorRegion && selectedViewMode == AnatomyViewMode.POSTERIOR) {
                                            selectedViewMode = AnatomyViewMode.ANTERIOR
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isFocused) accentColor.copy(alpha = 0.15f) else Color(0xFF0B1015),
                                border = BorderStroke(
                                    1.dp,
                                    if (isFocused) accentColor else Color(0xFF1E2835)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = accentColor.copy(alpha = 0.15f),
                                            border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.5f))
                                        ) {
                                            Text(
                                                text = if (item.isPrimary) "PRIMARY" else "STABILIZER",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = accentColor,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 8.sp
                                                ),
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = item.displayName,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        ),
                                        maxLines = 1
                                    )

                                    Text(
                                        text = item.biomechanicalFunction,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF8899A6),
                                            fontSize = 10.sp,
                                            lineHeight = 13.sp
                                        ),
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Returns corresponding drawable resource ID for a region key.
 */
private fun getMaskDrawableResource(key: String): Int {
    return when (key.lowercase()) {
        "chest" -> R.drawable.mask_chest
        "deltoids_front" -> R.drawable.mask_deltoids_front
        "deltoids_rear" -> R.drawable.mask_deltoids_rear
        "biceps" -> R.drawable.mask_biceps
        "triceps" -> R.drawable.mask_triceps
        "forearms_front" -> R.drawable.mask_forearms_front
        "abs" -> R.drawable.mask_abs
        "obliques" -> R.drawable.mask_obliques
        "quads" -> R.drawable.mask_quads
        "glutes" -> R.drawable.mask_glutes
        "hamstrings" -> R.drawable.mask_hamstrings
        "calves_front" -> R.drawable.mask_calves_front
        "calves_rear" -> R.drawable.mask_calves_rear
        "lats" -> R.drawable.mask_lats
        "traps" -> R.drawable.mask_traps
        "lower_back" -> R.drawable.mask_lower_back
        else -> 0
    }
}

private fun isPosteriorMuscle(key: String): Boolean {
    return when (key.lowercase()) {
        "traps", "deltoids_rear", "triceps", "lats", "lower_back", "glutes", "hamstrings", "calves_rear" -> true
        else -> false
    }
}

private fun formatRegionDisplayName(key: String): String {
    return when (key.lowercase()) {
        "chest" -> "Pectoralis Major"
        "deltoids_front" -> "Anterior Deltoids"
        "deltoids_rear" -> "Posterior Deltoids"
        "biceps" -> "Biceps Brachii"
        "triceps" -> "Triceps Brachii"
        "forearms_front" -> "Forearms (Flexors)"
        "abs" -> "Rectus Abdominis"
        "obliques" -> "External Obliques"
        "quads" -> "Quadriceps Femoris"
        "glutes" -> "Gluteus Maximus"
        "hamstrings" -> "Hamstrings"
        "calves_front" -> "Tibialis Anterior"
        "calves_rear" -> "Gastrocnemius & Soleus"
        "lats" -> "Latissimus Dorsi"
        "traps" -> "Trapezius (Upper/Mid)"
        "lower_back" -> "Erector Spinae"
        else -> key.replace("_", " ").capitalize()
    }
}

private fun getBiomechanicalFunction(key: String, isPrimary: Boolean): String {
    return when (key.lowercase()) {
        "chest" -> if (isPrimary) "Prime mover for horizontal arm adduction and press exertion." else "Synergistic stabilization during pushing kinematics."
        "deltoids_front" -> if (isPrimary) "Primary arm forward flexion and shoulder abduction." else "Front shoulder stability maintaining humeral alignment."
        "deltoids_rear" -> if (isPrimary) "Scapular horizontal abduction and external rotation." else "Rear shoulder stabilizer opposing anterior humeral glide."
        "biceps" -> if (isPrimary) "Primary elbow flexion and radio-ulnar supination." else "Synergistic pulling assistance and forearm control."
        "triceps" -> if (isPrimary) "Primary elbow extension and overhead arm lockout." else "Concentric lockout support and elbow joint integrity."
        "forearms_front" -> "Grip endurance, carpal flexor force, and wrist stabilization."
        "abs" -> if (isPrimary) "Core spinal flexion and intra-abdominal force transfer." else "Rigid anterior core bracing preventing lumbar hyperextension."
        "obliques" -> "Rotational torque generation and lateral pelvic stability."
        "quads" -> if (isPrimary) "Primary knee extension and eccentric deceleration power." else "Assists knee alignment through concentric drive."
        "glutes" -> if (isPrimary) "Primary hip extension, pelvic drive, and hip external rotation." else "Pelvic girdle stability and hip hinge control."
        "hamstrings" -> if (isPrimary) "Knee flexion and posterior hip hinge extension." else "Eccentric knee joint deceleration and pelvic control."
        "calves_rear", "calves_front" -> "Ankle plantarflexion, kinetic push-off, and ground stability."
        "lats" -> if (isPrimary) "Primary humeral adduction, extension, and vertical pulling drive." else "Shoulder girdle depression and scapular retraction."
        "traps" -> if (isPrimary) "Scapular elevation, retraction, and upward rotation." else "Neck and upper spine postural stabilization."
        "lower_back" -> "Lumbar spinal extension and posterior chain rigidity."
        else -> "Provides controlled muscular recruitment and structural stability."
    }
}

private fun fallbackDerivePrimaryRegions(targetMuscle: String): List<String> {
    val m = targetMuscle.lowercase()
    return when {
        m.contains("chest") || m.contains("pec") -> listOf("chest")
        m.contains("quad") || m.contains("thigh") -> listOf("quads")
        m.contains("bicep") -> listOf("biceps")
        m.contains("tricep") -> listOf("triceps")
        m.contains("lat") || (m.contains("back") && !m.contains("lower")) -> listOf("lats")
        m.contains("glute") -> listOf("glutes")
        m.contains("hamstring") -> listOf("hamstrings")
        m.contains("shoulder") || m.contains("delt") -> listOf("deltoids_front", "deltoids_rear")
        m.contains("ab") || m.contains("core") -> listOf("abs")
        m.contains("calf") || m.contains("calves") -> listOf("calves_rear")
        else -> listOf("chest")
    }
}

private fun fallbackDeriveSecondaryRegions(secondaryString: String): List<String> {
    val s = secondaryString.lowercase()
    val list = mutableListOf<String>()
    if (s.contains("tricep")) list.add("triceps")
    if (s.contains("bicep")) list.add("biceps")
    if (s.contains("delt") || s.contains("shoulder")) list.add("deltoids_front")
    if (s.contains("core") || s.contains("ab")) list.add("abs")
    if (s.contains("glute")) list.add("glutes")
    if (s.contains("hamstring")) list.add("hamstrings")
    if (s.contains("forearm")) list.add("forearms_front")
    if (s.contains("trap") || s.contains("rhomboid")) list.add("traps")
    if (s.contains("lower back") || s.contains("erector")) list.add("lower_back")
    return list.distinct()
}
