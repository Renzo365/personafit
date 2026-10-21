package com.personafit.app.presentation.workout

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.personafit.app.data.remote.ApiClient
import com.personafit.app.presentation.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer

object ExerciseAssetResolver {
    private var cachedAssets: Set<String>? = null

    fun getAssetFiles(context: Context): Set<String> {
        if (cachedAssets == null) {
            cachedAssets = try {
                context.assets.list("exercises")?.toSet() ?: emptySet()
            } catch (e: Exception) {
                emptySet()
            }
        }
        return cachedAssets ?: emptySet()
    }

    data class AssetResolution(
        val uri: String?,
        val isDedicatedVariant: Boolean,
        val limitationNote: String? = null
    )

    private fun resolveExplicitAssetUri(
        rawPath: String?,
        assets: Set<String>
    ): String? {
        if (rawPath.isNullOrBlank()) return null
        val trimmed = rawPath.trim()
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed
        }
        val leaf = trimmed.substringAfterLast('/')
        if (assets.contains(leaf) && leaf.endsWith(".gif", ignoreCase = true)) {
            return "file:///android_asset/exercises/$leaf"
        }
        val gifLeaf = leaf.replace(Regex("\\.[a-zA-Z0-9]+$"), ".gif")
        if (assets.contains(gifLeaf)) {
            return "file:///android_asset/exercises/$gifLeaf"
        }
        // Remote host URL fallback
        val host = ApiClient.getBaseUrl().removeSuffix("api/")
        val path = trimmed.removePrefix("/")
        return "$host$path"
    }

    private fun resolveBaseMovementUri(
        imageUrl: String?,
        properFormAsset: String?,
        cleanName: String,
        assets: Set<String>
    ): String? {
        // Try explicit proper form asset or image_url
        if (!properFormAsset.isNullOrBlank()) {
            val resolved = resolveExplicitAssetUri(properFormAsset, assets)
            if (resolved != null && resolved.startsWith("file:///android_asset/")) return resolved
        }
        if (!imageUrl.isNullOrBlank()) {
            val resolved = resolveExplicitAssetUri(imageUrl, assets)
            if (resolved != null && resolved.startsWith("file:///android_asset/")) return resolved
        }

        val nameCandidates = listOf(
            "$cleanName.gif",
            "${cleanName}_proper.gif"
        )
        for (n in nameCandidates) {
            if (assets.contains(n)) {
                return "file:///android_asset/exercises/$n"
            }
        }

        val fuzzyMatch = assets.firstOrNull { it.startsWith(cleanName) && it.endsWith(".gif", ignoreCase = true) }
        if (fuzzyMatch != null) {
            return "file:///android_asset/exercises/$fuzzyMatch"
        }

        return resolveExplicitAssetUri(properFormAsset ?: imageUrl, assets)
    }

    fun resolveAsset(
        context: Context,
        imageUrl: String?,
        exerciseName: String,
        mode: AnimationMode = AnimationMode.PROPER_FORM,
        properFormAsset: String? = null,
        commonMistakesAsset: String? = null,
        injuryPreventionAsset: String? = null
    ): AssetResolution {
        val assets = getAssetFiles(context)
        val cleanName = exerciseName.lowercase()
            .replace(Regex("[^a-z0-9]"), "_")
            .replace(Regex("_+"), "_")
            .trim('_')

        val baseUri = resolveBaseMovementUri(imageUrl, properFormAsset, cleanName, assets)

        // 1. Common Mistakes mode
        if (mode == AnimationMode.COMMON_MISTAKES) {
            if (!commonMistakesAsset.isNullOrBlank()) {
                val resolvedFromCms = resolveExplicitAssetUri(commonMistakesAsset, assets)
                if (resolvedFromCms != null && resolvedFromCms != baseUri) {
                    return AssetResolution(
                        uri = resolvedFromCms,
                        isDedicatedVariant = true
                    )
                }
            }

            val mistakeCandidates = mutableListOf("${cleanName}_mistake.gif")
            if (cleanName.contains("pushup") || cleanName.contains("push_up")) {
                mistakeCandidates.add("pushup_mistake.gif")
            } else if (cleanName.contains("squat")) {
                mistakeCandidates.add("squat_mistake.gif")
            }
            for (m in mistakeCandidates) {
                if (assets.contains(m)) {
                    return AssetResolution(
                        uri = "file:///android_asset/exercises/$m",
                        isDedicatedVariant = true
                    )
                }
            }

            // Fallback to base movement with annotated visual warning & fault breakdown
            return AssetResolution(
                uri = baseUri,
                isDedicatedVariant = false,
                limitationNote = "Dedicated mistake GIF pending. Interactive fault breakdown & anatomical hotspot overlays active."
            )
        } else if (mode == AnimationMode.INJURY_PREVENTION) {
            // 2. Injury Prevention mode
            if (!injuryPreventionAsset.isNullOrBlank()) {
                val resolvedFromCms = resolveExplicitAssetUri(injuryPreventionAsset, assets)
                if (resolvedFromCms != null && resolvedFromCms != baseUri) {
                    return AssetResolution(
                        uri = resolvedFromCms,
                        isDedicatedVariant = true
                    )
                }
            }

            val prevCandidates = mutableListOf("${cleanName}_prevention.gif")
            if (cleanName.contains("pushup") || cleanName.contains("push_up")) {
                prevCandidates.addAll(listOf("pushup_incline_prevention.gif", "pushup_knee_prevention.gif", "pushup_wall_prevention.gif"))
            }
            for (p in prevCandidates) {
                if (assets.contains(p)) {
                    return AssetResolution(
                        uri = "file:///android_asset/exercises/$p",
                        isDedicatedVariant = true
                    )
                }
            }

            // Fallback to base movement with protective joint shield overlays & cues
            return AssetResolution(
                uri = baseUri,
                isDedicatedVariant = false,
                limitationNote = "Dedicated prevention GIF pending. Protective alignment guidelines & joint shield overlays active."
            )
        }

        // 3. Proper Form mode
        return AssetResolution(
            uri = baseUri,
            isDedicatedVariant = baseUri != null,
            limitationNote = if (baseUri == null) "Proper form asset pending in CMS. 60 FPS anatomical kinematics active." else null
        )
    }

    fun resolveUri(
        context: Context,
        imageUrl: String?,
        exerciseName: String,
        mode: AnimationMode = AnimationMode.PROPER_FORM,
        properFormAsset: String? = null,
        commonMistakesAsset: String? = null,
        injuryPreventionAsset: String? = null
    ): String? {
        return resolveAsset(context, imageUrl, exerciseName, mode, properFormAsset, commonMistakesAsset, injuryPreventionAsset).uri
    }
}

enum class AnimationMode {
    PROPER_FORM,
    COMMON_MISTAKES,
    INJURY_PREVENTION
}

enum class MovementCategory {
    PUSH_UP,
    CHEST_PRESS,
    CHEST_FLY,
    ROW_HORIZONTAL,
    LAT_PULLDOWN,
    FACE_PULL,
    WALL_SIT,
    SQUAT,
    HIP_HINGE_DEADLIFT,
    GLUTE_BRIDGE,
    SHOULDER_PRESS,
    LATERAL_RAISE,
    BICEP_CURL,
    TRICEP_PUSHDOWN,
    DEAD_BUG,
    PLANK,
    BIRD_DOG,
    GENERAL
}

