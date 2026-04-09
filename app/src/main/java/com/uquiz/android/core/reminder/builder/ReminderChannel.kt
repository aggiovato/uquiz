package com.uquiz.android.core.reminder.builder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

const val REMINDER_CHANNEL_ID = "practice_reminders"

fun createReminderNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            REMINDER_CHANNEL_ID,
            "Practice reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Daily reminders to practice with UQuiz"
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
