package com.uquiz.android.data.content.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Fila de la tabla de unión `pack_questions`. Asocia un pack con sus preguntas
 * y registra el [sortOrder] que determina el orden de presentación.
 */
@Entity(
    tableName = "pack_questions",
    primaryKeys = ["packId", "questionId"],
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("packId"), Index("questionId")]
)
data class PackQuestionEntity(
    val packId: String,
    val questionId: String,
    val sortOrder: Int
)
