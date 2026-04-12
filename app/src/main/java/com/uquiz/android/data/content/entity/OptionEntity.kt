package com.uquiz.android.data.content.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.model.AuditTimestamps

/**
 * Fila de la tabla `options`. Representa una opción de respuesta para una pregunta.
 * Solo una opción por pregunta tiene [isCorrect] = `true`.
 *
 * @see com.uquiz.android.domain.content.model.Option
 */
@Entity(
    tableName = "options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("questionId")],
)
data class OptionEntity(
    @PrimaryKey
    val id: String,
    val questionId: String,
    val label: String,
    val text: String,
    val isCorrect: Boolean,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
