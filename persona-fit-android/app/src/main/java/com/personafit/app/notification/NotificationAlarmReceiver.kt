package com.personafit.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.personafit.app.PersonaFitApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotificationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        android.util.Log.d("PersonaFitNotif", "onReceive action=${intent.action}")
        val app = context.applicationContext as? PersonaFitApp ?: run {
            android.util.Log.e("PersonaFitNotif", "Application is not PersonaFitApp")
            return
        }
        val sessionManager = app.sessionManager
        val settingsRepository = app.settingsRepository
        val activityRepository = app.activityRepository

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (intent.action) {
                    NotificationScheduler.ACTION_WORKOUT_REMINDER -> {
                        android.util.Log.d("PersonaFitNotif", "Handling ACTION_WORKOUT_REMINDER")
                        handleWorkoutReminder(context, app, sessionManager, settingsRepository)
                    }
                    NotificationScheduler.ACTION_RECOVERY_REMINDER -> {
                        android.util.Log.d("PersonaFitNotif", "Handling ACTION_RECOVERY_REMINDER")
                        handleRecoveryReminder(context, sessionManager, settingsRepository)
                    }
                    NotificationScheduler.ACTION_PROGRESS_REMINDER -> {
                        android.util.Log.d("PersonaFitNotif", "Handling ACTION_PROGRESS_REMINDER")
                        handleProgressReminder(context, sessionManager, activityRepository)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("PersonaFitNotif", "Error in onReceive: ${e.message}", e)
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun handleWorkoutReminder(
        context: Context,
        app: PersonaFitApp,
        sessionManager: com.personafit.app.data.local.SessionManager,
        settingsRepository: com.personafit.app.data.repository.SettingsRepository
    ) {
        val enabled = sessionManager.notifWorkoutEnabled.firstOrNull() ?: true
        if (!enabled) return

        // Check user available days from persisted local session (offline resilient)
        val availableDays = sessionManager.getAvailableDays()

        val todayName = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US) ?: ""
        android.util.Log.d("PersonaFitNotif", "handleWorkoutReminder: availableDays=$availableDays today=$todayName isScheduled=${availableDays.contains(todayName)}")
        if (!availableDays.contains(todayName)) {
            // Not a scheduled workout day! Reschedule next and exit.
            val hour = sessionManager.workoutTimeHour.firstOrNull() ?: 8
            val minute = sessionManager.workoutTimeMinute.firstOrNull() ?: 0
            NotificationScheduler.scheduleNextWorkoutReminder(context, availableDays, hour, minute)
            return
        }

        // Duplicate prevention for today
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().time)
        val lastDate = sessionManager.getLastWorkoutDate()
        if (lastDate == todayStr) {
            // Already delivered for today's session
            return
        }

        // Deliver notification
        val planName = sessionManager.personaArchetype.firstOrNull()?.let { "$it Workout Routine" }
            ?: "Daily Prescribed Routine"
        PersonaFitNotificationManager.showWorkoutReminder(context, planName)
        sessionManager.setLastWorkoutDate(todayStr)

        // Reschedule for next session
        val hour = sessionManager.workoutTimeHour.firstOrNull() ?: 8
        val minute = sessionManager.workoutTimeMinute.firstOrNull() ?: 0
        NotificationScheduler.scheduleNextWorkoutReminder(context, availableDays, hour, minute)
    }

    private suspend fun handleRecoveryReminder(
        context: Context,
        sessionManager: com.personafit.app.data.local.SessionManager,
        settingsRepository: com.personafit.app.data.repository.SettingsRepository
    ) {
        val enabled = sessionManager.notifRecoveryEnabled.firstOrNull() ?: true
        if (!enabled) return

        val availableDays = sessionManager.getAvailableDays()

        val todayName = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US) ?: ""
        // Safety rule: NEVER send a rest day reminder on a scheduled workout day!
        if (availableDays.contains(todayName)) {
            return
        }

        // Duplicate prevention for today
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().time)
        val lastDate = sessionManager.getLastRecoveryDate()
        if (lastDate == todayStr) {
            return
        }

        PersonaFitNotificationManager.showRecoveryReminder(context)
        sessionManager.setLastRecoveryDate(todayStr)

        // Reschedule next
        val allDays = setOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val restDays = allDays.filter { !availableDays.contains(it) }.toSet()
        if (restDays.isNotEmpty()) {
            NotificationScheduler.scheduleNextRecoveryReminder(context, restDays, 9, 0)
        }
    }

    private suspend fun handleProgressReminder(
        context: Context,
        sessionManager: com.personafit.app.data.local.SessionManager,
        activityRepository: com.personafit.app.data.repository.ActivityRepository
    ) {
        val enabled = sessionManager.notifProgressEnabled.firstOrNull() ?: true
        if (!enabled) return

        // Duplicate prevention: check current calendar year and week
        val cal = Calendar.getInstance()
        val currentWeekStr = "${cal.get(Calendar.YEAR)}-W${cal.get(Calendar.WEEK_OF_YEAR)}"
        val lastWeek = sessionManager.getLastProgressWeek()
        if (lastWeek == currentWeekStr) {
            return
        }

        // Fetch actual recorded progress
        var completedCount = 0
        var totalVolumeKg = 0f
        var streak = 0
        try {
            val progressRes = activityRepository.getProgressDashboard()
            if (progressRes.isSuccess) {
                val data = progressRes.getOrNull()
                completedCount = data?.workouts?.thisWeekCompleted ?: 0
                totalVolumeKg = data?.workouts?.totalVolumeKg ?: 0f
                streak = data?.streakDays ?: 0
            }
        } catch (_: Exception) {}

        PersonaFitNotificationManager.showWeeklyProgressReminder(context, completedCount, totalVolumeKg, streak)
        sessionManager.setLastProgressWeek(currentWeekStr)

        // Reschedule for next week
        val progressDay = sessionManager.progressDay.firstOrNull() ?: "Sunday"
        val hour = sessionManager.progressTimeHour.firstOrNull() ?: 18
        val minute = sessionManager.progressTimeMinute.firstOrNull() ?: 0
        NotificationScheduler.scheduleNextProgressReminder(context, progressDay, hour, minute)
    }
}
