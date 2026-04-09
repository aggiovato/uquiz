package com.uquiz.android.core.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.uquiz.android.core.reminder.scheduler.ReminderScheduler
import com.uquiz.android.core.reminder.usecase.ShowReminderNotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val hour = intent.getIntExtra(ReminderScheduler.EXTRA_HOUR, 20)
        val minute = intent.getIntExtra(ReminderScheduler.EXTRA_MINUTE, 0)
        val pendingResult = goAsync()

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                ShowReminderNotificationUseCase.create(context).invoke()
            } finally {
                ReminderScheduler(context).scheduleDaily(hour, minute)
                pendingResult.finish()
            }
        }
    }
}
