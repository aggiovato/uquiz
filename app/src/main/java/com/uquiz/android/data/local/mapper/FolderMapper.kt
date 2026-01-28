package com.uquiz.android.data.local.mapper

import com.uquiz.android.data.local.entity.AuditTimestamps
import com.uquiz.android.data.local.entity.FolderEntity
import com.uquiz.android.domain.model.Folder

/**
 * Mapper: FolderEntity <-> Folder (domain model)
 */
object FolderMapper {

    fun toEntity(folder: Folder): FolderEntity {
        return FolderEntity(
            id = folder.id,
            name = folder.name,
            parentId = folder.parentId,
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
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelList(entities: List<FolderEntity>): List<Folder> {
        return entities.map { toModel(it) }
    }

    fun toEntityList(folders: List<Folder>): List<FolderEntity> {
        return folders.map { toEntity(it) }
    }
}
