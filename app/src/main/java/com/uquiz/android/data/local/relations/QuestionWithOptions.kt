package com.uquiz.android.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.local.entity.OptionEntity
import com.uquiz.android.data.local.entity.QuestionEntity

/**
 * Room relation: Question with its Options
 *
 * Used for displaying a question with all answer choices
 */
data class QuestionWithOptions(
    @Embedded
    val question: QuestionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val options: List<OptionEntity>
)
