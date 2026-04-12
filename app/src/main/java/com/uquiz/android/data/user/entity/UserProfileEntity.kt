package com.uquiz.android.data.user.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.model.AuditTimestamps

/**
 * Fila de la tabla `user_profiles`. Almacena el nombre y avatar del usuario activo.
 *
 * @see com.uquiz.android.domain.user.model.UserProfile
 */
@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    val id: String,
    val displayName: String,
    val avatarIcon: String? = null,
    val avatarImageUri: String? = null,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
