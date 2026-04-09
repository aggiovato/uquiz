package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.local.db.AuditTimestamps
import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.content.entity.PackWithQuestionCountEntity
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.projection.PackWithQuestionCount

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
            colorHex = pack.colorHex,
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
            colorHex = entity.colorHex,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt
        )
    }

    fun toModelList(entities: List<PackEntity>): List<Pack> {
        return entities.map { toModel(it) }
    }

    fun toModelWithQuestionCount(entity: PackWithQuestionCountEntity): PackWithQuestionCount {
        return PackWithQuestionCount(
            pack = toModel(entity.pack),
            questionCount = entity.questionCount
        )
    }

    fun toEntityList(packs: List<Pack>): List<PackEntity> {
        return packs.map { toEntity(it) }
    }
}
