package com.uquiz.android.data.attempts.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.uquiz.android.data.attempts.entity.AttemptEntity
import com.uquiz.android.data.attempts.entity.AttemptPackEntity
import com.uquiz.android.data.content.entity.PackEntity

/**
 * Relación Room: intento con los packs usados (vía tabla de unión `attempt_packs`).
 * Usada para mostrar qué packs formaron parte de una sesión de Game mode.
 */
data class AttemptWithPacks(
    @Embedded
    val attempt: AttemptEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AttemptPackEntity::class,
            parentColumn = "attemptId",
            entityColumn = "packId"
        )
    )
    val packs: List<PackEntity>
)
