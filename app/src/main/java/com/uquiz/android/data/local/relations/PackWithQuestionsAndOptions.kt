package com.uquiz.android.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.uquiz.android.data.local.entity.PackEntity
import com.uquiz.android.data.local.entity.PackQuestionEntity

/**
 * Room relation: Pack with Questions and their Options (nested)
 *
 * Used for loading complete pack data for Study/Game modes
 * This is a nested relation - each question includes its options
 */
data class PackWithQuestionsAndOptions(
    @Embedded
    val pack: PackEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PackQuestionEntity::class,
            parentColumn = "packId",
            entityColumn = "questionId"
        ),
        entity = com.uquiz.android.data.local.entity.QuestionEntity::class
    )
    val questionsWithOptions: List<QuestionWithOptions>
)
