package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.AttemptAnswerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptAnswerDao {

    /**
     * Observe all answers for an attempt
     */
    @Query("""
        SELECT * FROM attempt_answers
        WHERE attemptId = :attemptId
        ORDER BY createdAt ASC
    """)
    fun observeByAttemptId(attemptId: String): Flow<List<AttemptAnswerEntity>>

    /**
     * Get all answers for an attempt
     */
    @Query("""
        SELECT * FROM attempt_answers
        WHERE attemptId = :attemptId
        ORDER BY createdAt ASC
    """)
    suspend fun getByAttemptId(attemptId: String): List<AttemptAnswerEntity>

    /**
     * Get answer by ID
     */
    @Query("SELECT * FROM attempt_answers WHERE id = :id")
    suspend fun getById(id: String): AttemptAnswerEntity?

    /**
     * Get answer for specific question in attempt
     */
    @Query("""
        SELECT * FROM attempt_answers
        WHERE attemptId = :attemptId AND questionId = :questionId
        LIMIT 1
    """)
    suspend fun getAnswer(attemptId: String, questionId: String): AttemptAnswerEntity?

    /**
     * Count correct answers in attempt
     */
    @Query("""
        SELECT COUNT(*) FROM attempt_answers
        WHERE attemptId = :attemptId AND isCorrect = 1
    """)
    suspend fun countCorrectAnswers(attemptId: String): Int

    /**
     * Count total answers in attempt
     */
    @Query("""
        SELECT COUNT(*) FROM attempt_answers
        WHERE attemptId = :attemptId
    """)
    suspend fun countTotalAnswers(attemptId: String): Int

    /**
     * Get average time per question in attempt (in ms)
     */
    @Query("""
        SELECT AVG(timeMs) FROM attempt_answers
        WHERE attemptId = :attemptId
    """)
    suspend fun getAverageTime(attemptId: String): Double?

    /**
     * Get answers for a specific question across all attempts
     */
    @Query("""
        SELECT * FROM attempt_answers
        WHERE questionId = :questionId
        ORDER BY createdAt DESC
    """)
    suspend fun getByQuestionId(questionId: String): List<AttemptAnswerEntity>

    /**
     * Insert or update answer
     */
    @Upsert
    suspend fun upsert(answer: AttemptAnswerEntity)

    /**
     * Insert or update multiple answers
     */
    @Upsert
    suspend fun upsertAll(answers: List<AttemptAnswerEntity>)

    /**
     * Delete answer
     */
    @Delete
    suspend fun delete(answer: AttemptAnswerEntity)

    /**
     * Delete all answers for an attempt
     */
    @Query("DELETE FROM attempt_answers WHERE attemptId = :attemptId")
    suspend fun deleteByAttemptId(attemptId: String)
}
