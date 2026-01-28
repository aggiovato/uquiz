package com.uquiz.android.data.local.mapper

import com.uquiz.android.data.local.entity.AuditTimestamps
import com.uquiz.android.data.local.entity.PackEntity
import com.uquiz.android.domain.model.Pack

/**
 * Mapper: PackEntity <-> Pack (domain model)
 */
object PackMapper {

    fun toEntity(pack: Pack): PackEntity {
        return PackEntity(
            id = pack.id,
            title = pack.title,
            description = pack.description,
            folderId = pack.folderId,
            icon = pack.icon,
            audit = AuditTimestamps(
                createdAt = pack.createdAt,
                updatedAt = pack.updatedAt
            )
        )
    }

    fun toModel(entity: PackEntity): Pack {
        return Pack(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            folderId = entity.folderId,
            icon = entity.icon,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelList(entities: List<PackEntity>): List<Pack> {
        return entities.map { toModel(it) }
    }

    fun toEntityList(packs: List<Pack>): List<PackEntity> {
        return packs.map { toEntity(it) }
    }
}
