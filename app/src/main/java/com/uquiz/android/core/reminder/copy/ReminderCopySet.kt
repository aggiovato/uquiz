package com.uquiz.android.core.reminder.copy

import com.uquiz.android.core.reminder.model.ReminderNotificationContext
import com.uquiz.android.domain.ranking.enums.UserRank

data class ReminderCopySet(
    val genericTitles: List<String>,
    val genericBodies: List<ReminderBodyTemplate>,
    val streakTitles: List<String>,
    val streakBodies: List<ReminderBodyTemplate>,
    val progressTitles: List<String>,
    val progressBodies: List<ReminderBodyTemplate>,
    val resumePackTitles: List<String>,
    val resumePackBodies: List<ReminderBodyTemplate>,
    val rankLabel: (UserRank) -> String,
)

fun interface ReminderBodyTemplate {
    fun render(context: ReminderNotificationContext): String
}
