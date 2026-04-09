package com.uquiz.android.domain.stats.projection

/**
 * ### PackPracticeStats
 *
 * Modelo de proyección compacto que resume la práctica acumulada de un pack.
 *
 * Esta estructura se usa cuando la interfaz solo necesita exponer el volumen de
 * sesiones y la precisión asociada, sin cargar el resto de métricas del pack.
 *
 * Propiedades:
 * - [sessionsCount]: número de sesiones registradas para el contexto consultado.
 * - [accuracyPercent]: precisión media asociada a esas sesiones.
 */
data class PackPracticeStats(
    val sessionsCount: Int? = null,
    val accuracyPercent: Int? = null,
)
