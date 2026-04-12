package com.uquiz.android.core.reminder.enums

import androidx.annotation.DrawableRes
import com.uquiz.android.R

enum class ReminderMessageCategory {
    GENERIC,
    STREAK,
    PROGRESS,
    RESUME_PACK,
}

@DrawableRes
fun ReminderMessageCategory.largeIconRes(): Int = when (this) {
    ReminderMessageCategory.GENERIC -> R.drawable.notification_large_generic
    ReminderMessageCategory.STREAK -> R.drawable.notification_large_streak
    ReminderMessageCategory.PROGRESS -> R.drawable.notification_large_progress
    ReminderMessageCategory.RESUME_PACK -> R.drawable.notification_large_resume
}
