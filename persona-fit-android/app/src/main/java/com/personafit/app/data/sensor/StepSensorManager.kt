package com.personafit.app.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.personafit.app.data.repository.ActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StepSensorManager(
    private val context: Context,
    private val activityRepository: ActivityRepository
) : SensorEventListener {

    private val sensorManager: SensorManager? = try {
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    } catch (e: Exception) {
        null
    }

    private val stepSensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    } catch (e: Exception) {
        null
    }

    private val _liveSteps = MutableStateFlow(0)
    val liveSteps: StateFlow<Int> = _liveSteps.asStateFlow()

    private var initialStepOffset: Int = -1
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun startListening() {
        try {
            if (sensorManager != null && stepSensor != null) {
                sensorManager.registerListener(
                    this,
                    stepSensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        } catch (e: Exception) {
            // Gracefully handle emulator or devices without sensor permissions
        }
    }

    fun stopListening() {
        try {
            sensorManager?.unregisterListener(this)
        } catch (e: Exception) {
            // No-op
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSensorSteps = event.values[0].toInt()

            if (initialStepOffset == -1) {
                initialStepOffset = totalSensorSteps
            }

            val currentSessionSteps = totalSensorSteps - initialStepOffset
            val totalStepsToday = maxOf(0, currentSessionSteps)

            _liveSteps.value = totalStepsToday

            // Sync with backend / local Room database periodically
            coroutineScope.launch {
                activityRepository.syncSteps(totalStepsToday)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }

    fun syncManualSteps(steps: Int) {
        _liveSteps.value = steps
        coroutineScope.launch {
            activityRepository.syncSteps(steps)
        }
    }
}
