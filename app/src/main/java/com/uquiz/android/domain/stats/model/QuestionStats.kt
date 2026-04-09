package com.uquiz.android.domain.stats.model

import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel

/**
 * ### QuestionStats
 *
 * Modelo persistido que representa la estadística acumulada de una pregunta para
 * un usuario concreto dentro de un pack.
 *
 * Este agregado resume el historial de intentos, aciertos, tiempos y rachas de
 * una pregunta, y además expone una valoración sintética del dominio alcanzado
 * mediante [masteryScore] y [masteryLevel].
 *
 * Propiedades:
 * - [id]: identificador único del registro estadístico.
 * - [userId]: identificador del usuario propietario de la estadística.
 * - [questionId]: identificador de la pregunta analizada.
 * - [packId]: identificador del pack al que pertenece la pregunta.
 * - [totalAttempts]: número total de respuestas registradas para la pregunta.
 * - [totalCorrect]: número total de respuestas correctas.
 * - [totalIncorrect]: número total de respuestas incorrectas.
 * - [totalTimeout]: número total de respuestas agotadas por tiempo.
 * - [studyAttempts]: número de intentos realizados en modo estudio.
 * - [studyCorrect]: número de aciertos en modo estudio.
 * - [gameAttempts]: número de intentos realizados en modo juego.
 * - [gameCorrect]: número de aciertos en modo juego.
 * - [avgGameTimeMs]: tiempo medio de respuesta en modo juego.
 * - [bestGameTimeMs]: mejor tiempo de respuesta en modo juego.
 * - [currentCorrectStreak]: racha actual de respuestas correctas consecutivas.
 * - [bestCorrectStreak]: mejor racha histórica de respuestas correctas.
 * - [masteryScore]: puntuación continua usada para estimar dominio.
 * - [masteryLevel]: nivel categórico de dominio calculado para la pregunta.
 * - [lastAnsweredAt]: marca temporal de la respuesta más reciente.
 * - [createdAt]: marca temporal de creación del registro.
 * - [updatedAt]: marca temporal de la última actualización del registro.
 */
data class QuestionStats(
    val id: String,
    val userId: String,
    val questionId: String,
    val packId: String,
    val totalAttempts: Int = 0,
    val totalCorrect: Int = 0,
    val totalIncorrect: Int = 0,
    val totalTimeout: Int = 0,
    val studyAttempts: Int = 0,
    val studyCorrect: Int = 0,
    val gameAttempts: Int = 0,
    val gameCorrect: Int = 0,
    val avgGameTimeMs: Long? = null,
    val bestGameTimeMs: Long? = null,
    val currentCorrectStreak: Int = 0,
    val bestCorrectStreak: Int = 0,
    val masteryScore: Float = 0f,
    val masteryLevel: QuestionMasteryLevel = QuestionMasteryLevel.NEW,
    val lastAnsweredAt: Long? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
