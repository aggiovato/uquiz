package com.uquiz.android.data.content.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.content.entity.FolderEntity
import com.uquiz.android.data.content.entity.PackEntity

/**
 * Room relation: Folder with its Packs
 *
 * Used for displaying folder contents (packs inside)
 */
data class FolderWithPacks(
    @Embedded
    val folder: FolderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "folderId"
    )
    val packs: List<PackEntity>
)
