package com.uquiz.android.data.attempts.mapper

import com.uquiz.android.data.attempts.entity.AttemptEntity
import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.domain.attempts.model.Attempt

/**
 * Mapper: AttemptEntity <-> Attempt (domain model)
 */
object AttemptMapper {

    fun toEntity(attempt: Attempt): AttemptEntity {
        return AttemptEntity(
            id = attempt.id,
            userId = attempt.userId,
            mode = attempt.mode,
            status = attempt.status,
            startedAt = attempt.startedAt,
            completedAt = attempt.completedAt,
            durationMs = attempt.durationMs,
            score = attempt.score,
            primaryPackId = attempt.primaryPackId,
            totalQuestions = attempt.totalQuestions,
            correctAnswers = attempt.correctAnswers,
            currentQuestionIndex = attempt.currentQuestionIndex,
            audit = AuditTimestamps(
                createdAt = attempt.createdAt,
                updatedAt = attempt.updatedAt
            )
        )
    }

    fun toModel(entity: AttemptEntity): Attempt {
        return Attempt(
            id = entity.id,
            userId = entity.userId,
            mode = entity.mode,
            status = entity.status,
            startedAt = entity.startedAt,
            completedAt = entity.completedAt,
            durationMs = entity.durationMs,
            score = entity.score,
            primaryPackId = entity.primaryPackId,
            totalQuestions = entity.totalQuestions,
            correctAnswers = entity.correctAnswers,
            currentQuestionIndex = entity.currentQuestionIndex,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelList(entities: List<AttemptEntity>): List<Attempt> {
        return entities.map { toModel(it) }
    }

    fun toEntityList(attempts: List<Attempt>): List<AttemptEntity> {
        return attempts.map { toEntity(it) }
    }
}
