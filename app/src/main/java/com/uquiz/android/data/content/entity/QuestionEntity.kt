package com.uquiz.android.data.content.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.domain.content.enums.DifficultyLevel

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val explanation: String? = null,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
