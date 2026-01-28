package com.uquiz.android.data.local.mapper

import com.uquiz.android.data.local.relations.QuestionWithOptions as QuestionWithOptionsRelation
import com.uquiz.android.domain.model.QuestionWithOptions as QuestionWithOptionsModel

/**
 * Mapper: QuestionWithOptions (Room relation) <-> QuestionWithOptions (domain model)
 */
object QuestionWithOptionsMapper {

    fun toModel(relation: QuestionWithOptionsRelation): QuestionWithOptionsModel {
        return QuestionWithOptionsModel(
            question = QuestionMapper.toModel(relation.question),
            options = OptionMapper.toModelList(relation.options)
        )
    }

    fun toModelList(relations: List<QuestionWithOptionsRelation>): List<QuestionWithOptionsModel> {
        return relations.map { toModel(it) }
    }
}
