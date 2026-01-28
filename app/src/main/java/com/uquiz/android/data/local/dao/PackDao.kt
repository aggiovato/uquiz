package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.PackEntity
import com.uquiz.android.data.local.entity.PackQuestionEntity
import com.uquiz.android.data.local.relations.OrderedQuestionWithOptions
import com.uquiz.android.data.local.relations.PackWithQuestions
import com.uquiz.android.data.local.relations.PackWithQuestionsAndOptions
import kotlinx.coroutines.flow.Flow

@Dao
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
    SELECT pq.sortOrder as position, q.*
    FROM pack_questions pq
    JOIN questions q ON q.id = pq.questionId
    WHERE pq.packId = :packId
    ORDER BY pq.sortOrder ASC
  """)
    fun observeOrderedQuestionWithOptions(packId: String):
            Flow<List<OrderedQuestionWithOptions>>

    /**
     * Get pack with all its questions
     */
    @Transaction
    @Query("SELECT * FROM packs WHERE id = :packId")
    suspend fun getPackWithQuestions(packId: String): PackWithQuestions?

    /**
     * Get pack with questions and their options (nested relation)
     */
    @Transaction
    @Query("SELECT * FROM packs WHERE id = :packId")
    suspend fun getPackWithQuestionsAndOptions(packId: String): PackWithQuestionsAndOptions?

    /**
     * Observe pack with questions and options
     */
    @Transaction
    @Query("SELECT * FROM packs WHERE id = :packId")
    fun observePackWithQuestionsAndOptions(packId: String): Flow<PackWithQuestionsAndOptions?>

    /**
     * Get all packs in a folder
     */
    @Query("SELECT * FROM packs WHERE folderId = :folderId ORDER BY title ASC")
    suspend fun getByFolderId(folderId: String): List<PackEntity>

    /**
     * Observe all packs in a folder
     */
    @Query("SELECT * FROM packs WHERE folderId = :folderId ORDER BY title ASC")
    fun observeByFolderId(folderId: String): Flow<List<PackEntity>>

    /**
     * Delete pack
     */
    @Delete
    suspend fun delete(pack: PackEntity)
}