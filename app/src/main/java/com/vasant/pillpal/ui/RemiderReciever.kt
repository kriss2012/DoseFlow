package com.PillPal.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.PillPal.data.db.Medicine
import com.PillPal.data.sharedPref.NotificationPrefs
import com.PillPal.repository.MedicineRepo
import com.PillPal.ui.components.REMINDER
import com.PillPal.ui.components.cancelAlarm
import com.PillPal.ui.components.snoozeAlarm
import com.PillPal.ui.notifications.NotificationActions
import com.PillPal.ui.notifications.cancelReminderNotification
import com.PillPal.ui.notifications.showReminderNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.PillPal.services.ensureChannelForSettings

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var updateMedicine: MedicineRepo
    @Inject
    lateinit var prefs: NotificationPrefs

    override fun onReceive(context: Context, intent: Intent) {
        val reminderJson = intent.getStringExtra(REMINDER) ?: return
        val reminder = Gson().fromJson(reminderJson, Medicine::class.java)

        when (intent.action) {
            NotificationActions.DONE_ACTION -> {
                runBlocking { updateMedicine.updateMedicine(reminder.copy(isCompleted = true)) }
                cancelAlarm(context, reminder)
                cancelReminderNotification(context, reminder)
            }
            NotificationActions.SNOOZE_ACTION -> {
                snoozeAlarm(context, reminder, minutes = 10)
                cancelReminderNotification(context, reminder)
            }
            NotificationActions.DISMISS_ACTION -> {
                cancelReminderNotification(context, reminder)
            }
            else -> {
                // Respect current notification settings
                if (!prefs.isEnabled()) return
                val channelId = ensureChannelForSettings(
                    context = context,
                    soundEnabled = prefs.isSoundEnabled(),
                    vibration = prefs.isVibrationEnabled(),
                    soundUriString = prefs.getSoundUri()
                )
                showReminderNotification(context, reminder, channelId)
            }
        }
    }
}
