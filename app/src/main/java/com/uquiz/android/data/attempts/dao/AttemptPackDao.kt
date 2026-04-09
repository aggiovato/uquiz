package com.uquiz.android.data.attempts.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.attempts.entity.AttemptPackEntity

@Dao
interface AttemptPackDao {

    /**
     * Link pack to attempt
     */
    @Upsert
    suspend fun upsert(attemptPack: AttemptPackEntity)

    /**
     * Link multiple packs to attempt
     */
    @Upsert
    suspend fun upsertAll(attemptPacks: List<AttemptPackEntity>)

    /**
     * Get pack IDs for an attempt
     */
    @Query("""
        SELECT packId FROM attempt_packs
        WHERE attemptId = :attemptId
    """)
    suspend fun getPackIds(attemptId: String): List<String>

    /**
     * Get attempt IDs that used a specific pack
     */
    @Query("""
        SELECT attemptId FROM attempt_packs
        WHERE packId = :packId
    """)
    suspend fun getAttemptIds(packId: String): List<String>

    /**
     * Remove pack from attempt
     */
    @Query("""
        DELETE FROM attempt_packs
        WHERE attemptId = :attemptId AND packId = :packId
    """)
    suspend fun delete(attemptId: String, packId: String)

    /**
     * Remove all packs from attempt
     */
    @Query("DELETE FROM attempt_packs WHERE attemptId = :attemptId")
    suspend fun deleteByAttemptId(attemptId: String)
}
