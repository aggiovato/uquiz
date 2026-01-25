package com.uquiz.android.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "pack_questions",
    primaryKeys = ["packId", "questionId"],
    indices = [Index("packId"), Index("questionId")]
)
data class PackQuestionEntity(
    val packId: String,
    val questionId: String,
    val position: Int
)
