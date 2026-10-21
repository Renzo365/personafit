package com.personafit.app.presentation.workout

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.R
import com.personafit.app.presentation.theme.*
import kotlin.math.PI
import kotlin.math.cos

/**
 * Biomechanical Push-Up Animation for PersonaFit
 * Customized to feature the Persona Warrior character:
 * - Balaclava mask, athletic shorts, orange wrist wraps, and glowing mint abs.
 */
@Composable
fun PushUpAnimation(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    showSpineGuide: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PushUpTransition")
    
    val cycleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CycleProgress"
    )

    val actualProgress = if (isPlaying) cycleProgress else 0f

    val (descentFactor, phaseText, phaseColor) = remember(actualProgress) {
        when {
            actualProgress <= 0.40f -> {
                val p = actualProgress / 0.40f
                val factor = (1f - cos(p * PI.toFloat())) / 2f
                Triple(factor, "ECCENTRIC (LOWERING)", NeonCyan)
            }
            actualProgress <= 0.50f -> {
                Triple(1.0f, "BOTTOM HOLD (1s)", CautionAmber)
            }
            actualProgress <= 0.85f -> {
                val p = (actualProgress - 0.50f) / 0.35f
                val factor = 1.0f - ((1f - cos(p * PI.toFloat())) / 2f)
                Triple(factor, "CONCENTRIC (PUSH UP)", ElectricMint)
            }
            else -> {
                Triple(0.0f, "TOP PLANK RESET", SafeGreen)
            }
        }
    }

    // Persona Warrior Custom Colors
    val skinColor = Color(0xFFC58C65)
    val balaclavaColor = Color(0xFF1F242A)
    val shortsColor = Color(0xFF2B3540)
    val orangeWrap = Color(0xFFFF7A29)
    val shoeColor = Color(0xFF1B2228)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Character Badge & Phase at Top Left
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(SurfaceElevatedDark.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                    .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Character Mini Avatar thumbnail
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(SurfaceElevatedDark)
                        .border(1.dp, ElectricMint, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persona_avatar),
                        contentDescription = "Persona Warrior",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(phaseColor, CircleShape)
                )
                Text(
                    text = phaseText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 11.sp
                    )
                )
            }

            // Canvas drawing the stylized Persona Warrior doing Push-Ups
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, bottom = 12.dp)
            ) {
                val w = size.width
                val h = size.height

                val floorY = h * 0.78f
                val handX = w * 0.35f
                val toeX = w * 0.82f

                // 1. Gym Floor
                drawLine(
                    color = BorderDark,
                    start = Offset(0f, floorY),
                    end = Offset(w, floorY),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )

                // Hand contact base
                drawCircle(
                    color = ElectricMint.copy(alpha = 0.8f),
                    radius = 8f,
                    center = Offset(handX, floorY - 3f)
                )

                // Kinematics calculations
                val shoulderTopY = floorY - (h * 0.48f)
                val shoulderBottomY = floorY - (h * 0.16f)
                val shoulderY = shoulderTopY + descentFactor * (shoulderBottomY - shoulderTopY)
                val shoulderX = handX + 16f + descentFactor * 10f

                val headX = shoulderX - 36f
                val headY = shoulderY - 14f

                // Elbow positioning
                val midArmX = (shoulderX + handX) / 2f
                val midArmY = (shoulderY + floorY) / 2f
                val bendAmount = descentFactor * 42f + 8f
                val elbowX = midArmX + bendAmount * 0.85f
                val elbowY = midArmY - bendAmount * 0.35f

                val bodyDx = shoulderX - toeX
                val bodyDy = shoulderY - floorY

                val hipX = shoulderX - bodyDx * 0.38f
                val hipY = shoulderY - bodyDy * 0.38f

                val kneeX = shoulderX - bodyDx * 0.70f
                val kneeY = shoulderY - bodyDy * 0.70f

                // 2. Spine Guide Line
                if (showSpineGuide) {
                    drawLine(
                        color = SafeGreen.copy(alpha = 0.7f),
                        start = Offset(headX, headY),
                        end = Offset(toeX, floorY),
                        strokeWidth = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }

                // 3. Far Arm Shadow
                drawLine(
                    color = Color.Black.copy(alpha = 0.4f),
                    start = Offset(shoulderX + 8f, shoulderY),
                    end = Offset(elbowX + 8f, elbowY),
                    strokeWidth = 10f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.Black.copy(alpha = 0.4f),
                    start = Offset(elbowX + 8f, elbowY),
                    end = Offset(handX + 8f, floorY),
                    strokeWidth = 9f,
                    cap = StrokeCap.Round
                )

                // 4. Legs (Muscular skin tone)
                drawLine(
                    color = skinColor,
                    start = Offset(hipX, hipY),
                    end = Offset(kneeX, kneeY),
                    strokeWidth = 14f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = skinColor,
                    start = Offset(kneeX, kneeY),
                    end = Offset(toeX, floorY),
                    strokeWidth = 12f,
                    cap = StrokeCap.Round
                )

                // Athletic Shorts
                val thighMidX = (hipX + kneeX) / 2f
                val thighMidY = (hipY + kneeY) / 2f
                drawLine(
                    color = shortsColor,
                    start = Offset(hipX - 2f, hipY),
                    end = Offset(thighMidX + 4f, thighMidY),
                    strokeWidth = 17f,
                    cap = StrokeCap.Round
                )
                // Mint trim on shorts
                drawLine(
                    color = ElectricMint,
                    start = Offset(hipX, hipY + 5f),
                    end = Offset(thighMidX + 2f, thighMidY + 5f),
                    strokeWidth = 2.5f,
                    cap = StrokeCap.Round
                )

                // Sneakers
                drawLine(
                    color = shoeColor,
                    start = Offset(toeX - 10f, floorY - 3f),
                    end = Offset(toeX + 6f, floorY),
                    strokeWidth = 10f,
                    cap = StrokeCap.Round
                )
                // Sneaker mint sole
                drawLine(
                    color = ElectricMint,
                    start = Offset(toeX - 8f, floorY),
                    end = Offset(toeX + 6f, floorY),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )

                // 5. Torso (Skin tone with glowing Mint Abs)
                drawLine(
                    color = skinColor,
                    start = Offset(shoulderX, shoulderY),
                    end = Offset(hipX, hipY),
                    strokeWidth = 18f,
                    cap = StrokeCap.Round
                )

                // Glowing Cyan/Mint Abs (Identical to character avatar!)
                val absStartX = shoulderX - (shoulderX - hipX) * 0.35f
                val absStartY = shoulderY - (shoulderY - hipY) * 0.35f
                val absEndX = shoulderX - (shoulderX - hipX) * 0.85f
                val absEndY = shoulderY - (shoulderY - hipY) * 0.85f
                drawLine(
                    color = ElectricMint,
                    start = Offset(absStartX, absStartY + 3f),
                    end = Offset(absEndX, absEndY + 3f),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )

                // 6. Near Arm
                drawLine(
                    color = skinColor,
                    start = Offset(shoulderX, shoulderY),
                    end = Offset(elbowX, elbowY),
                    strokeWidth = 13f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = skinColor,
                    start = Offset(elbowX, elbowY),
                    end = Offset(handX, floorY),
                    strokeWidth = 11f,
                    cap = StrokeCap.Round
                )

                // Orange Wrist Wrap (Matching uploaded image)
                val wrapX = handX + (elbowX - handX) * 0.22f
                val wrapY = floorY + (elbowY - floorY) * 0.22f
                drawLine(
                    color = orangeWrap,
                    start = Offset(handX, floorY - 2f),
                    end = Offset(wrapX, wrapY),
                    strokeWidth = 12f,
                    cap = StrokeCap.Round
                )

                // 7. Head & Balaclava Hood (Matching uploaded image)
                drawLine(
                    color = balaclavaColor,
                    start = Offset(shoulderX, shoulderY),
                    end = Offset(headX + 10f, headY + 4f),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
                drawCircle(
                    color = balaclavaColor,
                    radius = 13f,
                    center = Offset(headX, headY)
                )
                // Eye slit with skin exposure
                drawCircle(
                    color = skinColor,
                    radius = 3.5f,
                    center = Offset(headX - 6f, headY)
                )

                // 8. Joint Pivots
                drawCircle(color = ElectricMint, radius = 6f, center = Offset(shoulderX, shoulderY))
                drawCircle(color = NeonCyan, radius = 5f, center = Offset(elbowX, elbowY))
                drawCircle(color = TextPrimary, radius = 5f, center = Offset(hipX, hipY))
            }
        }
    }
}
