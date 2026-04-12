package com.uquiz.android.data.stats.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.attempts.enums.AttemptMode

/**
 * Fila de la tabla `pack_stats`. Acumula estadísticas históricas de un usuario para un pack concreto:
 * sesiones totales, precisión media, mejor puntuación, progreso de mastery y más.
 *
 * @see com.uquiz.android.domain.stats.model.PackStats
 */
@Entity(
    tableName = "pack_stats",
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("userId"), Index("packId")],
)
data class PackStatsEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val packId: String,
    val totalSessions: Int = 0,
    val totalStudySessions: Int = 0,
    val totalGameSessions: Int = 0,
    val averageAccuracyPercent: Int? = null,
    val averageStudyAccuracyPercent: Int? = null,
    val averageGameAccuracyPercent: Int? = null,
    val averageDurationMs: Long? = null,
    val averageStudyDurationMs: Long? = null,
    val averageGameDurationMs: Long? = null,
    val bestScore: Int? = null,
    val bestStudyAccuracyPercent: Int? = null,
    val bestGameScore: Int? = null,
    val lastSessionAt: Long? = null,
    val lastSessionMode: AttemptMode? = null,
    val mostUsedMode: AttemptMode? = null,
    val dominatedQuestions: Int = 0,
    val totalQuestionsSnapshot: Int = 0,
    val progressPercent: Int = 0,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
