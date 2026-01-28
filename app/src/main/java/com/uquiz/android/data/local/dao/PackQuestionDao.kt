package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.PackQuestionEntity

@Dao
interface PackQuestionDao {

    /**
     * Link a question to a pack
     */
    @Upsert
    suspend fun upsert(packQuestion: PackQuestionEntity)

    /**
     * Link multiple questions to a pack
     */
    @Upsert
    suspend fun upsertAll(packQuestions: List<PackQuestionEntity>)

    /**
     * Remove question from pack
     */
    @Query("""
        DELETE FROM pack_questions
        WHERE packId = :packId AND questionId = :questionId
    """)
    suspend fun delete(packId: String, questionId: String)

    /**
     * Remove all questions from pack
     */
    @Query("DELETE FROM pack_questions WHERE packId = :packId")
    suspend fun deleteByPackId(packId: String)

    /**
     * Remove question from all packs
     */
    @Query("DELETE FROM pack_questions WHERE questionId = :questionId")
    suspend fun deleteByQuestionId(questionId: String)

    /**
     * Get question IDs for a pack (ordered by sortOrder)
     */
    @Query("""
        SELECT questionId FROM pack_questions
        WHERE packId = :packId
        ORDER BY sortOrder ASC
    """)
    suspend fun getQuestionIds(packId: String): List<String>

    /**
     * Get pack IDs for a question
     */
    @Query("""
        SELECT packId FROM pack_questions
        WHERE questionId = :questionId
    """)
    suspend fun getPackIds(questionId: String): List<String>

    /**
     * Count questions in pack
     */
    @Query("""
        SELECT COUNT(*) FROM pack_questions
        WHERE packId = :packId
    """)
    suspend fun countQuestionsInPack(packId: String): Int
}
