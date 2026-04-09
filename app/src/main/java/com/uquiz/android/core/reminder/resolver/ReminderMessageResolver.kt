package com.uquiz.android.core.reminder.resolver

import com.uquiz.android.core.reminder.copy.ReminderCopyRegistry
import com.uquiz.android.core.reminder.model.ReminderMessage
import com.uquiz.android.core.reminder.enums.ReminderMessageCategory
import com.uquiz.android.core.reminder.model.ReminderNotificationContext
import com.uquiz.android.domain.ranking.enums.UserRank
import kotlin.random.Random

class ReminderMessageResolver(
    private val random: Random = Random.Default
) {

    fun resolve(context: ReminderNotificationContext): ReminderMessage {
        val copy = ReminderCopyRegistry.forLanguage(context.languageCode)
        val category = selectCategory(context)

        val title = when (category) {
            ReminderMessageCategory.GENERIC -> copy.genericTitles.random(random)
            ReminderMessageCategory.STREAK -> copy.streakTitles.random(random)
            ReminderMessageCategory.PROGRESS -> copy.progressTitles.random(random)
            ReminderMessageCategory.RESUME_PACK -> copy.resumePackTitles.random(random)
        }
        val text = when (category) {
            ReminderMessageCategory.GENERIC -> copy.genericBodies.random(random).render(context)
            ReminderMessageCategory.STREAK -> copy.streakBodies.random(random).render(context)
            ReminderMessageCategory.PROGRESS -> copy.progressBodies.random(random).render(context)
            ReminderMessageCategory.RESUME_PACK -> copy.resumePackBodies.random(random).render(context)
        }

        return ReminderMessage(title = title, text = text, category = category)
    }

    private fun selectCategory(context: ReminderNotificationContext): ReminderMessageCategory = when {
        context.unfinishedPackTitle != null && (context.remainingQuestions ?: 0) > 0 -> {
            ReminderMessageCategory.RESUME_PACK
        }
        context.streak > 0 -> ReminderMessageCategory.STREAK
        context.points > 0 || context.rank != UserRank.INITIATE -> ReminderMessageCategory.PROGRESS
        else -> ReminderMessageCategory.GENERIC
    }
}
