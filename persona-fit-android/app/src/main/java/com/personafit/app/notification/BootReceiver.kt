package com.personafit.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.personafit.app.PersonaFitApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED ||
            intent?.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            val app = context.applicationContext as? PersonaFitApp ?: return
            val sessionManager = app.sessionManager
            val settingsRepository = app.settingsRepository

            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    PersonaFitNotificationManager.createNotificationChannels(context)
                    val settingsRes = settingsRepository.getSettings()
                    val availableDays = settingsRes.getOrNull()?.profile?.availableDays?.toSet()
                        ?: setOf("Monday", "Wednesday", "Friday")
                    NotificationScheduler.rescheduleAll(context, sessionManager, availableDays)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
