package com.uquiz.android.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.uquiz.android.data.local.entity.FolderEntity

/**
 * Room relation: Folder with its Child Folders
 *
 * Used for building folder hierarchy (nested folders)
 */
data class FolderWithChildren(
    @Embedded
    val folder: FolderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    )
    val children: List<FolderEntity>
)
