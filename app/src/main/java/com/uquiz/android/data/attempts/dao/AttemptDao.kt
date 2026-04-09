package com.uquiz.android.data.attempts.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.attempts.entity.AttemptEntity
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.enums.AttemptStatus
import com.uquiz.android.data.attempts.relations.AttemptWithAnswers
import com.uquiz.android.data.attempts.relations.AttemptWithPacks
import com.uquiz.android.data.stats.dao.PackPracticeStatsProjection
import com.uquiz.android.data.stats.dao.PackAggregateStatsProjection
import com.uquiz.android.data.stats.dao.PackRecentActivityProjection
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {

    /**
     * Observe all attempts ordered by date (newest first)
     */
    @Query("""
        SELECT * FROM attempts
        WHERE userId = :userId
        ORDER BY startedAt DESC
    """)
    fun observeAll(userId: String): Flow<List<AttemptEntity>>

    /**
     * Observe attempts by mode
     */
    @Query("""
        SELECT * FROM attempts
        WHERE userId = :userId
          AND mode = :mode
        ORDER BY startedAt DESC
    """)
    fun observeByMode(userId: String, mode: AttemptMode): Flow<List<AttemptEntity>>

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
        WHERE userId = :userId
          AND status = :status
        ORDER BY startedAt DESC
    """)
    suspend fun getIncompleteAttempts(
        userId: String,
        status: AttemptStatus = AttemptStatus.IN_PROGRESS
    ): List<AttemptEntity>

    @Query("""
        SELECT * FROM attempts
        WHERE userId = :userId
          AND mode = :mode
          AND primaryPackId = :packId
          AND status = :status
        ORDER BY startedAt DESC
        LIMIT 1
    """)
    suspend fun getActiveAttemptForPack(
        userId: String,
        packId: String,
        mode: AttemptMode = AttemptMode.STUDY,
        status: AttemptStatus = AttemptStatus.IN_PROGRESS
    ): AttemptEntity?

    /**
     * Get completed attempts
     */
    @Query("""
        SELECT * FROM attempts
        WHERE userId = :userId
          AND status = :status
        ORDER BY completedAt DESC
        LIMIT :limit
    """)
    suspend fun getRecentCompleted(
        userId: String,
        limit: Int = 10,
        status: AttemptStatus = AttemptStatus.COMPLETED
    ): List<AttemptEntity>

    /**
     * Get average score for a mode
     */
    @Query("""
        SELECT AVG(score) FROM attempts
        WHERE userId = :userId AND mode = :mode AND completedAt IS NOT NULL
    """)
    suspend fun getAverageScore(userId: String, mode: AttemptMode): Double?

    /**
     * Get best score for a mode
     */
    @Query("""
        SELECT MAX(score) FROM attempts
        WHERE userId = :userId AND mode = :mode AND completedAt IS NOT NULL
    """)
    suspend fun getBestScore(userId: String, mode: AttemptMode): Int?

    @Query("""
        SELECT
            COUNT(*) as sessionsCount,
            CASE
                WHEN SUM(totalQuestions) > 0
                    THEN CAST(ROUND((SUM(correctAnswers) * 100.0) / SUM(totalQuestions)) AS INTEGER)
                ELSE NULL
            END as accuracyPercent
        FROM attempts
        WHERE userId = :userId
          AND primaryPackId = :packId
          AND mode = :mode
          AND status = 'COMPLETED'
    """)
    fun observePackPracticeStats(
        userId: String,
        packId: String,
        mode: AttemptMode = AttemptMode.STUDY
    ): Flow<PackPracticeStatsProjection>

    @Query("""
        SELECT
            COUNT(*) AS totalSessions,
            SUM(CASE WHEN mode = 'STUDY' THEN 1 ELSE 0 END) AS totalStudySessions,
            SUM(CASE WHEN mode = 'GAME' THEN 1 ELSE 0 END) AS totalGameSessions,
            COALESCE(SUM(correctAnswers), 0) AS totalCorrectAnswers,
            COALESCE(SUM(totalQuestions), 0) AS totalQuestions,
            COALESCE(SUM(CASE WHEN mode = 'STUDY' THEN correctAnswers ELSE 0 END), 0) AS totalStudyCorrectAnswers,
            COALESCE(SUM(CASE WHEN mode = 'STUDY' THEN totalQuestions ELSE 0 END), 0) AS totalStudyQuestions,
            COALESCE(SUM(CASE WHEN mode = 'GAME' THEN correctAnswers ELSE 0 END), 0) AS totalGameCorrectAnswers,
            COALESCE(SUM(CASE WHEN mode = 'GAME' THEN totalQuestions ELSE 0 END), 0) AS totalGameQuestions,
            CAST(AVG(durationMs) AS INTEGER) AS averageDurationMs,
            CAST(AVG(CASE WHEN mode = 'STUDY' THEN durationMs END) AS INTEGER) AS averageStudyDurationMs,
            CAST(AVG(CASE WHEN mode = 'GAME' THEN durationMs END) AS INTEGER) AS averageGameDurationMs,
            MAX(CASE
                WHEN mode = 'STUDY' AND totalQuestions > 0
                    THEN CAST(ROUND((correctAnswers * 100.0) / totalQuestions) AS INTEGER)
                ELSE NULL
            END) AS bestStudyAccuracyPercent,
            MAX(CASE WHEN mode = 'GAME' THEN score ELSE NULL END) AS bestGameScore,
            MAX(completedAt) AS lastSessionAt
        FROM attempts
        WHERE userId = :userId
          AND primaryPackId = :packId
          AND status = 'COMPLETED'
    """)
    fun observePackAggregateStats(
        userId: String,
        packId: String
    ): Flow<PackAggregateStatsProjection?>

    @Query("""
        SELECT
            id AS attemptId,
            mode AS mode,
            completedAt AS completedAt,
            durationMs AS durationMs,
            score AS score,
            correctAnswers AS correctAnswers,
            totalQuestions AS totalQuestions
        FROM attempts
        WHERE userId = :userId
          AND primaryPackId = :packId
          AND status = 'COMPLETED'
        ORDER BY completedAt DESC
        LIMIT :limit
    """)
    fun observeRecentCompletedByPack(
        userId: String,
        packId: String,
        limit: Int = 10
    ): Flow<List<PackRecentActivityProjection>>

    @Query("""
        SELECT
            COUNT(*) AS totalSessions,
            SUM(CASE WHEN mode = 'STUDY' THEN 1 ELSE 0 END) AS totalStudySessions,
            SUM(CASE WHEN mode = 'GAME' THEN 1 ELSE 0 END) AS totalGameSessions,
            COALESCE(SUM(correctAnswers), 0) AS totalCorrectAnswers,
            COALESCE(SUM(totalQuestions), 0) AS totalQuestions,
            COALESCE(SUM(CASE WHEN mode = 'STUDY' THEN correctAnswers ELSE 0 END), 0) AS totalStudyCorrectAnswers,
            COALESCE(SUM(CASE WHEN mode = 'STUDY' THEN totalQuestions ELSE 0 END), 0) AS totalStudyQuestions,
            COALESCE(SUM(CASE WHEN mode = 'GAME' THEN correctAnswers ELSE 0 END), 0) AS totalGameCorrectAnswers,
            COALESCE(SUM(CASE WHEN mode = 'GAME' THEN totalQuestions ELSE 0 END), 0) AS totalGameQuestions,
            CAST(AVG(durationMs) AS INTEGER) AS averageDurationMs,
            CAST(AVG(CASE WHEN mode = 'STUDY' THEN durationMs END) AS INTEGER) AS averageStudyDurationMs,
            CAST(AVG(CASE WHEN mode = 'GAME' THEN durationMs END) AS INTEGER) AS averageGameDurationMs,
            MAX(CASE
                WHEN mode = 'STUDY' AND totalQuestions > 0
                    THEN CAST(ROUND((correctAnswers * 100.0) / totalQuestions) AS INTEGER)
                ELSE NULL
            END) AS bestStudyAccuracyPercent,
            MAX(CASE WHEN mode = 'GAME' THEN score ELSE NULL END) AS bestGameScore,
            MAX(completedAt) AS lastSessionAt
        FROM attempts
        WHERE userId = :userId
          AND primaryPackId = :packId
          AND status = 'COMPLETED'
    """)
    suspend fun getPackAggregateStats(
        userId: String,
        packId: String
    ): PackAggregateStatsProjection?

    @Query("""
        SELECT
            a.id AS attemptId,
            a.primaryPackId AS packId,
            a.mode AS mode,
            COUNT(aa.id) AS answeredCount
            ,a.startedAt AS startedAt
        FROM attempts a
        LEFT JOIN attempt_answers aa ON aa.attemptId = a.id
        WHERE a.userId = :userId
          AND a.mode = :mode
          AND a.status = :status
          AND a.primaryPackId IS NOT NULL
        GROUP BY a.id
        ORDER BY a.startedAt DESC
    """)
    fun observeActivePackProgress(
        userId: String,
        mode: AttemptMode = AttemptMode.STUDY,
        status: AttemptStatus = AttemptStatus.IN_PROGRESS
    ): Flow<List<ActivePackProgressProjection>>

    /**
     * Insert or update attempt
     */
    @Upsert
    suspend fun upsert(attempt: AttemptEntity)

    /**
     * Delete attempt (will cascade to answers)
     */
    @Query("DELETE FROM attempts WHERE id = :id")
    suspend fun deleteById(id: String)

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
