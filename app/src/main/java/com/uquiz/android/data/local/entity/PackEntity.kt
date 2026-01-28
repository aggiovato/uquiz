package com.uquiz.android.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "packs",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("folderId")]
)
data class PackEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String? = null,
    val folderId: String,
    val icon: String? = null,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
