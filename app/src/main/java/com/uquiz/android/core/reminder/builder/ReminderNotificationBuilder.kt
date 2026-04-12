package com.uquiz.android.core.reminder.builder

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.uquiz.android.R
import com.uquiz.android.core.reminder.model.ReminderMessage

class ReminderNotificationBuilder(
    private val context: Context,
) {
    fun show(message: ReminderMessage): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return false
        }

        createReminderNotificationChannel(context)

        val tapIntent =
            context.packageManager
                .getLaunchIntentForPackage(context.packageName)
                ?.apply {
                    flags =
                        android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
                }

        val tapPendingIntent =
            tapIntent?.let {
                PendingIntent.getActivity(
                    context,
                    0,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            }

        val largeIcon = BitmapFactory.decodeResource(context.resources, message.largeIconRes)

        val notification =
            NotificationCompat
                .Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.u_reminder)
                .setColor(ContextCompat.getColor(context, R.color.notification_accent))
                .setLargeIcon(largeIcon)
                .setContentTitle(message.title)
                .setContentText(message.text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message.text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .apply { tapPendingIntent?.let { setContentIntent(it) } }
                .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        return true
    }

    companion object {
        const val NOTIFICATION_ID = 2001
    }
}
