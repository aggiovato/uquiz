package com.uquiz.android.data.content.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class FolderWithCountsEntity(
    @Embedded val folder: FolderEntity,
    @ColumnInfo(name = "childFolderCount") val childFolderCount: Int,
    @ColumnInfo(name = "packCount") val packCount: Int
)
