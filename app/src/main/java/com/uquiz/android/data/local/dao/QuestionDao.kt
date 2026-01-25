package com.uquiz.android.data.local.dao

import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.OptionEntity
import com.uquiz.android.data.local.entity.QuestionEntity
import com.uquiz.android.data.local.relation.OrderedQuestionWithOptions
import kotlinx.coroutines.flow.Flow

interface QuestionDao {

    @Upsert
    suspend fun upsertQuestion(question: QuestionEntity)

    @Upsert
    suspend fun upsertOptions(options: List<OptionEntity>)

    @Query("DELETE FROM options WHERE questionId = :questionId")
    suspend fun deleteOptionsByQuestion(questionId: String)

    @Query("SELECT 0 as position, q.* FROM questions q WHERE q.id = :id")
    fun observeQuestionWithOptions(id: String): Flow<List<OrderedQuestionWithOptions>>

}