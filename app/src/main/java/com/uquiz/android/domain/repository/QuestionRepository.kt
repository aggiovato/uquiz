package com.uquiz.android.domain.repository

import com.uquiz.android.data.local.enums.DifficultyLevel
import com.uquiz.android.domain.model.Option
import com.uquiz.android.domain.model.Question
import com.uquiz.android.domain.model.QuestionWithOptions
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository interface for Question operations
 */
interface QuestionRepository {

    /**
     * Observe question by ID
     */
    fun observeQuestion(questionId: String): Flow<Question?>

    /**
     * Get question by ID
     */
    suspend fun getById(questionId: String): Question?

    /**
     * Get question with options
     */
    suspend fun getQuestionWithOptions(questionId: String): QuestionWithOptions?

    /**
     * Observe question with options
     */
    fun observeQuestionWithOptions(questionId: String): Flow<QuestionWithOptions?>

    /**
     * Create new question with options
     * @return Generated question with ID
     */
    suspend fun createQuestion(
        text: String,
        explanation: String? = null,
        difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
        options: List<CreateOptionData>
    ): Question

    /**
     * Update question (without options)
     */
    suspend fun updateQuestion(question: Question)

    /**
     * Update question options
     */
    suspend fun updateQuestionOptions(questionId: String, options: List<Option>)

    /**
     * Delete question (cascade to options and pack_questions)
     */
    suspend fun deleteQuestion(question: Question)
}

/**
 * Data class for creating options
 */
data class CreateOptionData(
    val label: String,
    val text: String,
    val isCorrect: Boolean
)