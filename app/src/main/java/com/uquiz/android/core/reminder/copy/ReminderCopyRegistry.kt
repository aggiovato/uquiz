package com.uquiz.android.core.reminder.copy

object ReminderCopyRegistry {
    fun forLanguage(code: String): ReminderCopySet = when (code) {
        "es" -> ReminderCopyEs
        "it" -> ReminderCopyIt
        "ja" -> ReminderCopyJa
        else -> ReminderCopyEn
    }
}
