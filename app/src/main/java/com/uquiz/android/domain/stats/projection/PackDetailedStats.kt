package com.uquiz.android.domain.stats.projection

import com.uquiz.android.domain.attempts.enums.AttemptMode

/**
 * ### PackDetailedStats
 *
 * Modelo de proyección enriquecido que agrupa la visión detallada de estadísticas
 * de un pack lista para consumo en UI.
 *
 * Esta proyección combina métricas globales, métricas por modo, actividad reciente
 * y mejor rendimiento en una única estructura de lectura orientada a pantallas de
 * detalle y resúmenes avanzados.
 *
 * Propiedades:
 * - [packId]: identificador del pack al que pertenecen las estadísticas.
 * - [totalSessions]: número total de sesiones registradas.
 * - [averageAccuracyPercent]: precisión media global del pack.
 * - [averageDurationMs]: duración media global de las sesiones.
 * - [progressPercent]: porcentaje de progreso acumulado del pack.
 * - [dominatedQuestions]: número de preguntas dominadas.
 * - [totalQuestions]: total de preguntas consideradas en la proyección.
 * - [lastSessionAt]: marca temporal de la sesión más reciente.
 * - [lastSessionMode]: modo de la sesión más reciente.
 * - [mostUsedMode]: modo predominante en el historial del pack.
 * - [studyStats]: métricas agregadas específicas del modo estudio.
 * - [gameStats]: métricas agregadas específicas del modo juego.
 * - [recentActivity]: lista resumida de actividad reciente del pack.
 * - [bestPerformance]: mejor rendimiento destacado cuando existe.
 */
data class PackDetailedStats(
    val packId: String,
    val totalSessions: Int = 0,
    val averageAccuracyPercent: Int? = null,
    val averageDurationMs: Long? = null,
    val progressPercent: Int = 0,
    val dominatedQuestions: Int = 0,
    val totalQuestions: Int = 0,
    val lastSessionAt: Long? = null,
    val lastSessionMode: AttemptMode? = null,
    val mostUsedMode: AttemptMode? = null,
    val studyStats: PackModeStats = PackModeStats(AttemptMode.STUDY),
    val gameStats: PackModeStats = PackModeStats(AttemptMode.GAME),
    val recentActivity: List<PackRecentActivity> = emptyList(),
    val bestPerformance: PackBestPerformance? = null,
)
