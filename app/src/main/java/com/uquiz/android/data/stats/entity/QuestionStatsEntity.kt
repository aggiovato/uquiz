package com.uquiz.android.data.stats.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.content.entity.QuestionEntity
import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel

@Entity(
    tableName = "question_stats",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("questionId"), Index("packId")]
)
data class QuestionStatsEntity(
    @PrimaryKey
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
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
