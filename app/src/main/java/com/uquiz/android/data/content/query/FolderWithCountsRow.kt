package com.uquiz.android.data.content.query

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.uquiz.android.data.content.entity.FolderEntity

/** Resultado de [com.uquiz.android.data.content.dao.FolderDao.observeByParentWithCounts]: carpeta con conteos de subcarpetas y packs. */
data class FolderWithCountsRow(
    @Embedded val folder: FolderEntity,
    @ColumnInfo(name = "childFolderCount") val childFolderCount: Int,
    @ColumnInfo(name = "packCount") val packCount: Int,
)
