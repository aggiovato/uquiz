package com.uquiz.android.core.reminder.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.uquiz.android.core.reminder.receiver.ReminderReceiver
import java.util.Calendar

class ReminderScheduler(private val context: Context) {

    fun scheduleDaily(hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val pendingIntent = requireNotNull(
            buildPendingIntent(hour, minute, PendingIntent.FLAG_UPDATE_CURRENT)
        ) { "Reminder PendingIntent could not be created." }
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextTriggerMillis(hour, minute),
            pendingIntent
        )
    }

    fun cancel() {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val pendingIntent = buildPendingIntent(0, 0, PendingIntent.FLAG_NO_CREATE)
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun buildPendingIntent(hour: Int, minute: Int, flags: Int): PendingIntent? {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(EXTRA_HOUR, hour)
            putExtra(EXTRA_MINUTE, minute)
        }
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            flags or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun nextTriggerMillis(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (target.timeInMillis <= now.timeInMillis) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }
        return target.timeInMillis
    }

    companion object {
        const val REQUEST_CODE = 1001
        const val EXTRA_HOUR = "reminder_hour"
        const val EXTRA_MINUTE = "reminder_minute"
    }
}
