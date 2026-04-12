package com.uquiz.android.data.content.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * Fila de la tabla `questions`. Representa una pregunta de opción múltiple.
 * Sus opciones de respuesta se almacenan en `options` (relación 1-N).
 *
 * @see com.uquiz.android.domain.content.model.Question
 */
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val explanation: String? = null,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
