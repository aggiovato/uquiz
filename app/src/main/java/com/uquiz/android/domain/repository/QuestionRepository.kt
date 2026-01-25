package com.uquiz.android.domain.repository

import com.uquiz.android.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    fun observeQuestion(questionId: String): Flow<Question?>

    suspend fun upsertQuestion(question: Question)
}