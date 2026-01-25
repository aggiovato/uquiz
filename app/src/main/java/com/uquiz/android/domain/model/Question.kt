package com.uquiz.android.domain.model

data class Question(
    val id: String,
    val text: String,
    val options: List<Option>,
    val correctOptionIds: List<String>
)
