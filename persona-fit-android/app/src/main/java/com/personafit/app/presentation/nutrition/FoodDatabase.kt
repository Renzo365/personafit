package com.personafit.app.presentation.nutrition

import kotlin.math.roundToInt

enum class FoodCategory(val id: String, val displayName: String, val icon: String) {
    ALL("all", "All Foods", "🍽️"),
    MEAT("meat", "Meat", "🥩"),
    RICE("rice", "Rice & Grains", "🍚"),
    FISH("fish", "Fish & Seafood", "🐟"),
    VEGETABLES("vegetables", "Vegetables", "🥦"),
    FRUITS("fruits", "Fruits", "🍎"),
    SNACKS("snacks", "Dietary Snacks", "🍿"),
    EGGS_DAIRY("eggs_dairy", "Eggs & Dairy", "🥚"),
    HEALTHY_FATS("healthy_fats", "Nuts & Fats", "🥜")
}

sealed class ServingConfiguration {
    data class DiscreteSizes(
        val baseUnitName: String,
        val sizeOptions: List<ServingSizeOption>,
        val defaultSizeId: String
    ) : ServingConfiguration()

    data class WeightInGrams(
        val per100gCalories: Float,
        val per100gProtein: Float,
        val per100gCarbs: Float,
        val per100gFat: Float,
        val presetGrams: List<Int> = listOf(50, 100, 150, 200),
        val defaultGrams: Int = 100
    ) : ServingConfiguration()
}

data class ServingSizeOption(
    val id: String,
    val label: String,
    val grams: Int,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float
)

data class FoodItem(
    val id: String,
    val name: String,
    val category: FoodCategory,
    val servingConfig: ServingConfiguration,
    val dietaryHighlight: String? = null
)

data class SelectedMealItem(
    val foodItem: FoodItem,
    val selectedSize: ServingSizeOption? = null,
    val grams: Float = 100f,
    val quantity: Int = 1,
    val customDisplayWeight: String? = null
) {
    constructor(foodItem: FoodItem, selectedSize: ServingSizeOption? = null, grams: Int, quantity: Int = 1) :
        this(foodItem, selectedSize, grams.toFloat(), quantity, null)

    val totalCalories: Int
        get() = when (val cfg = foodItem.servingConfig) {
            is ServingConfiguration.DiscreteSizes -> {
                val size = selectedSize ?: cfg.sizeOptions.first()
                size.calories * quantity
            }
            is ServingConfiguration.WeightInGrams -> {
                ((cfg.per100gCalories * grams / 100f) * quantity).roundToInt()
            }
        }

    val totalProtein: Float
        get() = when (val cfg = foodItem.servingConfig) {
            is ServingConfiguration.DiscreteSizes -> {
                val size = selectedSize ?: cfg.sizeOptions.first()
                roundToOneDecimal(size.protein * quantity)
            }
            is ServingConfiguration.WeightInGrams -> {
                roundToOneDecimal((cfg.per100gProtein * grams / 100f) * quantity)
            }
        }

    val totalCarbs: Float
        get() = when (val cfg = foodItem.servingConfig) {
            is ServingConfiguration.DiscreteSizes -> {
                val size = selectedSize ?: cfg.sizeOptions.first()
                roundToOneDecimal(size.carbs * quantity)
            }
            is ServingConfiguration.WeightInGrams -> {
                roundToOneDecimal((cfg.per100gCarbs * grams / 100f) * quantity)
            }
        }

    val totalFat: Float
        get() = when (val cfg = foodItem.servingConfig) {
            is ServingConfiguration.DiscreteSizes -> {
                val size = selectedSize ?: cfg.sizeOptions.first()
                roundToOneDecimal(size.fat * quantity)
            }
            is ServingConfiguration.WeightInGrams -> {
                roundToOneDecimal((cfg.per100gFat * grams / 100f) * quantity)
            }
        }

    val displayDescription: String
        get() = when (val cfg = foodItem.servingConfig) {
            is ServingConfiguration.DiscreteSizes -> {
                val size = selectedSize ?: cfg.sizeOptions.first()
                "${if (quantity > 1) "${quantity}x " else ""}${size.label}"
            }
            is ServingConfiguration.WeightInGrams -> {
                val weightLabel = customDisplayWeight ?: run {
                    val g = (grams * quantity)
                    if (g % 1f == 0f) "${g.toInt()}g" else "${g}g"
                }
                "$weightLabel ${foodItem.name}"
            }
        }

    private fun roundToOneDecimal(value: Float): Float {
        return (value * 10f).roundToInt() / 10f
    }
}

