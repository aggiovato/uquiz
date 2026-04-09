package com.uquiz.android.data.content.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.content.entity.OptionEntity
import com.uquiz.android.data.content.entity.QuestionEntity

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
