package com.uquiz.android.data.attempts.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * Fila de la tabla `attempt_question_plan`. Registra el orden planificado de preguntas
 * para un intento, incluyendo el límite de tiempo asignado a cada slot.
 */
@Entity(
    tableName = "attempt_question_plan",
    primaryKeys = ["attemptId", "questionId"],
    indices = [Index("attemptId")],
)
data class AttemptQuestionPlanEntity(
    val attemptId: String,
    val questionId: String,
    val orderIndex: Int,
    val timeLimitMs: Long,
)
