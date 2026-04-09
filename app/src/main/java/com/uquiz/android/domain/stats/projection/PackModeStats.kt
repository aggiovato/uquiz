package com.uquiz.android.domain.stats.projection

import com.uquiz.android.domain.attempts.enums.AttemptMode

/**
 * ### PackModeStats
 *
 * Modelo de proyección que resume las métricas agregadas de un pack para un modo
 * concreto de uso.
 *
 * Se utiliza como subestructura dentro de proyecciones más grandes cuando se
 * necesita comparar el comportamiento de estudio y juego de forma separada.
 *
 * Propiedades:
 * - [mode]: modo al que pertenecen las métricas.
 * - [sessions]: número de sesiones registradas en ese modo.
 * - [accuracyPercent]: precisión media en porcentaje para ese modo.
 * - [averageDurationMs]: duración media de las sesiones de ese modo.
 */
data class PackModeStats(
    val mode: AttemptMode,
    val sessions: Int = 0,
    val accuracyPercent: Int? = null,
    val averageDurationMs: Long? = null,
)
