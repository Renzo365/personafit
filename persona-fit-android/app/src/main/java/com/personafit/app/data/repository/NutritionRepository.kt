package com.personafit.app.data.repository

import com.personafit.app.data.remote.PersonaFitApiService
import com.personafit.app.data.remote.dto.*

class NutritionRepository(private val apiService: PersonaFitApiService) {

    suspend fun getTodayNutrition(): Result<NutritionSummaryData> {
        return try {
            val response = apiService.getTodayNutrition()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to fetch nutrition data."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logMeal(mealType: String, foodName: String, calories: Int, p: Float, c: Float, f: Float): Result<MealLogDto> {
        return try {
            val response = apiService.logMeal(LogMealRequest(mealType, foodName, calories, p, c, f))
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to log meal."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logWater(amountMl: Int): Result<LogWaterResponse> {
        return try {
            val response = apiService.logWater(LogWaterRequest(amountMl))
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception("Failed to log water intake."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
