package com.uquiz.android.data.content.mapper

import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.content.entity.PackWithQuestionCountEntity
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.projection.PackWithQuestionCount

/**
 * Mapeo bidireccional entre [com.uquiz.android.data.content.entity.PackEntity] y
 * [com.uquiz.android.domain.content.model.Pack], y conversión de
 * [com.uquiz.android.data.content.entity.PackWithQuestionCountEntity] a proyección de dominio.
 */
internal object PackMapper {
    fun toEntity(pack: Pack): PackEntity =
        PackEntity(
            id = pack.id,
            title = pack.title,
            description = pack.description,
            folderId = pack.folderId,
            icon = pack.icon,
            colorHex = pack.colorHex,
            audit =
                AuditTimestamps(
                    createdAt = pack.createdAt,
                    updatedAt = pack.updatedAt,
                ),
        )

    fun toModel(entity: PackEntity): Pack =
        Pack(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            folderId = entity.folderId,
            icon = entity.icon,
            colorHex = entity.colorHex,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt,
        )

    fun toModelList(entities: List<PackEntity>): List<Pack> = entities.map { toModel(it) }

    fun toModelWithQuestionCount(entity: PackWithQuestionCountEntity): PackWithQuestionCount =
        PackWithQuestionCount(
            pack = toModel(entity.pack),
            questionCount = entity.questionCount,
        )

    fun toEntityList(packs: List<Pack>): List<PackEntity> = packs.map { toEntity(it) }
}
