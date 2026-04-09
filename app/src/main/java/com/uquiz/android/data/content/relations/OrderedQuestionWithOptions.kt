package com.uquiz.android.data.content.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.content.entity.OptionEntity
import com.uquiz.android.data.content.entity.QuestionEntity

data class OrderedQuestionWithOptions(
    val position: Int,
    @Embedded
    val question: QuestionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val options: List<OptionEntity>
)
