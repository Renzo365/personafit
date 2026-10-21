package com.personafit.app

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.personafit.app.data.local.PersonaFitDatabase
import com.personafit.app.data.local.SessionManager
import com.personafit.app.data.remote.ApiClient
import com.personafit.app.data.repository.*
import com.personafit.app.data.sensor.StepSensorManager

class PersonaFitApp : Application(), ImageLoaderFactory {

    lateinit var sessionManager: SessionManager
        private set

    lateinit var database: PersonaFitDatabase
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var assessmentRepository: AssessmentRepository
        private set

    lateinit var workoutRepository: WorkoutRepository
        private set

    lateinit var nutritionRepository: NutritionRepository
        private set

    lateinit var activityRepository: ActivityRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    lateinit var stepSensorManager: StepSensorManager
        private set

    override fun onCreate() {
        super.onCreate()

        sessionManager = SessionManager(this)
        database = PersonaFitDatabase.getDatabase(this)

        val apiService = ApiClient.getService(this)

        authRepository = AuthRepository(apiService, sessionManager)
        assessmentRepository = AssessmentRepository(apiService, sessionManager)
        workoutRepository = WorkoutRepository(apiService, database.workoutCacheDao())
        nutritionRepository = NutritionRepository(apiService)
        activityRepository = ActivityRepository(apiService, database.stepCacheDao())
        settingsRepository = SettingsRepository(apiService, sessionManager)

        stepSensorManager = StepSensorManager(this, activityRepository)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .respectCacheHeaders(false)
            .build()
    }
}
