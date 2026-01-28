package com.uquiz.android.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.enums.AttemptMode

@Entity(tableName = "attempts")
data class AttemptEntity(
    @PrimaryKey
    val id: String,
    val mode: AttemptMode,
    val startedAt: Long,
    val completedAt: Long? = null,
    val durationMs: Long? = null,
    val score: Int = 0,
    val primaryPackId: String? = null,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
