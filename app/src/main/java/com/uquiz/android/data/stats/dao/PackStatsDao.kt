package com.uquiz.android.data.stats.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.stats.entity.PackStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackStatsDao {

    @Query("""
        SELECT * FROM pack_stats
        WHERE userId = :userId AND packId = :packId
        LIMIT 1
    """)
    suspend fun getByPackId(userId: String, packId: String): PackStatsEntity?

    @Query("""
        SELECT * FROM pack_stats
        WHERE userId = :userId AND packId = :packId
        LIMIT 1
    """)
    fun observeByPackId(userId: String, packId: String): Flow<PackStatsEntity?>

    @Upsert
    suspend fun upsert(stats: PackStatsEntity)
}
