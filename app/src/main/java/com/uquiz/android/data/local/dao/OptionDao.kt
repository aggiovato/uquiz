package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.OptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OptionDao {

    /**
     * Get all options for a question
     * Ordered by label (A, B, C, D)
     */
    @Query("""
        SELECT * FROM options
        WHERE questionId = :questionId
        ORDER BY label ASC
    """)
    suspend fun getByQuestionId(questionId: String): List<OptionEntity>

    /**
     * Observe options for a question
     */
    @Query("""
        SELECT * FROM options
        WHERE questionId = :questionId
        ORDER BY label ASC
    """)
    fun observeByQuestionId(questionId: String): Flow<List<OptionEntity>>

    /**
     * Get option by ID
     */
    @Query("SELECT * FROM options WHERE id = :id")
    suspend fun getById(id: String): OptionEntity?

    /**
     * Get correct option for a question
     */
    @Query("""
        SELECT * FROM options
        WHERE questionId = :questionId AND isCorrect = 1
        LIMIT 1
    """)
    suspend fun getCorrectOption(questionId: String): OptionEntity?

    /**
     * Insert or update option
     */
    @Upsert
    suspend fun upsert(option: OptionEntity)

    /**
     * Insert or update multiple options
     */
    @Upsert
    suspend fun upsertAll(options: List<OptionEntity>)

    /**
     * Delete option
     */
    @Delete
    suspend fun delete(option: OptionEntity)

    /**
     * Delete all options for a question
     */
    @Query("DELETE FROM options WHERE questionId = :questionId")
    suspend fun deleteByQuestionId(questionId: String)
}
