package com.uquiz.android.data.attempts.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.uquiz.android.data.content.entity.PackEntity
import androidx.room.Index

@Entity(
    tableName = "attempt_packs",
    primaryKeys = ["attemptId", "packId"],
    foreignKeys = [
        ForeignKey(
            entity = AttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("attemptId"), Index("packId")]
)
data class AttemptPackEntity(
    val attemptId: String,
    val packId: String
)
