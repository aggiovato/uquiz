package com.uquiz.android.data.ranking.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.ranking.enums.UserRank

/**
 * Fila de la tabla `user_rank`. Almacena el estado de ranking del usuario: MMR, XP acumulado,
 * EWMA de rendimiento y contadores históricos de respuestas.
 *
 * @see com.uquiz.android.domain.ranking.model.UserRankState
 */
@Entity(tableName = "user_rank")
data class UserRankEntity(
    @PrimaryKey
    val userId: String,
    val currentRank: UserRank = UserRank.INITIATE,
    val mmr: Float = 600f,
    val perfEwma: Float = 0.5f,
    val lifetimeCorrect: Int = 0,
    val lifetimeIncorrect: Int = 0,
    val lifetimeTimeout: Int = 0,
    val totalGameAnswers: Int = 0,
    val totalStudyAnswers: Int = 0,
    val lastRankChangeAt: Long? = null,
    val answersSinceRankChange: Int = 0,
    val totalXp: Long = 0L,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
