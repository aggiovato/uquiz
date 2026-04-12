package com.uquiz.android.data.content.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.content.entity.FolderEntity
import com.uquiz.android.data.content.entity.PackEntity

/** Relación Room: carpeta con sus packs. Usada para mostrar el contenido de una carpeta. */
data class FolderWithPacks(
    @Embedded
    val folder: FolderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "folderId"
    )
    val packs: List<PackEntity>
)
