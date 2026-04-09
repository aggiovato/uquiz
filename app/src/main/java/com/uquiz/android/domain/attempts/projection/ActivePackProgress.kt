package com.uquiz.android.domain.attempts.projection

import com.uquiz.android.domain.attempts.enums.AttemptMode

/**
 * ### ActivePackProgress
 *
 * Modelo de proyección que representa el progreso activo de un pack dentro de un
 * intento en curso.
 *
 * Esta estructura se utiliza para mostrar sesiones abiertas y su avance acumulado
 * sin necesidad de cargar el detalle completo del intento y sus respuestas.
 *
 * Propiedades:
 * - [attemptId]: identificador del intento activo.
 * - [packId]: identificador del pack asociado al intento.
 * - [mode]: modo en el que se está ejecutando el intento.
 * - [answeredCount]: número de preguntas ya respondidas.
 * - [startedAt]: marca temporal de inicio del intento activo.
 */
data class ActivePackProgress(
    val attemptId: String,
    val packId: String,
    val mode: AttemptMode,
    val answeredCount: Int,
    val startedAt: Long,
)
