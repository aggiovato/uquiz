package com.uquiz.android.domain.stats.model

import com.uquiz.android.domain.attempts.enums.AttemptMode

/**
 * ### PackStats
 *
 * Modelo persistido que representa el agregado estadístico acumulado de un pack
 * para un usuario concreto.
 *
 * Este modelo consolida métricas de rendimiento, uso y progreso calculadas a
 * partir del historial de sesiones del pack, separando además los valores
 * específicos de estudio y juego cuando corresponde.
 *
 * Propiedades:
 * - [id]: identificador único del registro estadístico.
 * - [userId]: identificador del usuario propietario de la estadística.
 * - [packId]: identificador del pack al que pertenece la agregación.
 * - [totalSessions]: número total de sesiones completadas sobre el pack.
 * - [totalStudySessions]: número de sesiones completadas en modo estudio.
 * - [totalGameSessions]: número de sesiones completadas en modo juego.
 * - [averageAccuracyPercent]: precisión media global del pack en porcentaje.
 * - [averageStudyAccuracyPercent]: precisión media en sesiones de estudio.
 * - [averageGameAccuracyPercent]: precisión media en sesiones de juego.
 * - [averageDurationMs]: duración media global de las sesiones del pack.
 * - [averageStudyDurationMs]: duración media de las sesiones de estudio.
 * - [averageGameDurationMs]: duración media de las sesiones de juego.
 * - [bestScore]: mejor puntuación global registrada para el pack.
 * - [bestStudyAccuracyPercent]: mejor precisión lograda en modo estudio.
 * - [bestGameScore]: mejor puntuación lograda en modo juego.
 * - [lastSessionAt]: marca temporal de la sesión más reciente.
 * - [lastSessionMode]: modo de la sesión más reciente.
 * - [mostUsedMode]: modo predominante según el historial del pack.
 * - [dominatedQuestions]: cantidad de preguntas consideradas dominadas.
 * - [totalQuestionsSnapshot]: número total de preguntas usado para el cálculo actual.
 * - [progressPercent]: progreso global del pack expresado como porcentaje.
 * - [createdAt]: marca temporal de creación del registro.
 * - [updatedAt]: marca temporal de la última actualización del registro.
 */
data class PackStats(
    val id: String,
    val userId: String,
    val packId: String,
    val totalSessions: Int = 0,
    val totalStudySessions: Int = 0,
    val totalGameSessions: Int = 0,
    val averageAccuracyPercent: Int? = null,
    val averageStudyAccuracyPercent: Int? = null,
    val averageGameAccuracyPercent: Int? = null,
    val averageDurationMs: Long? = null,
    val averageStudyDurationMs: Long? = null,
    val averageGameDurationMs: Long? = null,
    val bestScore: Int? = null,
    val bestStudyAccuracyPercent: Int? = null,
    val bestGameScore: Int? = null,
    val lastSessionAt: Long? = null,
    val lastSessionMode: AttemptMode? = null,
    val mostUsedMode: AttemptMode? = null,
    val dominatedQuestions: Int = 0,
    val totalQuestionsSnapshot: Int = 0,
    val progressPercent: Int = 0,
    val createdAt: Long,
    val updatedAt: Long,
)