fun detectMovementCategory(name: String, targetMuscle: String): MovementCategory {
    val n = name.lowercase()
    val m = targetMuscle.lowercase()
    return when {
        n.contains("push-up") || n.contains("pushup") -> MovementCategory.PUSH_UP
        n.contains("pec fly") || n.contains("fly") || n.contains("pec deck") -> MovementCategory.CHEST_FLY
        n.contains("chest press") || n.contains("bench press") || (m.contains("chest") && n.contains("press")) -> MovementCategory.CHEST_PRESS
        n.contains("cable row") || n.contains("dumbbell row") || n.contains("barbell row") || (m.contains("back") && n.contains("row")) -> MovementCategory.ROW_HORIZONTAL
        n.contains("lat pulldown") || n.contains("pull-up") || n.contains("pullup") -> MovementCategory.LAT_PULLDOWN
        n.contains("face pull") -> MovementCategory.FACE_PULL
        n.contains("wall sit") -> MovementCategory.WALL_SIT
        n.contains("squat") || n.contains("leg press") || n.contains("lunge") -> MovementCategory.SQUAT
        n.contains("deadlift") || n.contains("rdl") || (m.contains("hamstring") && !n.contains("curl")) -> MovementCategory.HIP_HINGE_DEADLIFT
        n.contains("glute bridge") || n.contains("hip thrust") || n.contains("kickback") -> MovementCategory.GLUTE_BRIDGE
        n.contains("overhead") || (m.contains("shoulder") && n.contains("press")) -> MovementCategory.SHOULDER_PRESS
        n.contains("lateral raise") -> MovementCategory.LATERAL_RAISE
        n.contains("bicep curl") || n.contains("hammer curl") || n.contains("ez-bar") -> MovementCategory.BICEP_CURL
        n.contains("tricep") || n.contains("pushdown") || n.contains("dip") -> MovementCategory.TRICEP_PUSHDOWN
        n.contains("dead bug") -> MovementCategory.DEAD_BUG
        n.contains("bird dog") -> MovementCategory.BIRD_DOG
        n.contains("plank") -> MovementCategory.PLANK
        else -> MovementCategory.GENERAL
    }
}

data class FaultHotspot(
    val bodyPart: String,
    val faultTitle: String,
    val fixCue: String,
    val xPercent: Float,
    val yPercent: Float
)

data class JointProtectionMarker(
    val jointName: String,
    val protectionRule: String,
    val xPercent: Float,
    val yPercent: Float
)

fun getFaultHotspotsForCategory(category: MovementCategory): List<FaultHotspot> {
    return when (category) {
        MovementCategory.PUSH_UP -> listOf(
            FaultHotspot("Elbows", "90° Flaring Out", "Tuck at 45° in scapular plane", 0.20f, 0.40f),
            FaultHotspot("Lower Back", "Sagging Lumbar Hips", "Squeeze glutes & brace abs", 0.64f, 0.46f)
        )
        MovementCategory.CHEST_PRESS, MovementCategory.CHEST_FLY -> listOf(
            FaultHotspot("Shoulders", "Elbows Flared 90°", "Tuck elbows at 45-60° angle", 0.22f, 0.38f),
            FaultHotspot("Wrists", "Wrists Bent Back", "Stack wrists over forearms", 0.65f, 0.44f)
        )
        MovementCategory.ROW_HORIZONTAL -> listOf(
            FaultHotspot("Traps", "Shrugging Shoulders", "Depress blades, pull to hips", 0.22f, 0.38f),
            FaultHotspot("Lower Back", "Jerking Momentum", "Lock torso angle at 45°", 0.65f, 0.48f)
        )
        MovementCategory.LAT_PULLDOWN -> listOf(
            FaultHotspot("Torso", "Leaning Too Far Back", "Limit backward lean to 15°", 0.22f, 0.40f),
            FaultHotspot("Neck", "Pulling Behind Neck", "Pull to upper chest collarbone", 0.64f, 0.34f)
        )
        MovementCategory.SQUAT, MovementCategory.WALL_SIT -> listOf(
            FaultHotspot("Knees", "Knees Caving Inward", "Push knees outward over toes", 0.22f, 0.52f),
            FaultHotspot("Lumbar", "Rounding Lower Back", "Keep chest proud, brace core", 0.64f, 0.36f)
        )
        MovementCategory.HIP_HINGE_DEADLIFT -> listOf(
            FaultHotspot("Spine", "Rounding Lower Back", "Lock lats & hinge at hips", 0.22f, 0.36f),
            FaultHotspot("Bar Path", "Drifting Away from Shins", "Keep bar skimming legs", 0.64f, 0.54f)
        )
        MovementCategory.GLUTE_BRIDGE -> listOf(
            FaultHotspot("Lower Back", "Hyperextending Lumbar", "Stop when glutes contract", 0.22f, 0.40f),
            FaultHotspot("Feet", "Toes Pushing vs Heels", "Drive through flat heels", 0.64f, 0.52f)
        )
        MovementCategory.SHOULDER_PRESS -> listOf(
            FaultHotspot("Lower Back", "Spine Hyperextension", "Brace core & tuck pelvis", 0.22f, 0.48f),
            FaultHotspot("Elbows", "Behind Body Plane", "Press 30° in scapular plane", 0.64f, 0.38f)
        )
        MovementCategory.LATERAL_RAISE -> listOf(
            FaultHotspot("Traps", "Shrugging Shoulders", "Keep shoulder blades down", 0.22f, 0.36f),
            FaultHotspot("Torso", "Swinging Momentum", "Control smooth 2s tempo", 0.64f, 0.48f)
        )
        MovementCategory.BICEP_CURL -> listOf(
            FaultHotspot("Elbows", "Swinging Forward", "Pin elbows beside ribcage", 0.22f, 0.44f),
            FaultHotspot("Back", "Leaning Backward", "Stand vertical, no sway", 0.64f, 0.38f)
        )
        MovementCategory.TRICEP_PUSHDOWN -> listOf(
            FaultHotspot("Elbows", "Flaring Outward", "Pin elbows tight to sides", 0.22f, 0.42f),
            FaultHotspot("Shoulders", "Internal Rotation", "Roll shoulders back & down", 0.64f, 0.36f)
        )
        MovementCategory.DEAD_BUG, MovementCategory.PLANK, MovementCategory.BIRD_DOG -> listOf(
            FaultHotspot("Pelvis", "Anterior Pelvic Tilt", "Press lower back into floor", 0.22f, 0.42f),
            FaultHotspot("Breathing", "Holding Breath", "Exhale on reach extension", 0.64f, 0.36f)
        )
        else -> listOf(
            FaultHotspot("Torso", "Loss of Core Tension", "Brace abdominals firmly", 0.22f, 0.40f),
            FaultHotspot("Tempo", "Uncontrolled Jerks", "Maintain steady 2-1-2 tempo", 0.64f, 0.48f)
        )
    }
}

fun getJointProtectionMarkersForCategory(category: MovementCategory): List<JointProtectionMarker> {
    return when (category) {
        MovementCategory.PUSH_UP -> listOf(
            JointProtectionMarker("Rotator Cuff", "45° Scapular Plane Tuck", 0.20f, 0.38f),
            JointProtectionMarker("Carpal Wrists", "Stacked Under Shoulders", 0.64f, 0.52f)
        )
        MovementCategory.CHEST_PRESS, MovementCategory.CHEST_FLY -> listOf(
            JointProtectionMarker("Glenohumeral Joint", "Retract & Depress Scapulae", 0.22f, 0.36f),
            JointProtectionMarker("Acromion Arch", "Safe Touchpoint at Mid-Sternum", 0.64f, 0.44f)
        )
        MovementCategory.ROW_HORIZONTAL -> listOf(
            JointProtectionMarker("Lumbar Spine", "Neutral Anti-Flexion Brace", 0.22f, 0.46f),
            JointProtectionMarker("Scapular Retractors", "Lead Movement With Elbows", 0.64f, 0.34f)
        )
        MovementCategory.LAT_PULLDOWN -> listOf(
            JointProtectionMarker("Subacromial Space", "Front Plane Pull Only", 0.22f, 0.34f),
            JointProtectionMarker("Rotator Cuff", "Drive Elbows Down to Pockets", 0.64f, 0.44f)
        )
        MovementCategory.SQUAT, MovementCategory.WALL_SIT -> listOf(
            JointProtectionMarker("Patellar Tendon", "Track Knees Over 2nd Toe", 0.22f, 0.52f),
            JointProtectionMarker("Lumbar L5-S1", "Rigid Upright Core Neutral", 0.64f, 0.36f)
        )
        MovementCategory.HIP_HINGE_DEADLIFT -> listOf(
            JointProtectionMarker("L4-S1 Lumbar Discs", "Strict Anti-Flexion Lat Lock", 0.22f, 0.36f),
            JointProtectionMarker("Proximal Hamstring", "Soft Knee 15° Hinge Angle", 0.64f, 0.54f)
        )
        MovementCategory.GLUTE_BRIDGE -> listOf(
            JointProtectionMarker("Lumbar Vertebrae", "Posterior Pelvic Tilt Lock", 0.22f, 0.40f),
            JointProtectionMarker("Knee Ligaments", "90° Shin-to-Thigh Angle", 0.64f, 0.52f)
        )
        MovementCategory.SHOULDER_PRESS -> listOf(
            JointProtectionMarker("Subacromial Space", "Scapular Plane Press 30°", 0.22f, 0.36f),
            JointProtectionMarker("Lumbar Spine", "Ribcage Down / Anti-Extension", 0.64f, 0.48f)
        )
        MovementCategory.LATERAL_RAISE -> listOf(
            JointProtectionMarker("Supraspinatus Tendon", "Slight 15° Forward Scaption", 0.22f, 0.36f),
            JointProtectionMarker("Elbow Joint", "Slight Soft 10° Elbow Bend", 0.64f, 0.48f)
        )
        MovementCategory.BICEP_CURL -> listOf(
            JointProtectionMarker("Distal Biceps Tendon", "Controlled 2s Negative descent", 0.22f, 0.44f),
            JointProtectionMarker("Carpal Tunnel", "Neutral Unbroken Wrist", 0.64f, 0.56f)
        )
        MovementCategory.TRICEP_PUSHDOWN -> listOf(
            JointProtectionMarker("Triceps Tendon", "Pin Upper Arm to Torso", 0.22f, 0.40f),
            JointProtectionMarker("Shoulder Capsule", "No Forward Shoulder Roll", 0.64f, 0.34f)
        )
        MovementCategory.DEAD_BUG, MovementCategory.PLANK, MovementCategory.BIRD_DOG -> listOf(
            JointProtectionMarker("Lumbar Spine", "Full Floor Contact / No Arch", 0.22f, 0.42f),
            JointProtectionMarker("Shoulder Stability", "Protract & Push Floor Away", 0.64f, 0.36f)
        )
        else -> listOf(
            JointProtectionMarker("Primary Joint", "Active Muscle Tension Lock", 0.22f, 0.38f),
            JointProtectionMarker("Spinal Column", "Neutral Cervical-Lumbar Alignment", 0.64f, 0.46f)
        )
    }
}

