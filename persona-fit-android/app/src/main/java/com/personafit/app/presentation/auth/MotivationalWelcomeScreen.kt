package com.personafit.app.presentation.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.data.local.SessionManager
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*
import kotlinx.coroutines.delay

@Composable
fun MotivationalWelcomeScreen(
    sessionManager: SessionManager,
    onContinue: () -> Unit
) {
    val userName by sessionManager.userName.collectAsState(initial = null)
    val persona by sessionManager.personaArchetype.collectAsState(initial = null)

    val quotes = remember {
        listOf(
            "Small daily disciplines create massive lifelong transformations." to "Consistency is Key",
            "Consistency beats intensity every single day." to "Show Up Today",
            "Protect your joints, elevate your strength." to "Smart & Injury-Safe",
            "Every rep today is an investment in your future self." to "Focus & Execute",
            "Your body can stand almost anything; it's your mind that you have to convince." to "Mental Toughness",
            "Discipline is choosing between what you want now and what you want most." to "Stay Committed"
        )
    }

    val selectedQuote = remember { quotes.random() }

    // Pulsing animation for logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    // Animated progress countdown (3 seconds)
    var progress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 2800, easing = LinearEasing),
        label = "auto_progress"
    )

    LaunchedEffect(Unit) {
        progress = 1f
        delay(3000)
        onContinue()
    }

    Scaffold(
        containerColor = DarkSlateBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Tagline
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = ElectricMint,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DAILY MOTIVATION & READINESS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        )
                    )
                }

                // Centerpiece: Glowing Emblem, Greeting & Motivational Quote Card
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Pulsing Glow Barbell Emblem
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(pulseScale),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient Glow Ring
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            ElectricMint.copy(alpha = glowAlpha),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                        // Solid Center Badge
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .background(ElectricMint, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = TextOnAccent,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Personalized Welcome Header
                    val displayName = userName?.ifBlank { "Athlete" } ?: "Athlete"
                    Text(
                        text = "Welcome Back, $displayName!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    )

                    // Persona Archetype Badge if known
                    if (!persona.isNullOrBlank()) {
                        val formattedPersona = persona!!.replace("_", " ").split(" ")
                            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceElevatedDark,
                            border = BorderStroke(1.dp, BorderDark),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Persona: $formattedPersona",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Motivational Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceCardDark,
                        border = BorderStroke(1.dp, BorderDark),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = ElectricMint.copy(alpha = 0.8f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"${selectedQuote.first}\"",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "— ${selectedQuote.second}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = TextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // Bottom Action & Auto-Forward Bar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = ElectricMint,
                        trackColor = SurfaceElevatedDark
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryButton(
                        text = "Let's Go ⚡",
                        onClick = onContinue
                    )
                }
            }
        }
    }
}
