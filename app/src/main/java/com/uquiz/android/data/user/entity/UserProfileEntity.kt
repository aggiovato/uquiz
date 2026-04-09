package com.uquiz.android.data.user.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.AuditTimestamps

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    val id: String,
    val displayName: String,
    val avatarIcon: String? = null,
    val avatarImageUri: String? = null,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps()
)
