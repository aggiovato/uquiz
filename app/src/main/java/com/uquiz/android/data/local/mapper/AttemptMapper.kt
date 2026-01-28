package com.uquiz.android.data.local.mapper

import com.uquiz.android.data.local.entity.AttemptEntity
import com.uquiz.android.data.local.entity.AuditTimestamps
import com.uquiz.android.domain.model.Attempt

/**
 * Mapper: AttemptEntity <-> Attempt (domain model)
 */
object AttemptMapper {

    fun toEntity(attempt: Attempt): AttemptEntity {
        return AttemptEntity(
            id = attempt.id,
            mode = attempt.mode,
            startedAt = attempt.startedAt,
            completedAt = attempt.completedAt,
            durationMs = attempt.durationMs,
            score = attempt.score,
            primaryPackId = attempt.primaryPackId,
            totalQuestions = attempt.totalQuestions,
            correctAnswers = attempt.correctAnswers,
            audit = AuditTimestamps(
                createdAt = attempt.createdAt,
                updatedAt = attempt.updatedAt
            )
        )
    }

    fun toModel(entity: AttemptEntity): Attempt {
        return Attempt(
            id = entity.id,
            mode = entity.mode,
            startedAt = entity.startedAt,
            completedAt = entity.completedAt,
            durationMs = entity.durationMs,
            score = entity.score,
            primaryPackId = entity.primaryPackId,
            totalQuestions = entity.totalQuestions,
            correctAnswers = entity.correctAnswers,
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
