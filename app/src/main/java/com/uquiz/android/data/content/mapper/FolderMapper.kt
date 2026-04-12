package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.content.entity.FolderEntity
import com.uquiz.android.data.content.query.FolderWithCountsRow
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.projection.FolderWithCounts

/**
 * Mapeo bidireccional entre [com.uquiz.android.data.content.entity.FolderEntity] y
 * [com.uquiz.android.domain.content.model.Folder], y conversión de
 * [com.uquiz.android.data.content.query.FolderWithCountsRow] a proyección de dominio.
 */
internal object FolderMapper {
    fun toEntity(folder: Folder): FolderEntity =
        FolderEntity(
            id = folder.id,
            name = folder.name,
            parentId = folder.parentId,
            colorHex = folder.colorHex,
            icon = folder.icon,
            audit =
                AuditTimestamps(
                    createdAt = folder.createdAt,
                    updatedAt = folder.updatedAt,
                ),
        )

    fun toModel(entity: FolderEntity): Folder =
        Folder(
            id = entity.id,
            name = entity.name,
            parentId = entity.parentId,
            colorHex = entity.colorHex,
            icon = entity.icon,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt,
        )

    fun toModelWithCounts(entity: FolderWithCountsRow): FolderWithCounts =
        FolderWithCounts(toModel(entity.folder), entity.childFolderCount, entity.packCount)

    fun toModelList(entities: List<FolderEntity>): List<Folder> = entities.map { toModel(it) }

    fun toEntityList(folders: List<Folder>): List<FolderEntity> = folders.map { toEntity(it) }
}
