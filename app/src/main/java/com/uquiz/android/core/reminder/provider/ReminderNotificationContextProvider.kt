package com.uquiz.android.core.reminder.provider

import com.uquiz.android.core.reminder.model.ReminderNotificationContext
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.domain.user.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first

class ReminderNotificationContextProvider(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userStatsRepository: UserStatsRepository,
    private val userRankRepository: UserRankRepository,
    private val attemptRepository: AttemptRepository,
    private val packRepository: PackRepository,
) {

    suspend fun getContext(): ReminderNotificationContext {
        val preferences = userPreferencesRepository.getPreferences()
        val stats = userStatsRepository.observeSnapshot().first()
        val rank = runCatching { userRankRepository.getCurrent() }.getOrNull()
        val latestIncomplete = runCatching { attemptRepository.getIncomplete().firstOrNull { it.primaryPackId != null } }
            .getOrNull()
        val packInfo = latestIncomplete?.primaryPackId?.let { packId ->
            val pack = packRepository.getById(packId) ?: return@let null
            val answeredCount = runCatching { attemptRepository.getAnswers(latestIncomplete.id).size }.getOrDefault(0)
            val totalQuestions = latestIncomplete.totalQuestions.takeIf { it > 0 }
                ?: runCatching { packRepository.getQuestionCount(packId) }.getOrDefault(0)
            val remaining = (totalQuestions - answeredCount).coerceAtLeast(0)
            if (remaining <= 0) null else Triple(pack.title, remaining, answeredCount)
        }

        return ReminderNotificationContext(
            languageCode = preferences.languageCode,
            streak = stats.dayStreak,
            points = stats.totalPoints,
            rank = rank?.currentRank ?: UserRank.INITIATE,
            unfinishedPackTitle = packInfo?.first,
            remainingQuestions = packInfo?.second,
            nextQuestionIndex = packInfo?.third,
        )
    }
}
