package com.uquiz.android.data.stats.mapper

import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.data.stats.entity.QuestionStatsEntity
import com.uquiz.android.domain.stats.model.QuestionStats

object QuestionStatsMapper {

    fun toEntity(model: QuestionStats): QuestionStatsEntity = QuestionStatsEntity(
        id = model.id,
        userId = model.userId,
        questionId = model.questionId,
        packId = model.packId,
        totalAttempts = model.totalAttempts,
        totalCorrect = model.totalCorrect,
        totalIncorrect = model.totalIncorrect,
        totalTimeout = model.totalTimeout,
        studyAttempts = model.studyAttempts,
        studyCorrect = model.studyCorrect,
        gameAttempts = model.gameAttempts,
        gameCorrect = model.gameCorrect,
        avgGameTimeMs = model.avgGameTimeMs,
        bestGameTimeMs = model.bestGameTimeMs,
        currentCorrectStreak = model.currentCorrectStreak,
        bestCorrectStreak = model.bestCorrectStreak,
        masteryScore = model.masteryScore,
        masteryLevel = model.masteryLevel,
        lastAnsweredAt = model.lastAnsweredAt,
        audit = AuditTimestamps(
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    )

    fun toModel(entity: QuestionStatsEntity): QuestionStats = QuestionStats(
        id = entity.id,
        userId = entity.userId,
        questionId = entity.questionId,
        packId = entity.packId,
        totalAttempts = entity.totalAttempts,
        totalCorrect = entity.totalCorrect,
        totalIncorrect = entity.totalIncorrect,
        totalTimeout = entity.totalTimeout,
        studyAttempts = entity.studyAttempts,
        studyCorrect = entity.studyCorrect,
        gameAttempts = entity.gameAttempts,
        gameCorrect = entity.gameCorrect,
        avgGameTimeMs = entity.avgGameTimeMs,
        bestGameTimeMs = entity.bestGameTimeMs,
        currentCorrectStreak = entity.currentCorrectStreak,
        bestCorrectStreak = entity.bestCorrectStreak,
        masteryScore = entity.masteryScore,
        masteryLevel = entity.masteryLevel,
        lastAnsweredAt = entity.lastAnsweredAt,
        createdAt = entity.audit.createdAt,
        updatedAt = entity.audit.updatedAt
    )
}
