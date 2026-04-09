package com.uquiz.android.ui.feature.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.Option
import com.uquiz.android.domain.content.model.Question
import com.uquiz.android.domain.content.repository.CreateOptionData
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.content.repository.QuestionRepository
import com.uquiz.android.ui.feature.question.model.EditableOptionUiModel
import com.uquiz.android.ui.feature.question.model.QuestionMode
import com.uquiz.android.ui.feature.question.model.QuestionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class QuestionViewModel(
    private val questionRepository: QuestionRepository,
    private val packRepository: PackRepository,
    private val packId: String,
    private val questionId: String?
) : ViewModel() {

    private val mode = if (questionId == null) QuestionMode.CREATE else QuestionMode.EDIT
    private var existingQuestion: Question? = null

    private val _uiState = MutableStateFlow(
        QuestionUiState(
            mode = mode,
            packId = packId,
            questionId = questionId,
            isLoading = questionId != null,
            options = defaultOptions()
        )
    )
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    init {
        if (questionId != null) {
            viewModelScope.launch {
                val questionWithOptions = questionRepository.getWithOptions(questionId)
                val question = questionWithOptions?.question
                if (question == null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                existingQuestion = question
                val firstCorrectId = questionWithOptions.options.firstOrNull { option -> option.isCorrect }?.id
                val mappedOptions = questionWithOptions.options.map { option ->
                    EditableOptionUiModel(
                        id = option.id,
                        text = option.text,
                        isCorrect = option.id == firstCorrectId
                    )
                }.padToMinimum()

                _uiState.value = QuestionUiState(
                    mode = QuestionMode.EDIT,
                    packId = packId,
                    questionId = questionId,
                    isLoading = false,
                    questionMarkdown = question.text,
                    explanationMarkdown = question.explanation.orEmpty(),
                    options = mappedOptions,
                    difficulty = if (question.difficulty == DifficultyLevel.EXPERT) {
                        DifficultyLevel.HARD
                    } else {
                        question.difficulty
                    }
                )
            }
        }
    }

    fun updateQuestionMarkdown(value: String) {
        _uiState.update { it.copy(questionMarkdown = value) }
    }

    fun updateExplanationMarkdown(value: String) {
        _uiState.update { it.copy(explanationMarkdown = value) }
    }

    fun updateDifficulty(value: DifficultyLevel) {
        _uiState.update { it.copy(difficulty = value) }
    }

    fun updateOptionText(optionId: String, value: String) {
        _uiState.update { state ->
            state.copy(
                options = state.options.map { option ->
                    if (option.id == optionId) option.copy(text = value) else option
                }
            )
        }
    }

    fun selectCorrectOption(optionId: String) {
        _uiState.update { state ->
            state.copy(
                options = state.options.map { option ->
                    option.copy(isCorrect = option.id == optionId)
                }
            )
        }
    }

    fun addOption() {
        _uiState.update { state ->
            state.copy(options = state.options + EditableOptionUiModel(id = tempOptionId(), text = ""))
        }
    }

    fun removeOption(optionId: String) {
        _uiState.update { state ->
            if (state.options.size <= 2) return@update state
            val updated = state.options.filterNot { it.id == optionId }
            state.copy(
                options = if (updated.any { it.isCorrect }) updated else updated.map { it.copy(isCorrect = false) }
            )
        }
    }

    suspend fun save(): Boolean {
        val state = _uiState.value
        if (!state.canSave) return false

        val cleanedOptions = state.options
            .filter { it.text.isNotBlank() }
            .mapIndexed { index, option ->
                option to buildOptionLabel(index)
            }

        val explanation = state.explanationMarkdown.trim().ifBlank { null }

        return if (mode == QuestionMode.CREATE) {
            val createdQuestion = questionRepository.createQuestion(
                text = state.questionMarkdown.trim(),
                explanation = explanation,
                difficulty = state.difficulty,
                options = cleanedOptions.map { (option, label) ->
                    CreateOptionData(
                        label = label,
                        text = option.text.trim(),
                        isCorrect = option.isCorrect
                    )
                }
            )
            val sortOrder = packRepository.getQuestionCount(packId)
            packRepository.addQuestionToPack(packId, createdQuestion.id, sortOrder)
            true
        } else {
            val question = existingQuestion ?: return false
            questionRepository.updateQuestion(
                question.copy(
                    text = state.questionMarkdown.trim(),
                    explanation = explanation,
                    difficulty = state.difficulty
                )
            )
            questionRepository.updateOptions(
                question.id,
                cleanedOptions.map { (option, label) ->
                    Option(
                        id = if (option.id.startsWith("temp-")) UUID.randomUUID().toString() else option.id,
                        questionId = question.id,
                        label = label,
                        text = option.text.trim(),
                        isCorrect = option.isCorrect,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                }
            )
            true
        }
    }

    suspend fun delete(): Boolean {
        val question = existingQuestion ?: return false
        questionRepository.deleteQuestion(question.id)
        return true
    }

    class Factory(
        private val questionRepository: QuestionRepository,
        private val packRepository: PackRepository,
        private val packId: String,
        private val questionId: String?
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuestionViewModel(
                questionRepository = questionRepository,
                packRepository = packRepository,
                packId = packId,
                questionId = questionId
            ) as T
        }
    }
}

private fun defaultOptions(): List<EditableOptionUiModel> = List(4) {
    EditableOptionUiModel(id = tempOptionId(), text = "")
}

private fun List<EditableOptionUiModel>.padToMinimum(): List<EditableOptionUiModel> {
    if (size >= 4) return this
    return this + List(4 - size) { EditableOptionUiModel(id = tempOptionId(), text = "") }
}

private fun buildOptionLabel(index: Int): String {
    var number = index
    val builder = StringBuilder()
    do {
        builder.append(('A'.code + (number % 26)).toChar())
        number = number / 26 - 1
    } while (number >= 0)
    return builder.reverse().toString()
}

private fun tempOptionId(): String = "temp-${UUID.randomUUID()}"
