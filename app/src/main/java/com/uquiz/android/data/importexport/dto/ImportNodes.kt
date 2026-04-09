package com.uquiz.android.data.importexport.dto

import com.uquiz.android.domain.content.enums.DifficultyLevel

data class ImportFolderNode(
    val name: String,
    val colorHex: String,
    val icon: String,
    val folders: List<ImportFolderNode>,
    val packs: List<ImportPackNode>,
)

data class ImportPackNode(
    val title: String,
    val description: String?,
    val colorHex: String,
    val icon: String,
    val questions: List<ImportQuestionNode>,
)

data class ImportQuestionNode(
    val text: String,
    val explanation: String?,
    val difficulty: DifficultyLevel,
    val options: List<ImportOptionNode>,
)

data class ImportOptionNode(
    val label: String,
    val text: String,
    val isCorrect: Boolean,
    val position: Int,
)