object FoodDatabase {
    val foods: List<FoodItem> = listOf(
        // ==================== 1. EGGS & DAIRY ====================
        FoodItem(
            id = "egg_whole",
            name = "Whole Egg",
            category = FoodCategory.EGGS_DAIRY,
            dietaryHighlight = "Complete Protein & Choline",
            servingConfig = ServingConfiguration.DiscreteSizes(
                baseUnitName = "egg",
                defaultSizeId = "large",
                sizeOptions = listOf(
                    ServingSizeOption("small", "Small Egg (38g)", 38, 54, 4.8f, 0.3f, 3.6f),
                    ServingSizeOption("medium", "Medium Egg (44g)", 44, 63, 5.5f, 0.4f, 4.2f),
                    ServingSizeOption("large", "Large Egg (50g)", 50, 72, 6.3f, 0.4f, 4.8f),
                    ServingSizeOption("jumbo", "Jumbo Egg (56g)", 56, 80, 7.0f, 0.5f, 5.4f)
                )
            )
        ),
        FoodItem(
            id = "egg_whites",
            name = "Egg Whites",
            category = FoodCategory.EGGS_DAIRY,
            dietaryHighlight = "Pure Fat-Free Protein",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 52f,
                per100gProtein = 11.0f,
                per100gCarbs = 0.7f,
                per100gFat = 0.2f,
                presetGrams = listOf(50, 100, 150, 200),
                defaultGrams = 100
            )
        ),
        FoodItem(
            id = "greek_yogurt",
            name = "Non-Fat Greek Yogurt",
            category = FoodCategory.EGGS_DAIRY,
            dietaryHighlight = "Gut Probiotics & Casein",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 59f,
                per100gProtein = 10.0f,
                per100gCarbs = 3.6f,
                per100gFat = 0.4f,
                presetGrams = listOf(100, 150, 170, 200),
                defaultGrams = 170
            )
        ),
        FoodItem(
            id = "cottage_cheese",
            name = "Low-Fat Cottage Cheese",
            category = FoodCategory.EGGS_DAIRY,
            dietaryHighlight = "Slow-Release Casein",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 81f,
                per100gProtein = 11.0f,
                per100gCarbs = 4.8f,
                per100gFat = 2.3f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),

        // ==================== 2. RICE & GRAINS ====================
        FoodItem(
            id = "white_rice",
            name = "White Rice (Cooked)",
            category = FoodCategory.RICE,
            dietaryHighlight = "Clean High-Carb Energy",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 130f,
                per100gProtein = 2.7f,
                per100gCarbs = 28.2f,
                per100gFat = 0.3f,
                presetGrams = listOf(50, 100, 150, 200, 250),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "brown_rice",
            name = "Brown Rice (Cooked)",
            category = FoodCategory.RICE,
            dietaryHighlight = "Complex Carb & Fiber",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 112f,
                per100gProtein = 2.6f,
                per100gCarbs = 23.5f,
                per100gFat = 0.9f,
                presetGrams = listOf(50, 100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "jasmine_rice",
            name = "Jasmine Rice (Cooked)",
            category = FoodCategory.RICE,
            dietaryHighlight = "Aromatic Fuel for Training",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 129f,
                per100gProtein = 2.5f,
                per100gCarbs = 28.0f,
                per100gFat = 0.4f,
                presetGrams = listOf(50, 100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "rolled_oats",
            name = "Rolled Oats (Dry)",
            category = FoodCategory.RICE,
            dietaryHighlight = "Beta-Glucan Heart Health",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 389f,
                per100gProtein = 16.9f,
                per100gCarbs = 66.3f,
                per100gFat = 6.9f,
                presetGrams = listOf(40, 50, 80, 100),
                defaultGrams = 40
            )
        ),
        FoodItem(
            id = "quinoa_cooked",
            name = "Quinoa (Cooked)",
            category = FoodCategory.RICE,
            dietaryHighlight = "Complete Amino Acid Grain",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 120f,
                per100gProtein = 4.4f,
                per100gCarbs = 21.3f,
                per100gFat = 1.9f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "sweet_potato",
            name = "Sweet Potato (Baked)",
            category = FoodCategory.RICE,
            dietaryHighlight = "Vitamin A & Potassium",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 90f,
                per100gProtein = 2.0f,
                per100gCarbs = 20.7f,
                per100gFat = 0.2f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "whole_wheat_bread",
            name = "Whole Wheat Bread",
            category = FoodCategory.RICE,
            dietaryHighlight = "Whole Grain Dietary Fiber",
            servingConfig = ServingConfiguration.DiscreteSizes(
                baseUnitName = "slice",
                defaultSizeId = "1_slice",
                sizeOptions = listOf(
                    ServingSizeOption("1_slice", "1 Slice (35g)", 35, 86, 4.2f, 15.0f, 1.2f),
                    ServingSizeOption("2_slices", "2 Slices (70g)", 70, 172, 8.4f, 30.0f, 2.4f)
                )
            )
        ),

        // ==================== 3. MEAT ====================
        FoodItem(
            id = "chicken_breast",
            name = "Chicken Breast (Cooked)",
            category = FoodCategory.MEAT,
            dietaryHighlight = "High Protein, Low Fat",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 165f,
                per100gProtein = 31.0f,
                per100gCarbs = 0.0f,
                per100gFat = 3.6f,
                presetGrams = listOf(100, 150, 200, 250),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "lean_beef",
            name = "Lean Ground Beef (90/10)",
            category = FoodCategory.MEAT,
            dietaryHighlight = "Iron & Zinc Rich",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 215f,
                per100gProtein = 26.0f,
                per100gCarbs = 0.0f,
                per100gFat = 12.0f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "sirloin_steak",
            name = "Beef Sirloin Steak",
            category = FoodCategory.MEAT,
            dietaryHighlight = "Natural Creatine & Protein",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 205f,
                per100gProtein = 27.0f,
                per100gCarbs = 0.0f,
                per100gFat = 10.0f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "turkey_breast",
            name = "Turkey Breast (Lean)",
            category = FoodCategory.MEAT,
            dietaryHighlight = "Ultra-Lean Poultry",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 145f,
                per100gProtein = 30.0f,
                per100gCarbs = 0.0f,
                per100gFat = 2.0f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "pork_tenderloin",
            name = "Pork Tenderloin",
            category = FoodCategory.MEAT,
            dietaryHighlight = "Thiamine & Lean Protein",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 143f,
                per100gProtein = 26.0f,
                per100gCarbs = 0.0f,
                per100gFat = 3.5f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),

        // ==================== 4. FISH & SEAFOOD ====================
        FoodItem(
            id = "salmon_fillet",
            name = "Atlantic Salmon Fillet",
            category = FoodCategory.FISH,
            dietaryHighlight = "Rich in Omega-3 EPA/DHA",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 208f,
                per100gProtein = 22.0f,
                per100gCarbs = 0.0f,
                per100gFat = 13.0f,
                presetGrams = listOf(100, 150, 180, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "canned_tuna",
            name = "Canned Tuna (in Water)",
            category = FoodCategory.FISH,
            dietaryHighlight = "High Protein Convenience",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 116f,
                per100gProtein = 26.0f,
                per100gCarbs = 0.0f,
                per100gFat = 1.0f,
                presetGrams = listOf(80, 120, 150),
                defaultGrams = 120
            )
        ),
        FoodItem(
            id = "tilapia_fillet",
            name = "Tilapia Fillet",
            category = FoodCategory.FISH,
            dietaryHighlight = "Low-Calorie Lean Fish",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 128f,
                per100gProtein = 26.0f,
                per100gCarbs = 0.0f,
                per100gFat = 2.7f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "white_cod",
            name = "White Cod Fillet",
            category = FoodCategory.FISH,
            dietaryHighlight = "Ultra-Lean White Fish",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 105f,
                per100gProtein = 23.0f,
                per100gCarbs = 0.0f,
                per100gFat = 0.9f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 150
            )
        ),
        FoodItem(
            id = "shrimp_steamed",
            name = "Shrimp / Prawns (Steamed)",
            category = FoodCategory.FISH,
            dietaryHighlight = "High Protein, Zero Fat",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 99f,
                per100gProtein = 24.0f,
                per100gCarbs = 0.2f,
                per100gFat = 0.3f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 100
            )
        ),

        // ==================== 5. VEGETABLES ====================
        FoodItem(
            id = "broccoli",
            name = "Broccoli (Steamed)",
            category = FoodCategory.VEGETABLES,
            dietaryHighlight = "Sulforaphane & Vitamin C",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 35f,
                per100gProtein = 2.4f,
                per100gCarbs = 7.0f,
                per100gFat = 0.4f,
                presetGrams = listOf(50, 100, 150, 200),
                defaultGrams = 100
            )
        ),
        FoodItem(
            id = "spinach_fresh",
            name = "Fresh Baby Spinach",
            category = FoodCategory.VEGETABLES,
            dietaryHighlight = "Iron & Dietary Nitrates",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 23f,
                per100gProtein = 2.9f,
                per100gCarbs = 3.6f,
                per100gFat = 0.4f,
                presetGrams = listOf(50, 100, 150),
                defaultGrams = 50
            )
        ),
        FoodItem(
            id = "asparagus",
            name = "Asparagus Spears",
            category = FoodCategory.VEGETABLES,
            dietaryHighlight = "Natural Diuretic & Prebiotic",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 20f,
                per100gProtein = 2.2f,
                per100gCarbs = 3.9f,
                per100gFat = 0.1f,
                presetGrams = listOf(100, 150),
                defaultGrams = 100
            )
        ),
        FoodItem(
            id = "carrots",
            name = "Sliced Carrots",
            category = FoodCategory.VEGETABLES,
            dietaryHighlight = "Beta-Carotene & Fiber",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 41f,
                per100gProtein = 0.9f,
                per100gCarbs = 9.6f,
                per100gFat = 0.2f,
                presetGrams = listOf(50, 100, 150),
                defaultGrams = 100
            )
        ),
        FoodItem(
            id = "bell_peppers",
            name = "Bell Peppers (Sliced)",
            category = FoodCategory.VEGETABLES,
            dietaryHighlight = "High Vitamin C Antioxidant",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 31f,
                per100gProtein = 1.0f,
                per100gCarbs = 6.0f,
                per100gFat = 0.3f,
                presetGrams = listOf(50, 100, 150),
                defaultGrams = 100
            )
        ),

        // ==================== 6. FRUITS ====================
        FoodItem(
            id = "banana",
            name = "Fresh Banana",
            category = FoodCategory.FRUITS,
            dietaryHighlight = "Potassium & Quick Glycogen",
            servingConfig = ServingConfiguration.DiscreteSizes(
                baseUnitName = "banana",
                defaultSizeId = "medium",
                sizeOptions = listOf(
                    ServingSizeOption("small", "Small (100g)", 100, 89, 1.1f, 22.8f, 0.3f),
                    ServingSizeOption("medium", "Medium (118g)", 118, 105, 1.3f, 27.0f, 0.3f),
                    ServingSizeOption("large", "Large (136g)", 136, 121, 1.5f, 31.1f, 0.4f)
                )
            )
        ),
        FoodItem(
            id = "apple",
            name = "Red Apple",
            category = FoodCategory.FRUITS,
            dietaryHighlight = "Pectin Prebiotic Fiber",
            servingConfig = ServingConfiguration.DiscreteSizes(
                baseUnitName = "apple",
                defaultSizeId = "medium",
                sizeOptions = listOf(
                    ServingSizeOption("small", "Small (150g)", 150, 77, 0.4f, 20.5f, 0.3f),
                    ServingSizeOption("medium", "Medium (182g)", 182, 95, 0.5f, 25.0f, 0.3f)
                )
            )
        ),
        FoodItem(
            id = "strawberries",
            name = "Fresh Strawberries",
            category = FoodCategory.FRUITS,
            dietaryHighlight = "Low Glycemic Polyphenols",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 32f,
                per100gProtein = 0.7f,
                per100gCarbs = 7.7f,
                per100gFat = 0.3f,
                presetGrams = listOf(100, 150, 200),
                defaultGrams = 100
            )
        ),
        FoodItem(
            id = "blueberries",
            name = "Fresh Blueberries",
            category = FoodCategory.FRUITS,
            dietaryHighlight = "Brain Health Anthocyanins",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 57f,
                per100gProtein = 0.7f,
                per100gCarbs = 14.5f,
                per100gFat = 0.3f,
                presetGrams = listOf(80, 120, 150),
                defaultGrams = 100
            )
        ),
        FoodItem(
            id = "avocado",
            name = "Hass Avocado",
            category = FoodCategory.FRUITS,
            dietaryHighlight = "Monounsaturated Healthy Fats",
            servingConfig = ServingConfiguration.DiscreteSizes(
                baseUnitName = "avocado",
                defaultSizeId = "half",
                sizeOptions = listOf(
                    ServingSizeOption("half", "Half Avocado (100g)", 100, 160, 2.0f, 8.5f, 14.7f),
                    ServingSizeOption("whole", "Whole Avocado (200g)", 200, 320, 4.0f, 17.0f, 29.4f)
                )
            )
        ),

        // ==================== 7. HEALTHY FATS & NUTS ====================
        FoodItem(
            id = "almonds_raw",
            name = "Raw Almonds",
            category = FoodCategory.HEALTHY_FATS,
            dietaryHighlight = "Vitamin E & Magnesium",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 579f,
                per100gProtein = 21.2f,
                per100gCarbs = 21.6f,
                per100gFat = 49.9f,
                presetGrams = listOf(20, 28, 40),
                defaultGrams = 28
            )
        ),
        FoodItem(
            id = "peanut_butter",
            name = "Natural Peanut Butter",
            category = FoodCategory.HEALTHY_FATS,
            dietaryHighlight = "Nutrient Dense Healthy Fats",
            servingConfig = ServingConfiguration.DiscreteSizes(
                baseUnitName = "tbsp",
                defaultSizeId = "1_tbsp",
                sizeOptions = listOf(
                    ServingSizeOption("1_tbsp", "1 tbsp (16g)", 16, 94, 4.0f, 3.2f, 8.0f),
                    ServingSizeOption("2_tbsp", "2 tbsp (32g)", 32, 188, 8.0f, 6.4f, 16.0f)
                )
            )
        ),
        FoodItem(
            id = "olive_oil",
            name = "Extra Virgin Olive Oil",
            category = FoodCategory.HEALTHY_FATS,
            dietaryHighlight = "Heart-Healthy Oleic Acid",
            servingConfig = ServingConfiguration.DiscreteSizes(
                baseUnitName = "tbsp",
                defaultSizeId = "1_tbsp",
                sizeOptions = listOf(
                    ServingSizeOption("1_tsp", "1 tsp (5ml)", 5, 44, 0.0f, 0.0f, 5.0f),
                    ServingSizeOption("1_tbsp", "1 tbsp (14ml)", 14, 124, 0.0f, 0.0f, 14.0f)
                )
            )
        ),

        // ==================== 8. DIETARY SNACKS ====================
        FoodItem(
            id = "protein_bar_whey",
            name = "Protein Bar (Whey & Oats)",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "20g Protein Muscle Fuel",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 360f,
                per100gProtein = 33.3f,
                per100gCarbs = 28.0f,
                per100gFat = 11.5f,
                defaultGrams = 60
            )
        ),
        FoodItem(
            id = "rice_cakes_crispy",
            name = "Crispy Rice Cakes",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Low-Calorie Light Crunch",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 387f,
                per100gProtein = 8.0f,
                per100gCarbs = 82.0f,
                per100gFat = 2.8f,
                defaultGrams = 28
            )
        ),
        FoodItem(
            id = "roasted_edamame",
            name = "Roasted Edamame",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "High Plant Protein & Fiber",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 436f,
                per100gProtein = 39.0f,
                per100gCarbs = 31.0f,
                per100gFat = 17.0f,
                defaultGrams = 30
            )
        ),
        FoodItem(
            id = "roasted_chickpeas",
            name = "Roasted Crunchy Chickpeas",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Fiber & Sustained Energy",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 380f,
                per100gProtein = 19.0f,
                per100gCarbs = 61.0f,
                per100gFat = 6.0f,
                defaultGrams = 40
            )
        ),
        FoodItem(
            id = "dark_chocolate_85",
            name = "Dark Chocolate (85% Cacao)",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Rich Antioxidants & Flavonoids",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 580f,
                per100gProtein = 8.5f,
                per100gCarbs = 36.0f,
                per100gFat = 46.0f,
                defaultGrams = 25
            )
        ),
        FoodItem(
            id = "air_popped_popcorn",
            name = "Air-Popped Popcorn (Plain)",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Whole Grain High Volume Snack",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 387f,
                per100gProtein = 13.0f,
                per100gCarbs = 78.0f,
                per100gFat = 4.5f,
                defaultGrams = 30
            )
        ),
        FoodItem(
            id = "lean_beef_jerky",
            name = "Lean Beef Jerky",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Low-Fat High Protein Jerky",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 310f,
                per100gProtein = 33.0f,
                per100gCarbs = 11.0f,
                per100gFat = 7.0f,
                defaultGrams = 35
            )
        ),
        FoodItem(
            id = "mixed_trail_mix",
            name = "Energy Trail Mix (Nuts & Berries)",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Heart-Healthy Omega-3s",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 462f,
                per100gProtein = 14.0f,
                per100gCarbs = 45.0f,
                per100gFat = 26.0f,
                defaultGrams = 35
            )
        ),
        FoodItem(
            id = "chia_seed_pudding",
            name = "Chia Seed Pudding",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Soluble Fiber & Omega-3",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 135f,
                per100gProtein = 4.5f,
                per100gCarbs = 12.0f,
                per100gFat = 7.5f,
                defaultGrams = 120
            )
        ),
        FoodItem(
            id = "greek_yogurt_berries",
            name = "Greek Yogurt with Blueberries",
            category = FoodCategory.SNACKS,
            dietaryHighlight = "Probiotics & Antioxidants",
            servingConfig = ServingConfiguration.WeightInGrams(
                per100gCalories = 75f,
                per100gProtein = 8.5f,
                per100gCarbs = 8.0f,
                per100gFat = 0.5f,
                defaultGrams = 150
            )
        )
    )

    fun getFoodsByCategory(category: FoodCategory): List<FoodItem> {
        return if (category == FoodCategory.ALL) foods else foods.filter { it.category == category }
    }

    fun searchFoods(query: String, category: FoodCategory = FoodCategory.ALL): List<FoodItem> {
        val base = getFoodsByCategory(category)
        if (query.isBlank()) return base
        return base.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.category.displayName.contains(query, ignoreCase = true) ||
            (it.dietaryHighlight?.contains(query, ignoreCase = true) == true)
        }
    }
}
