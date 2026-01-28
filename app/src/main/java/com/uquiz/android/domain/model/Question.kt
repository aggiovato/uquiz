package com.uquiz.android.domain.model

import com.uquiz.android.data.local.enums.DifficultyLevel

/**
 * Domain model for Question
 *
 * Represents a quiz question with Markdown support
 */
data class Question(
    val id: String,
    val text: String,
    val explanation: String? = null,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val createdAt: Long,
    val updatedAt: Long
)
