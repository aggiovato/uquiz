package com.uquiz.android.data.content.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.model.AuditTimestamps

/**
 * Fila de la tabla `folders`. Representa una carpeta que agrupa packs.
 * El campo [parentId] es `null` para carpetas raíz; no nulo para subcarpetas.
 *
 * @see com.uquiz.android.domain.content.model.Folder
 */
@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("parentId")],
)
data class FolderEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val parentId: String? = null,
    val colorHex: String? = null,
    val icon: String? = null,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
