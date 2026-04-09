package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.data.content.entity.QuestionEntity
import com.uquiz.android.domain.content.model.Question

/**
 * Mapper: QuestionEntity <-> Question (domain model)
 */
object QuestionMapper {

    fun toEntity(question: Question): QuestionEntity {
        return QuestionEntity(
            id = question.id,
            text = question.text,
            explanation = question.explanation,
            difficulty = question.difficulty,
            audit = AuditTimestamps(
                createdAt = question.createdAt,
                updatedAt = question.updatedAt
            )
        )
    }

    fun toModel(entity: QuestionEntity): Question {
        return Question(
            id = entity.id,
            text = entity.text,
            explanation = entity.explanation,
            difficulty = entity.difficulty,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelList(entities: List<QuestionEntity>): List<Question> {
        return entities.map { toModel(it) }
    }

    fun toEntityList(questions: List<Question>): List<QuestionEntity> {
        return questions.map { toEntity(it) }
    }
}
