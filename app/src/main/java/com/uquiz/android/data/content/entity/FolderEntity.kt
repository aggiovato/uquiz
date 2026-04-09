package com.uquiz.android.data.content.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.AuditTimestamps

@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("parentId")]
)
data class FolderEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val parentId: String? = null,
    val colorHex: String? = null,
    val icon: String? = null,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
