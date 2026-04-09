package com.uquiz.android.data.ranking.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.ranking.entity.UserRankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRankDao {

    @Query("""
        SELECT * FROM user_rank
        WHERE userId = :userId
        LIMIT 1
    """)
    suspend fun getByUserId(userId: String): UserRankEntity?

    @Query("""
        SELECT * FROM user_rank
        WHERE userId = :userId
        LIMIT 1
    """)
    fun observeByUserId(userId: String): Flow<UserRankEntity?>

    @Upsert
    suspend fun upsert(rank: UserRankEntity)
}
