package com.uquiz.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val correctOptionIds: List<String>
)
