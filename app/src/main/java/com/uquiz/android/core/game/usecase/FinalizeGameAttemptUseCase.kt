package com.uquiz.android.core.game.usecase

import com.uquiz.android.core.analytics.usecase.FinalizeAttemptAnalyticsUseCase
import com.uquiz.android.core.game.model.GameAnswerInput
import com.uquiz.android.core.game.model.GameFinalizationResult
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.QuestionRepository
import com.uquiz.android.domain.ranking.repository.UserRankRepository

/**
 * Finaliza una sesión de Game mode: calcula el score visible, completa el intento,
 * actualiza analíticas y ranking, elimina el plan de preguntas y devuelve el resultado.
 *
 * Secuencia:
 * 1. Lee las respuestas y determina el score visible del juego.
 * 2. Marca el intento como completado con el score calculado.
 * 3. Ejecuta el pipeline de analíticas (stats + MMR + XP).
 * 4. Elimina el plan de preguntas persistido.
 * 5. Devuelve el resultado con el cambio de rango y XP ganada.
 */
class FinalizeGameAttemptUseCase(
    private val attemptRepository: AttemptRepository,
    private val questionRepository: QuestionRepository,
    private val userRankRepository: UserRankRepository,
    private val computeGameScoreUseCase: ComputeGameScoreUseCase,
    private val finalizeAttemptAnalyticsUseCase: FinalizeAttemptAnalyticsUseCase,
) {
    /**
     * @param attemptId  Identificador del intento a finalizar.
     * @param durationMs Duración real de la sesión en milisegundos, o null para calcularla automáticamente.
     * @return [GameFinalizationResult] con el score, XP ganada y cambio de rango.
     */
    suspend operator fun invoke(
        attemptId: String,
        durationMs: Long? = null,
    ): GameFinalizationResult {
        val answers = attemptRepository.getAnswers(attemptId)
        val previousRankState = userRankRepository.getCurrent()

        // Construye entradas para el cálculo de score visible
        val gameAnswerInputs =
            answers.map { answer ->
                val difficulty =
                    questionRepository.getById(answer.questionId)?.difficulty
                        ?: DifficultyLevel.MEDIUM
                GameAnswerInput(
                    isCorrect = answer.isCorrect,
                    timeMs = answer.timeMs,
                    timeLimitMs = answer.timeLimitMs,
                    difficulty = difficulty,
                )
            }

        val scoreResult = computeGameScoreUseCase(gameAnswerInputs)
        val correctAnswers = answers.count { it.isCorrect }

        // Completa el intento con el score visible del juego
        attemptRepository.completeAttempt(
            attemptId = attemptId,
            score = scoreResult.sessionScore,
            correctAnswers = correctAnswers,
            totalQuestions = answers.size,
            durationMs = durationMs,
        )

        // Actualiza estadísticas de preguntas, del pack y el ranking del usuario
        finalizeAttemptAnalyticsUseCase(attemptId)

        val currentRankState = userRankRepository.getCurrent()

        // Limpia el plan: ya no es necesario para reanudar
        attemptRepository.deleteQuestionPlan(attemptId)

        return GameFinalizationResult(
            sessionScore = scoreResult.sessionScore,
            xpGained = (currentRankState.totalXp - previousRankState.totalXp).coerceAtLeast(0L),
            previousRank = previousRankState.currentRank,
            currentRank = currentRankState.currentRank,
            rankChanged = currentRankState.currentRank != previousRankState.currentRank,
            previousMmr = previousRankState.mmr,
        )
    }
}
