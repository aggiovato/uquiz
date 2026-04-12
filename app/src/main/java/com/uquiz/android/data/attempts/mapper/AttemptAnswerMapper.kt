package com.uquiz.android.data.attempts.mapper

import com.uquiz.android.data.attempts.entity.AttemptAnswerEntity
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.attempts.model.AttemptAnswer

/**
 * Mapeo bidireccional entre [com.uquiz.android.data.attempts.entity.AttemptAnswerEntity] y
 * [com.uquiz.android.domain.attempts.model.AttemptAnswer].
 */
internal object AttemptAnswerMapper {
    fun toEntity(answer: AttemptAnswer): AttemptAnswerEntity =
        AttemptAnswerEntity(
            id = answer.id,
            attemptId = answer.attemptId,
            questionId = answer.questionId,
            pickedOptionId = answer.pickedOptionId,
            isCorrect = answer.isCorrect,
            timeMs = answer.timeMs,
            timeLimitMs = answer.timeLimitMs,
            audit =
                AuditTimestamps(
                    createdAt = answer.createdAt,
                    updatedAt = answer.updatedAt,
                ),
        )

    fun toModel(entity: AttemptAnswerEntity): AttemptAnswer =
        AttemptAnswer(
            id = entity.id,
            attemptId = entity.attemptId,
            questionId = entity.questionId,
            pickedOptionId = entity.pickedOptionId,
            isCorrect = entity.isCorrect,
            timeMs = entity.timeMs,
            timeLimitMs = entity.timeLimitMs,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt,
        )

    fun toModelList(entities: List<AttemptAnswerEntity>): List<AttemptAnswer> = entities.map { toModel(it) }

    fun toEntityList(answers: List<AttemptAnswer>): List<AttemptAnswerEntity> = answers.map { toEntity(it) }
}
