package com.uquiz.android.domain.stats.projection

import com.uquiz.android.domain.attempts.enums.AttemptMode

/**
 * ### PackBestPerformance
 *
 * Modelo de proyección que representa el mejor resultado destacado de un pack
 * para un modo concreto.
 *
 * Esta proyección se utiliza cuando la capa de presentación necesita resumir el
 * mejor rendimiento alcanzado sin exponer el detalle completo de todas las
 * sesiones registradas.
 *
 * Propiedades:
 * - [mode]: modo al que corresponde el mejor rendimiento.
 * - [scoreLabel]: texto listo para mostrar que resume la métrica principal.
 * - [numericScore]: valor numérico asociado cuando existe una puntuación comparable.
 */
data class PackBestPerformance(
    val mode: AttemptMode,
    val scoreLabel: String,
    val numericScore: Int? = null,
)
