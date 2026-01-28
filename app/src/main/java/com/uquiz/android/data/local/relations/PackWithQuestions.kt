package com.uquiz.android.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.uquiz.android.data.local.entity.PackEntity
import com.uquiz.android.data.local.entity.PackQuestionEntity
import com.uquiz.android.data.local.entity.QuestionEntity

/**
 * Room relation: Pack with its Questions (through junction table)
 *
 * Used for loading a pack's complete question set
 */
data class PackWithQuestions(
    @Embedded
    val pack: PackEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PackQuestionEntity::class,
            parentColumn = "packId",
            entityColumn = "questionId"
        )
    )
    val questions: List<QuestionEntity>
)
