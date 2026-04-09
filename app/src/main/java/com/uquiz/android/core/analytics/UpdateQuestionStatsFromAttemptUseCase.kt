package com.uquiz.android.core.analytics

import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.model.Attempt
import com.uquiz.android.domain.attempts.model.AttemptAnswer
import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel
import com.uquiz.android.domain.stats.model.QuestionStats
import com.uquiz.android.domain.stats.repository.QuestionStatsRepository

class UpdateQuestionStatsFromAttemptUseCase(
    private val questionStatsRepository: QuestionStatsRepository
) {
    suspend operator fun invoke(
        attempt: Attempt,
        answers: List<AttemptAnswer>
    ) {
        val packId = attempt.primaryPackId ?: return
        val now = attempt.completedAt ?: System.currentTimeMillis()

        answers.forEach { answer ->
            val current = questionStatsRepository.getByQuestion(answer.questionId)
            val isTimeout = answer.timeLimitMs?.let { answer.timeMs >= it } == true
            val totalAttempts = (current?.totalAttempts ?: 0) + 1
            val totalCorrect = (current?.totalCorrect ?: 0) + if (answer.isCorrect) 1 else 0
            val totalIncorrect = (current?.totalIncorrect ?: 0) + if (!answer.isCorrect && !isTimeout) 1 else 0
            val totalTimeout = (current?.totalTimeout ?: 0) + if (isTimeout) 1 else 0
            val studyAttempts = (current?.studyAttempts ?: 0) + if (attempt.mode == AttemptMode.STUDY) 1 else 0
            val studyCorrect = (current?.studyCorrect ?: 0) + if (attempt.mode == AttemptMode.STUDY && answer.isCorrect) 1 else 0
            val gameAttempts = (current?.gameAttempts ?: 0) + if (attempt.mode == AttemptMode.GAME) 1 else 0
            val gameCorrect = (current?.gameCorrect ?: 0) + if (attempt.mode == AttemptMode.GAME && answer.isCorrect) 1 else 0
            val currentStreak = if (answer.isCorrect) (current?.currentCorrectStreak ?: 0) + 1 else 0
            val bestStreak = maxOf(current?.bestCorrectStreak ?: 0, currentStreak)
            val avgGameTimeMs = if (attempt.mode == AttemptMode.GAME) {
                val previousAttempts = current?.gameAttempts ?: 0
                val previousAverage = current?.avgGameTimeMs ?: 0L
                (((previousAverage * previousAttempts) + answer.timeMs) / (previousAttempts + 1)).coerceAtLeast(0L)
            } else {
                current?.avgGameTimeMs
            }
            val bestGameTimeMs = if (attempt.mode == AttemptMode.GAME) {
                minOf(current?.bestGameTimeMs ?: Long.MAX_VALUE, answer.timeMs)
                    .takeUnless { it == Long.MAX_VALUE }
            } else {
                current?.bestGameTimeMs
            }
            val masteryScore = computeMasteryScore(
                totalAttempts = totalAttempts,
                totalCorrect = totalCorrect,
                gameAttempts = gameAttempts,
                currentCorrectStreak = currentStreak
            )
            val masteryLevel = masteryLevelFor(
                masteryScore = masteryScore,
                totalAttempts = totalAttempts,
                totalCorrect = totalCorrect,
                gameAttempts = gameAttempts,
                studyAttempts = studyAttempts
            )

            questionStatsRepository.upsert(
                QuestionStats(
                    id = current?.id ?: "${attempt.userId}:${answer.questionId}",
                    userId = attempt.userId,
                    questionId = answer.questionId,
                    packId = current?.packId ?: packId,
                    totalAttempts = totalAttempts,
                    totalCorrect = totalCorrect,
                    totalIncorrect = totalIncorrect,
                    totalTimeout = totalTimeout,
                    studyAttempts = studyAttempts,
                    studyCorrect = studyCorrect,
                    gameAttempts = gameAttempts,
                    gameCorrect = gameCorrect,
                    avgGameTimeMs = avgGameTimeMs,
                    bestGameTimeMs = bestGameTimeMs,
                    currentCorrectStreak = currentStreak,
                    bestCorrectStreak = bestStreak,
                    masteryScore = masteryScore,
                    masteryLevel = masteryLevel,
                    lastAnsweredAt = now,
                    createdAt = current?.createdAt ?: now,
                    updatedAt = now
                )
            )
        }
    }

    private fun computeMasteryScore(
        totalAttempts: Int,
        totalCorrect: Int,
        gameAttempts: Int,
        currentCorrectStreak: Int
    ): Float {
        if (totalAttempts == 0) return 0f
        val accuracy = totalCorrect.toFloat() / totalAttempts
        val confidence = (totalAttempts / 5f).coerceAtMost(1f)
        val gameBonus = if (gameAttempts > 0) 0.08f else 0f
        val streakBonus = (currentCorrectStreak * 0.03f).coerceAtMost(0.12f)
        return ((accuracy * 0.72f) + (confidence * 0.20f) + gameBonus + streakBonus).coerceIn(0f, 1f)
    }

    private fun masteryLevelFor(
        masteryScore: Float,
        totalAttempts: Int,
        totalCorrect: Int,
        gameAttempts: Int,
        studyAttempts: Int
    ): QuestionMasteryLevel {
        val evidenceSatisfied = totalAttempts >= 3 &&
            totalCorrect >= 2 &&
            (gameAttempts >= 1 || studyAttempts >= 2)
        return when {
            masteryScore >= 0.80f && evidenceSatisfied -> QuestionMasteryLevel.MASTERED
            masteryScore >= 0.50f -> QuestionMasteryLevel.PRACTICED
            masteryScore >= 0.20f -> QuestionMasteryLevel.LEARNING
            else -> QuestionMasteryLevel.NEW
        }
    }
}
