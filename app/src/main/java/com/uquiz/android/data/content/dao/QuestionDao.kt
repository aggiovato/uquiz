package com.uquiz.android.data.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.content.entity.OptionEntity
import com.uquiz.android.data.content.entity.QuestionEntity
import com.uquiz.android.data.content.relations.OrderedQuestionWithOptions
import com.uquiz.android.data.content.relations.QuestionWithOptions
import kotlinx.coroutines.flow.Flow

/**
 * ### QuestionDao
 *
 * Acceso reactivo y puntual a la tabla `questions` y sus opciones de respuesta.
 */
@Dao
interface QuestionDao {

    @Upsert
    suspend fun upsertQuestion(question: QuestionEntity)

    @Upsert
    suspend fun upsertOptions(options: List<OptionEntity>)

    @Query("DELETE FROM options WHERE questionId = :questionId")
    suspend fun deleteOptionsByQuestion(questionId: String)

    @Query("SELECT 0 as position, q.* FROM questions q WHERE q.id = :id")
    fun observeQuestionWithOptions(id: String): Flow<List<OrderedQuestionWithOptions>>

    @Transaction
    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionWithOptions(questionId: String): QuestionWithOptions?

    @Transaction
    @Query("SELECT * FROM questions WHERE id = :questionId")
    fun observeQuestionWithOptionsFlow(questionId: String): Flow<QuestionWithOptions?>

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getById(questionId: String): QuestionEntity?

    @Query("SELECT * FROM questions WHERE id IN (:questionIds)")
    suspend fun getByIds(questionIds: List<String>): List<QuestionEntity>

    /** Elimina la pregunta en cascada (opciones y entradas de `pack_questions` incluidas). */
    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteById(id: String)
}
