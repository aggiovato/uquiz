package com.uquiz.android.core.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.uquiz.android.core.preferences.PreferencesModule
import com.uquiz.android.core.reminder.scheduler.ReminderScheduler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val prefs = runBlocking {
            PreferencesModule.getDataStore(context).data.first()
        }

        val enabled = prefs[REMINDER_ENABLED_KEY] ?: false
        if (!enabled) return

        val hour = prefs[REMINDER_HOUR_KEY] ?: 20
        val minute = prefs[REMINDER_MINUTE_KEY] ?: 0
        ReminderScheduler(context).scheduleDaily(hour, minute)
    }

    private companion object {
        val REMINDER_ENABLED_KEY = booleanPreferencesKey("practice_reminder_enabled")
        val REMINDER_HOUR_KEY = intPreferencesKey("practice_reminder_hour")
        val REMINDER_MINUTE_KEY = intPreferencesKey("practice_reminder_minute")
    }
}