@Composable
private fun FaultHotspotOverlay(
    hotspot: FaultHotspot,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hotspot_pulse_${hotspot.bodyPart}")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale, alpha = pulseAlpha)
                    .background(CoralAccent, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(CoralAccent, CircleShape)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = SurfaceCardDark.copy(alpha = 0.94f),
            border = BorderStroke(1.dp, CoralAccent.copy(alpha = 0.8f)),
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)) {
                Text(
                    text = "❌ ${hotspot.bodyPart}: ${hotspot.faultTitle}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CoralAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    maxLines = 1
                )
                Text(
                    text = "Fix: ${hotspot.fixCue}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.Medium,
                        fontSize = 8.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun JointProtectionOverlay(
    marker: JointProtectionMarker,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shield_pulse_${marker.jointName}")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale, alpha = pulseAlpha)
                    .background(Color(0xFF00E5FF), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .background(Color(0xFF00E5FF), CircleShape)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = SurfaceCardDark.copy(alpha = 0.94f),
            border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.8f)),
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)) {
                Text(
                    text = "🛡️ ${marker.jointName}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    maxLines = 1
                )
                Text(
                    text = marker.protectionRule,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 8.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Biomechanical Kinetic Exercise Animation Component
 * Renders real 60fps anatomical vector animations tailored to each exercise movement pattern,
 * supporting distinct modes for Proper Form, Common Mistakes, and Injury Prevention.
 */
@Composable
fun ExerciseAnimationView(
    exerciseName: String,
    targetMuscle: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    properFormAsset: String? = null,
    commonMistakesAsset: String? = null,
    injuryPreventionAsset: String? = null,
    heightDp: Int = 250,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    compact: Boolean = false
) {
    val context = LocalContext.current
    val assetResolution = remember(exerciseName, imageUrl, properFormAsset, commonMistakesAsset, injuryPreventionAsset, mode) {
        ExerciseAssetResolver.resolveAsset(
            context = context,
            imageUrl = imageUrl,
            exerciseName = exerciseName,
            mode = mode,
            properFormAsset = properFormAsset,
            commonMistakesAsset = commonMistakesAsset,
            injuryPreventionAsset = injuryPreventionAsset
        )
    }
    val gifUri = assetResolution.uri
    val isDedicatedVariant = assetResolution.isDedicatedVariant

    var showKineticSkeleton by remember { mutableStateOf(false) }

    val category = remember(exerciseName, targetMuscle) {
        detectMovementCategory(exerciseName, targetMuscle)
    }

    var isPlaying by remember { mutableStateOf(true) }
    var manualStep by remember { mutableStateOf<Int?>(null) } // null = auto, 1 = start, 2 = mid/peak, 3 = finish

    val infiniteTransition = rememberInfiniteTransition(label = "ExerciseAnimationTransition")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CycleProgress"
    )

    val currentProgress = when (manualStep) {
        1 -> 0.05f // Start position
        2 -> 0.50f // Peak movement position
        3 -> 0.95f // Finished/locked return
        else -> if (isPlaying) animatedProgress else 0.50f
    }

    // Kinematic motion factor (0 = start, 1 = full peak displacement)
    val motionFactor = remember(currentProgress) {
        when {
            currentProgress <= 0.42f -> {
                val p = currentProgress / 0.42f
                (1f - cos(p * PI.toFloat())) / 2f
            }
            currentProgress <= 0.55f -> 1.0f
            currentProgress <= 0.92f -> {
                val p = (currentProgress - 0.55f) / 0.37f
                1.0f - ((1f - cos(p * PI.toFloat())) / 2f)
            }
            else -> 0.0f
        }
    }

    val (phaseText, phaseColor) = remember(currentProgress, category, mode) {
        if (mode == AnimationMode.COMMON_MISTAKES) {
            Pair("FAULT: VULNERABLE JOINT STRESS", CoralAccent)
        } else if (mode == AnimationMode.INJURY_PREVENTION) {
            Pair("SAFETY: PROTECTIVE RANGE LIMITER", SafeGreen)
        } else {
            when {
                currentProgress <= 0.42f -> {
                    when (category) {
                        MovementCategory.ROW_HORIZONTAL, MovementCategory.LAT_PULLDOWN, MovementCategory.BICEP_CURL, MovementCategory.FACE_PULL ->
                            Pair("CONCENTRIC (PULL / DRIVE)", ElectricMint)
                        MovementCategory.WALL_SIT, MovementCategory.PLANK ->
                            Pair("ISOMETRIC TENSION HOLD", CautionAmber)
                        else ->
                            Pair("ECCENTRIC (LOWERING / SETUP)", NeonCyan)
                    }
                }
                currentProgress <= 0.55f -> Pair("PEAK CONTRACTION HOLD (1s)", CautionAmber)
                currentProgress <= 0.92f -> {
                    when (category) {
                        MovementCategory.ROW_HORIZONTAL, MovementCategory.LAT_PULLDOWN, MovementCategory.BICEP_CURL, MovementCategory.FACE_PULL ->
                            Pair("CONTROLLED RETURN / ECCENTRIC", NeonCyan)
                        MovementCategory.WALL_SIT, MovementCategory.PLANK ->
                            Pair("POSTURAL STABILIZATION", ElectricMint)
                        else ->
                            Pair("CONCENTRIC (DRIVING / PRESS)", ElectricMint)
                    }
                }
                else -> Pair("START POSITION RESET", SafeGreen)
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp.dp),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(
            if (mode == AnimationMode.COMMON_MISTAKES || mode == AnimationMode.INJURY_PREVENTION) 2.dp else 1.5.dp,
            when (mode) {
                AnimationMode.COMMON_MISTAKES -> CoralAccent
                AnimationMode.INJURY_PREVENTION -> Color(0xFF00E5FF)
                else -> NeonCyan.copy(alpha = 0.5f)
            }
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Main Animation / Skeleton Display
            if (gifUri != null && !showKineticSkeleton) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.White)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(gifUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "$exerciseName visual animation",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = NeonCyan,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        error = {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(SurfaceCardDark)
                                    .padding(
                                        top = if (compact) 24.dp else 36.dp,
                                        bottom = 8.dp,
                                        start = if (compact) 6.dp else 16.dp,
                                        end = if (compact) 6.dp else 16.dp
                                    )
                            ) {
                                val w = size.width
                                val h = size.height
                                when (category) {
                                    MovementCategory.PUSH_UP -> drawPushUpKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.CHEST_PRESS -> drawChestPressKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.CHEST_FLY -> drawChestFlyKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.ROW_HORIZONTAL -> drawRowKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.LAT_PULLDOWN -> drawLatPulldownKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.WALL_SIT -> drawWallSitKinematics(w, h, currentProgress, mode)
                                    MovementCategory.SQUAT -> drawSquatKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.HIP_HINGE_DEADLIFT -> drawDeadliftKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.GLUTE_BRIDGE -> drawGluteBridgeKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.SHOULDER_PRESS -> drawShoulderPressKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.BICEP_CURL -> drawBicepCurlKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.DEAD_BUG -> drawDeadBugKinematics(w, h, motionFactor, mode, currentProgress)
                                    MovementCategory.PLANK -> drawPlankKinematics(w, h, currentProgress, mode)
                                    else -> drawGeneralMovementKinematics(w, h, motionFactor, targetMuscle, mode, currentProgress)
                                }
                            }
                        }
                    )

                    // Distinct Vignette & Scrim Overlays when displaying base GIF
                    if (!isDedicatedVariant) {
                        if (mode == AnimationMode.COMMON_MISTAKES) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                CoralAccent.copy(alpha = 0.08f),
                                                CoralAccent.copy(alpha = 0.32f)
                                            )
                                        )
                                    )
                            )
                        } else if (mode == AnimationMode.INJURY_PREVENTION) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xFF00E5FF).copy(alpha = 0.06f),
                                                Color(0xFF00E5FF).copy(alpha = 0.26f)
                                            )
                                        )
                                    )
                            )
                        }
                    }
                }
            } else {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = if (compact) 24.dp else 36.dp,
                            bottom = 8.dp,
                            start = if (compact) 6.dp else 16.dp,
                            end = if (compact) 6.dp else 16.dp
                        )
                ) {
                    val w = size.width
                    val h = size.height
                    when (category) {
                        MovementCategory.PUSH_UP -> drawPushUpKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.CHEST_PRESS -> drawChestPressKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.CHEST_FLY -> drawChestFlyKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.ROW_HORIZONTAL -> drawRowKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.LAT_PULLDOWN -> drawLatPulldownKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.WALL_SIT -> drawWallSitKinematics(w, h, currentProgress, mode)
                        MovementCategory.SQUAT -> drawSquatKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.HIP_HINGE_DEADLIFT -> drawDeadliftKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.GLUTE_BRIDGE -> drawGluteBridgeKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.SHOULDER_PRESS -> drawShoulderPressKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.BICEP_CURL -> drawBicepCurlKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.DEAD_BUG -> drawDeadBugKinematics(w, h, motionFactor, mode, currentProgress)
                        MovementCategory.PLANK -> drawPlankKinematics(w, h, currentProgress, mode)
                        else -> drawGeneralMovementKinematics(w, h, motionFactor, targetMuscle, mode, currentProgress)
                    }
                }
            }

            // HUD Overlays
            if (compact) {
                // Compact Mode: Sleek contextual mode pill
                val (badgeText, badgeColor) = when (mode) {
                    AnimationMode.PROPER_FORM -> {
                        if (gifUri == null) Pair("⚙ 60FPS KINEMATICS", CautionAmber)
                        else Pair("✓ PROPER FORM", ElectricMint)
                    }
                    AnimationMode.COMMON_MISTAKES -> {
                        if (gifUri == null) Pair("⚙ MISTAKE SIMULATION", CoralAccent)
                        else Pair(if (isDedicatedVariant) "✕ MISTAKE DEMO" else "✕ TECHNIQUE FAULT", CoralAccent)
                    }
                    AnimationMode.INJURY_PREVENTION -> {
                        if (gifUri == null) Pair("⚙ PREVENTIVE SIMULATION", SafeGreen)
                        else Pair(if (isDedicatedVariant) "🛡️ PREVENTIVE FORM" else "🛡️ SAFE ALIGNMENT", SafeGreen)
                    }
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceElevatedDark.copy(alpha = 0.92f),
                    border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                if (!isDedicatedVariant || gifUri == null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = SurfaceElevatedDark.copy(alpha = 0.92f),
                        border = BorderStroke(1.dp, BorderDark)
                    ) {
                        Text(
                            text = if (gifUri == null) "Asset Pending in CMS" else "Reference Movement",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                if (gifUri != null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clickable { showKineticSkeleton = !showKineticSkeleton },
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceElevatedDark.copy(alpha = 0.92f),
                        border = BorderStroke(1.dp, if (showKineticSkeleton) NeonCyan else BorderDark)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = if (showKineticSkeleton) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle View",
                                tint = if (showKineticSkeleton) NeonCyan else TextSecondary,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = if (showKineticSkeleton) "GIF" else "Joints",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (showKineticSkeleton) NeonCyan else TextSecondary,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            } else {
                // Non-compact Mode: Rich contextual header badge
                val (badgeText, badgeColor, badgeIcon) = when (mode) {
                    AnimationMode.PROPER_FORM -> Triple(
                        "✓ Correct Biomechanics & Form",
                        ElectricMint,
                        Icons.Default.CheckCircle
                    )
                    AnimationMode.COMMON_MISTAKES -> Triple(
                        if (isDedicatedVariant) "⚠️ Form Breakdown & Common Mistakes" else "⚠️ Form Breakdown & Mistakes to Avoid",
                        CoralAccent,
                        Icons.Default.Warning
                    )
                    AnimationMode.INJURY_PREVENTION -> Triple(
                        if (isDedicatedVariant) "🛡️ Preventive Form Execution" else "🛡️ Joint & Injury Protection View",
                        Color(0xFF00E5FF),
                        Icons.Default.Shield
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .background(SurfaceElevatedDark.copy(alpha = 0.94f), RoundedCornerShape(20.dp))
                        .border(1.2.dp, badgeColor.copy(alpha = 0.85f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (showKineticSkeleton) phaseText else badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (showKineticSkeleton) phaseColor else badgeColor,
                            fontSize = 11.sp
                        )
                    )
                }

                // Controls Top Right: Play/Pause, View Toggle
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (gifUri != null) {
                        Surface(
                            modifier = Modifier
                                .clickable { showKineticSkeleton = !showKineticSkeleton },
                            shape = RoundedCornerShape(8.dp),
                            color = if (showKineticSkeleton) ElectricMint.copy(alpha = 0.2f) else SurfaceElevatedDark.copy(alpha = 0.92f),
                            border = BorderStroke(1.dp, if (showKineticSkeleton) ElectricMint else BorderDark)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = if (showKineticSkeleton) ElectricMint else TextSecondary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = if (showKineticSkeleton) "Real Animation" else "Biomechanics",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (showKineticSkeleton) ElectricMint else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    if (showKineticSkeleton) {
                        IconButton(
                            onClick = {
                                manualStep = null
                                isPlaying = !isPlaying
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = ElectricMint,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Interactive Overlays & In-Context Cues (when not in skeleton mode)
                if (!showKineticSkeleton) {
                    when (mode) {
                        AnimationMode.PROPER_FORM -> {
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                                shape = RoundedCornerShape(14.dp),
                                color = SurfaceElevatedDark.copy(alpha = 0.94f),
                                border = BorderStroke(1.dp, ElectricMint.copy(alpha = 0.6f)),
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ElectricMint, modifier = Modifier.size(11.dp))
                                        Text("Posture Alignment", style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold))
                                    }
                                    Box(modifier = Modifier.size(3.dp).background(TextMuted, CircleShape))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ElectricMint, modifier = Modifier.size(11.dp))
                                        Text("Controlled Tempo", style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold))
                                    }
                                    Box(modifier = Modifier.size(3.dp).background(TextMuted, CircleShape))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ElectricMint, modifier = Modifier.size(11.dp))
                                        Text("Full Active Range", style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold))
                                    }
                                }
                            }
                        }
                        AnimationMode.COMMON_MISTAKES -> {
                            if (!isDedicatedVariant) {
                                val hotspots = remember(category) { getFaultHotspotsForCategory(category) }
                                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                    val maxW = maxWidth
                                    val maxH = maxHeight
                                    hotspots.forEach { hotspot ->
                                        val xOffset = ((maxW - 130.dp) * hotspot.xPercent).coerceIn(8.dp, maxW - 145.dp)
                                        val yOffset = ((maxH - 65.dp) * hotspot.yPercent).coerceIn(38.dp, maxH - 65.dp)
                                        FaultHotspotOverlay(
                                            hotspot = hotspot,
                                            modifier = Modifier.offset(x = xOffset, y = yOffset)
                                        )
                                    }
                                }
                            }
                        }
                        AnimationMode.INJURY_PREVENTION -> {
                            if (!isDedicatedVariant) {
                                val jointMarkers = remember(category) { getJointProtectionMarkersForCategory(category) }
                                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                    val maxW = maxWidth
                                    val maxH = maxHeight
                                    jointMarkers.forEach { marker ->
                                        val xOffset = ((maxW - 130.dp) * marker.xPercent).coerceIn(8.dp, maxW - 145.dp)
                                        val yOffset = ((maxH - 65.dp) * marker.yPercent).coerceIn(38.dp, maxH - 65.dp)
                                        JointProtectionOverlay(
                                            marker = marker,
                                            modifier = Modifier.offset(x = xOffset, y = yOffset)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom scrubbers when in skeleton mode
                if (showKineticSkeleton) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                            .background(SurfaceElevatedDark.copy(alpha = 0.92f), RoundedCornerShape(20.dp))
                            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(1 to "Start", 2 to "Peak", 3 to "Return").forEach { (step, label) ->
                            val isSelected = manualStep == step
                            Surface(
                                modifier = Modifier
                                    .clickable {
                                        if (isSelected) {
                                            manualStep = null
                                            isPlaying = true
                                        } else {
                                            manualStep = step
                                            isPlaying = false
                                        }
                                    },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSelected) ElectricMint.copy(alpha = 0.25f) else Color.Transparent,
                                border = if (isSelected) BorderStroke(1.dp, ElectricMint) else null
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) ElectricMint else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== KINEMATIC DRAWING IMPLEMENTATIONS ====================

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPushUpKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.82f
    val handX = w * 0.35f
    val toeX = w * 0.82f

    // Floor
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    // Hand & Toe base
    drawCircle(color = ElectricMint, radius = 7f, center = Offset(handX, floorY - 3f))
    drawCircle(color = TextSecondary, radius = 6f, center = Offset(toeX, floorY - 3f))

    val shoulderTopY = floorY - (h * 0.50f)
    val shoulderBottomY = floorY - (h * 0.18f)
    val shoulderY = shoulderTopY + motionFactor * (shoulderBottomY - shoulderTopY)
    val shoulderX = handX + 15f + motionFactor * 8f

    val headX = shoulderX - 35f
    val headY = shoulderY - 14f

    val elbowX = (shoulderX + handX) / 2f + (motionFactor * 40f + 8f) * 0.85f
    val elbowY = (shoulderY + floorY) / 2f - (motionFactor * 40f + 8f) * 0.30f

    val bodyDx = shoulderX - toeX
    val bodyDy = shoulderY - floorY

    // In COMMON_MISTAKES mode: hips sag downward causing anterior pelvic lumbar shear
    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    val hipSag = if (isMistake) 32f else 0f
    val hipX = shoulderX - bodyDx * 0.38f
    val hipY = shoulderY - bodyDy * 0.38f + hipSag
    val kneeX = shoulderX - bodyDx * 0.70f
    val kneeY = shoulderY - bodyDy * 0.70f + (hipSag * 0.4f)

    if (mode == AnimationMode.INJURY_PREVENTION) {
        // Spine neutral guide line
        drawLine(
            color = SafeGreen.copy(alpha = 0.7f),
            start = Offset(headX, headY),
            end = Offset(toeX, floorY),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
        )
    }

    if (isMistake) {
        // Red stress indicator on sagging lumbar spine
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(hipX, hipY))
        drawCircle(color = WarningRed, radius = 6f, center = Offset(hipX, hipY))
    } else if (motionFactor > 0.4f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.3f * motionFactor), radius = 24f, center = Offset(shoulderX - 10f, shoulderY + 8f))
    }

    // Legs
    drawLine(color = TextSecondary, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(kneeX, kneeY), end = Offset(toeX, floorY), strokeWidth = 11f, cap = StrokeCap.Round)

    // Torso
    val spineColor = if (isMistake) CoralAccent else TextPrimary
    drawLine(color = spineColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawLine(color = if (isMistake) WarningRed else ElectricMint, start = Offset(shoulderX - 2f, shoulderY + 2f), end = Offset(hipX - 2f, hipY + 2f), strokeWidth = 3f, cap = StrokeCap.Round)

    // Arm (Upper arm + Forearm)
    val armColor = if (isMistake) CoralAccent else NeonCyan
    drawLine(color = armColor, start = Offset(shoulderX, shoulderY), end = Offset(elbowX, elbowY), strokeWidth = 11f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(elbowX, elbowY), end = Offset(handX, floorY), strokeWidth = 10f, cap = StrokeCap.Round)

    // Head
    drawCircle(color = TextPrimary, radius = 12f, center = Offset(headX, headY))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawChestPressKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.85f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val seatBackX = w * 0.30f
    val seatY = floorY - h * 0.35f
    drawLine(color = SurfaceElevatedDark, start = Offset(seatBackX, floorY - h * 0.70f), end = Offset(seatBackX, seatY), strokeWidth = 12f)
    drawLine(color = SurfaceElevatedDark, start = Offset(seatBackX - 10f, seatY), end = Offset(seatBackX + 50f, seatY), strokeWidth = 12f)

    val hipX = seatBackX + 16f
    val hipY = seatY - 10f
    val shoulderX = seatBackX + 16f
    val shoulderY = seatY - h * 0.30f
    val headX = shoulderX
    val headY = shoulderY - 26f

    val kneeX = hipX + 45f
    val kneeY = hipY + 10f
    val footX = kneeX
    val footY = floorY - 4f
    drawLine(color = TextSecondary, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 10f, cap = StrokeCap.Round)

    drawLine(color = TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 13f, center = Offset(headX, headY))

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    // Common Mistake: 90-degree lateral flare (elbows way too high level with shoulders/neck)
    val elbowHeightOffset = if (isMistake) -20f else 28f - motionFactor * 10f

    val handStartX = shoulderX + 25f
    val handEndX = shoulderX + 110f
    val handX = handStartX + motionFactor * (handEndX - handStartX)
    val handY = shoulderY + 4f

    val elbowStartX = shoulderX - 15f
    val elbowEndX = shoulderX + 45f
    val elbowX = elbowStartX + motionFactor * (elbowEndX - elbowStartX)
    val elbowY = shoulderY + elbowHeightOffset

    // Machine Handles Bar
    drawLine(color = BorderDark, start = Offset(handX, handY - 20f), end = Offset(handX, handY + 20f), strokeWidth = 5f, cap = StrokeCap.Round)
    drawCircle(color = if (isMistake) WarningRed else NeonCyan, radius = 6f, center = Offset(handX, handY))

    // Arm bones
    val armColor = if (isMistake) CoralAccent else NeonCyan
    drawLine(color = armColor, start = Offset(shoulderX, shoulderY), end = Offset(elbowX, elbowY), strokeWidth = 11f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(elbowX, elbowY), end = Offset(handX, handY), strokeWidth = 10f, cap = StrokeCap.Round)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 24f, center = Offset(shoulderX + 5f, shoulderY - 5f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Safe 45-degree angle scapular limiter
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(shoulderX, shoulderY),
            end = Offset(shoulderX + 50f, shoulderY + 35f),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.35f * motionFactor), radius = 22f, center = Offset(shoulderX + 18f, shoulderY + 12f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawChestFlyKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val centerX = w * 0.50f
    val centerY = h * 0.50f

    // Cable Pulleys left and right
    drawCircle(color = BorderDark, radius = 10f, center = Offset(centerX - 130f, centerY))
    drawCircle(color = BorderDark, radius = 10f, center = Offset(centerX + 130f, centerY))

    // Torso in Center
    drawCircle(color = TextPrimary, radius = 14f, center = Offset(centerX, centerY - 45f)) // Head
    drawLine(color = TextPrimary, start = Offset(centerX, centerY - 30f), end = Offset(centerX, centerY + 35f), strokeWidth = 18f, cap = StrokeCap.Round)

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    // Common Mistake: Arms locked rigid straight, overextended 25px behind torso plane
    val maxSpread = if (isMistake) 130f else 105f
    val leftArmSpread = maxSpread - motionFactor * 80f
    val rightArmSpread = maxSpread - motionFactor * 80f

    val bendRatio = if (isMistake) 0.95f else 0.60f

    // Left Arm
    val leftElbowX = centerX - leftArmSpread * bendRatio
    val leftElbowY = centerY + (if (isMistake) -5f else 10f)
    val armColor = if (isMistake) CoralAccent else NeonCyan
    drawLine(color = armColor, start = Offset(centerX - 12f, centerY - 15f), end = Offset(leftElbowX, leftElbowY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(leftElbowX, leftElbowY), end = Offset(centerX - leftArmSpread, centerY), strokeWidth = 9f, cap = StrokeCap.Round)
    drawCircle(color = if (isMistake) WarningRed else ElectricMint, radius = 5f, center = Offset(centerX - leftArmSpread, centerY))

    // Right Arm
    val rightElbowX = centerX + rightArmSpread * bendRatio
    val rightElbowY = centerY + (if (isMistake) -5f else 10f)
    drawLine(color = armColor, start = Offset(centerX + 12f, centerY - 15f), end = Offset(rightElbowX, rightElbowY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(rightElbowX, rightElbowY), end = Offset(centerX + rightArmSpread, centerY), strokeWidth = 9f, cap = StrokeCap.Round)
    drawCircle(color = if (isMistake) WarningRed else ElectricMint, radius = 5f, center = Offset(centerX + rightArmSpread, centerY))

    // Cable lines
    drawLine(color = TextSecondary.copy(alpha = 0.5f), start = Offset(centerX - 130f, centerY), end = Offset(centerX - leftArmSpread, centerY), strokeWidth = 2f)
    drawLine(color = TextSecondary.copy(alpha = 0.5f), start = Offset(centerX + 130f, centerY), end = Offset(centerX + rightArmSpread, centerY), strokeWidth = 2f)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(centerX - 12f, centerY - 15f))
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(centerX + 12f, centerY - 15f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Safe range limiter brackets
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(centerX - 100f, centerY - 25f),
            end = Offset(centerX - 100f, centerY + 25f),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(centerX + 100f, centerY - 25f),
            end = Offset(centerX + 100f, centerY + 25f),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.4f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.4f * motionFactor), radius = 24f, center = Offset(centerX, centerY - 5f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRowKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.85f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val padX = w * 0.55f
    val seatY = floorY - h * 0.32f
    drawLine(color = SurfaceElevatedDark, start = Offset(padX, floorY - h * 0.65f), end = Offset(padX, seatY), strokeWidth = 12f)
    drawLine(color = SurfaceElevatedDark, start = Offset(padX + 5f, seatY), end = Offset(padX + 60f, seatY), strokeWidth = 12f)

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    // Common Mistake: Rocking torso backward 35px using lower-back momentum instead of staying against pad
    val torsoRock = if (isMistake) motionFactor * 35f else 0f

    val hipX = padX + 45f
    val hipY = seatY - 10f
    val shoulderX = padX + 35f + torsoRock
    val shoulderY = seatY - h * 0.32f
    val headX = shoulderX
    val headY = shoulderY - 26f

    val kneeX = hipX - 35f
    val kneeY = hipY + 12f
    val footX = kneeX - 10f
    val footY = floorY - 4f
    drawLine(color = TextSecondary, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 10f, cap = StrokeCap.Round)

    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 13f, center = Offset(headX, headY))

    val handStartX = padX - 45f
    val handEndX = shoulderX - 5f
    val handX = handStartX + motionFactor * (handEndX - handStartX)
    val handY = shoulderY + 12f

    val elbowStartX = shoulderX - 15f
    val elbowEndX = shoulderX + 45f
    val elbowX = elbowStartX + motionFactor * (elbowEndX - elbowStartX)
    val elbowY = shoulderY + 15f

    drawLine(color = TextSecondary.copy(alpha = 0.5f), start = Offset(padX - 80f, handY), end = Offset(handX, handY), strokeWidth = 2f)
    drawLine(color = if (isMistake) CoralAccent else NeonCyan, start = Offset(shoulderX, shoulderY), end = Offset(elbowX, elbowY), strokeWidth = 11f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(elbowX, elbowY), end = Offset(handX, handY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawCircle(color = if (isMistake) WarningRed else ElectricMint, radius = 6f, center = Offset(handX, handY))

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(hipX - 5f, hipY - 10f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Chest contact line
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(padX + 2f, floorY - h * 0.65f),
            end = Offset(padX + 2f, seatY),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.35f * motionFactor), radius = 24f, center = Offset(shoulderX + 10f, shoulderY + 18f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLatPulldownKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.88f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val seatX = w * 0.50f
    val seatY = floorY - h * 0.30f
    drawLine(color = SurfaceElevatedDark, start = Offset(seatX - 30f, seatY), end = Offset(seatX + 30f, seatY), strokeWidth = 12f)

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    val hipX = seatX
    val hipY = seatY - 10f
    val shoulderX = seatX - (if (isMistake) motionFactor * 25f else 4f)
    val shoulderY = seatY - h * 0.30f
    val headX = shoulderX + (if (isMistake) 18f else 0f) // Head juts forward
    val headY = shoulderY - 25f

    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 13f, center = Offset(headX, headY))

    val barTopY = shoulderY - 70f
    val barBottomY = shoulderY + (if (isMistake) 15f else 4f)
    val barY = barTopY + motionFactor * (barBottomY - barTopY)

    drawLine(color = BorderDark, start = Offset(shoulderX - 70f, barY), end = Offset(shoulderX + 70f, barY), strokeWidth = 5f, cap = StrokeCap.Round)

    val elbowY = barTopY + 25f + motionFactor * 40f
    val leftElbowX = shoulderX - 40f - motionFactor * 10f
    val rightElbowX = shoulderX + 40f + motionFactor * 10f

    val armColor = if (isMistake) CoralAccent else NeonCyan
    drawLine(color = armColor, start = Offset(shoulderX - 10f, shoulderY), end = Offset(leftElbowX, elbowY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(leftElbowX, elbowY), end = Offset(shoulderX - 55f, barY), strokeWidth = 9f, cap = StrokeCap.Round)

    drawLine(color = armColor, start = Offset(shoulderX + 10f, shoulderY), end = Offset(rightElbowX, elbowY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(rightElbowX, elbowY), end = Offset(shoulderX + 55f, barY), strokeWidth = 9f, cap = StrokeCap.Round)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(headX - 10f, headY + 12f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Clavicle target line
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(shoulderX - 60f, shoulderY + 4f),
            end = Offset(shoulderX + 60f, shoulderY + 4f),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.35f * motionFactor), radius = 26f, center = Offset(shoulderX, shoulderY + 15f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWallSitKinematics(
    w: Float,
    h: Float,
    progress: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM
) {
    val floorY = h * 0.85f
    val wallX = w * 0.35f

    drawLine(color = BorderDark, start = Offset(wallX, 0f), end = Offset(wallX, floorY), strokeWidth = 6f)
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    val backX = wallX + 8f
    val shoulderY = floorY - h * 0.50f
    val hipY = floorY - h * 0.28f
    val headY = shoulderY - 24f

    // Common mistake: feet too close to wall, knees jutting far forward past toes
    val footX = if (isMistake) backX + h * 0.16f else backX + h * 0.28f
    val kneeX = backX + h * 0.28f
    val kneeY = hipY
    val footY = floorY - 3f

    drawLine(color = TextPrimary, start = Offset(backX, shoulderY), end = Offset(backX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 13f, center = Offset(backX, headY))

    drawLine(color = if (isMistake) CoralAccent else NeonCyan, start = Offset(backX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 14f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 12f, cap = StrokeCap.Round)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 24f, center = Offset(kneeX, kneeY))
    } else {
        // Joint alignment 90 deg marker
        drawLine(color = SafeGreen.copy(alpha = 0.6f), start = Offset(kneeX - 12f, kneeY), end = Offset(kneeX - 12f, kneeY + 12f), strokeWidth = 2f)
        drawLine(color = SafeGreen.copy(alpha = 0.6f), start = Offset(kneeX - 12f, kneeY + 12f), end = Offset(kneeX, kneeY + 12f), strokeWidth = 2f)

        val pulseAlpha = 0.3f + 0.2f * sin(progress * 2 * PI.toFloat())
        drawCircle(color = ElectricMint.copy(alpha = pulseAlpha), radius = 26f, center = Offset((backX + kneeX) / 2f, hipY - 2f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSquatKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.85f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val footX = w * 0.50f
    val footY = floorY - 4f

    val topHipY = floorY - h * 0.50f
    val bottomHipY = floorY - h * 0.26f
    val hipY = topHipY + motionFactor * (bottomHipY - topHipY)
    val hipX = footX - 18f - motionFactor * 28f

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    // Common Mistake: Inward knee valgus collapse and heels lifting
    val kneeValgusShift = if (isMistake) -motionFactor * 20f else motionFactor * 14f
    val heelLift = if (isMistake) motionFactor * 12f else 0f

    val kneeX = footX + 12f + kneeValgusShift
    val kneeY = floorY - h * 0.25f

    val shoulderY = hipY - h * 0.30f
    val shoulderX = hipX + 16f + motionFactor * 12f
    val headX = shoulderX
    val headY = shoulderY - 22f

    drawLine(color = TextSecondary, start = Offset(kneeX, kneeY), end = Offset(footX, footY - heelLift), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = if (isMistake) CoralAccent else NeonCyan, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 14f, cap = StrokeCap.Round)

    drawLine(color = TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 13f, center = Offset(headX, headY))
    drawCircle(color = CautionAmber, radius = 8f, center = Offset(shoulderX, shoulderY - 3f))

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(kneeX, kneeY))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // 90 deg parallel depth guide line
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(footX - 60f, bottomHipY),
            end = Offset(footX + 40f, bottomHipY),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.35f * motionFactor), radius = 26f, center = Offset((hipX + kneeX) / 2f, (hipY + kneeY) / 2f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDeadliftKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.85f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val footX = w * 0.52f
    val footY = floorY - 4f

    val topHipY = floorY - h * 0.50f
    val bottomHipY = floorY - h * 0.35f
    val hipY = topHipY + motionFactor * (bottomHipY - topHipY)
    val hipX = footX - 10f - motionFactor * 42f

    val kneeX = footX + 8f
    val kneeY = floorY - h * 0.24f

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    val shoulderY = hipY - h * 0.32f + motionFactor * (h * 0.22f)
    val shoulderX = hipX + 30f + motionFactor * 15f
    val headX = shoulderX + 15f
    val headY = shoulderY - 14f

    // Bar drifting forward away from body during mistake
    val barDrift = if (isMistake) motionFactor * 25f else 0f
    val barX = footX + barDrift
    val barStartY = floorY - h * 0.38f
    val barEndY = floorY - 16f
    val barY = barStartY + motionFactor * (barEndY - barStartY)

    drawLine(color = TextSecondary, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = NeonCyan, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 14f, cap = StrokeCap.Round)

    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    if (!isMistake) {
        drawLine(color = SafeGreen, start = Offset(shoulderX - 2f, shoulderY), end = Offset(hipX - 2f, hipY), strokeWidth = 3f, cap = StrokeCap.Round)
    }
    drawCircle(color = TextPrimary, radius = 12f, center = Offset(headX, headY))

    drawLine(color = TextSecondary, start = Offset(shoulderX, shoulderY), end = Offset(barX, barY), strokeWidth = 9f, cap = StrokeCap.Round)
    drawCircle(color = if (isMistake) WarningRed else CautionAmber, radius = 10f, center = Offset(barX, barY))

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 24f, center = Offset((shoulderX + hipX) / 2f, (shoulderY + hipY) / 2f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Vertical bar track guide line close to body
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(footX, barStartY - 10f),
            end = Offset(footX, barEndY + 10f),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.35f * motionFactor), radius = 24f, center = Offset(hipX + 10f, hipY + 15f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGluteBridgeKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.80f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val headX = w * 0.25f
    val shoulderX = headX + 25f
    val shoulderY = floorY - 10f

    val footX = w * 0.75f
    val footY = floorY - 4f
    val kneeX = footX - 20f

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    // Common Mistake: Hyperextending lower back past alignment
    val extraHyperextend = if (isMistake) motionFactor * 25f else 0f
    val bottomHipY = floorY - 10f
    val topHipY = floorY - h * 0.35f - extraHyperextend
    val hipY = bottomHipY + motionFactor * (topHipY - bottomHipY)
    val hipX = (shoulderX + footX) / 2f - 10f
    val kneeY = floorY - h * 0.32f

    drawCircle(color = TextPrimary, radius = 12f, center = Offset(headX, shoulderY))
    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawLine(color = NeonCyan, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 14f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 12f, cap = StrokeCap.Round)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(hipX - 5f, hipY + 5f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Straight line guide from shoulders to knees
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(shoulderX, shoulderY),
            end = Offset(kneeX, kneeY),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.4f * motionFactor), radius = 24f, center = Offset(hipX, hipY + 4f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawShoulderPressKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.88f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    // Common Mistake: Leaning backward 25px into lumbar hyperextension
    val torsoLean = if (isMistake) motionFactor * 22f else 0f
    val centerX = w * 0.50f
    val hipY = floorY - h * 0.40f
    val shoulderY = hipY - h * 0.28f
    val shoulderX = centerX - torsoLean
    val headY = shoulderY - 24f
    val headX = shoulderX

    drawLine(color = TextSecondary, start = Offset(centerX - 15f, floorY), end = Offset(centerX, hipY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(centerX + 15f, floorY), end = Offset(centerX, hipY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(centerX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 13f, center = Offset(headX, headY))

    val leftArmX = shoulderX - 32f
    val rightArmX = shoulderX + 32f
    val dbStartY = shoulderY + 5f
    val dbEndY = shoulderY - 60f
    val dbY = dbStartY + motionFactor * (dbEndY - dbStartY)

    drawLine(color = BorderDark, start = Offset(leftArmX - 12f, dbY), end = Offset(leftArmX + 12f, dbY), strokeWidth = 5f, cap = StrokeCap.Round)
    drawLine(color = BorderDark, start = Offset(rightArmX - 12f, dbY), end = Offset(rightArmX + 12f, dbY), strokeWidth = 5f, cap = StrokeCap.Round)

    val elbowY = shoulderY + 22f - motionFactor * 45f
    val leftElbowX = shoulderX - 35f - motionFactor * 10f
    val rightElbowX = shoulderX + 35f + motionFactor * 10f

    val armColor = if (isMistake) CoralAccent else NeonCyan
    drawLine(color = armColor, start = Offset(shoulderX - 12f, shoulderY), end = Offset(leftElbowX, elbowY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(leftElbowX, elbowY), end = Offset(leftArmX, dbY), strokeWidth = 9f, cap = StrokeCap.Round)
    drawLine(color = armColor, start = Offset(shoulderX + 12f, shoulderY), end = Offset(rightElbowX, elbowY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawLine(color = TextPrimary, start = Offset(rightElbowX, elbowY), end = Offset(rightArmX, dbY), strokeWidth = 9f, cap = StrokeCap.Round)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(centerX, hipY - 10f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Vertical spine axis
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(centerX, floorY - 10f),
            end = Offset(centerX, shoulderY - 40f),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.35f * motionFactor), radius = 22f, center = Offset(centerX - 16f, shoulderY + 2f))
        drawCircle(color = ElectricMint.copy(alpha = 0.35f * motionFactor), radius = 22f, center = Offset(centerX + 16f, shoulderY + 2f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBicepCurlKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.88f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    val torsoSwing = if (isMistake) motionFactor * 16f else 0f
    val torsoX = w * 0.42f - torsoSwing
    val hipY = floorY - h * 0.42f
    val shoulderY = hipY - h * 0.28f
    val headY = shoulderY - 24f

    drawLine(color = TextSecondary, start = Offset(torsoX, floorY), end = Offset(torsoX, hipY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(torsoX, shoulderY), end = Offset(torsoX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 13f, center = Offset(torsoX, headY))

    // Common Mistake: Elbow drifts forward 25px rather than staying pinned
    val elbowDrift = if (isMistake) motionFactor * 25f else 0f
    val elbowX = torsoX + 16f + elbowDrift
    val elbowY = shoulderY + 45f - (elbowDrift * 0.5f)
    drawLine(color = TextSecondary, start = Offset(torsoX + 8f, shoulderY), end = Offset(elbowX, elbowY), strokeWidth = 11f, cap = StrokeCap.Round)

    val armAngle = (PI.toFloat() / 2f) - motionFactor * (PI.toFloat() * 0.75f)
    val forearmLength = 48f
    val handX = elbowX + forearmLength * cos(armAngle)
    val handY = elbowY + forearmLength * sin(armAngle)

    drawLine(color = if (isMistake) CoralAccent else NeonCyan, start = Offset(elbowX, elbowY), end = Offset(handX, handY), strokeWidth = 10f, cap = StrokeCap.Round)
    drawCircle(color = if (isMistake) WarningRed else CautionAmber, radius = 8f, center = Offset(handX, handY))

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 20f, center = Offset(torsoX + 16f, shoulderY + 5f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        // Vertical elbow lock guide line
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(torsoX + 16f, shoulderY),
            end = Offset(torsoX + 16f, shoulderY + 65f),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else if (motionFactor > 0.3f) {
        drawCircle(color = ElectricMint.copy(alpha = 0.4f * motionFactor), radius = 20f, center = Offset(elbowX - 2f, elbowY - 20f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDeadBugKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val floorY = h * 0.65f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val headX = w * 0.25f
    val hipX = w * 0.65f
    val shoulderX = headX + 30f
    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    // Common Mistake: Lower back arches up off mat with a visible gap
    val lumbarArch = if (isMistake) motionFactor * 16f else 0f
    val spineY = floorY - 8f - lumbarArch

    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(shoulderX, spineY), end = Offset(hipX, spineY), strokeWidth = 16f, cap = StrokeCap.Round)
    if (!isMistake) {
        drawLine(color = SafeGreen, start = Offset(shoulderX, spineY + 2f), end = Offset(hipX, spineY + 2f), strokeWidth = 3f, cap = StrokeCap.Round)
    }
    drawCircle(color = TextPrimary, radius = 12f, center = Offset(headX, spineY))

    drawLine(color = TextSecondary, start = Offset(shoulderX, spineY), end = Offset(shoulderX, spineY - 50f), strokeWidth = 9f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(hipX, spineY), end = Offset(hipX - 10f, spineY - 45f), strokeWidth = 11f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(hipX - 10f, spineY - 45f), end = Offset(hipX + 30f, spineY - 45f), strokeWidth = 10f, cap = StrokeCap.Round)

    val movingArmEndX = shoulderX - motionFactor * 45f
    val movingArmEndY = spineY - 50f + motionFactor * 35f
    drawLine(color = if (isMistake) CoralAccent else NeonCyan, start = Offset(shoulderX, spineY), end = Offset(movingArmEndX, movingArmEndY), strokeWidth = 9f, cap = StrokeCap.Round)

    val movingLegKneeX = hipX + 15f + motionFactor * 30f
    val movingLegKneeY = spineY - 45f + motionFactor * 35f
    val movingLegFootX = movingLegKneeX + 35f
    val movingLegFootY = movingLegKneeY + 5f
    drawLine(color = ElectricMint, start = Offset(hipX, spineY), end = Offset(movingLegKneeX, movingLegKneeY), strokeWidth = 11f, cap = StrokeCap.Round)
    drawLine(color = ElectricMint, start = Offset(movingLegKneeX, movingLegKneeY), end = Offset(movingLegFootX, movingLegFootY), strokeWidth = 10f, cap = StrokeCap.Round)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset((shoulderX + hipX) / 2f, spineY + 6f))
    } else {
        drawCircle(color = ElectricMint.copy(alpha = 0.35f), radius = 24f, center = Offset((shoulderX + hipX) / 2f, spineY - 4f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPlankKinematics(
    w: Float,
    h: Float,
    progress: Float,
    mode: AnimationMode = AnimationMode.PROPER_FORM
) {
    val floorY = h * 0.78f
    drawLine(color = BorderDark, start = Offset(0f, floorY), end = Offset(w, floorY), strokeWidth = 3f)

    val forearmX = w * 0.30f
    val toeX = w * 0.80f

    drawLine(color = ElectricMint, start = Offset(forearmX - 25f, floorY - 3f), end = Offset(forearmX, floorY - 3f), strokeWidth = 6f, cap = StrokeCap.Round)

    val shoulderY = floorY - 45f
    val shoulderX = forearmX
    val headX = shoulderX - 25f
    val headY = shoulderY - 8f

    val isMistake = mode == AnimationMode.COMMON_MISTAKES
    val hipSag = if (isMistake) 24f else 0f
    val hipX = shoulderX + (toeX - shoulderX) * 0.40f
    val hipY = shoulderY + (floorY - shoulderY) * 0.40f + hipSag

    val kneeX = shoulderX + (toeX - shoulderX) * 0.72f
    val kneeY = shoulderY + (floorY - shoulderY) * 0.72f + (hipSag * 0.4f)

    if (mode == AnimationMode.INJURY_PREVENTION) {
        drawLine(color = SafeGreen.copy(alpha = 0.7f), start = Offset(headX, headY), end = Offset(toeX, floorY), strokeWidth = 2.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f)))
    }

    drawLine(color = TextSecondary, start = Offset(hipX, hipY), end = Offset(toeX, floorY), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 16f, cap = StrokeCap.Round)
    drawLine(color = NeonCyan, start = Offset(shoulderX, shoulderY), end = Offset(forearmX, floorY - 3f), strokeWidth = 11f, cap = StrokeCap.Round)
    drawCircle(color = TextPrimary, radius = 12f, center = Offset(headX, headY))

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(hipX, hipY))
    } else {
        val corePulse = 0.3f + 0.2f * sin(progress * 2 * PI.toFloat())
        drawCircle(color = ElectricMint.copy(alpha = corePulse), radius = 24f, center = Offset((shoulderX + hipX) / 2f, (shoulderY + hipY) / 2f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGeneralMovementKinematics(
    w: Float,
    h: Float,
    motionFactor: Float,
    muscle: String,
    mode: AnimationMode = AnimationMode.PROPER_FORM,
    progress: Float = 0f
) {
    val centerX = w * 0.50f
    val centerY = h * 0.50f
    val isMistake = mode == AnimationMode.COMMON_MISTAKES

    drawCircle(color = TextPrimary, radius = 14f, center = Offset(centerX, centerY - 55f))
    drawLine(color = if (isMistake) CoralAccent else TextPrimary, start = Offset(centerX, centerY - 40f), end = Offset(centerX, centerY + 25f), strokeWidth = 16f, cap = StrokeCap.Round)

    val armOffset = motionFactor * (if (isMistake) 36f else 25f)
    val armColor = if (isMistake) CoralAccent else NeonCyan
    drawLine(color = armColor, start = Offset(centerX, centerY - 35f), end = Offset(centerX - 35f - armOffset, centerY - 10f + armOffset), strokeWidth = 10f, cap = StrokeCap.Round)
    drawLine(color = armColor, start = Offset(centerX, centerY - 35f), end = Offset(centerX + 35f + armOffset, centerY - 10f - armOffset), strokeWidth = 10f, cap = StrokeCap.Round)

    drawLine(color = TextSecondary, start = Offset(centerX, centerY + 25f), end = Offset(centerX - 25f, centerY + 70f), strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color = TextSecondary, start = Offset(centerX, centerY + 25f), end = Offset(centerX + 25f, centerY + 70f), strokeWidth = 12f, cap = StrokeCap.Round)

    if (isMistake) {
        val stressPulse = 0.5f + 0.3f * sin(progress * 4 * PI.toFloat())
        drawCircle(color = WarningRed.copy(alpha = stressPulse), radius = 22f, center = Offset(centerX, centerY - 25f))
    } else if (mode == AnimationMode.INJURY_PREVENTION) {
        drawLine(
            color = SafeGreen.copy(alpha = 0.8f),
            start = Offset(centerX, centerY - 65f),
            end = Offset(centerX, centerY + 75f),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    } else {
        drawCircle(color = ElectricMint.copy(alpha = 0.4f * motionFactor), radius = 25f, center = Offset(centerX, centerY - 15f))
    }
}
