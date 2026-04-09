package com.uquiz.android.ui.feature.question.model

import com.uquiz.android.domain.content.enums.DifficultyLevel

enum class QuestionMode {
    CREATE,
    EDIT
}

data class EditableOptionUiModel(
    val id: String,
    val text: String,
    val isCorrect: Boolean = false
)

data class QuestionUiState(
    val mode: QuestionMode,
    val packId: String,
    val questionId: String? = null,
    val isLoading: Boolean = false,
    val questionMarkdown: String = "",
    val explanationMarkdown: String = "",
    val options: List<EditableOptionUiModel> = emptyList(),
    val difficulty: DifficultyLevel = DifficultyLevel.EASY
) {
    val canSave: Boolean
        get() {
            val nonEmptyOptions = options.filter { it.text.isNotBlank() }
            val correctCount = nonEmptyOptions.count { it.isCorrect }
            return questionMarkdown.isNotBlank() && nonEmptyOptions.size >= 2 && correctCount == 1
        }
}
