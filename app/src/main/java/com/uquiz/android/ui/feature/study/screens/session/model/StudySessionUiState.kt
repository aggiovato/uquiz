package com.uquiz.android.ui.feature.study.screens.session.model

import com.uquiz.android.domain.content.enums.DifficultyLevel

data class StudyOptionUiModel(
    val optionId: String,
    val label: String,
    val markdownText: String,
    val isCorrect: Boolean
)

data class StudyQuestionUiModel(
    val questionId: String,
    val markdownText: String,
    val explanationMarkdown: String?,
    val difficulty: DifficultyLevel,
    val options: List<StudyOptionUiModel>
)

data class StudySavedAnswerUiModel(
    val pickedOptionId: String?,
    val isCorrect: Boolean,
    val timeMs: Long
)

data class StudySessionUiState(
    val isLoading: Boolean = true,
    val attemptId: String? = null,
    val packId: String,
    val packTitle: String = "",
    val questions: List<StudyQuestionUiModel> = emptyList(),
    val currentIndex: Int = 0,
    // Índice más alto visitado en la sesión; la barra de progreso se basa en este valor.
    val maxReachedIndex: Int = 0,
    val selectedOptionId: String? = null,
    val answersByQuestionId: Map<String, StudySavedAnswerUiModel> = emptyMap()
) {
    val currentQuestion: StudyQuestionUiModel?
        get() = questions.getOrNull(currentIndex)

    val currentSavedAnswer: StudySavedAnswerUiModel?
        get() = currentQuestion?.let { answersByQuestionId[it.questionId] }

    val totalQuestions: Int
        get() = questions.size

    val answeredCount: Int
        get() = answersByQuestionId.size

    val correctCount: Int
        get() = answersByQuestionId.values.count { it.isCorrect }

    val effectiveTimeMs: Long
        get() = answersByQuestionId.values.sumOf { it.timeMs }

    val isCurrentVerified: Boolean
        get() = currentSavedAnswer != null

    val canVerify: Boolean
        get() = !isCurrentVerified && selectedOptionId != null

    val canGoPrevious: Boolean
        get() = currentIndex > 0

    val isLastQuestion: Boolean
        get() = currentIndex == questions.lastIndex

    val canFinish: Boolean
        get() = answeredCount == totalQuestions && totalQuestions > 0

    // La barra de progreso avanza al navegar hacia adelante, no al verificar.
    val progressFraction: Float
        get() = if (totalQuestions == 0) 0f
                else (maxReachedIndex + 1).toFloat() / totalQuestions
}
