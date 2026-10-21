package com.personafit.app.presentation.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToWorkout: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit
) {
    val userName by viewModel.userName.collectAsState(initial = "Athlete")
    val persona by viewModel.personaArchetype.collectAsState(initial = "Busy Pro")
    val todayWorkout by viewModel.todayWorkout.collectAsState()
    val nutrition by viewModel.nutritionSummary.collectAsState()
    val liveSteps by viewModel.liveSensorSteps.collectAsState()
    val todayStepLog by viewModel.todayStepLog.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hello, ${userName ?: "Athlete"} 👋",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ElectricMint.copy(alpha = 0.15f),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = (persona ?: "Active Archetype").replace('_', ' ').uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricMint,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadDashboardData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = ElectricMint
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = TextMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSlateBg
                )
            )
        },
        containerColor = DarkSlateBg,
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceCardDark,
                contentColor = TextPrimary
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextOnAccent,
                        indicatorColor = ElectricMint,
                        selectedTextColor = ElectricMint,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onNavigateToWorkout()
                    },
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workout") },
                    label = { Text("Workout", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextOnAccent,
                        indicatorColor = ElectricMint,
                        selectedTextColor = ElectricMint,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onNavigateToNutrition()
                    },
                    icon = { Icon(Icons.Default.Restaurant, contentDescription = "Nutrition") },
                    label = { Text("Nutrition", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextOnAccent,
                        indicatorColor = ElectricMint,
                        selectedTextColor = ElectricMint,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        onNavigateToProgress()
                    },
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Progress") },
                    label = { Text("Progress", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextOnAccent,
                        indicatorColor = ElectricMint,
                        selectedTextColor = ElectricMint,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            // 1. Today's Prescribed Routine Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToWorkout() },
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCardDark,
                    border = BorderStroke(1.5.dp, BorderHighlight)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TODAY'S WORKOUT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricMint,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            )
                            todayWorkout?.let { p ->
                                Text(
                                    text = "~${p.estimatedDurationMinutes} mins",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = todayWorkout?.planName ?: "Personalized Routine",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )

                        Text(
                            text = "Focus: ${todayWorkout?.focusArea ?: "Full Body"} • ${todayWorkout?.totalExercises ?: 5} Exercises",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        if ((todayWorkout?.substitutionsAppliedCount ?: 0) > 0) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SafeGreen.copy(alpha = 0.15f),
                                modifier = Modifier.padding(bottom = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = SafeGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${todayWorkout?.substitutionsAppliedCount} Safe Joint Substitutions Active",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = SafeGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }

                        PrimaryButton(
                            text = "Start Workout Session ⚡",
                            onClick = onNavigateToWorkout
                        )
                    }
                }
            }

            // 2. Nutrition Summary Mini Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToNutrition() },
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
                            Text(
                                text = "NUTRITION TARGET",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricMint,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "View",
                                tint = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        nutrition?.let { nut ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${nut.calories.consumed} / ${nut.calories.target} kcal",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "${nut.calories.remaining} kcal remaining today",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                                    )
                                }

                                CircularProgressIndicator(
                                    progress = { (nut.calories.percentage / 100f).coerceIn(0f, 1f) },
                                    modifier = Modifier.size(56.dp),
                                    color = CoralAccent,
                                    trackColor = SurfaceElevatedDark,
                                    strokeWidth = 6.dp
                                )
                            }
                        } ?: run {
                            Text(text = "Loading nutrition prescription...", color = TextSecondary)
                        }
                    }
                }
            }

            // 3. Hydration Card (8 Clickable Glasses)
            item {
                nutrition?.let { nut ->
                    val targetMl = if (nut.water.targetMl > 0) nut.water.targetMl else 2000
                    val glassSizeMl = (targetMl / 8).coerceAtLeast(200)
                    val glassesDrank = if (glassSizeMl > 0) (nut.water.consumedMl / glassSizeMl).coerceIn(0, 8) else 0
                    val waterProgress = if (targetMl > 0) (nut.water.consumedMl.toFloat() / targetMl.toFloat()).coerceIn(0f, 1f) else 0f
                    val percent = (waterProgress * 100f).roundToInt()

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceCardDark,
                        border = BorderStroke(1.dp, BorderDark)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
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
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "HYDRATION TRACKER",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = ElectricMint,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$glassesDrank / 8 Glasses",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "${nut.water.consumedMl} / $targetMl ml ($percent%)",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (glassesDrank >= 8) ElectricMint else TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Tap each glass as you drink (~${glassSizeMl}ml per glass):",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // 8 Interactive Clickable Glasses in 2 rows of 4
                            for (rowIndex in 0 until 2) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    for (colIndex in 0 until 4) {
                                        val glassIndex = rowIndex * 4 + colIndex
                                        val isDrank = glassIndex < glassesDrank
                                        val isNextToDrink = glassIndex == glassesDrank

                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(68.dp)
                                                .clickable {
                                                    if (!isDrank) {
                                                        viewModel.quickAddWater(glassSizeMl)
                                                    }
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            color = when {
                                                isDrank -> TealContainer.copy(alpha = 0.35f)
                                                isNextToDrink -> SurfaceElevatedDark
                                                else -> SurfaceElevatedDark.copy(alpha = 0.6f)
                                            },
                                            border = BorderStroke(
                                                width = if (isDrank || isNextToDrink) 1.5.dp else 1.dp,
                                                color = when {
                                                    isDrank -> ElectricMint
                                                    isNextToDrink -> NeonCyan
                                                    else -> BorderDark
                                                }
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(vertical = 6.dp, horizontal = 2.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = if (isDrank) Icons.Default.CheckCircle else Icons.Default.WaterDrop,
                                                    contentDescription = "Glass ${glassIndex + 1}",
                                                    tint = when {
                                                        isDrank -> ElectricMint
                                                        isNextToDrink -> NeonCyan
                                                        else -> TextMuted.copy(alpha = 0.5f)
                                                    },
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = "Glass ${glassIndex + 1}",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = if (isDrank) ElectricMint else if (isNextToDrink) TextPrimary else TextSecondary,
                                                        fontWeight = if (isDrank || isNextToDrink) FontWeight.Bold else FontWeight.Normal,
                                                        fontSize = 10.sp
                                                    )
                                                )
                                                Text(
                                                    text = if (isDrank) "Drank ✓" else if (isNextToDrink) "+${glassSizeMl}ml" else "${glassSizeMl}ml",
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = if (isDrank) ElectricMint.copy(alpha = 0.8f) else if (isNextToDrink) NeonCyan else TextMuted,
                                                        fontSize = 9.sp,
                                                        fontWeight = if (isNextToDrink) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                                if (rowIndex == 0) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            LinearProgressIndicator(
                                progress = { waterProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = if (glassesDrank >= 8) ElectricMint else NeonCyan,
                                trackColor = SurfaceElevatedDark
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (glassesDrank >= 8) {
                                    "🎉 Daily hydration goal achieved! Great job keeping your body fueled."
                                } else {
                                    "${8 - glassesDrank} glass${if (8 - glassesDrank > 1) "es" else ""} remaining today. Stay hydrated!"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (glassesDrank >= 8) ElectricMint else TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            // 4. Daily Hardware Pedometer Card
            item {
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
                                    imageVector = Icons.Default.DirectionsWalk,
                                    contentDescription = null,
                                    tint = ElectricMint,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "HARDWARE PEDOMETER",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                            Text(
                                text = "Sensor Active",
                                style = MaterialTheme.typography.labelSmall.copy(color = SafeGreen, fontWeight = FontWeight.SemiBold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val displaySteps = maxOf(liveSteps, todayStepLog?.stepCount ?: 0)
                        val displayDist = todayStepLog?.distanceKm ?: (displaySteps * 0.00075f)
                        val displayKcal = todayStepLog?.caloriesBurned ?: (displaySteps * 0.04f)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "$displaySteps",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Goal: 10,000 steps",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = String.format("%.2f km", displayDist),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = String.format("%.1f kcal", displayKcal),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = CoralAccent, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
