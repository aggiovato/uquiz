package com.uquiz.android.ui.feature.study.screens.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.feature.study.screens.intro.model.StudyIntroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class StudyIntroViewModel(
    private val packRepository: PackRepository,
    private val attemptRepository: AttemptRepository,
    private val packId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudyIntroUiState(packId = packId))
    val uiState: StateFlow<StudyIntroUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val pack = packRepository.getById(packId)
            val questions = packRepository.getWithQuestions(packId)
            val activeAttempt = attemptRepository.getActiveStudyAttempt(packId)
            val answeredCount = activeAttempt?.let { attemptRepository.getAnswers(it.id).size } ?: 0

            _uiState.value =
                StudyIntroUiState(
                    isLoading = false,
                    packId = packId,
                    packTitle = pack?.title.orEmpty(),
                    questionCount = questions.size,
                    averageDifficulty = averageDifficulty(questions.map { it.question.difficulty }),
                    hasActiveAttempt = activeAttempt != null,
                    activeProgress = answeredCount,
                )
        }
    }

    class Factory(
        private val packRepository: PackRepository,
        private val attemptRepository: AttemptRepository,
        private val packId: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = StudyIntroViewModel(packRepository, attemptRepository, packId) as T
    }
}

private fun averageDifficulty(values: List<DifficultyLevel>): DifficultyLevel? {
    if (values.isEmpty()) return null
    return when (values.map { it.ordinal }.average().roundToInt()) {
        0 -> DifficultyLevel.EASY
        1 -> DifficultyLevel.MEDIUM
        2 -> DifficultyLevel.HARD
        else -> DifficultyLevel.EXPERT
    }
}
