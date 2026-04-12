package com.uquiz.android.data.attempts.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.attempts.entity.AttemptAnswerEntity
import com.uquiz.android.data.attempts.query.UserDifficultyStatsRow
import com.uquiz.android.data.attempts.query.UserQuestionInsightRow
import com.uquiz.android.domain.attempts.enums.AttemptMode
import kotlinx.coroutines.flow.Flow

/**
 * ### AttemptAnswerDao
 *
 * Acceso reactivo y puntual a la tabla `attempt_answers`.
 * Las queries analíticas (dificultad, preguntas destacadas, tiempos) producen rows
 * tipados definidos en `query/`.
 */
@Dao
interface AttemptAnswerDao {

    /** Observa todas las respuestas de un intento ordenadas por fecha de creación. */
    @Query("""
        SELECT * FROM attempt_answers
        WHERE attemptId = :attemptId
        ORDER BY createdAt ASC
    """)
    fun observeByAttemptId(attemptId: String): Flow<List<AttemptAnswerEntity>>

    @Query("""
        SELECT * FROM attempt_answers
        WHERE attemptId = :attemptId
        ORDER BY createdAt ASC
    """)
    suspend fun getByAttemptId(attemptId: String): List<AttemptAnswerEntity>

    @Query("SELECT * FROM attempt_answers WHERE id = :id")
    suspend fun getById(id: String): AttemptAnswerEntity?

    /** Devuelve la respuesta guardada para una pregunta concreta dentro de un intento, o `null` si no se ha respondido. */
    @Query("""
        SELECT * FROM attempt_answers
        WHERE attemptId = :attemptId AND questionId = :questionId
        LIMIT 1
    """)
    suspend fun getAnswer(attemptId: String, questionId: String): AttemptAnswerEntity?

    @Query("""
        SELECT COUNT(*) FROM attempt_answers
        WHERE attemptId = :attemptId AND isCorrect = 1
    """)
    suspend fun countCorrectAnswers(attemptId: String): Int

    @Query("""
        SELECT COUNT(*) FROM attempt_answers
        WHERE attemptId = :attemptId
    """)
    suspend fun countTotalAnswers(attemptId: String): Int

    @Query("""
        SELECT CAST(AVG(aa.timeMs) AS INTEGER)
        FROM attempt_answers aa
        JOIN attempts a ON a.id = aa.attemptId
        WHERE a.userId = :userId
          AND a.status = 'COMPLETED'
          AND (:mode IS NULL OR a.mode = :mode)
          AND (:startedAfter IS NULL OR a.completedAt >= :startedAfter)
    """)
    suspend fun getAverageAnswerTime(
        userId: String,
        mode: AttemptMode?,
        startedAfter: Long?,
    ): Long?

    @Query("""
        SELECT
            q.difficulty AS difficulty,
            COUNT(aa.id) AS answeredCount,
            COALESCE(SUM(CASE WHEN aa.isCorrect = 1 THEN 1 ELSE 0 END), 0) AS correctAnswers,
            CAST(AVG(aa.timeMs) AS INTEGER) AS averageTimeMs
        FROM attempt_answers aa
        JOIN attempts a ON a.id = aa.attemptId
        JOIN questions q ON q.id = aa.questionId
        WHERE a.userId = :userId
          AND a.status = 'COMPLETED'
          AND (:mode IS NULL OR a.mode = :mode)
          AND (:startedAfter IS NULL OR a.completedAt >= :startedAfter)
        GROUP BY q.difficulty
        ORDER BY answeredCount DESC
    """)
    suspend fun getDifficultyStats(
        userId: String,
        mode: AttemptMode?,
        startedAfter: Long?,
    ): List<UserDifficultyStatsRow>

    @Query("""
        SELECT
            q.id AS questionId,
            q.text AS questionText,
            MIN(aa.timeMs) AS value
        FROM attempt_answers aa
        JOIN attempts a ON a.id = aa.attemptId
        JOIN questions q ON q.id = aa.questionId
        WHERE a.userId = :userId
          AND a.status = 'COMPLETED'
          AND (:mode IS NULL OR a.mode = :mode)
          AND (:startedAfter IS NULL OR a.completedAt >= :startedAfter)
        GROUP BY q.id
        ORDER BY value ASC
        LIMIT 1
    """)
    suspend fun getFastestQuestion(
        userId: String,
        mode: AttemptMode?,
        startedAfter: Long?,
    ): UserQuestionInsightRow?

    @Query("""
        SELECT
            q.id AS questionId,
            q.text AS questionText,
            COALESCE(SUM(CASE WHEN aa.isCorrect = 0 THEN 1 ELSE 0 END), 0) AS value
        FROM attempt_answers aa
        JOIN attempts a ON a.id = aa.attemptId
        JOIN questions q ON q.id = aa.questionId
        WHERE a.userId = :userId
          AND a.status = 'COMPLETED'
          AND (:mode IS NULL OR a.mode = :mode)
          AND (:startedAfter IS NULL OR a.completedAt >= :startedAfter)
        GROUP BY q.id
        HAVING value > 0
        ORDER BY value DESC
        LIMIT 1
    """)
    suspend fun getMostFailedQuestion(
        userId: String,
        mode: AttemptMode?,
        startedAfter: Long?,
    ): UserQuestionInsightRow?

    @Upsert
    suspend fun upsert(answer: AttemptAnswerEntity)

    @Upsert
    suspend fun upsertAll(answers: List<AttemptAnswerEntity>)

    @Delete
    suspend fun delete(answer: AttemptAnswerEntity)

    @Query("DELETE FROM attempt_answers WHERE attemptId = :attemptId")
    suspend fun deleteByAttemptId(attemptId: String)
}
