package com.uquiz.android.data.local.dao

import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.PackEntity
import com.uquiz.android.data.local.entity.PackQuestionEntity
import com.uquiz.android.data.local.relation.OrderedQuestionWithOptions
import kotlinx.coroutines.flow.Flow

interface PackDao {

    @Upsert
    suspend fun upsertPack(pack: PackEntity)

    @Query("SELECT * FROM packs WHERE id = :packId LIMIT 1")
    fun observePack(packId: String): Flow<PackEntity?>

    @Query("DELETE FROM pack_questions WHERE packId = :packId")
    suspend fun clearPackQuestions(packId: String)

    @Upsert
    suspend fun upsertPackQuestions(rows: List<PackQuestionEntity>)

    @Transaction
    @Query("""
    SELECT pq.position as position, q.*
    FROM pack_questions pq
    JOIN questions q ON q.id = pq.questionId
    WHERE pq.packId = :packId
    ORDER BY pq.position ASC
  """)
    fun observeOrderedQuestionWithOptions(packId: String):
            Flow<List<OrderedQuestionWithOptions>>
}