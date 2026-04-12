package com.uquiz.android.data.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.content.entity.PackQuestionEntity
import kotlinx.coroutines.flow.Flow

/**
 * ### PackQuestionDao
 *
 * Gestiona la tabla de unión `pack_questions` que relaciona packs con preguntas.
 */
@Dao
interface PackQuestionDao {

    @Upsert
    suspend fun upsert(packQuestion: PackQuestionEntity)

    @Upsert
    suspend fun upsertAll(packQuestions: List<PackQuestionEntity>)

    @Query("""
        DELETE FROM pack_questions
        WHERE packId = :packId AND questionId = :questionId
    """)
    suspend fun delete(packId: String, questionId: String)

    @Query("DELETE FROM pack_questions WHERE packId = :packId")
    suspend fun deleteByPackId(packId: String)

    @Query("DELETE FROM pack_questions WHERE questionId = :questionId")
    suspend fun deleteByQuestionId(questionId: String)

    /** Devuelve los IDs de preguntas del pack, ordenados por [sortOrder]. */
    @Query("""
        SELECT questionId FROM pack_questions
        WHERE packId = :packId
        ORDER BY sortOrder ASC
    """)
    suspend fun getQuestionIds(packId: String): List<String>

    @Query("""
        SELECT packId FROM pack_questions
        WHERE questionId = :questionId
    """)
    suspend fun getPackIds(questionId: String): List<String>

    @Query("""
        SELECT COUNT(*) FROM pack_questions
        WHERE packId = :packId
    """)
    suspend fun countQuestionsInPack(packId: String): Int

    @Query("""
        SELECT COUNT(*) FROM pack_questions
        WHERE packId = :packId
    """)
    fun observeQuestionCount(packId: String): Flow<Int>
}
