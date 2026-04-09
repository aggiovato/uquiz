package com.uquiz.android.domain.stats.projection

/**
 * ### UserStatsSnapshot
 *
 * Modelo de proyección que representa una instantánea global de las estadísticas
 * del usuario.
 *
 * Esta estructura resume indicadores transversales de actividad y rendimiento
 * para alimentar dashboards, cabeceras y vistas generales del perfil del usuario.
 *
 * Propiedades:
 * - [dayStreak]: racha actual de días con actividad registrada.
 * - [totalPoints]: total de puntos acumulados por el usuario.
 * - [completedSessions]: número de sesiones completadas.
 * - [accuracyPercent]: precisión global del usuario cuando está disponible.
 * - [totalCorrect]: número total de respuestas correctas.
 * - [totalAnswered]: número total de respuestas contestadas.
 */
data class UserStatsSnapshot(
    val dayStreak: Int = 0,
    val totalPoints: Long = 0L,
    val completedSessions: Int = 0,
    val accuracyPercent: Int? = null,
    val totalCorrect: Int = 0,
    val totalAnswered: Int = 0,
)
