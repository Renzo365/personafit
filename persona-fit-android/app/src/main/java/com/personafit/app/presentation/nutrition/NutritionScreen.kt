package com.personafit.app.presentation.nutrition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.data.remote.dto.MealLogDto
import com.personafit.app.data.remote.dto.NutritionSummaryData
import kotlin.math.roundToInt
import com.personafit.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    viewModel: NutritionViewModel,
    onNavigateBack: () -> Unit
) {
    val nutritionSummary by viewModel.nutritionSummary.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var showLogMealDialog by remember { mutableStateOf(false) }

    if (showLogMealDialog) {
        LogMealDialog(
            onDismiss = { showLogMealDialog = false },
            onSaveMeal = { type, name, cal, p, c, f ->
                viewModel.logMeal(type, name, cal, p, c, f)
                showLogMealDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nutrition & Hydration",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadTodayNutrition() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = ElectricMint
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSlateBg
                )
            )
        },
        containerColor = DarkSlateBg,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showLogMealDialog = true },
                containerColor = ElectricMint,
                contentColor = TextOnAccent,
                icon = { Icon(Icons.Default.Add, contentDescription = null, tint = TextOnAccent) },
                text = { Text("Log Meal", fontWeight = FontWeight.ExtraBold) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState is NutritionUiState.Loading && nutritionSummary == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ElectricMint)
                }
            } else {
                nutritionSummary?.let { nut ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp)
                    ) {
                        // 1. Calorie Donut Ring Card
                        item {
                            CalorieDonutCard(nut = nut)
                        }

                        // 2. Macronutrient Breakdown
                        item {
                            MacroBreakdownCard(nut = nut)
                        }

                        // 3. Hydration Target
                        item {
                            HydrationCard(
                                nut = nut,
                                onQuickAdd = { amount -> viewModel.logWater(amount) }
                            )
                        }

                        // 4. Meal Log History
                        item {
                            Text(
                                text = "LOGGED MEALS TODAY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricMint,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                ),
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        if (nut.meals.isEmpty()) {
                            item {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = SurfaceCardDark,
                                    border = BorderStroke(1.dp, BorderDark)
                                ) {
                                    Text(
                                        text = "No meals logged today yet. Tap '+ Log Meal' below!",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        } else {
                            items(nut.meals) { meal ->
                                MealLogItemCard(meal = meal)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalorieDonutCard(nut: NutritionSummaryData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "DAILY ENERGY BUDGET",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${nut.calories.consumed}",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "of ${nut.calories.target} kcal target",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SurfaceElevatedDark
                ) {
                    Text(
                        text = "${nut.calories.remaining} kcal left",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CoralAccent,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Donut Ring Canvas
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    // Track background
                    drawArc(
                        color = BorderDark,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // Progress arc
                    val progressAngle = (nut.calories.percentage * 360f).coerceIn(0f, 360f)
                    drawArc(
                        color = CoralAccent,
                        startAngle = -90f,
                        sweepAngle = progressAngle,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "${(nut.calories.percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )
            }
        }
    }
}

@Composable
fun MacroBreakdownCard(nut: NutritionSummaryData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "MACRONUTRIENT DISTRIBUTION",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = ElectricMint,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Protein Bar
            MacroLinearBar(
                name = "Protein",
                consumed = nut.macros.protein.consumedGrams,
                target = nut.macros.protein.targetGrams,
                color = ElectricMint
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Carbs Bar
            MacroLinearBar(
                name = "Carbs",
                consumed = nut.macros.carbs.consumedGrams,
                target = nut.macros.carbs.targetGrams,
                color = NeonCyan
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fats Bar
            MacroLinearBar(
                name = "Fats",
                consumed = nut.macros.fat.consumedGrams,
                target = nut.macros.fat.targetGrams,
                color = CoralAccent
            )
        }
    }
}

@Composable
fun MacroLinearBar(
    name: String,
    consumed: Float,
    target: Int,
    color: Color
) {
    val progress = if (target > 0) (consumed / target).coerceIn(0f, 1f) else 0f
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )
            Text(
                text = "${consumed.toInt()}g / ${target}g",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(SurfaceElevatedDark)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
fun HydrationCard(
    nut: NutritionSummaryData,
    onQuickAdd: (Int) -> Unit
) {
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
            // Header
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

            // Subtitle hint
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
                                        onQuickAdd(glassSizeMl)
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

            // Hydration progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevatedDark)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(waterProgress)
                        .clip(CircleShape)
                        .background(if (glassesDrank >= 8) ElectricMint else NeonCyan)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Goal status encouragement
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

@Composable
fun MealLogItemCard(meal: MealLogDto) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = meal.mealType.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = meal.foodName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "P: ${meal.proteinGrams.toInt()}g • C: ${meal.carbsGrams.toInt()}g • F: ${meal.fatGrams.toInt()}g",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }

            Text(
                text = "${meal.calories} kcal",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = CoralAccent
                )
            )
        }
    }
}
