package com.uquiz.android.domain.repository

import com.uquiz.android.domain.model.Pack
import com.uquiz.android.domain.model.QuestionWithOptions
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository interface for Pack operations
 */
interface PackRepository {

    /**
     * Observe pack by ID
     */
    fun observePack(packId: String): Flow<Pack?>

    /**
     * Get pack by ID
     */
    suspend fun getById(packId: String): Pack?

    /**
     * Observe all packs in a folder
     */
    fun observeByFolderId(folderId: String): Flow<List<Pack>>

    /**
     * Get all packs in a folder
     */
    suspend fun getByFolderId(folderId: String): List<Pack>

    /**
     * Get pack with all questions and options (for Study/Game mode)
     */
    suspend fun getPackWithQuestions(packId: String): List<QuestionWithOptions>

    /**
     * Observe pack with questions
     */
    fun observePackWithQuestions(packId: String): Flow<List<QuestionWithOptions>>

    /**
     * Create new pack
     * @return Generated pack with ID
     */
    suspend fun createPack(
        title: String,
        description: String? = null,
        folderId: String,
        icon: String? = null
    ): Pack

    /**
     * Update pack
     */
    suspend fun updatePack(pack: Pack)

    /**
     * Delete pack (cascade to pack_questions)
     */
    suspend fun deletePack(pack: Pack)

    /**
     * Set pack questions with order
     */
    suspend fun setPackQuestions(packId: String, orderedQuestionIds: List<String>)

    /**
     * Add question to pack
     */
    suspend fun addQuestionToPack(packId: String, questionId: String, sortOrder: Int)

    /**
     * Remove question from pack
     */
    suspend fun removeQuestionFromPack(packId: String, questionId: String)

    /**
     * Get question count in pack
     */
    suspend fun getQuestionCount(packId: String): Int
}