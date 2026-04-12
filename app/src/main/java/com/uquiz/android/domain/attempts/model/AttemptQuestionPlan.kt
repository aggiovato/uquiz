package com.uquiz.android.domain.attempts.model

/**
 * ### AttemptQuestionPlan
 *
 * Representa un slot del plan de preguntas para una sesión de Game mode.
 * Cada instancia corresponde a una pregunta dentro del intento, con su posición
 * en el orden de presentación y el límite de tiempo pre-calculado.
 *
 * Este modelo se persiste para permitir la reanudación real de una sesión sin
 * recalcular el plan de tiempos desde cero.
 *
 * @param attemptId   Identificador del intento al que pertenece este slot.
 * @param questionId  Identificador de la pregunta asignada a esta posición.
 * @param orderIndex  Posición (0-based) de la pregunta dentro de la sesión.
 * @param timeLimitMs Tiempo límite pre-calculado para responder esta pregunta, en milisegundos.
 */
data class AttemptQuestionPlan(
    val attemptId: String,
    val questionId: String,
    val orderIndex: Int,
    val timeLimitMs: Long,
)
