package com.uquiz.android.data.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.content.entity.PackQuestionEntity
import com.uquiz.android.data.content.entity.PackWithQuestionCountEntity
import com.uquiz.android.data.content.relations.OrderedQuestionWithOptions
import com.uquiz.android.data.content.relations.PackWithQuestions
import com.uquiz.android.data.content.relations.PackWithQuestionsAndOptions
import kotlinx.coroutines.flow.Flow

@Dao
interface PackDao {
    @Upsert
    suspend fun upsertPack(pack: PackEntity)

    @Query("SELECT * FROM packs WHERE id = :packId LIMIT 1")
    suspend fun getById(packId: String): PackEntity?

    @Query("SELECT * FROM packs WHERE id = :packId LIMIT 1")
    fun observePack(packId: String): Flow<PackEntity?>

    @Query("DELETE FROM pack_questions WHERE packId = :packId")
    suspend fun clearPackQuestions(packId: String)

    @Upsert
    suspend fun upsertPackQuestions(rows: List<PackQuestionEntity>)

    @Transaction
    @Query(
        """
    SELECT pq.sortOrder as position, q.*
    FROM pack_questions pq
    JOIN questions q ON q.id = pq.questionId
    WHERE pq.packId = :packId
    ORDER BY pq.sortOrder ASC
  """,
    )
    fun observeOrderedQuestionWithOptions(packId: String): Flow<List<OrderedQuestionWithOptions>>

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

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM packs
            WHERE folderId = :folderId
              AND LOWER(title) = LOWER(:title)
              AND (:excludeId IS NULL OR id != :excludeId)
        )
    """,
    )
    suspend fun existsPackWithTitle(
        folderId: String,
        title: String,
        excludeId: String? = null,
    ): Boolean

    /**
     * Observe all packs in a folder
     */
    @Query("SELECT * FROM packs WHERE folderId = :folderId ORDER BY title ASC")
    fun observeByFolderId(folderId: String): Flow<List<PackEntity>>

    @Query("SELECT * FROM packs ORDER BY updatedAt DESC LIMIT 50")
    fun observeAll(): Flow<List<PackEntity>>

    @Query(
        """
        SELECT p.*,
            COUNT(pq.questionId) AS questionCount
        FROM packs p
        LEFT JOIN pack_questions pq ON pq.packId = p.id
        WHERE p.folderId = :folderId
        GROUP BY p.id
        ORDER BY p.title ASC
    """,
    )
    fun observeByFolderIdWithQuestionCounts(folderId: String): Flow<List<PackWithQuestionCountEntity>>

    @Query(
        """
        SELECT p.*,
            COUNT(pq.questionId) AS questionCount
        FROM packs p
        LEFT JOIN pack_questions pq ON pq.packId = p.id
        GROUP BY p.id
        ORDER BY p.updatedAt DESC
        LIMIT 50
    """,
    )
    fun observeAllWithQuestionCounts(): Flow<List<PackWithQuestionCountEntity>>

    /**
     * Delete pack by ID (cascades to pack_questions)
     */
    @Query("DELETE FROM packs WHERE id = :id")
    suspend fun deleteById(id: String)
}
