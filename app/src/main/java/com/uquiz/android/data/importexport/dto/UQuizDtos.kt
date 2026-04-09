package com.uquiz.android.data.importexport.dto

import kotlinx.serialization.Serializable

@Serializable
data class UQuizFileDto(
    val schemaVersion: Int = 1,
    val format: String = "uquiz",
    val exportedAt: String,
    val folder: UQuizFolderDto? = null,
    val pack: UQuizPackDto? = null,
)

@Serializable
data class UQuizFolderDto(
    val name: String,
    val color: String? = null,
    val icon: String? = null,
    val folders: List<UQuizFolderDto> = emptyList(),
    val packs: List<UQuizPackDto> = emptyList(),
)

@Serializable
data class UQuizPackDto(
    val title: String,
    val description: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val questions: List<UQuizQuestionDto>,
)

@Serializable
data class UQuizQuestionDto(
    val text: String,
    val explanation: String? = null,
    val difficulty: String = "MEDIUM",
    val options: List<UQuizOptionDto>,
)

@Serializable
data class UQuizOptionDto(
    val label: String,
    val text: String,
    val isCorrect: Boolean,
    val position: Int,
)
