<?php

namespace App\Services;

class NutritionCalculatorService
{
    /**
     * Calculate BMR using the Mifflin-St Jeor Equation.
     * Men: (10 * weight_kg) + (6.25 * height_cm) - (5 * age) + 5
     * Women: (10 * weight_kg) + (6.25 * height_cm) - (5 * age) - 161
     */
    public function calculateBMR(string $gender, int $age, float $heightCm, float $weightKg): float
    {
        $base = (10 * $weightKg) + (6.25 * $heightCm) - (5 * $age);

        if (strtolower($gender) === 'male') {
            return round($base + 5, 2);
        }

        return round($base - 161, 2);
    }

    /**
     * Calculate Total Daily Energy Expenditure (TDEE).
     */
    public function calculateTDEE(float $bmr, string $activityLevel): float
    {
        $multipliers = [
            'sedentary' => 1.2,        // Little to no exercise, desk job
            'light' => 1.375,          // Light exercise 1-3 days/week
            'moderate' => 1.55,        // Moderate exercise 3-5 days/week
            'very_active' => 1.725,    // Hard exercise 6-7 days/week
        ];

        $factor = $multipliers[strtolower($activityLevel)] ?? 1.2;

        return round($bmr * $factor, 0);
    }

    /**
     * Calculate Goal-Adjusted Daily Calorie Target.
     */
    public function calculateDailyCalories(float $tdee, string $fitnessGoal): int
    {
        return match (strtolower($fitnessGoal)) {
            'fat_loss' => (int) max(1200, round($tdee - 500)),   // 500 kcal deficit (min 1200 safe limit)
            'muscle_gain' => (int) round($tdee + 300),           // 300 kcal surplus
            default => (int) round($tdee),                       // Maintenance
        };
    }

    /**
     * Calculate Macronutrient Split in grams:
     * - Protein: 2.0g per kg bodyweight (4 kcal/g)
     * - Fat: 25% of total daily calories (9 kcal/g)
     * - Carbohydrates: Remaining calories / 4 kcal/g
     */
    public function calculateMacros(int $dailyCalories, float $weightKg): array
    {
        // Protein: 2.0g per kg of bodyweight
        $proteinGrams = (int) round($weightKg * 2.0);
        $proteinCalories = $proteinGrams * 4;

        // Fat: 25% of total calories
        $fatCalories = $dailyCalories * 0.25;
        $fatGrams = (int) round($fatCalories / 9);

        // Carbohydrates: Remaining calories
        $remainingCalories = max(0, $dailyCalories - $proteinCalories - $fatCalories);
        $carbsGrams = (int) round($remainingCalories / 4);

        return [
            'protein_grams' => $proteinGrams,
            'carbs_grams' => $carbsGrams,
            'fat_grams' => $fatGrams,
        ];
    }

    /**
     * Calculate Recommended Daily Hydration (ml).
     * Rule of thumb: 35ml per kg bodyweight + 500ml exercise buffer.
     */
    public function calculateHydrationTarget(float $weightKg): int
    {
        $base = $weightKg * 35;
        return (int) round(max(2000, $base + 500));
    }

    /**
     * Complete Nutrition Profile Generator.
     */
    public function generateFullProfile(string $gender, int $age, float $heightCm, float $weightKg, string $activityLevel, string $fitnessGoal): array
    {
        $bmr = $this->calculateBMR($gender, $age, $heightCm, $weightKg);
        $tdee = $this->calculateTDEE($bmr, $activityLevel);
        $dailyCalories = $this->calculateDailyCalories($tdee, $fitnessGoal);
        $macros = $this->calculateMacros($dailyCalories, $weightKg);
        $waterTarget = $this->calculateHydrationTarget($weightKg);

        return [
            'bmr' => $bmr,
            'tdee' => $tdee,
            'daily_calories' => $dailyCalories,
            'protein_grams' => $macros['protein_grams'],
            'carbs_grams' => $macros['carbs_grams'],
            'fat_grams' => $macros['fat_grams'],
            'water_ml_target' => $waterTarget,
        ];
    }
}
