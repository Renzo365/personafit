package com.personafit.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.personafit.app.data.local.entity.CachedStepLog
import com.personafit.app.data.local.entity.CachedWorkoutRoutine
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutCacheDao {
    @Query("SELECT * FROM cached_workout_routines ORDER BY lastUpdated DESC LIMIT 1")
    fun getLatestCachedRoutine(): Flow<CachedWorkoutRoutine?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: CachedWorkoutRoutine)

    @Query("DELETE FROM cached_workout_routines")
    suspend fun clearCache()
}

@Dao
interface StepCacheDao {
    @Query("SELECT * FROM cached_step_logs WHERE logDate = :date LIMIT 1")
    suspend fun getStepLogByDate(date: String): CachedStepLog?

    @Query("SELECT * FROM cached_step_logs WHERE isSynced = 0")
    suspend fun getUnsyncedStepLogs(): List<CachedStepLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStepLog(log: CachedStepLog)

    @Query("UPDATE cached_step_logs SET isSynced = 1 WHERE logDate = :date")
    suspend fun markAsSynced(date: String)
}
