package com.uquiz.android.data.content.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.content.entity.PackQuestionEntity
import com.uquiz.android.data.content.entity.QuestionEntity

/** Relación Room: pack con todas sus preguntas (vía tabla de unión `pack_questions`). */
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
