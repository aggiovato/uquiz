package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.OptionEntity
import com.uquiz.android.data.local.entity.QuestionEntity
import com.uquiz.android.data.local.relations.OrderedQuestionWithOptions
import com.uquiz.android.data.local.relations.QuestionWithOptions
import kotlinx.coroutines.flow.Flow

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

    /**
     * Get question with options
     */
    @Transaction
    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionWithOptions(questionId: String): QuestionWithOptions?

    /**
     * Observe question with options
     */
    @Transaction
    @Query("SELECT * FROM questions WHERE id = :questionId")
    fun observeQuestionWithOptionsFlow(questionId: String): Flow<QuestionWithOptions?>

    /**
     * Get question by ID
     */
    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getById(questionId: String): QuestionEntity?

    /**
     * Delete question (will cascade to options and pack_questions)
     */
    @Delete
    suspend fun delete(question: QuestionEntity)

}