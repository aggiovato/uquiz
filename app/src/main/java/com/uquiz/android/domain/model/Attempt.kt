package com.uquiz.android.domain.model

import com.uquiz.android.data.local.enums.AttemptMode

/**
 * Domain model for Attempt
 *
 * Represents a Study or Game session
 */
data class Attempt(
    val id: String,
    val mode: AttemptMode,
    val startedAt: Long,
    val completedAt: Long? = null,
    val durationMs: Long? = null,
    val score: Int = 0,
    val primaryPackId: String? = null,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val createdAt: Long,
    val updatedAt: Long
)
