package com.personafit.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.personafit.app.MainActivity
import com.personafit.app.R

object PersonaFitNotificationManager {

    const val CHANNEL_WORKOUT = "channel_workout_reminders"
    const val CHANNEL_RECOVERY = "channel_recovery_reminders"
    const val CHANNEL_PROGRESS = "channel_weekly_progress"
    const val CHANNEL_MEALS = "channel_meal_confirmations"

    const val NOTIFICATION_ID_WORKOUT = 2001
    const val NOTIFICATION_ID_RECOVERY = 2002
    const val NOTIFICATION_ID_PROGRESS = 2003
    const val NOTIFICATION_ID_MEAL = 2004

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. Workout Reminders (High Importance)
            val workoutChannel = NotificationChannel(
                CHANNEL_WORKOUT,
                "Workout Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily training alerts scheduled on your active workout calendar days"
                enableVibration(true)
                setShowBadge(true)
            }

            // 2. Recovery / Rest Day Reminders (Default Importance)
            val recoveryChannel = NotificationChannel(
                CHANNEL_RECOVERY,
                "Recovery & Rest Days",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Rest and active recovery guidance scheduled on non-workout days"
                enableVibration(true)
            }

            // 3. Weekly Progress Reminder (Default Importance)
            val progressChannel = NotificationChannel(
                CHANNEL_PROGRESS,
                "Weekly Progress Reports",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Weekly workout volume, completed sessions, and streak analytics"
                enableVibration(true)
            }

            // 4. Meal Logging Confirmation (Low / Subtle Importance)
            val mealChannel = NotificationChannel(
                CHANNEL_MEALS,
                "Meal Logging Confirmations",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Immediate confirmation when meal entries and weights are saved"
                enableVibration(false)
            }

            notificationManager.createNotificationChannels(
                listOf(workoutChannel, recoveryChannel, progressChannel, mealChannel)
            )
        }
    }

    /**
     * Show Workout Reminder on user's scheduled calendar day.
     */
    fun showWorkoutReminder(context: Context, planName: String = "Today's Prescribed Routine") {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data = Uri.parse("personafit://today_workout")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WORKOUT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_WORKOUT)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Time to Train! 🏋️")
            .setContentText("Your $planName is ready. Let's hit today's training targets!")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Your $planName is ready. Form cues, injury safeguards, and optimal load recommendations are set. Tap to start your session."
            ))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WORKOUT, notification)
            android.util.Log.d("PersonaFitNotif", "Workout reminder notification posted successfully")
        } catch (e: Exception) {
            android.util.Log.e("PersonaFitNotif", "Error posting workout notification: ${e.message}", e)
        }
    }

    /**
     * Show Active Recovery / Rest Day Reminder on non-workout days.
     */
    fun showRecoveryReminder(context: Context) {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_RECOVERY,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_RECOVERY)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Active Recovery Day 🧘")
            .setContentText("Today is your designated rest day. Prioritize hydration and joint mobility.")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "No workouts scheduled today. Prioritize hydration, joint decompression, and restorative sleep to rebuild your muscle fibers for upcoming training."
            ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_RECOVERY, notification)
            android.util.Log.d("PersonaFitNotif", "Recovery reminder notification posted successfully")
        } catch (e: Exception) {
            android.util.Log.e("PersonaFitNotif", "Error posting recovery notification: ${e.message}", e)
        }
    }

    /**
     * Show Weekly Progress Reminder with actual recorded workout numbers.
     * Tapping opens the Progress section directly via deep link.
     */
    fun showWeeklyProgressReminder(
        context: Context,
        workoutsCompleted: Int,
        totalVolumeKg: Float,
        streakDays: Int
    ) {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data = Uri.parse("personafit://progress")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_PROGRESS,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "Weekly Progress Report 📊"
        val message = if (workoutsCompleted > 0) {
            "You completed $workoutsCompleted workouts this week (${totalVolumeKg.toInt()} kg lifted) with a $streakDays-day streak! Tap to view full analytics."
        } else {
            "Review your weekly fitness & nutrition adherence. Tap to view your progress analytics."
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_PROGRESS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_PROGRESS, notification)
            android.util.Log.d("PersonaFitNotif", "Weekly progress notification posted successfully")
        } catch (e: Exception) {
            android.util.Log.e("PersonaFitNotif", "Error posting progress notification: ${e.message}", e)
        }
    }

    /**
     * Show Meal Logging Confirmation with actual food weight and meal type.
     */
    fun showMealLoggedNotification(
        context: Context,
        foodName: String,
        weightDesc: String,
        mealType: String,
        calories: Int
    ) {
        createNotificationChannels(context)

        val formattedMeal = mealType.replaceFirstChar { it.uppercase() }
        val title = "Meal Logged ($formattedMeal) ✓"
        val message = "$foodName ($weightDesc) saved • +$calories kcal"

        val notification = NotificationCompat.Builder(context, CHANNEL_MEALS)
            .setSmallIcon(android.R.drawable.ic_input_add)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_MEAL, notification)
        } catch (_: SecurityException) {}
    }

    /**
     * Lightweight in-app confirmation chime played immediately upon successful food save.
     * Prevents excessive OS notification spam while delivering immediate audible confirmation.
     */
    fun playMealConfirmationChime(context: Context? = null) {
        try {
            val toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 75)
            toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 180)
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    toneGenerator.release()
                } catch (_: Exception) {}
            }, 250)
        } catch (e: Exception) {
            try {
                if (context != null) {
                    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                    audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
                }
            } catch (_: Exception) {}
        }
    }
}
