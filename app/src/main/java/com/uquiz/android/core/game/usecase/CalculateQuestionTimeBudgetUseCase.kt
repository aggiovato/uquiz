package com.uquiz.android.core.game.usecase

import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.QuestionWithOptions
import com.uquiz.android.domain.stats.model.QuestionStats

/**
 * Calcula el tiempo límite en milisegundos asignado a una pregunta en modo Game.
 *
 * La fórmula combina una base según dificultad, un bono por longitud de texto y,
 * si existe historial, un blend ponderado con el tiempo promedio histórico del usuario
 * en esa pregunta (con un margen del 50 %). El resultado se acota siempre en [5 s, 60 s].
 *
 * Esta función es pura: no tiene efectos secundarios ni accede a fuentes de datos externas.
 */
class CalculateQuestionTimeBudgetUseCase {

    /**
     * @param questionWithOptions Pregunta con sus opciones, usada para calcular longitud total del texto.
     * @param stats               Estadísticas históricas de la pregunta para el usuario, o null si es nueva.
     * @return Tiempo límite en milisegundos.
     */
    operator fun invoke(
        questionWithOptions: QuestionWithOptions,
        stats: QuestionStats?,
    ): Long {
        val baseDurationMs = questionWithOptions.question.difficulty.toBaseMs()

        val totalChars = questionWithOptions.question.text.length +
            questionWithOptions.options.sumOf { it.text.length }
        // +1 s por cada 100 caracteres adicionales, tope de 15 s
        val lengthBonusMs = ((totalChars / 100) * 1_000L).coerceAtMost(15_000L)

        val calculatedMs = baseDurationMs + lengthBonusMs

        val timeLimitMs = stats?.avgGameTimeMs?.let { avgMs ->
            // 40 % calculado + 60 % del promedio histórico con +50 % de margen de holgura
            (calculatedMs * 0.40 + avgMs * 1.50 * 0.60).toLong()
        } ?: calculatedMs

        return timeLimitMs.coerceIn(MIN_MS, MAX_MS)
    }

    private fun DifficultyLevel.toBaseMs(): Long = when (this) {
        DifficultyLevel.EASY -> 15_000L
        DifficultyLevel.MEDIUM -> 20_000L
        DifficultyLevel.HARD -> 25_000L
        DifficultyLevel.EXPERT -> 30_000L
    }

    private companion object {
        const val MIN_MS = 5_000L
        const val MAX_MS = 60_000L
    }
}
