package com.uquiz.android.domain.model

/**
 * Domain model for Option
 *
 * Represents an answer choice for a question
 */
data class Option(
    val id: String,
    val questionId: String,
    val label: String,
    val text: String,
    val isCorrect: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)