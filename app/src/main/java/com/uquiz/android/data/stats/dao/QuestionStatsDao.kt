package com.uquiz.android.data.stats.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.stats.entity.QuestionStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionStatsDao {

    @Query("""
        SELECT * FROM question_stats
        WHERE userId = :userId AND questionId = :questionId
        LIMIT 1
    """)
    suspend fun getByQuestionId(userId: String, questionId: String): QuestionStatsEntity?

    @Query("""
        SELECT * FROM question_stats
        WHERE userId = :userId AND packId = :packId
        ORDER BY updatedAt DESC
    """)
    fun observeByPackId(userId: String, packId: String): Flow<List<QuestionStatsEntity>>

    @Query("""
        SELECT COUNT(*) AS dominatedQuestions
        FROM question_stats
        WHERE userId = :userId
          AND packId = :packId
          AND masteryLevel = 'MASTERED'
    """)
    fun observeMasterySummary(userId: String, packId: String): Flow<QuestionMasterySummaryProjection>

    @Query("""
        SELECT COUNT(*)
        FROM question_stats
        WHERE userId = :userId
          AND packId = :packId
          AND masteryLevel = 'MASTERED'
    """)
    suspend fun countMasteredByPack(userId: String, packId: String): Int

    @Upsert
    suspend fun upsert(stats: QuestionStatsEntity)
}
