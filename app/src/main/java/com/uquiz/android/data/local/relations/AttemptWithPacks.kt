package com.uquiz.android.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.uquiz.android.data.local.entity.AttemptEntity
import com.uquiz.android.data.local.entity.AttemptPackEntity
import com.uquiz.android.data.local.entity.PackEntity

/**
 * Room relation: Attempt with its Packs (through junction table)
 *
 * Used for showing which packs were used in a Game mode session
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
