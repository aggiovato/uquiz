package com.uquiz.android.data.ranking.mapper

import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.data.ranking.entity.UserRankEntity
import com.uquiz.android.domain.ranking.model.UserRankState

object UserRankMapper {

    fun toEntity(model: UserRankState): UserRankEntity = UserRankEntity(
        userId = model.userId,
        currentRank = model.currentRank,
        mmr = model.mmr,
        perfEwma = model.perfEwma,
        lifetimeCorrect = model.lifetimeCorrect,
        lifetimeIncorrect = model.lifetimeIncorrect,
        lifetimeTimeout = model.lifetimeTimeout,
        totalGameAnswers = model.totalGameAnswers,
        totalStudyAnswers = model.totalStudyAnswers,
        lastRankChangeAt = model.lastRankChangeAt,
        answersSinceRankChange = model.answersSinceRankChange,
        totalXp = model.totalXp,
        audit = AuditTimestamps(
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    )

    fun toModel(entity: UserRankEntity): UserRankState = UserRankState(
        userId = entity.userId,
        currentRank = entity.currentRank,
        mmr = entity.mmr,
        perfEwma = entity.perfEwma,
        lifetimeCorrect = entity.lifetimeCorrect,
        lifetimeIncorrect = entity.lifetimeIncorrect,
        lifetimeTimeout = entity.lifetimeTimeout,
        totalGameAnswers = entity.totalGameAnswers,
        totalStudyAnswers = entity.totalStudyAnswers,
        lastRankChangeAt = entity.lastRankChangeAt,
        answersSinceRankChange = entity.answersSinceRankChange,
        totalXp = entity.totalXp,
        createdAt = entity.audit.createdAt,
        updatedAt = entity.audit.updatedAt
    )
}
