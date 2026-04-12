package com.uquiz.android.core.reminder.model

import androidx.annotation.DrawableRes
import com.uquiz.android.core.reminder.enums.ReminderMessageCategory

data class ReminderMessage(
    val title: String,
    val text: String,
    val category: ReminderMessageCategory,
    @param:DrawableRes val largeIconRes: Int,
)
