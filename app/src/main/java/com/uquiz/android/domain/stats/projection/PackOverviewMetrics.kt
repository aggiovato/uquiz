package com.uquiz.android.domain.stats.projection

/**
 * ### PackOverviewMetrics
 *
 * Modelo de proyección reducido para mostrar métricas clave de un pack en vistas
 * de resumen, listas o tarjetas.
 *
 * Esta estructura concentra solo los indicadores necesarios para una visión rápida
 * del estado y rendimiento del pack sin requerir el detalle completo de estadísticas.
 *
 * Propiedades:
 * - [questionCount]: cantidad total de preguntas del pack.
 * - [accuracyPercent]: precisión acumulada mostrable en porcentaje.
 * - [sessionsCount]: número de sesiones registradas cuando existe.
 * - [progressPercent]: porcentaje de progreso del pack cuando está disponible.
 * - [dominatedQuestions]: número de preguntas dominadas.
 * - [totalQuestions]: total de preguntas usado para contextualizar el progreso.
 */
data class PackOverviewMetrics(
    val questionCount: Int = 0,
    val accuracyPercent: Int? = null,
    val sessionsCount: Int? = null,
    val progressPercent: Int? = null,
    val dominatedQuestions: Int = 0,
    val totalQuestions: Int = 0,
)
