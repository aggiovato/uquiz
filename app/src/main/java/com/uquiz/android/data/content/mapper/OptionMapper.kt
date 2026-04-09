package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.data.content.entity.OptionEntity
import com.uquiz.android.domain.content.model.Option

/**
 * Mapper: OptionEntity <-> Option (domain model)
 */
object OptionMapper {

    fun toEntity(option: Option): OptionEntity {
        return OptionEntity(
            id = option.id,
            questionId = option.questionId,
            label = option.label,
            text = option.text,
            isCorrect = option.isCorrect,
            audit = AuditTimestamps(
                createdAt = option.createdAt,
                updatedAt = option.updatedAt
            )
        )
    }

    fun toModel(entity: OptionEntity): Option {
        return Option(
            id = entity.id,
            questionId = entity.questionId,
            label = entity.label,
            text = entity.text,
            isCorrect = entity.isCorrect,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelList(entities: List<OptionEntity>): List<Option> {
        return entities.map { toModel(it) }
    }

    fun toEntityList(options: List<Option>): List<OptionEntity> {
        return options.map { toEntity(it) }
    }
}
