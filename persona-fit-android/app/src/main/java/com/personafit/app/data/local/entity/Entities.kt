package com.personafit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_workout_routines")
data class CachedWorkoutRoutine(
    @PrimaryKey val planId: Long,
    val planName: String,
    val focusArea: String,
    val durationMinutes: Int,
    val exercisesJson: String, // Serialized list of prescribed exercises
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_step_logs")
data class CachedStepLog(
    @PrimaryKey val logDate: String,
    val stepCount: Int,
    val distanceKm: Float,
    val caloriesBurned: Float,
    val isSynced: Boolean = false
)
