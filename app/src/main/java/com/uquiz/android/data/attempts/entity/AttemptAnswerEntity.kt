package com.uquiz.android.data.attempts.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.content.entity.OptionEntity
import com.uquiz.android.data.content.entity.QuestionEntity
import com.uquiz.android.data.local.db.AuditTimestamps

@Entity(
    tableName = "attempt_answers",
    foreignKeys = [
        ForeignKey(
            entity = AttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = OptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["pickedOptionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("attemptId"), Index("questionId"), Index("pickedOptionId")]
)
data class AttemptAnswerEntity(
    @PrimaryKey
    val id: String,
    val attemptId: String,
    val questionId: String,
    val pickedOptionId: String? = null,
    val isCorrect: Boolean,
    val timeMs: Long,
    val timeLimitMs: Long? = null,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
