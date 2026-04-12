package com.uquiz.android.core.game.usecase

import com.uquiz.android.core.game.model.GameAnswerInput
import com.uquiz.android.core.game.model.GameScoreResult
import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * Calcula el score visible de una sesión de Game mode a partir de las respuestas del usuario.
 *
 * El score por pregunta se determina por corrección, velocidad y dificultad.
 * El score de sesión es la suma directa de los scores individuales y puede ser negativo.
 *
 * Esta función es pura: no tiene efectos secundarios ni accede a fuentes de datos externas.
 *
 * Constantes:
 * - `CORRECT_BASE = 10`  — puntos base por respuesta correcta
 * - `INCORRECT_PENALTY = 5` — penalización por respuesta incorrecta
 * - `TIMEOUT_PENALTY = 8`   — penalización por tiempo agotado (mayor que fallo normal)
 */
class ComputeGameScoreUseCase {
    /** Calcula el score de la sesión a partir de la lista de respuestas. */
    operator fun invoke(answers: List<GameAnswerInput>): GameScoreResult {
        val questionScores =
            answers.map { answer ->
                val difficultyWeight = answer.difficulty.toWeight()
                val isTimeout = answer.timeLimitMs != null && answer.timeMs >= answer.timeLimitMs

                val score =
                    when {
                        isTimeout -> {
                            -(TIMEOUT_PENALTY * difficultyWeight)
                        }

                        answer.isCorrect -> {
                            val speedScore =
                                answer.timeLimitMs
                                    ?.takeIf { it > 0 }
                                    ?.let { limit ->
                                        (1f - (answer.timeMs.toFloat() / limit.toFloat())).coerceIn(0f, 1f)
                                    }
                                    ?: 1f
                            // speedMultiplier ∈ [0.5, 2.0]: lento → ×0.5, rápido → ×2.0
                            val speedMultiplier = 0.5f + speedScore * 1.5f
                            CORRECT_BASE * speedMultiplier * difficultyWeight
                        }

                        else -> {
                            -(INCORRECT_PENALTY * difficultyWeight)
                        }
                    }
                score.toInt()
            }

        return GameScoreResult(
            questionScores = questionScores,
            sessionScore = questionScores.sum(),
        )
    }

    private fun DifficultyLevel.toWeight(): Float =
        when (this) {
            DifficultyLevel.EASY -> 0.90f
            DifficultyLevel.MEDIUM -> 1.00f
            DifficultyLevel.HARD -> 1.15f
            DifficultyLevel.EXPERT -> 1.30f
        }

    private companion object {
        const val CORRECT_BASE = 10f
        const val INCORRECT_PENALTY = 5f
        const val TIMEOUT_PENALTY = 8f
    }
}
