package com.uquiz.android.data.stats.mapper

import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.data.stats.entity.PackStatsEntity
import com.uquiz.android.domain.stats.model.PackStats

/**
 * Mapeo bidireccional entre [com.uquiz.android.data.stats.entity.PackStatsEntity] y
 * [com.uquiz.android.domain.stats.model.PackStats].
 */
internal object PackStatsMapper {
    fun toEntity(model: PackStats): PackStatsEntity =
        PackStatsEntity(
            id = model.id,
            userId = model.userId,
            packId = model.packId,
            totalSessions = model.totalSessions,
            totalStudySessions = model.totalStudySessions,
            totalGameSessions = model.totalGameSessions,
            averageAccuracyPercent = model.averageAccuracyPercent,
            averageStudyAccuracyPercent = model.averageStudyAccuracyPercent,
            averageGameAccuracyPercent = model.averageGameAccuracyPercent,
            averageDurationMs = model.averageDurationMs,
            averageStudyDurationMs = model.averageStudyDurationMs,
            averageGameDurationMs = model.averageGameDurationMs,
            bestScore = model.bestScore,
            bestStudyAccuracyPercent = model.bestStudyAccuracyPercent,
            bestGameScore = model.bestGameScore,
            lastSessionAt = model.lastSessionAt,
            lastSessionMode = model.lastSessionMode,
            mostUsedMode = model.mostUsedMode,
            dominatedQuestions = model.dominatedQuestions,
            totalQuestionsSnapshot = model.totalQuestionsSnapshot,
            progressPercent = model.progressPercent,
            audit =
                AuditTimestamps(
                    createdAt = model.createdAt,
                    updatedAt = model.updatedAt,
                ),
        )

    fun toModel(entity: PackStatsEntity): PackStats =
        PackStats(
            id = entity.id,
            userId = entity.userId,
            packId = entity.packId,
            totalSessions = entity.totalSessions,
            totalStudySessions = entity.totalStudySessions,
            totalGameSessions = entity.totalGameSessions,
            averageAccuracyPercent = entity.averageAccuracyPercent,
            averageStudyAccuracyPercent = entity.averageStudyAccuracyPercent,
            averageGameAccuracyPercent = entity.averageGameAccuracyPercent,
            averageDurationMs = entity.averageDurationMs,
            averageStudyDurationMs = entity.averageStudyDurationMs,
            averageGameDurationMs = entity.averageGameDurationMs,
            bestScore = entity.bestScore,
            bestStudyAccuracyPercent = entity.bestStudyAccuracyPercent,
            bestGameScore = entity.bestGameScore,
            lastSessionAt = entity.lastSessionAt,
            lastSessionMode = entity.lastSessionMode,
            mostUsedMode = entity.mostUsedMode,
            dominatedQuestions = entity.dominatedQuestions,
            totalQuestionsSnapshot = entity.totalQuestionsSnapshot,
            progressPercent = entity.progressPercent,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt,
        )
}
