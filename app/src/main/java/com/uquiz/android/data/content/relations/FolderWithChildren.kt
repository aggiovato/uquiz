package com.uquiz.android.data.content.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.content.entity.FolderEntity

/** Relación Room: carpeta con sus subcarpetas directas. Usada para construir la jerarquía de carpetas. */
data class FolderWithChildren(
    @Embedded
    val folder: FolderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    )
    val children: List<FolderEntity>
)
