<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\MealLog;
use App\Models\WaterLog;
use Carbon\Carbon;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class NutritionController extends Controller
{
    /**
     * Get today's nutrition and hydration summary (Targets vs Consumed).
     */
    public function getTodaySummary(Request $request): JsonResponse
    {
        $user = $request->user()->load('profile');
        $today = Carbon::today()->toDateString();

        $profile = $user->profile;
        $targetCalories = $profile ? $profile->daily_calories : 2000;
        $targetProtein = $profile ? $profile->protein_grams : 150;
        $targetCarbs = $profile ? $profile->carbs_grams : 200;
        $targetFat = $profile ? $profile->fat_grams : 60;
        $targetWater = $profile ? $profile->water_ml_target : 2500;

        // Fetch today's meals
        $meals = MealLog::where('user_id', $user->id)
            ->whereDate('log_date', $today)
            ->orderBy('created_at')
            ->get();

        $consumedCalories = $meals->sum('calories');
        $consumedProtein = $meals->sum('protein_grams');
        $consumedCarbs = $meals->sum('carbs_grams');
        $consumedFat = $meals->sum('fat_grams');

        // Fetch today's water logs
        $waterLogs = WaterLog::where('user_id', $user->id)
            ->whereDate('log_date', $today)
            ->get();

        $consumedWater = $waterLogs->sum('amount_ml');

        return response()->json([
            'status' => 'success',
            'data' => [
                'date' => $today,
                'calories' => [
                    'target' => $targetCalories,
                    'consumed' => $consumedCalories,
                    'remaining' => max(0, $targetCalories - $consumedCalories),
                    'percentage' => $targetCalories > 0 ? min(100, round(($consumedCalories / $targetCalories) * 100, 1)) : 0,
                ],
                'macros' => [
                    'protein' => [
                        'target_g' => $targetProtein,
                        'consumed_g' => round($consumedProtein, 1),
                        'remaining_g' => max(0, round($targetProtein - $consumedProtein, 1)),
                    ],
                    'carbs' => [
                        'target_g' => $targetCarbs,
                        'consumed_g' => round($consumedCarbs, 1),
                        'remaining_g' => max(0, round($targetCarbs - $consumedCarbs, 1)),
                    ],
                    'fat' => [
                        'target_g' => $targetFat,
                        'consumed_g' => round($consumedFat, 1),
                        'remaining_g' => max(0, round($targetFat - $consumedFat, 1)),
                    ],
                ],
                'water' => [
                    'target_ml' => $targetWater,
                    'consumed_ml' => $consumedWater,
                    'remaining_ml' => max(0, $targetWater - $consumedWater),
                    'percentage' => $targetWater > 0 ? min(100, round(($consumedWater / $targetWater) * 100, 1)) : 0,
                ],
                'meals' => $meals,
            ],
        ]);
    }

    /**
     * Log a meal entry.
     */
    public function logMeal(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'meal_type' => ['required', 'in:breakfast,lunch,dinner,snack'],
            'food_name' => ['required', 'string', 'max:150'],
            'calories' => ['required', 'integer', 'min:0'],
            'protein_grams' => ['nullable', 'numeric', 'min:0'],
            'carbs_grams' => ['nullable', 'numeric', 'min:0'],
            'fat_grams' => ['nullable', 'numeric', 'min:0'],
            'log_date' => ['nullable', 'date'],
        ]);

        $user = $request->user();
        $date = $validated['log_date'] ?? Carbon::today()->toDateString();

        $meal = MealLog::create([
            'user_id' => $user->id,
            'log_date' => $date,
            'meal_type' => $validated['meal_type'],
            'food_name' => $validated['food_name'],
            'calories' => $validated['calories'],
            'protein_grams' => $validated['protein_grams'] ?? 0,
            'carbs_grams' => $validated['carbs_grams'] ?? 0,
            'fat_grams' => $validated['fat_grams'] ?? 0,
        ]);

        return response()->json([
            'status' => 'success',
            'message' => 'Meal logged successfully.',
            'data' => $meal,
        ], 201);
    }

    /**
     * Add water intake (e.g. +250ml or +500ml).
     */
    public function logWater(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'amount_ml' => ['required', 'integer', 'min:50', 'max:2000'],
            'log_date' => ['nullable', 'date'],
        ]);

        $user = $request->user();
        $date = $validated['log_date'] ?? Carbon::today()->toDateString();

        $water = WaterLog::create([
            'user_id' => $user->id,
            'log_date' => $date,
            'amount_ml' => $validated['amount_ml'],
        ]);

        $totalToday = WaterLog::where('user_id', $user->id)
            ->whereDate('log_date', $date)
            ->sum('amount_ml');

        return response()->json([
            'status' => 'success',
            'message' => 'Water intake logged.',
            'data' => [
                'logged_entry' => $water,
                'total_water_today_ml' => $totalToday,
            ],
        ]);
    }

    /**
     * Get the master food database categorized with selectable portion sizes and macros.
     */
    public function getFoodDatabase(): JsonResponse
    {
        $categories = [
            'meat' => 'Meat',
            'rice' => 'Rice & Grains',
            'fish' => 'Fish & Seafood',
            'vegetables' => 'Vegetables',
            'fruits' => 'Fruits',
            'eggs_dairy' => 'Eggs & Dairy',
            'healthy_fats' => 'Healthy Fats & Nuts',
        ];

        $foods = [
            // ==================== 1. EGGS & DAIRY ====================
            [
                'id' => 'egg_whole',
                'name' => 'Whole Egg',
                'category' => 'eggs_dairy',
                'icon' => 'egg',
                'serving_type' => 'discrete_sizes',
                'base_unit' => 'piece',
                'sizes' => [
                    ['id' => 'small', 'label' => 'Small Egg (38g)', 'grams' => 38, 'calories' => 54, 'protein' => 4.8, 'carbs' => 0.3, 'fat' => 3.6],
                    ['id' => 'medium', 'label' => 'Medium Egg (44g)', 'grams' => 44, 'calories' => 63, 'protein' => 5.5, 'carbs' => 0.4, 'fat' => 4.2],
                    ['id' => 'large', 'label' => 'Large Egg (50g)', 'grams' => 50, 'calories' => 72, 'protein' => 6.3, 'carbs' => 0.4, 'fat' => 4.8],
                    ['id' => 'jumbo', 'label' => 'Jumbo Egg (56g)', 'grams' => 56, 'calories' => 80, 'protein' => 7.0, 'carbs' => 0.5, 'fat' => 5.4],
                ],
                'default_size' => 'large',
            ],
            [
                'id' => 'egg_whites',
                'name' => 'Egg Whites',
                'category' => 'eggs_dairy',
                'icon' => 'egg_alt',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 52, 'protein' => 11.0, 'carbs' => 0.7, 'fat' => 0.2],
                'presets' => [50, 100, 150, 200],
                'default_amount' => 100,
            ],
            [
                'id' => 'greek_yogurt',
                'name' => 'Non-Fat Greek Yogurt',
                'category' => 'eggs_dairy',
                'icon' => 'coffee',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 59, 'protein' => 10.0, 'carbs' => 3.6, 'fat' => 0.4],
                'presets' => [100, 150, 170, 200],
                'default_amount' => 170,
            ],
            [
                'id' => 'cottage_cheese',
                'name' => 'Low-Fat Cottage Cheese',
                'category' => 'eggs_dairy',
                'icon' => 'restaurant',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 81, 'protein' => 11.0, 'carbs' => 4.8, 'fat' => 2.3],
                'presets' => [100, 150, 200],
                'default_amount' => 150,
            ],

            // ==================== 2. RICE & GRAINS ====================
            [
                'id' => 'white_rice',
                'name' => 'White Rice (Cooked)',
                'category' => 'rice',
                'icon' => 'rice_bowl',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 130, 'protein' => 2.7, 'carbs' => 28.2, 'fat' => 0.3],
                'presets' => [50, 100, 150, 200, 250],
                'default_amount' => 150,
            ],
            [
                'id' => 'brown_rice',
                'name' => 'Brown Rice (Cooked)',
                'category' => 'rice',
                'icon' => 'rice_bowl',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 112, 'protein' => 2.6, 'carbs' => 23.5, 'fat' => 0.9],
                'presets' => [50, 100, 150, 200],
                'default_amount' => 150,
            ],
            [
                'id' => 'rolled_oats',
                'name' => 'Rolled Oats (Dry)',
                'category' => 'rice',
                'icon' => 'breakfast_dining',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 389, 'protein' => 16.9, 'carbs' => 66.3, 'fat' => 6.9],
                'presets' => [40, 50, 80, 100],
                'default_amount' => 40,
            ],
            [
                'id' => 'sweet_potato',
                'name' => 'Sweet Potato (Baked)',
                'category' => 'rice',
                'icon' => 'dinner_dining',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 90, 'protein' => 2.0, 'carbs' => 20.7, 'fat' => 0.2],
                'presets' => [100, 150, 200],
                'default_amount' => 150,
            ],
            [
                'id' => 'whole_wheat_bread',
                'name' => 'Whole Wheat Bread',
                'category' => 'rice',
                'icon' => 'bakery_dining',
                'serving_type' => 'discrete_sizes',
                'base_unit' => 'slice',
                'sizes' => [
                    ['id' => '1_slice', 'label' => '1 Slice (35g)', 'grams' => 35, 'calories' => 86, 'protein' => 4.2, 'carbs' => 15.0, 'fat' => 1.2],
                    ['id' => '2_slices', 'label' => '2 Slices (70g)', 'grams' => 70, 'calories' => 172, 'protein' => 8.4, 'carbs' => 30.0, 'fat' => 2.4],
                ],
                'default_size' => '1_slice',
            ],

            // ==================== 3. MEAT ====================
            [
                'id' => 'chicken_breast',
                'name' => 'Chicken Breast (Skinless, Cooked)',
                'category' => 'meat',
                'icon' => 'restaurant_menu',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 165, 'protein' => 31.0, 'carbs' => 0.0, 'fat' => 3.6],
                'presets' => [100, 150, 200, 250],
                'default_amount' => 150,
            ],
            [
                'id' => 'lean_beef',
                'name' => 'Lean Ground Beef (90/10, Cooked)',
                'category' => 'meat',
                'icon' => 'lunch_dining',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 215, 'protein' => 26.0, 'carbs' => 0.0, 'fat' => 12.0],
                'presets' => [100, 150, 200],
                'default_amount' => 150,
            ],
            [
                'id' => 'sirloin_steak',
                'name' => 'Beef Sirloin Steak (Grilled)',
                'category' => 'meat',
                'icon' => 'kebab_dining',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 205, 'protein' => 27.0, 'carbs' => 0.0, 'fat' => 10.0],
                'presets' => [100, 150, 200],
                'default_amount' => 150,
            ],
            [
                'id' => 'turkey_breast',
                'name' => 'Turkey Breast (Roasted)',
                'category' => 'meat',
                'icon' => 'set_meal',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 145, 'protein' => 30.0, 'carbs' => 0.0, 'fat' => 2.0],
                'presets' => [100, 150, 200],
                'default_amount' => 150,
            ],

            // ==================== 4. FISH & SEAFOOD ====================
            [
                'id' => 'salmon_fillet',
                'name' => 'Atlantic Salmon Fillet (Cooked)',
                'category' => 'fish',
                'icon' => 'set_meal',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 208, 'protein' => 22.0, 'carbs' => 0.0, 'fat' => 13.0],
                'presets' => [100, 150, 180, 200],
                'default_amount' => 150,
            ],
            [
                'id' => 'canned_tuna',
                'name' => 'Canned Tuna in Water (Drained)',
                'category' => 'fish',
                'icon' => 'set_meal',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 116, 'protein' => 26.0, 'carbs' => 0.0, 'fat' => 1.0],
                'presets' => [80, 120, 150],
                'default_amount' => 120,
            ],
            [
                'id' => 'tilapia_fillet',
                'name' => 'Tilapia Fillet (Steamed/Baked)',
                'category' => 'fish',
                'icon' => 'set_meal',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 128, 'protein' => 26.0, 'carbs' => 0.0, 'fat' => 2.7],
                'presets' => [100, 150, 200],
                'default_amount' => 150,
            ],
            [
                'id' => 'shrimp_steamed',
                'name' => 'Shrimp / Prawns (Steamed)',
                'category' => 'fish',
                'icon' => 'set_meal',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 99, 'protein' => 24.0, 'carbs' => 0.2, 'fat' => 0.3],
                'presets' => [100, 150, 200],
                'default_amount' => 100,
            ],

            // ==================== 5. VEGETABLES ====================
            [
                'id' => 'broccoli',
                'name' => 'Broccoli (Steamed)',
                'category' => 'vegetables',
                'icon' => 'eco',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 35, 'protein' => 2.4, 'carbs' => 7.0, 'fat' => 0.4],
                'presets' => [50, 100, 150, 200],
                'default_amount' => 100,
            ],
            [
                'id' => 'spinach_fresh',
                'name' => 'Fresh Baby Spinach',
                'category' => 'vegetables',
                'icon' => 'eco',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 23, 'protein' => 2.9, 'carbs' => 3.6, 'fat' => 0.4],
                'presets' => [50, 100, 150],
                'default_amount' => 50,
            ],
            [
                'id' => 'asparagus',
                'name' => 'Asparagus Spears (Roasted)',
                'category' => 'vegetables',
                'icon' => 'eco',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 20, 'protein' => 2.2, 'carbs' => 3.9, 'fat' => 0.1],
                'presets' => [100, 150],
                'default_amount' => 100,
            ],
            [
                'id' => 'carrots',
                'name' => 'Sliced Carrots',
                'category' => 'vegetables',
                'icon' => 'eco',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 41, 'protein' => 0.9, 'carbs' => 9.6, 'fat' => 0.2],
                'presets' => [50, 100, 150],
                'default_amount' => 100,
            ],

            // ==================== 6. FRUITS ====================
            [
                'id' => 'banana',
                'name' => 'Banana (Fresh)',
                'category' => 'fruits',
                'icon' => 'eco',
                'serving_type' => 'discrete_sizes',
                'base_unit' => 'piece',
                'sizes' => [
                    ['id' => 'small', 'label' => 'Small (100g)', 'grams' => 100, 'calories' => 89, 'protein' => 1.1, 'carbs' => 22.8, 'fat' => 0.3],
                    ['id' => 'medium', 'label' => 'Medium (118g)', 'grams' => 118, 'calories' => 105, 'protein' => 1.3, 'carbs' => 27.0, 'fat' => 0.3],
                    ['id' => 'large', 'label' => 'Large (136g)', 'grams' => 136, 'calories' => 121, 'protein' => 1.5, 'carbs' => 31.1, 'fat' => 0.4],
                ],
                'default_size' => 'medium',
            ],
            [
                'id' => 'apple',
                'name' => 'Red Apple (Fresh)',
                'category' => 'fruits',
                'icon' => 'eco',
                'serving_type' => 'discrete_sizes',
                'base_unit' => 'piece',
                'sizes' => [
                    ['id' => 'small', 'label' => 'Small (150g)', 'grams' => 150, 'calories' => 77, 'protein' => 0.4, 'carbs' => 20.5, 'fat' => 0.3],
                    ['id' => 'medium', 'label' => 'Medium (182g)', 'grams' => 182, 'calories' => 95, 'protein' => 0.5, 'carbs' => 25.0, 'fat' => 0.3],
                ],
                'default_size' => 'medium',
            ],
            [
                'id' => 'strawberries',
                'name' => 'Fresh Strawberries',
                'category' => 'fruits',
                'icon' => 'eco',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 32, 'protein' => 0.7, 'carbs' => 7.7, 'fat' => 0.3],
                'presets' => [100, 150, 200],
                'default_amount' => 100,
            ],
            [
                'id' => 'avocado',
                'name' => 'Hass Avocado',
                'category' => 'fruits',
                'icon' => 'eco',
                'serving_type' => 'discrete_sizes',
                'base_unit' => 'piece',
                'sizes' => [
                    ['id' => 'half', 'label' => 'Half Avocado (100g)', 'grams' => 100, 'calories' => 160, 'protein' => 2.0, 'carbs' => 8.5, 'fat' => 14.7],
                    ['id' => 'whole', 'label' => 'Whole Avocado (200g)', 'grams' => 200, 'calories' => 320, 'protein' => 4.0, 'carbs' => 17.0, 'fat' => 29.4],
                ],
                'default_size' => 'half',
            ],

            // ==================== 7. HEALTHY FATS & NUTS ====================
            [
                'id' => 'almonds_raw',
                'name' => 'Raw Almonds',
                'category' => 'healthy_fats',
                'icon' => 'grain',
                'serving_type' => 'weight_grams',
                'base_unit' => 'g',
                'per_100g' => ['calories' => 579, 'protein' => 21.2, 'carbs' => 21.6, 'fat' => 49.9],
                'presets' => [20, 28, 40],
                'default_amount' => 28,
            ],
            [
                'id' => 'peanut_butter',
                'name' => 'Natural Peanut Butter',
                'category' => 'healthy_fats',
                'icon' => 'local_dining',
                'serving_type' => 'discrete_sizes',
                'base_unit' => 'tbsp',
                'sizes' => [
                    ['id' => '1_tbsp', 'label' => '1 tbsp (16g)', 'grams' => 16, 'calories' => 94, 'protein' => 4.0, 'carbs' => 3.2, 'fat' => 8.0],
                    ['id' => '2_tbsp', 'label' => '2 tbsp (32g)', 'grams' => 32, 'calories' => 188, 'protein' => 8.0, 'carbs' => 6.4, 'fat' => 16.0],
                ],
                'default_size' => '1_tbsp',
            ],
            [
                'id' => 'olive_oil',
                'name' => 'Extra Virgin Olive Oil',
                'category' => 'healthy_fats',
                'icon' => 'water_drop',
                'serving_type' => 'discrete_sizes',
                'base_unit' => 'tbsp',
                'sizes' => [
                    ['id' => '1_tsp', 'label' => '1 tsp (5ml)', 'grams' => 5, 'calories' => 44, 'protein' => 0.0, 'carbs' => 0.0, 'fat' => 5.0],
                    ['id' => '1_tbsp', 'label' => '1 tbsp (14ml)', 'grams' => 14, 'calories' => 124, 'protein' => 0.0, 'carbs' => 0.0, 'fat' => 14.0],
                ],
                'default_size' => '1_tbsp',
            ],
        ];

        return response()->json([
            'status' => 'success',
            'data' => [
                'categories' => $categories,
                'foods' => $foods,
            ],
        ]);
    }
}
