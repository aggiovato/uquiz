package com.uquiz.android.data.stats.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.stats.entity.QuestionStatsEntity
import com.uquiz.android.data.stats.query.QuestionMasterySummaryRow
import com.uquiz.android.data.stats.query.UserMasterySummaryRow
import kotlinx.coroutines.flow.Flow

/**
 * ### QuestionStatsDao
 *
 * Acceso reactivo y puntual a la tabla `question_stats` (mastery y rendimiento por pregunta y usuario).
 */
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

    /** Observa el conteo de preguntas dominadas (MASTERED) para un pack específico. */
    @Query("""
        SELECT COUNT(*) AS dominatedQuestions
        FROM question_stats
        WHERE userId = :userId
          AND packId = :packId
          AND masteryLevel = 'MASTERED'
    """)
    fun observeMasterySummary(userId: String, packId: String): Flow<QuestionMasterySummaryRow>

    @Query("""
        SELECT COUNT(*)
        FROM question_stats
        WHERE userId = :userId
          AND packId = :packId
          AND masteryLevel = 'MASTERED'
    """)
    suspend fun countMasteredByPack(userId: String, packId: String): Int

    /**
     * Devuelve el resumen global de mastery del usuario: total de preguntas dominadas
     * sobre el total rastreado en todos los packs. Empleado en el dashboard de estadísticas.
     */
    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN masteryLevel = 'MASTERED' THEN 1 ELSE 0 END), 0) AS masteredQuestions,
            COUNT(*) AS trackedQuestions
        FROM question_stats
        WHERE userId = :userId
    """)
    suspend fun getUserMasterySummary(userId: String): UserMasterySummaryRow

    @Upsert
    suspend fun upsert(stats: QuestionStatsEntity)
}
