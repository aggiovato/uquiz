package com.uquiz.android.core.analytics.usecase

import com.uquiz.android.core.analytics.model.SessionPerformanceSnapshot
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.model.Attempt
import com.uquiz.android.domain.attempts.model.AttemptAnswer
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.QuestionRepository
import kotlin.math.roundToInt

class ComputeSessionPerformanceUseCase(
    private val questionRepository: QuestionRepository,
) {
    suspend operator fun invoke(
        attempt: Attempt,
        answers: List<AttemptAnswer>,
    ): SessionPerformanceSnapshot {
        if (answers.isEmpty()) {
            return SessionPerformanceSnapshot(
                sessionPerformance = 0f,
                totalAnswers = 0,
                correctAnswers = 0,
                incorrectAnswers = 0,
                timeoutAnswers = 0,
                normalizedScore = 0,
            )
        }

        val values = mutableListOf<Float>()
        var correct = 0
        var incorrect = 0
        var timeout = 0

        for (answer in answers) {
            val difficulty = questionRepository.getById(answer.questionId)?.difficulty ?: DifficultyLevel.MEDIUM
            val difficultyWeight = difficultyWeight(difficulty)
            val isTimeout = answer.timeLimitMs?.let { answer.timeMs >= it } == true
            if (answer.isCorrect) correct += 1 else incorrect += 1
            if (isTimeout) timeout += 1

            val pQuestion =
                when (attempt.mode) {
                    AttemptMode.GAME -> {
                        val speedScore =
                            answer.timeLimitMs
                                ?.takeIf { it > 0 }
                                ?.let { limit ->
                                    (1f - (answer.timeMs.toFloat() / limit).coerceIn(0f, 1f)).coerceIn(0f, 1f)
                                }
                                ?: if (answer.isCorrect) 0.5f else 0f
                        val base =
                            difficultyWeight * ((0.70f * if (answer.isCorrect) 1f else 0f) + (0.30f * speedScore))
                        if (answer.isCorrect) base else base - (0.10f * difficultyWeight)
                    }

                    AttemptMode.STUDY -> {
                        difficultyWeight * (0.85f * if (answer.isCorrect) 1f else 0f) * 0.35f
                    }
                }.coerceIn(0f, 1.2f)

            values += pQuestion
        }

        val performance = values.average().toFloat().coerceIn(0f, 1.2f)
        return SessionPerformanceSnapshot(
            sessionPerformance = performance,
            totalAnswers = answers.size,
            correctAnswers = correct,
            incorrectAnswers = incorrect,
            timeoutAnswers = timeout,
            normalizedScore = (performance * 100f).roundToInt(),
        )
    }

    private fun difficultyWeight(difficulty: DifficultyLevel): Float =
        when (difficulty) {
            DifficultyLevel.EASY -> 0.90f
            DifficultyLevel.MEDIUM -> 1.00f
            DifficultyLevel.HARD -> 1.15f
            DifficultyLevel.EXPERT -> 1.30f
        }
}
