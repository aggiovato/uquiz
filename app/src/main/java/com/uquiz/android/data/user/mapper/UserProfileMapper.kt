package com.uquiz.android.data.user.mapper

import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.data.user.entity.UserProfileEntity
import com.uquiz.android.domain.user.model.UserProfile

/**
 * Mapeo bidireccional entre [com.uquiz.android.data.user.entity.UserProfileEntity] y
 * [com.uquiz.android.domain.user.model.UserProfile].
 */
internal object UserProfileMapper {
    fun toEntity(profile: UserProfile): UserProfileEntity =
        UserProfileEntity(
            id = profile.id,
            displayName = profile.displayName,
            avatarIcon = profile.avatarIcon,
            avatarImageUri = profile.avatarImageUri,
            audit =
                AuditTimestamps(
                    createdAt = profile.createdAt,
                    updatedAt = profile.updatedAt,
                ),
        )

    fun toModel(entity: UserProfileEntity): UserProfile =
        UserProfile(
            id = entity.id,
            displayName = entity.displayName,
            avatarIcon = entity.avatarIcon,
            avatarImageUri = entity.avatarImageUri,
            createdAt = entity.audit.createdAt,
            updatedAt = entity.audit.updatedAt,
        )
}
