package com.uquiz.android.core.analytics

import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.model.Attempt
import com.uquiz.android.domain.attempts.model.AttemptAnswer
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import kotlin.math.exp
import kotlin.math.roundToLong

class UpdateUserRankFromAttemptUseCase(
    private val userRankRepository: UserRankRepository,
    private val computeSessionPerformanceUseCase: ComputeSessionPerformanceUseCase,
) {
    suspend operator fun invoke(
        attempt: Attempt,
        answers: List<AttemptAnswer>,
    ) {
        if (answers.isEmpty()) return

        val current = userRankRepository.getCurrent()
        val perf = computeSessionPerformanceUseCase(attempt, answers)
        val alpha = if (current.totalGameAnswers < 30) 0.10f else 0.05f
        val ewma = alpha * perf.sessionPerformance + ((1f - alpha) * current.perfEwma)
        val expected = sigmoid((current.mmr - 1200f) / 300f)
        val kBase =
            when {
                current.totalGameAnswers < 50 -> 60f
                current.totalGameAnswers < 200 -> 35f
                else -> 20f
            }
        val k = if (attempt.mode == AttemptMode.GAME) kBase else kBase * 0.25f
        val mmr = (current.mmr + (k * (ewma.coerceIn(0f, 1f) - expected))).coerceIn(0f, 4000f)
        val now = attempt.completedAt ?: System.currentTimeMillis()
        val nextTotalGameAnswers =
            current.totalGameAnswers + if (attempt.mode == AttemptMode.GAME) perf.totalAnswers else 0
        val nextTotalStudyAnswers =
            current.totalStudyAnswers + if (attempt.mode == AttemptMode.STUDY) perf.totalAnswers else 0
        val nextAnswersSinceRankChange = current.answersSinceRankChange + perf.totalAnswers
        val candidateRank = UserRank.fromMmr(mmr)
        val rank =
            resolveRank(
                currentRank = current.currentRank,
                candidateRank = candidateRank,
                mmr = mmr,
                answersSinceRankChange = nextAnswersSinceRankChange,
            )
        val rankChanged = rank != current.currentRank

        userRankRepository.upsert(
            current.copy(
                currentRank = rank,
                mmr = mmr,
                perfEwma = ewma,
                lifetimeCorrect = current.lifetimeCorrect + perf.correctAnswers,
                lifetimeIncorrect = current.lifetimeIncorrect + perf.incorrectAnswers,
                lifetimeTimeout = current.lifetimeTimeout + perf.timeoutAnswers,
                totalGameAnswers = nextTotalGameAnswers,
                totalStudyAnswers = nextTotalStudyAnswers,
                lastRankChangeAt = if (rankChanged) now else current.lastRankChangeAt,
                answersSinceRankChange = if (rankChanged) 0 else nextAnswersSinceRankChange,
                totalXp = current.totalXp + (perf.sessionPerformance * 100f).roundToLong(),
                updatedAt = now,
            ),
        )
    }

    private fun resolveRank(
        currentRank: UserRank,
        candidateRank: UserRank,
        mmr: Float,
        answersSinceRankChange: Int,
    ): UserRank {
        if (candidateRank == currentRank) return currentRank
        if (answersSinceRankChange < 10) return currentRank
        val hysteresis = 50f
        return if (candidateRank.ordinal > currentRank.ordinal) {
            val threshold = lowerBound(candidateRank) + hysteresis
            if (mmr >= threshold) candidateRank else currentRank
        } else {
            val threshold = lowerBound(currentRank) - hysteresis
            if (mmr < threshold) candidateRank else currentRank
        }
    }

    private fun lowerBound(rank: UserRank): Float =
        when (rank) {
            UserRank.INITIATE -> 0f
            UserRank.NEOPHYTE -> 800f
            UserRank.ACOLYTE -> 1100f
            UserRank.DISCIPLE -> 1400f
            UserRank.ADEPT -> 1700f
            UserRank.VIRTUOSO -> 2100f
            UserRank.ARCHON -> 2500f
            UserRank.PARAGON -> 3000f
        }

    private fun sigmoid(value: Float): Float = (1.0 / (1.0 + exp(-value.toDouble()))).toFloat()
}
