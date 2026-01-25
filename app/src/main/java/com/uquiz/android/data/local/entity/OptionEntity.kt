package com.uquiz.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns =  ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("questionId")]
)
data class OptionEntity(
    @PrimaryKey
    val id: String,
    val questionId: String,
    val text: String
)
