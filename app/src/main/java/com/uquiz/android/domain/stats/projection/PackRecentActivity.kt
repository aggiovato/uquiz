package com.uquiz.android.domain.stats.projection

import com.uquiz.android.domain.attempts.enums.AttemptMode

/**
 * ### PackRecentActivity
 *
 * Modelo de proyección que representa una entrada reciente del historial de un
 * pack.
 *
 * Esta estructura resulta útil para construir timelines o listados de actividad
 * reciente sin depender del modelo completo de intento.
 *
 * Propiedades:
 * - [attemptId]: identificador del intento al que pertenece la actividad.
 * - [mode]: modo en el que se completó el intento.
 * - [completedAt]: marca temporal de finalización.
 * - [durationMs]: duración del intento cuando está disponible.
 * - [score]: puntuación lograda cuando aplica.
 * - [accuracyPercent]: precisión obtenida en porcentaje cuando existe.
 */
data class PackRecentActivity(
    val attemptId: String,
    val mode: AttemptMode,
    val completedAt: Long,
    val durationMs: Long? = null,
    val score: Int? = null,
    val accuracyPercent: Int? = null,
)
