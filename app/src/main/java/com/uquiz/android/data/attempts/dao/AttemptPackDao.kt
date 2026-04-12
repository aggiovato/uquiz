package com.uquiz.android.data.attempts.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.attempts.entity.AttemptPackEntity

/**
 * ### AttemptPackDao
 *
 * Gestiona la tabla de unión `attempt_packs` que registra los packs usados en cada intento.
 */
@Dao
interface AttemptPackDao {

    @Upsert
    suspend fun upsert(attemptPack: AttemptPackEntity)

    @Upsert
    suspend fun upsertAll(attemptPacks: List<AttemptPackEntity>)

    @Query("""
        SELECT packId FROM attempt_packs
        WHERE attemptId = :attemptId
    """)
    suspend fun getPackIds(attemptId: String): List<String>

    @Query("""
        DELETE FROM attempt_packs
        WHERE attemptId = :attemptId AND packId = :packId
    """)
    suspend fun delete(attemptId: String, packId: String)

    @Query("DELETE FROM attempt_packs WHERE attemptId = :attemptId")
    suspend fun deleteByAttemptId(attemptId: String)
}
