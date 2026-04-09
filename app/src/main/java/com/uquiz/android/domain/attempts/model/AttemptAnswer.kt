package com.uquiz.android.domain.attempts.model

/**
 * ### AttemptAnswer
 *
 * Modelo de dominio que representa una respuesta concreta dada por el usuario
 * dentro de un intento.
 *
 * Este modelo registra el resultado de una pregunta respondida durante una sesión,
 * incluyendo la opción elegida, si la respuesta fue correcta y el tiempo empleado.
 *
 * Propiedades:
 * - [id]: identificador único de la respuesta registrada.
 * - [attemptId]: identificador del intento al que pertenece.
 * - [questionId]: identificador de la pregunta respondida.
 * - [pickedOptionId]: identificador de la opción elegida, si existe.
 * - [isCorrect]: indica si la respuesta fue correcta.
 * - [timeMs]: tiempo empleado en responder.
 * - [timeLimitMs]: límite de tiempo configurado para la pregunta, si existe.
 * - [createdAt]: marca temporal de creación del registro.
 * - [updatedAt]: marca temporal de la última actualización del registro.
 */
data class AttemptAnswer(
    val id: String,
    val attemptId: String,
    val questionId: String,
    val pickedOptionId: String? = null,
    val isCorrect: Boolean,
    val timeMs: Long,
    val timeLimitMs: Long? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
