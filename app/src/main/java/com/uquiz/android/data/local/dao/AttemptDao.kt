package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.AttemptEntity
import com.uquiz.android.data.local.enums.AttemptMode
import com.uquiz.android.data.local.relations.AttemptWithAnswers
import com.uquiz.android.data.local.relations.AttemptWithPacks
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {

    /**
     * Observe all attempts ordered by date (newest first)
     */
    @Query("""
        SELECT * FROM attempts
        ORDER BY startedAt DESC
    """)
    fun observeAll(): Flow<List<AttemptEntity>>

    /**
     * Observe attempts by mode
     */
    @Query("""
        SELECT * FROM attempts
        WHERE mode = :mode
        ORDER BY startedAt DESC
    """)
    fun observeByMode(mode: AttemptMode): Flow<List<AttemptEntity>>

    /**
     * Get attempt by ID
     */
    @Query("SELECT * FROM attempts WHERE id = :id")
    suspend fun getById(id: String): AttemptEntity?

    /**
     * Observe attempt by ID
     */
    @Query("SELECT * FROM attempts WHERE id = :id")
    fun observeById(id: String): Flow<AttemptEntity?>

    /**
     * Get incomplete attempts (completedAt is null)
     */
    @Query("""
        SELECT * FROM attempts
        WHERE completedAt IS NULL
        ORDER BY startedAt DESC
    """)
    suspend fun getIncompleteAttempts(): List<AttemptEntity>

    /**
     * Get completed attempts
     */
    @Query("""
        SELECT * FROM attempts
        WHERE completedAt IS NOT NULL
        ORDER BY startedAt DESC
        LIMIT :limit
    """)
    suspend fun getRecentCompleted(limit: Int = 10): List<AttemptEntity>

    /**
     * Get average score for a mode
     */
    @Query("""
        SELECT AVG(score) FROM attempts
        WHERE mode = :mode AND completedAt IS NOT NULL
    """)
    suspend fun getAverageScore(mode: AttemptMode): Double?

    /**
     * Get best score for a mode
     */
    @Query("""
        SELECT MAX(score) FROM attempts
        WHERE mode = :mode AND completedAt IS NOT NULL
    """)
    suspend fun getBestScore(mode: AttemptMode): Int?

    /**
     * Insert or update attempt
     */
    @Upsert
    suspend fun upsert(attempt: AttemptEntity)

    /**
     * Delete attempt (will cascade to answers)
     */
    @Delete
    suspend fun delete(attempt: AttemptEntity)

    /**
     * Delete old attempts
     */
    @Query("""
        DELETE FROM attempts
        WHERE startedAt < :beforeTimestamp
    """)
    suspend fun deleteOlderThan(beforeTimestamp: Long)

    /**
     * Get attempt with all answers
     */
    @Transaction
    @Query("SELECT * FROM attempts WHERE id = :attemptId")
    suspend fun getAttemptWithAnswers(attemptId: String): AttemptWithAnswers?

    /**
     * Observe attempt with answers
     */
    @Transaction
    @Query("SELECT * FROM attempts WHERE id = :attemptId")
    fun observeAttemptWithAnswers(attemptId: String): Flow<AttemptWithAnswers?>

    /**
     * Get attempt with packs used
     */
    @Transaction
    @Query("SELECT * FROM attempts WHERE id = :attemptId")
    suspend fun getAttemptWithPacks(attemptId: String): AttemptWithPacks?
}
