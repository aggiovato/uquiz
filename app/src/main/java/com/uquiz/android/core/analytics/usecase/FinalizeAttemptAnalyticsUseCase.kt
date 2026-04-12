package com.uquiz.android.core.analytics.usecase

import com.uquiz.android.domain.attempts.repository.AttemptRepository

class FinalizeAttemptAnalyticsUseCase(
    private val attemptRepository: AttemptRepository,
    private val updateQuestionStatsFromAttemptUseCase: UpdateQuestionStatsFromAttemptUseCase,
    private val updatePackStatsFromAttemptUseCase: UpdatePackStatsFromAttemptUseCase,
    private val updateUserRankFromAttemptUseCase: UpdateUserRankFromAttemptUseCase,
) {
    suspend operator fun invoke(attemptId: String) {
        val (attempt, answers) = attemptRepository.getWithAnswers(attemptId) ?: return
        updateQuestionStatsFromAttemptUseCase(attempt, answers)
        attempt.primaryPackId?.let { packId ->
            updatePackStatsFromAttemptUseCase(packId)
        }
        updateUserRankFromAttemptUseCase(attempt, answers)
    }
}
