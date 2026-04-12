package com.uquiz.android.data.attempts.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.attempts.entity.AttemptAnswerEntity
import com.uquiz.android.data.attempts.entity.AttemptEntity

/** Relación Room: intento con todas sus respuestas. Usada para calcular resultados y estadísticas. */
data class AttemptWithAnswers(
    @Embedded
    val attempt: AttemptEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "attemptId"
    )
    val answers: List<AttemptAnswerEntity>
)
