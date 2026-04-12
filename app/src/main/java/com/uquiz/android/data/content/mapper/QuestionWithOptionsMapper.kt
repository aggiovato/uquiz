package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.content.relations.QuestionWithOptions as QuestionWithOptionsRelation
import com.uquiz.android.data.content.relations.OrderedQuestionWithOptions as OrderedQuestionWithOptionsRelation
import com.uquiz.android.domain.content.model.QuestionWithOptions as QuestionWithOptionsModel

/**
 * Mapeo de las relaciones Room [com.uquiz.android.data.content.relations.QuestionWithOptions]
 * y [com.uquiz.android.data.content.relations.OrderedQuestionWithOptions] al modelo de dominio
 * [com.uquiz.android.domain.content.model.QuestionWithOptions].
 */
internal object QuestionWithOptionsMapper {

    fun toModel(relation: QuestionWithOptionsRelation): QuestionWithOptionsModel {
        return QuestionWithOptionsModel(
            question = QuestionMapper.toModel(relation.question),
            options = OptionMapper.toModelList(relation.options)
        )
    }

    fun toModelList(relations: List<QuestionWithOptionsRelation>): List<QuestionWithOptionsModel> {
        return relations.map { toModel(it) }
    }

    fun toModel(relation: OrderedQuestionWithOptionsRelation): QuestionWithOptionsModel {
        return QuestionWithOptionsModel(
            question = QuestionMapper.toModel(relation.question),
            options = OptionMapper.toModelList(relation.options)
        )
    }

    fun toOrderedModelList(relations: List<OrderedQuestionWithOptionsRelation>): List<QuestionWithOptionsModel> {
        return relations.map { toModel(it) }
    }
}
