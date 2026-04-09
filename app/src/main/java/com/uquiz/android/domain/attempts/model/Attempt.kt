package com.uquiz.android.domain.attempts.model

import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.enums.AttemptStatus

/**
 * ### Attempt
 *
 * Modelo de dominio que representa una sesión de estudio o juego iniciada por
 * el usuario.
 *
 * Este agregado contiene el estado actual del intento y sus métricas base, y se
 * utiliza para gestionar el ciclo de vida de una sesión desde su inicio hasta su
 * finalización o abandono.
 *
 * Propiedades:
 * - [id]: identificador único del intento.
 * - [userId]: identificador del usuario propietario del intento.
 * - [mode]: modo en el que se ejecuta la sesión.
 * - [status]: estado actual del intento.
 * - [startedAt]: marca temporal de inicio del intento.
 * - [completedAt]: marca temporal de finalización, si existe.
 * - [durationMs]: duración total del intento en milisegundos, si está cerrada.
 * - [score]: puntuación acumulada del intento.
 * - [primaryPackId]: pack principal asociado a la sesión, si aplica.
 * - [totalQuestions]: cantidad total de preguntas previstas para el intento.
 * - [correctAnswers]: número de respuestas correctas acumuladas.
 * - [currentQuestionIndex]: índice de la pregunta actual dentro de la sesión.
 * - [createdAt]: marca temporal de creación del registro.
 * - [updatedAt]: marca temporal de la última actualización del registro.
 */
data class Attempt(
    val id: String,
    val userId: String,
    val mode: AttemptMode,
    val status: AttemptStatus,
    val startedAt: Long,
    val completedAt: Long? = null,
    val durationMs: Long? = null,
    val score: Int = 0,
    val primaryPackId: String? = null,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val currentQuestionIndex: Int = 0,
    val createdAt: Long,
    val updatedAt: Long,
)
