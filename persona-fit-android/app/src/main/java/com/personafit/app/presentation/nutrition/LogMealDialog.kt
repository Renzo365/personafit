package com.personafit.app.presentation.nutrition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.personafit.app.presentation.common.PersonaInputField
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*
import kotlin.math.roundToInt

@Composable
fun LogMealDialog(
    onDismiss: () -> Unit,
    onSaveMeal: (mealType: String, foodName: String, calories: Int, p: Float, c: Float, f: Float) -> Unit
) {
    var mealType by remember { mutableStateOf("breakfast") }
    var entryMode by remember { mutableStateOf(MealEntryMode.DATABASE) }

    // Database selection state
    var selectedCategory by remember { mutableStateOf(FoodCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var activeFoodItem by remember {
        // Default to Whole Egg or White Rice
        mutableStateOf<FoodItem?>(FoodDatabase.foods.firstOrNull { it.id == "egg_whole" } ?: FoodDatabase.foods.firstOrNull())
    }

    // Portion state for activeFoodItem
    var selectedSizeOption by remember(activeFoodItem) {
        mutableStateOf(
            (activeFoodItem?.servingConfig as? ServingConfiguration.DiscreteSizes)?.let { cfg ->
                cfg.sizeOptions.firstOrNull { it.id == cfg.defaultSizeId } ?: cfg.sizeOptions.firstOrNull()
            }
        )
    }

    var weightUnit by remember { mutableStateOf("g") } // "g" or "kg"
    var weightInputText by remember(activeFoodItem) {
        val defGrams = (activeFoodItem?.servingConfig as? ServingConfiguration.WeightInGrams)?.defaultGrams ?: 150
        mutableStateOf(if (weightUnit == "kg") "${defGrams / 1000f}" else "$defGrams")
    }

    val parsedWeight = weightInputText.toFloatOrNull()
    val enteredGrams: Float? = when {
        parsedWeight == null || parsedWeight <= 0f -> null
        weightUnit == "kg" -> parsedWeight * 1000f
        else -> parsedWeight
    }
    val isWeightValid = enteredGrams != null && enteredGrams > 0f

    val displayWeightText = remember(parsedWeight, weightUnit, isWeightValid) {
        if (parsedWeight != null && isWeightValid) {
            val numStr = if (parsedWeight % 1f == 0f) parsedWeight.toInt().toString() else parsedWeight.toString()
            "$numStr$weightUnit"
        } else ""
    }

    var quantity by remember(activeFoodItem) { mutableIntStateOf(1) }

    // Meal Plate (multi-item list for the current meal period)
    val mealPlate = remember { mutableStateListOf<SelectedMealItem>() }

    // Custom manual entry states
    var customFoodName by remember { mutableStateOf("") }
    var customCalories by remember { mutableStateOf("") }
    var customProtein by remember { mutableStateOf("") }
    var customCarbs by remember { mutableStateOf("") }
    var customFat by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    // Active item calculation
    val currentActiveItem: SelectedMealItem? = activeFoodItem?.let { food ->
        when (food.servingConfig) {
            is ServingConfiguration.DiscreteSizes -> {
                SelectedMealItem(
                    foodItem = food,
                    selectedSize = selectedSizeOption,
                    quantity = quantity
                )
            }
            is ServingConfiguration.WeightInGrams -> {
                SelectedMealItem(
                    foodItem = food,
                    grams = enteredGrams ?: 0f,
                    quantity = 1,
                    customDisplayWeight = if (isWeightValid) displayWeightText else null
                )
            }
        }
    }

    // Plate total calculations
    val plateCalories = mealPlate.sumOf { it.totalCalories }
    val plateProtein = roundToOneDecimal(mealPlate.sumOf { it.totalProtein.toDouble() }.toFloat())
    val plateCarbs = roundToOneDecimal(mealPlate.sumOf { it.totalCarbs.toDouble() }.toFloat())
    val plateFat = roundToOneDecimal(mealPlate.sumOf { it.totalFat.toDouble() }.toFloat())

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceCardDark,
            border = BorderStroke(1.dp, BorderDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // ==================== 1. DIALOG HEADER ====================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = ElectricMint,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Log Meal & Nutrition",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Select foods with exact portion macros",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ==================== 2. MEAL PERIOD SELECTOR ====================
                val mealTypes = listOf(
                    "breakfast" to "Breakfast",
                    "lunch" to "Lunch",
                    "dinner" to "Dinner",
                    "snack" to "Snack"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    mealTypes.forEach { (key, label) ->
                        val isSelected = mealType == key
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clickable { mealType = key },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) TealContainer else SurfaceElevatedDark,
                            border = BorderStroke(1.dp, if (isSelected) ElectricMint else BorderDark)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) ElectricMint else TextSecondary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Mode Selector Tabs (Food Catalog vs Custom Entry)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clickable { entryMode = MealEntryMode.DATABASE },
                        shape = RoundedCornerShape(8.dp),
                        color = if (entryMode == MealEntryMode.DATABASE) SurfaceElevatedDark else Color.Transparent,
                        border = BorderStroke(
                            1.dp,
                            if (entryMode == MealEntryMode.DATABASE) ElectricMint.copy(alpha = 0.6f) else BorderDark.copy(alpha = 0.4f)
                        )
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "🥗 Food Catalog",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (entryMode == MealEntryMode.DATABASE) TextPrimary else TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clickable { entryMode = MealEntryMode.CUSTOM },
                        shape = RoundedCornerShape(8.dp),
                        color = if (entryMode == MealEntryMode.CUSTOM) SurfaceElevatedDark else Color.Transparent,
                        border = BorderStroke(
                            1.dp,
                            if (entryMode == MealEntryMode.CUSTOM) ElectricMint.copy(alpha = 0.6f) else BorderDark.copy(alpha = 0.4f)
                        )
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "✍️ Custom Manual Entry",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (entryMode == MealEntryMode.CUSTOM) TextPrimary else TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ==================== 3. CONTENT AREA ====================
                if (entryMode == MealEntryMode.DATABASE) {
                    DatabaseMealContent(
                        mealType = mealType,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it },
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { searchQuery = it },
                        activeFoodItem = activeFoodItem,
                        onSelectFoodItem = { food ->
                            activeFoodItem = food
                            quantity = 1
                            if (food.servingConfig is ServingConfiguration.DiscreteSizes) {
                                val cfg = food.servingConfig
                                selectedSizeOption = cfg.sizeOptions.firstOrNull { it.id == cfg.defaultSizeId } ?: cfg.sizeOptions.firstOrNull()
                            } else if (food.servingConfig is ServingConfiguration.WeightInGrams) {
                                val def = food.servingConfig.defaultGrams
                                weightInputText = if (weightUnit == "kg") "${def / 1000f}" else "$def"
                            }
                        },
                        selectedSizeOption = selectedSizeOption,
                        onSelectSizeOption = { selectedSizeOption = it },
                        weightInputText = weightInputText,
                        onWeightInputTextChanged = { weightInputText = it },
                        weightUnit = weightUnit,
                        onWeightUnitChanged = { weightUnit = it },
                        isWeightValid = isWeightValid,
                        displayWeightText = displayWeightText,
                        quantity = quantity,
                        onQuantityChanged = { quantity = it },
                        currentActiveItem = currentActiveItem,
                        mealPlate = mealPlate,
                        onAddToPlate = { item ->
                            mealPlate.add(item)
                            // Reset quantity
                            quantity = 1
                        },
                        onRemoveFromPlate = { index ->
                            if (index in mealPlate.indices) {
                                mealPlate.removeAt(index)
                            }
                        },
                        plateCalories = plateCalories,
                        plateProtein = plateProtein,
                        plateCarbs = plateCarbs,
                        plateFat = plateFat,
                        onSavePlate = {
                            if (mealPlate.isEmpty()) {
                                currentActiveItem?.let { single ->
                                    val isAllowed = if (activeFoodItem?.servingConfig is ServingConfiguration.WeightInGrams) isWeightValid else true
                                    if (isAllowed) {
                                        val desc = single.displayDescription
                                        onSaveMeal(mealType, desc, single.totalCalories, single.totalProtein, single.totalCarbs, single.totalFat)
                                        onDismiss()
                                    }
                                }
                            } else {
                                val combinedName = mealPlate.joinToString(", ") { it.displayDescription }
                                onSaveMeal(mealType, combinedName, plateCalories, plateProtein, plateCarbs, plateFat)
                                onDismiss()
                            }
                        }
                    )
                } else {
                    // Manual Entry Form
                    CustomMealContent(
                        mealType = mealType,
                        foodName = customFoodName,
                        onFoodNameChange = { customFoodName = it },
                        calories = customCalories,
                        onCaloriesChange = { customCalories = it },
                        protein = customProtein,
                        onProteinChange = { customProtein = it },
                        carbs = customCarbs,
                        onCarbsChange = { customCarbs = it },
                        fat = customFat,
                        onFatChange = { customFat = it },
                        errorText = errorText,
                        onSave = {
                            val cal = customCalories.toIntOrNull()
                            if (customFoodName.isBlank() || cal == null || cal <= 0) {
                                errorText = "Please enter a food name and valid calorie count."
                                return@CustomMealContent
                            }
                            val p = customProtein.toFloatOrNull() ?: 0f
                            val c = customCarbs.toFloatOrNull() ?: 0f
                            val f = customFat.toFloatOrNull() ?: 0f
                            onSaveMeal(mealType, customFoodName.trim(), cal, p, c, f)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

private enum class MealEntryMode {
    DATABASE,
    CUSTOM
}

@Composable
private fun DatabaseMealContent(
    mealType: String,
    selectedCategory: FoodCategory,
    onCategorySelected: (FoodCategory) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    activeFoodItem: FoodItem?,
    onSelectFoodItem: (FoodItem) -> Unit,
    selectedSizeOption: ServingSizeOption?,
    onSelectSizeOption: (ServingSizeOption) -> Unit,
    weightInputText: String,
    onWeightInputTextChanged: (String) -> Unit,
    weightUnit: String,
    onWeightUnitChanged: (String) -> Unit,
    isWeightValid: Boolean,
    displayWeightText: String,
    quantity: Int,
    onQuantityChanged: (Int) -> Unit,
    currentActiveItem: SelectedMealItem?,
    mealPlate: List<SelectedMealItem>,
    onAddToPlate: (SelectedMealItem) -> Unit,
    onRemoveFromPlate: (Int) -> Unit,
    plateCalories: Int,
    plateProtein: Float,
    plateCarbs: Float,
    plateFat: Float,
    onSavePlate: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // ==================== LIVE MEAL MACROS BREAKDOWN BANNER ====================
        val isPlateEmpty = mealPlate.isEmpty()
        val displayCalories = if (!isPlateEmpty) plateCalories else (currentActiveItem?.totalCalories ?: 0)
        val displayProtein = if (!isPlateEmpty) plateProtein else (currentActiveItem?.totalProtein ?: 0f)
        val displayCarbs = if (!isPlateEmpty) plateCarbs else (currentActiveItem?.totalCarbs ?: 0f)
        val displayFat = if (!isPlateEmpty) plateFat else (currentActiveItem?.totalFat ?: 0f)

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = SurfaceElevatedDark,
            border = BorderStroke(1.dp, BorderDark)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${mealType.uppercase()} MACRONUTRIENT TOTAL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = if (isPlateEmpty) "1 item preview" else "${mealPlate.size} items on plate",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MacroPill(
                        label = "Energy",
                        value = "$displayCalories",
                        unit = "kcal",
                        color = CoralAccent,
                        modifier = Modifier.weight(1f)
                    )
                    MacroPill(
                        label = "Protein",
                        value = "$displayProtein",
                        unit = "g",
                        color = ElectricMint,
                        modifier = Modifier.weight(1f)
                    )
                    MacroPill(
                        label = "Carbs",
                        value = "$displayCarbs",
                        unit = "g",
                        color = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    MacroPill(
                        label = "Fats",
                        value = "$displayFat",
                        unit = "g",
                        color = CautionAmber,
                        modifier = Modifier.weight(1f)
                    )
                }

                // If items are in the plate, list them
                if (mealPlate.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Plate Items:",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    mealPlate.forEachIndexed { index, plateItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• ${plateItem.displayDescription} (${plateItem.totalCalories} kcal)",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onRemoveFromPlate(index) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = WarningRed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==================== CATEGORIES SCROLL ROW ====================
        Text(
            text = "SELECT FOOD CATEGORY",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricMint,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        )
        Spacer(modifier = Modifier.height(6.dp))

        val categories = FoodCategory.values().toList()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categories.forEach { cat ->
                val isCatSelected = selectedCategory == cat
                Surface(
                    modifier = Modifier
                        .height(34.dp)
                        .clickable { onCategorySelected(cat) },
                    shape = RoundedCornerShape(17.dp),
                    color = if (isCatSelected) TealContainer else SurfaceElevatedDark,
                    border = BorderStroke(1.dp, if (isCatSelected) ElectricMint else BorderDark)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = cat.icon, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = cat.displayName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isCatSelected) ElectricMint else TextSecondary,
                                fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search egg, rice, chicken, salmon...", color = TextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = ElectricMint,
                unfocusedBorderColor = BorderDark,
                focusedContainerColor = SurfaceElevatedDark,
                unfocusedContainerColor = SurfaceElevatedDark
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ==================== FOOD ITEM SELECTION CHIPS ====================
        val filteredFoods = remember(searchQuery, selectedCategory) {
            FoodDatabase.searchFoods(searchQuery, selectedCategory)
        }

        Text(
            text = "RECOMMENDED FOODS (${filteredFoods.size})",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal chips row for quick food picking
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filteredFoods.forEach { food ->
                val isSelected = activeFoodItem?.id == food.id
                Surface(
                    modifier = Modifier
                        .clickable { onSelectFoodItem(food) },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) TealContainer else SurfaceElevatedDark,
                    border = BorderStroke(1.dp, if (isSelected) ElectricMint else BorderDark)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) ElectricMint else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        food.dietaryHighlight?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) ElectricMint.copy(alpha = 0.8f) else TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==================== ACTIVE FOOD PORTION CONFIGURATOR ====================
        activeFoodItem?.let { food ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceCardDark,
                border = BorderStroke(1.dp, BorderHighlight.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = food.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            )
                            food.dietaryHighlight?.let {
                                Text(
                                    text = "★ $it",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SurfaceElevatedDark
                        ) {
                            Text(
                                text = food.category.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonCyan,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Serving Configuration: Discrete Sizes vs Weight in Grams
                    when (val cfg = food.servingConfig) {
                        is ServingConfiguration.DiscreteSizes -> {
                            Text(
                                text = "SELECT SIZE (PER ${cfg.baseUnitName.uppercase()}):",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Grid of discrete sizes (e.g. Small, Medium, Large, Jumbo)
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                cfg.sizeOptions.forEach { opt ->
                                    val isSizeSelected = selectedSizeOption?.id == opt.id
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onSelectSizeOption(opt) },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSizeSelected) TealContainer else SurfaceElevatedDark,
                                        border = BorderStroke(1.dp, if (isSizeSelected) ElectricMint else BorderDark)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(
                                                    selected = isSizeSelected,
                                                    onClick = { onSelectSizeOption(opt) },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = ElectricMint,
                                                        unselectedColor = TextMuted
                                                    ),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = opt.label,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = if (isSizeSelected) Color.White else TextSecondary,
                                                        fontWeight = if (isSizeSelected) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                )
                                            }

                                            Text(
                                                text = "${opt.calories} kcal • P: ${opt.protein}g • C: ${opt.carbs}g • F: ${opt.fat}g",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = if (isSizeSelected) ElectricMint else TextMuted,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quantity Stepper
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Quantity:",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (quantity > 1) onQuantityChanged(quantity - 1) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceElevatedDark)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = TextPrimary, modifier = Modifier.size(16.dp))
                                    }

                                    Text(
                                        text = "$quantity",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 14.dp)
                                    )

                                    IconButton(
                                        onClick = { onQuantityChanged(quantity + 1) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceElevatedDark)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = TextPrimary, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        is ServingConfiguration.WeightInGrams -> {
                            Text(
                                text = "ENTER FOOD WEIGHT:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Flexible numeric weight input with unit selector (g / kg)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = weightInputText,
                                    onValueChange = { newVal ->
                                        val filtered = newVal.replace(',', '.')
                                        if (filtered.isEmpty() || filtered.matches(Regex("""^\d*\.?\d*$"""))) {
                                            onWeightInputTextChanged(filtered)
                                        }
                                    },
                                    label = { Text("Weight ($weightUnit)", color = TextSecondary, fontSize = 12.sp) },
                                    placeholder = { Text(if (weightUnit == "kg") "e.g. 0.25" else "e.g. 150", color = TextMuted) },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Done
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(58.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        focusedBorderColor = ElectricMint,
                                        unfocusedBorderColor = if (!isWeightValid && weightInputText.isNotBlank()) WarningRed else BorderDark,
                                        focusedContainerColor = SurfaceElevatedDark,
                                        unfocusedContainerColor = SurfaceElevatedDark
                                    )
                                )

                                // Unit Selector Toggle (g vs kg)
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SurfaceElevatedDark,
                                    border = BorderStroke(1.dp, BorderDark),
                                    modifier = Modifier.height(58.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Grams Button
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .clickable {
                                                    if (weightUnit != "g") {
                                                        val currentKg = weightInputText.toFloatOrNull()
                                                        if (currentKg != null && currentKg > 0f) {
                                                            val gVal = currentKg * 1000f
                                                            onWeightInputTextChanged(
                                                                if (gVal % 1f == 0f) gVal.toInt().toString() else gVal.toString()
                                                            )
                                                        }
                                                        onWeightUnitChanged("g")
                                                    }
                                                },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (weightUnit == "g") TealContainer else Color.Transparent,
                                            border = BorderStroke(1.dp, if (weightUnit == "g") ElectricMint else Color.Transparent)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .padding(horizontal = 14.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "g",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        color = if (weightUnit == "g") ElectricMint else TextSecondary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(4.dp))

                                        // Kilograms Button
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .clickable {
                                                    if (weightUnit != "kg") {
                                                        val currentG = weightInputText.toFloatOrNull()
                                                        if (currentG != null && currentG > 0f) {
                                                            val kgVal = currentG / 1000f
                                                            onWeightInputTextChanged(
                                                                if (kgVal % 1f == 0f) kgVal.toInt().toString() else kgVal.toString()
                                                            )
                                                        }
                                                        onWeightUnitChanged("kg")
                                                    }
                                                },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (weightUnit == "kg") TealContainer else Color.Transparent,
                                            border = BorderStroke(1.dp, if (weightUnit == "kg") ElectricMint else Color.Transparent)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .padding(horizontal = 14.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "kg",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        color = if (weightUnit == "kg") ElectricMint else TextSecondary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (!isWeightValid && weightInputText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Please enter a valid positive weight value.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = WarningRed, fontSize = 11.sp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Non-blocking Weighing Scale Tip
                            WeighingScaleTipCard()
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Active Item Live Macro Calculation Card
                    currentActiveItem?.let { item ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceElevatedDark,
                            border = BorderStroke(1.dp, BorderDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.totalCalories} kcal",
                                    style = MaterialTheme.typography.labelMedium.copy(color = CoralAccent, fontWeight = FontWeight.ExtraBold)
                                )
                                Text(text = "•", color = BorderDark)
                                Text(
                                    text = "${item.totalProtein}g Protein",
                                    style = MaterialTheme.typography.labelMedium.copy(color = ElectricMint, fontWeight = FontWeight.Bold)
                                )
                                Text(text = "•", color = BorderDark)
                                Text(
                                    text = "${item.totalCarbs}g Carbs",
                                    style = MaterialTheme.typography.labelMedium.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                                )
                                Text(text = "•", color = BorderDark)
                                Text(
                                    text = "${item.totalFat}g Fat",
                                    style = MaterialTheme.typography.labelMedium.copy(color = CautionAmber, fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dynamic Add to Plate Button with validation
                        val isAddAllowed = if (food.servingConfig is ServingConfiguration.WeightInGrams) isWeightValid else quantity > 0
                        val addButtonText = when {
                            food.servingConfig is ServingConfiguration.WeightInGrams -> {
                                if (isWeightValid) {
                                    "Add to Plate • $displayWeightText • ${item.totalCalories} kcal"
                                } else {
                                    "Add to Plate (Enter valid weight)"
                                }
                            }
                            else -> "Add ${item.displayDescription} to Plate • ${item.totalCalories} kcal"
                        }

                        Button(
                            onClick = { if (isAddAllowed) onAddToPlate(item) },
                            enabled = isAddAllowed,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TealContainer,
                                contentColor = ElectricMint,
                                disabledContainerColor = SurfaceElevatedDark,
                                disabledContentColor = TextMuted
                            ),
                            border = BorderStroke(1.dp, if (isAddAllowed) ElectricMint else BorderDark)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = addButtonText,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==================== SAVE / LOG MEAL PRIMARY BUTTON ====================
        val canSave = if (mealPlate.isEmpty()) {
            if (activeFoodItem?.servingConfig is ServingConfiguration.WeightInGrams) isWeightValid else (quantity > 0)
        } else true

        val actionText = if (mealPlate.isEmpty()) {
            if (activeFoodItem?.servingConfig is ServingConfiguration.WeightInGrams && !isWeightValid) {
                "Enter weight to log meal"
            } else {
                val itemDesc = currentActiveItem?.displayDescription ?: "Meal"
                val itemCal = currentActiveItem?.totalCalories ?: 0
                "Log $itemDesc ($itemCal kcal)"
            }
        } else {
            val count = mealPlate.size
            "Log ${mealType.replaceFirstChar { it.uppercase() }} ($plateCalories kcal • $count items)"
        }

        PrimaryButton(
            text = actionText,
            onClick = onSavePlate,
            enabled = canSave
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun WeighingScaleTipCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = TealContainer.copy(alpha = 0.22f),
        border = BorderStroke(1.dp, ElectricMint.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "⚖️",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 1.dp, end = 8.dp)
            )
            Column {
                Text(
                    text = "Tip for Accurate Food Logging",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricMint,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.3.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "For more accurate calorie and nutrition tracking, use a kitchen weighing scale to measure your food before logging it. Enter the actual weight of the food instead of estimating the portion.\n\nExample: If your cooked rice weighs 150g on the scale, enter 150g in the weight field.",
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

@Composable
private fun CustomMealContent(
    mealType: String,
    foodName: String,
    onFoodNameChange: (String) -> Unit,
    calories: String,
    onCaloriesChange: (String) -> Unit,
    protein: String,
    onProteinChange: (String) -> Unit,
    carbs: String,
    onCarbsChange: (String) -> Unit,
    fat: String,
    onFatChange: (String) -> Unit,
    errorText: String?,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PersonaInputField(
            value = foodName,
            onValueChange = onFoodNameChange,
            label = "Food / Meal Description",
            placeholder = "e.g. Homemade Omelette with sourdough bread",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        PersonaInputField(
            value = calories,
            onValueChange = onCaloriesChange,
            label = "Total Calories (kcal) *",
            placeholder = "e.g. 450",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "MACRONUTRIENT BREAKDOWN (OPTIONAL)",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricMint,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PersonaInputField(
                value = protein,
                onValueChange = onProteinChange,
                label = "Protein (g)",
                placeholder = "e.g. 30",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
            )
            PersonaInputField(
                value = carbs,
                onValueChange = onCarbsChange,
                label = "Carbs (g)",
                placeholder = "e.g. 45",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
            )
            PersonaInputField(
                value = fat,
                onValueChange = onFatChange,
                label = "Fat (g)",
                placeholder = "e.g. 15",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done)
            )
        }

        if (errorText != null) {
            Text(
                text = errorText,
                color = WarningRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Save Custom ${mealType.replaceFirstChar { it.uppercase() }}",
            onClick = onSave
        )
    }
}

@Composable
private fun MacroPill(
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = color,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 9.sp
                    )
                )
            }
        }
    }
}

private fun roundToOneDecimal(value: Float): Float {
    return (value * 10f).roundToInt() / 10f
}
