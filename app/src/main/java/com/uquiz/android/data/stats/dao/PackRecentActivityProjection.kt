package com.uquiz.android.data.stats.dao

import com.uquiz.android.domain.attempts.enums.AttemptMode

data class PackRecentActivityProjection(
    val attemptId: String,
    val mode: AttemptMode,
    val completedAt: Long,
    val durationMs: Long?,
    val score: Int?,
    val correctAnswers: Int,
    val totalQuestions: Int
)
