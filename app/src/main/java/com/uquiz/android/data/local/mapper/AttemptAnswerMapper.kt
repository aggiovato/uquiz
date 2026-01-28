package com.uquiz.android.data.local.mapper

import com.uquiz.android.data.local.entity.AttemptAnswerEntity
import com.uquiz.android.data.local.entity.AuditTimestamps
import com.uquiz.android.domain.model.AttemptAnswer

/**
 * Mapper: AttemptAnswerEntity <-> AttemptAnswer (domain model)
 */
object AttemptAnswerMapper {

    fun toEntity(answer: AttemptAnswer): AttemptAnswerEntity {
        return AttemptAnswerEntity(
            id = answer.id,
            attemptId = answer.attemptId,
            questionId = answer.questionId,
            pickedOptionId = answer.pickedOptionId,
            isCorrect = answer.isCorrect,
            timeMs = answer.timeMs,
            timeLimitMs = answer.timeLimitMs,
            audit = AuditTimestamps(
                createdAt = answer.createdAt,
                updatedAt = answer.updatedAt
            )
        )
    }

    fun toModel(entity: AttemptAnswerEntity): AttemptAnswer {
        return AttemptAnswer(
            id = entity.id,
            attemptId = entity.attemptId,
            questionId = entity.questionId,
            pickedOptionId = entity.pickedOptionId,
            isCorrect = entity.isCorrect,
            timeMs = entity.timeMs,
            timeLimitMs = entity.timeLimitMs,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelList(entities: List<AttemptAnswerEntity>): List<AttemptAnswer> {
        return entities.map { toModel(it) }
    }

    fun toEntityList(answers: List<AttemptAnswer>): List<AttemptAnswerEntity> {
        return answers.map { toEntity(it) }
    }
}
