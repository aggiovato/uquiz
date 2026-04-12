package com.uquiz.android.data.content.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.content.entity.PackQuestionEntity

/**
 * Relación Room anidada: pack con sus preguntas y las opciones de cada pregunta.
 * Usada para cargar los datos completos de una sesión de estudio o partida.
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
        entity = com.uquiz.android.data.content.entity.QuestionEntity::class
    )
    val questionsWithOptions: List<QuestionWithOptions>
)
