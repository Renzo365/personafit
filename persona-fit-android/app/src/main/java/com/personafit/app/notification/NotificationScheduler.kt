package com.personafit.app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.personafit.app.data.local.SessionManager
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

object NotificationScheduler {

    const val ACTION_WORKOUT_REMINDER = "com.personafit.app.ACTION_WORKOUT_REMINDER"
    const val ACTION_RECOVERY_REMINDER = "com.personafit.app.ACTION_RECOVERY_REMINDER"
    const val ACTION_PROGRESS_REMINDER = "com.personafit.app.ACTION_PROGRESS_REMINDER"

    const val REQUEST_CODE_WORKOUT = 1001
    const val REQUEST_CODE_RECOVERY = 1002
    const val REQUEST_CODE_PROGRESS = 1003

    private val DAY_OF_WEEK_MAP = mapOf(
        "Sunday" to Calendar.SUNDAY,
        "Monday" to Calendar.MONDAY,
        "Tuesday" to Calendar.TUESDAY,
        "Wednesday" to Calendar.WEDNESDAY,
        "Thursday" to Calendar.THURSDAY,
        "Friday" to Calendar.FRIDAY,
        "Saturday" to Calendar.SATURDAY
    )

    /**
     * Reschedules all active reminders based on the user's latest settings and calendar.
     */
    suspend fun rescheduleAll(
        context: Context,
        sessionManager: SessionManager,
        availableDays: Set<String>
    ) {
        val workoutEnabled = sessionManager.notifWorkoutEnabled.firstOrNull() ?: true
        val recoveryEnabled = sessionManager.notifRecoveryEnabled.firstOrNull() ?: true
        val progressEnabled = sessionManager.notifProgressEnabled.firstOrNull() ?: true

        val workoutHour = sessionManager.workoutTimeHour.firstOrNull() ?: 8
        val workoutMinute = sessionManager.workoutTimeMinute.firstOrNull() ?: 0

        val progressDay = sessionManager.progressDay.firstOrNull() ?: "Sunday"
        val progressHour = sessionManager.progressTimeHour.firstOrNull() ?: 18
        val progressMinute = sessionManager.progressTimeMinute.firstOrNull() ?: 0

        // 1. Workout Reminder
        if (workoutEnabled && availableDays.isNotEmpty()) {
            scheduleNextWorkoutReminder(context, availableDays, workoutHour, workoutMinute)
        } else {
            cancelWorkoutReminder(context)
        }

        // 2. Recovery / Rest Day Reminder (scheduled only for days NOT in availableDays)
        val allDays = DAY_OF_WEEK_MAP.keys
        val restDays = allDays.filter { !availableDays.contains(it) }.toSet()
        if (recoveryEnabled && restDays.isNotEmpty()) {
            scheduleNextRecoveryReminder(context, restDays, 9, 0)
        } else {
            cancelRecoveryReminder(context)
        }

        // 3. Weekly Progress Reminder
        if (progressEnabled) {
            scheduleNextProgressReminder(context, progressDay, progressHour, progressMinute)
        } else {
            cancelProgressReminder(context)
        }
    }

    fun scheduleNextWorkoutReminder(
        context: Context,
        availableDays: Set<String>,
        targetHour: Int,
        targetMinute: Int
    ) {
        val nextTimeMillis = findNextTargetTime(availableDays, targetHour, targetMinute) ?: return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, NotificationAlarmReceiver::class.java).apply {
            action = ACTION_WORKOUT_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_WORKOUT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        setAlarmSafely(alarmManager, nextTimeMillis, pendingIntent)
    }

    fun scheduleNextRecoveryReminder(
        context: Context,
        restDays: Set<String>,
        targetHour: Int,
        targetMinute: Int
    ) {
        val nextTimeMillis = findNextTargetTime(restDays, targetHour, targetMinute) ?: return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, NotificationAlarmReceiver::class.java).apply {
            action = ACTION_RECOVERY_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_RECOVERY,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        setAlarmSafely(alarmManager, nextTimeMillis, pendingIntent)
    }

    fun scheduleNextProgressReminder(
        context: Context,
        targetDay: String,
        targetHour: Int,
        targetMinute: Int
    ) {
        val nextTimeMillis = findNextTargetTime(setOf(targetDay), targetHour, targetMinute) ?: return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, NotificationAlarmReceiver::class.java).apply {
            action = ACTION_PROGRESS_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PROGRESS,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        setAlarmSafely(alarmManager, nextTimeMillis, pendingIntent)
    }

    fun cancelWorkoutReminder(context: Context) {
        cancelAlarm(context, ACTION_WORKOUT_REMINDER, REQUEST_CODE_WORKOUT)
    }

    fun cancelRecoveryReminder(context: Context) {
        cancelAlarm(context, ACTION_RECOVERY_REMINDER, REQUEST_CODE_RECOVERY)
    }

    fun cancelProgressReminder(context: Context) {
        cancelAlarm(context, ACTION_PROGRESS_REMINDER, REQUEST_CODE_PROGRESS)
    }

    private fun cancelAlarm(context: Context, actionStr: String, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, NotificationAlarmReceiver::class.java).apply { action = actionStr }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun setAlarmSafely(
        alarmManager: AlarmManager,
        triggerAtMillis: Long,
        pendingIntent: PendingIntent
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            // Fallback for devices restricting exact alarms
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    /**
     * Calculates the nearest upcoming timestamp (in millis) for a set of target days and time.
     */
    private fun findNextTargetTime(
        targetDays: Set<String>,
        targetHour: Int,
        targetMinute: Int
    ): Long? {
        if (targetDays.isEmpty()) return null

        val now = Calendar.getInstance()
        var earliestMillis: Long? = null

        for (dayName in targetDays) {
            val calendarDay = DAY_OF_WEEK_MAP[dayName] ?: continue
            val candidate = Calendar.getInstance().apply {
                timeInMillis = now.timeInMillis
                set(Calendar.HOUR_OF_DAY, targetHour)
                set(Calendar.MINUTE, targetMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // Adjust to the target day of week
                var diff = calendarDay - get(Calendar.DAY_OF_WEEK)
                if (diff < 0 || (diff == 0 && timeInMillis <= now.timeInMillis)) {
                    diff += 7
                }
                add(Calendar.DAY_OF_YEAR, diff)
            }

            val millis = candidate.timeInMillis
            if (earliestMillis == null || millis < earliestMillis) {
                earliestMillis = millis
            }
        }

        return earliestMillis
    }
}
