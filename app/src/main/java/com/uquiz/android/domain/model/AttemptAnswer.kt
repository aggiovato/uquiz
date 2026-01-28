package com.uquiz.android.domain.model

/**
 * Domain model for AttemptAnswer
 *
 * Represents a user's answer to a question in an attempt
 */
data class AttemptAnswer(
    val id: String,
    val attemptId: String,
    val questionId: String,
    val pickedOptionId: String? = null,
    val isCorrect: Boolean,
    val timeMs: Long,
    val timeLimitMs: Long? = null,
    val createdAt: Long,
    val updatedAt: Long
)
