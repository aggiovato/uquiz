package com.uquiz.android.domain.repository

import com.uquiz.android.data.local.enums.AttemptMode
import com.uquiz.android.domain.model.Attempt
import com.uquiz.android.domain.model.AttemptAnswer
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository interface for Attempt operations
 */
interface AttemptRepository {

    /**
     * Observe all attempts
     */
    fun observeAllAttempts(): Flow<List<Attempt>>

    /**
     * Observe attempts by mode
     */
    fun observeByMode(mode: AttemptMode): Flow<List<Attempt>>

    /**
     * Get attempt by ID
     */
    suspend fun getById(id: String): Attempt?

    /**
     * Observe attempt by ID
     */
    fun observeById(id: String): Flow<Attempt?>

    /**
     * Get attempt with all answers
     */
    suspend fun getAttemptWithAnswers(attemptId: String): Pair<Attempt, List<AttemptAnswer>>?

    /**
     * Get incomplete attempts
     */
    suspend fun getIncompleteAttempts(): List<Attempt>

    /**
     * Get recent completed attempts
     */
    suspend fun getRecentCompleted(limit: Int = 10): List<Attempt>

    /**
     * Get statistics
     */
    suspend fun getAverageScore(mode: AttemptMode): Double?
    suspend fun getBestScore(mode: AttemptMode): Int?

    /**
     * Create new attempt
     * @return Generated attempt with ID
     */
    suspend fun createAttempt(
        mode: AttemptMode,
        primaryPackId: String? = null,
        packIds: List<String> = emptyList()
    ): Attempt

    /**
     * Update attempt (for completing, updating score, etc.)
     */
    suspend fun updateAttempt(attempt: Attempt)

    /**
     * Complete attempt
     */
    suspend fun completeAttempt(
        attemptId: String,
        score: Int,
        correctAnswers: Int,
        totalQuestions: Int
    )

    /**
     * Record answer
     */
    suspend fun recordAnswer(
        attemptId: String,
        questionId: String,
        pickedOptionId: String?,
        isCorrect: Boolean,
        timeMs: Long,
        timeLimitMs: Long? = null
    ): AttemptAnswer

    /**
     * Delete attempt (cascade to answers)
     */
    suspend fun deleteAttempt(attempt: Attempt)

    /**
     * Delete old attempts
     */
    suspend fun deleteOlderThan(beforeTimestamp: Long)
}
