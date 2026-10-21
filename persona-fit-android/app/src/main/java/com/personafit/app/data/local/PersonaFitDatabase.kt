package com.personafit.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.personafit.app.data.local.dao.StepCacheDao
import com.personafit.app.data.local.dao.WorkoutCacheDao
import com.personafit.app.data.local.entity.CachedStepLog
import com.personafit.app.data.local.entity.CachedWorkoutRoutine

@Database(
    entities = [CachedWorkoutRoutine::class, CachedStepLog::class],
    version = 1,
    exportSchema = false
)
abstract class PersonaFitDatabase : RoomDatabase() {

    abstract fun workoutCacheDao(): WorkoutCacheDao
    abstract fun stepCacheDao(): StepCacheDao

    companion object {
        @Volatile
        private var INSTANCE: PersonaFitDatabase? = null

        fun getDatabase(context: Context): PersonaFitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonaFitDatabase::class.java,
                    "persona_fit_offline_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
