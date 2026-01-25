package com.uquiz.android.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.local.entity.OptionEntity
import com.uquiz.android.data.local.entity.QuestionEntity

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
