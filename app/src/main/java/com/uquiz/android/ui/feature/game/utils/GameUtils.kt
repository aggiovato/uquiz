package com.uquiz.android.ui.feature.game.utils

import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.QuestionWithOptions
import kotlin.math.roundToInt

/**
 * Calcula la dificultad media de una lista de preguntas.
 *
 * Utiliza la media aritmética de los ordinales de dificultad y redondea al nivel más cercano.
 *
 * @return [DifficultyLevel.MEDIUM] si la lista está vacía.
 */
internal fun computeAverageDifficulty(questions: List<QuestionWithOptions>): DifficultyLevel {
    if (questions.isEmpty()) return DifficultyLevel.MEDIUM
    val avgOrdinal = questions.map { it.question.difficulty.ordinal }.average()
    val levels = DifficultyLevel.entries
    return levels[avgOrdinal.roundToInt().coerceIn(0, levels.lastIndex)]
}

/**
 * Estima el tiempo total de partida en milisegundos sumando el tiempo base por dificultad
 * de cada pregunta más un tiempo de lectura proporcional al número de palabras del enunciado
 * y las opciones (~250 ms/palabra, máx. 8 s extra por pregunta).
 *
 * @return 0 si la lista está vacía.
 */
internal fun computeExpectedPlayTime(questions: List<QuestionWithOptions>): Long =
    questions.sumOf { questionBaseEstimateMs(it.question.difficulty) + questionReadingTimeMs(it) }

/**
 * Formatea una duración en milisegundos como cadena "mm:ss".
 *
 * @param durationMs Duración en milisegundos.
 * @return Texto con formato "mm:ss", p. ej. "03:45".
 */
internal fun formatGameDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

/**
 * Formatea una duración esperada de partida en un texto compacto con minutos y segundos.
 *
 * Ejemplos: "~2m 30s", "~45s", "~1m".
 *
 * @param durationMs Duración estimada en milisegundos.
 * @return Cadena legible con el tiempo aproximado.
 */
internal fun formatExpectedPlayTime(durationMs: Long): String {
    val totalSeconds = (durationMs / 1000).coerceAtLeast(1)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return when {
        minutes > 0 && seconds > 0 -> "~${minutes}m ${seconds}s"
        minutes > 0 -> "~${minutes}m"
        else -> "~${seconds}s"
    }
}

private fun questionBaseEstimateMs(difficulty: DifficultyLevel): Long = when (difficulty) {
    DifficultyLevel.EASY -> 15_000L
    DifficultyLevel.MEDIUM -> 20_000L
    DifficultyLevel.HARD -> 25_000L
    DifficultyLevel.EXPERT -> 30_000L
}

// ~250 ms por palabra; limitado a 8 s por pregunta para no disparar el total.
private fun questionReadingTimeMs(qwo: QuestionWithOptions): Long {
    val wordCount = qwo.question.text.trim().split(Regex("\\s+")).size +
        qwo.options.sumOf { it.text.trim().split(Regex("\\s+")).size }
    return (wordCount * 250L).coerceAtMost(8_000L)
}
