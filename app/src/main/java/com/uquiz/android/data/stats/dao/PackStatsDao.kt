package com.uquiz.android.data.stats.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.stats.entity.PackStatsEntity
import com.uquiz.android.data.stats.query.UserPackCompletionRow
import kotlinx.coroutines.flow.Flow

/**
 * ### PackStatsDao
 *
 * Acceso reactivo y puntual a la tabla `pack_stats` (progreso y rendimiento por pack y usuario).
 */
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

    /**
     * Devuelve el conteo de packs completados (≥ 100 % de progreso) y en progreso (> 0 % y < 100 %)
     * para el usuario dado. Empleado en el dashboard de estadísticas.
     */
    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN progressPercent >= 100 THEN 1 ELSE 0 END), 0) AS completedPacks,
            COALESCE(SUM(CASE WHEN progressPercent > 0 AND progressPercent < 100 THEN 1 ELSE 0 END), 0) AS inProgressPacks
        FROM pack_stats
        WHERE userId = :userId
    """)
    suspend fun getUserPackCompletion(userId: String): UserPackCompletionRow

    @Upsert
    suspend fun upsert(stats: PackStatsEntity)
}
