package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.content.entity.QuestionEntity
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.content.model.Question

/**
 * Mapeo bidireccional entre [com.uquiz.android.data.content.entity.QuestionEntity] y
 * [com.uquiz.android.domain.content.model.Question].
 */
internal object QuestionMapper {
    fun toEntity(question: Question): QuestionEntity =
        QuestionEntity(
            id = question.id,
            text = question.text,
            explanation = question.explanation,
            difficulty = question.difficulty,
            audit =
                AuditTimestamps(
                    createdAt = question.createdAt,
                    updatedAt = question.updatedAt,
                ),
        )

    fun toModel(entity: QuestionEntity): Question =
        Question(
            id = entity.id,
            text = entity.text,
            explanation = entity.explanation,
            difficulty = entity.difficulty,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt,
        )

    fun toModelList(entities: List<QuestionEntity>): List<Question> = entities.map { toModel(it) }

    fun toEntityList(questions: List<Question>): List<QuestionEntity> = questions.map { toEntity(it) }
}
