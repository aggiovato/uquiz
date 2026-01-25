package com.uquiz.android.domain.repository

import com.uquiz.android.domain.model.Pack
import com.uquiz.android.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface PackRepository {
    fun observePack(packId: String): Flow<Pack?>
    fun observePackQuestions(packId: String): Flow<List<Question>>

    suspend fun createPack(pack: Pack)
    suspend fun setPackQuestions(packId: String, orderedQuestionsIds: List<String>)
}