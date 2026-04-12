package com.uquiz.android.core.game.model

import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * Entrada de datos de una respuesta para el cálculo del score visible del Game mode.
 *
 * @param isCorrect   Si la opción seleccionada es correcta.
 * @param timeMs      Tiempo real empleado en responder, en milisegundos.
 * @param timeLimitMs Tiempo límite asignado a la pregunta, o null si no se aplicó límite.
 * @param difficulty  Nivel de dificultad de la pregunta.
 */
data class GameAnswerInput(
    val isCorrect: Boolean,
    val timeMs: Long,
    val timeLimitMs: Long?,
    val difficulty: DifficultyLevel,
)
