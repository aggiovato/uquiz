package com.uquiz.android.data.content.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.model.AuditTimestamps

/**
 * Fila de la tabla `packs`. Representa un conjunto de preguntas agrupadas bajo una carpeta.
 *
 * @see com.uquiz.android.domain.content.model.Pack
 */
@Entity(
    tableName = "packs",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("folderId")],
)
data class PackEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String? = null,
    val folderId: String,
    val icon: String? = null,
    val colorHex: String? = null,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
