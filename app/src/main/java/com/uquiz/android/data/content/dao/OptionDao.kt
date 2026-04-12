package com.uquiz.android.data.content.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.content.entity.OptionEntity
import kotlinx.coroutines.flow.Flow

/**
 * ### OptionDao
 *
 * Acceso reactivo y puntual a la tabla `options` (opciones de respuesta de las preguntas).
 */
@Dao
interface OptionDao {

    /** Devuelve las opciones de la pregunta ordenadas por etiqueta (A, B, C, D). */
    @Query("""
        SELECT * FROM options
        WHERE questionId = :questionId
        ORDER BY label ASC
    """)
    suspend fun getByQuestionId(questionId: String): List<OptionEntity>

    /** Observa las opciones de la pregunta ordenadas por etiqueta. */
    @Query("""
        SELECT * FROM options
        WHERE questionId = :questionId
        ORDER BY label ASC
    """)
    fun observeByQuestionId(questionId: String): Flow<List<OptionEntity>>

    @Query("SELECT * FROM options WHERE id = :id")
    suspend fun getById(id: String): OptionEntity?

    /** Devuelve la opción correcta de la pregunta, o `null` si no está definida. */
    @Query("""
        SELECT * FROM options
        WHERE questionId = :questionId AND isCorrect = 1
        LIMIT 1
    """)
    suspend fun getCorrectOption(questionId: String): OptionEntity?

    @Upsert
    suspend fun upsert(option: OptionEntity)

    @Upsert
    suspend fun upsertAll(options: List<OptionEntity>)

    @Delete
    suspend fun delete(option: OptionEntity)

    @Query("DELETE FROM options WHERE questionId = :questionId")
    suspend fun deleteByQuestionId(questionId: String)
}
