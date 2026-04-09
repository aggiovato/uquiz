package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.data.content.entity.FolderEntity
import com.uquiz.android.data.content.entity.FolderWithCountsEntity
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.projection.FolderWithCounts

object FolderMapper {

    fun toEntity(folder: Folder): FolderEntity {
        return FolderEntity(
            id = folder.id,
            name = folder.name,
            parentId = folder.parentId,
            colorHex = folder.colorHex,
            icon = folder.icon,
            audit = AuditTimestamps(
                createdAt = folder.createdAt,
                updatedAt = folder.updatedAt
            )
        )
    }

    fun toModel(entity: FolderEntity): Folder {
        return Folder(
            id = entity.id,
            name = entity.name,
            parentId = entity.parentId,
            colorHex = entity.colorHex,
            icon = entity.icon,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelWithCounts(entity: FolderWithCountsEntity): FolderWithCounts =
        FolderWithCounts(toModel(entity.folder), entity.childFolderCount, entity.packCount)

    fun toModelList(entities: List<FolderEntity>): List<Folder> {
        return entities.map { toModel(it) }
    }

    fun toEntityList(folders: List<Folder>): List<FolderEntity> {
        return folders.map { toEntity(it) }
    }
}
