package com.uquiz.android.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.local.entity.AttemptAnswerEntity
import com.uquiz.android.data.local.entity.AttemptEntity

/**
 * Room relation: Attempt with its Answers
 *
 * Used for displaying attempt results and statistics
 */
data class AttemptWithAnswers(
    @Embedded
    val attempt: AttemptEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "attemptId"
    )
    val answers: List<AttemptAnswerEntity>
)
