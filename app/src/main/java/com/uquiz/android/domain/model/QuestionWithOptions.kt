package com.uquiz.android.domain.model

/**
 * Domain model: Question with its Options
 *
 * Composite model used for displaying questions with answer choices
 */
data class QuestionWithOptions(
    val question: Question,
    val options: List<Option>
) {
    /**
     * Get the correct option
     */
    val correctOption: Option?
        get() = options.firstOrNull { it.isCorrect }
}
