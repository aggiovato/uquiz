package com.uquiz.android.data.content.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.AuditTimestamps

@Entity(
    tableName = "options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("questionId")]
)
data class OptionEntity(
    @PrimaryKey
    val id: String,
    val questionId: String,
    val label: String,
    val text: String,
    val isCorrect: Boolean,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
