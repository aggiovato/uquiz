package com.uquiz.android.ui.feature.study.screens.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.core.analytics.usecase.FinalizeAttemptAnalyticsUseCase
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.feature.study.screens.session.model.StudyOptionUiModel
import com.uquiz.android.ui.feature.study.screens.session.model.StudyQuestionUiModel
import com.uquiz.android.ui.feature.study.screens.session.model.StudySavedAnswerUiModel
import com.uquiz.android.ui.feature.study.screens.session.model.StudySessionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de sesión de estudio.
 *
 * Carga el pack, crea o reanuda el intento activo y restaura las respuestas guardadas.
 * Gestiona la navegación entre preguntas, la verificación de respuestas y la finalización
 * de la sesión con persistencia del progreso.
 */
class StudySessionViewModel(
    private val packRepository: PackRepository,
    private val attemptRepository: AttemptRepository,
    private val finalizeAttemptAnalyticsUseCase: FinalizeAttemptAnalyticsUseCase,
    private val packId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudySessionUiState(packId = packId))
    val uiState: StateFlow<StudySessionUiState> = _uiState.asStateFlow()

    private var questionStartedAt: Long = 0L

    init {
        viewModelScope.launch {
            val pack = packRepository.getById(packId)
            val questions =
                packRepository.getWithQuestions(packId).map { question ->
                    StudyQuestionUiModel(
                        questionId = question.question.id,
                        markdownText = question.question.text,
                        explanationMarkdown = question.question.explanation,
                        difficulty = question.question.difficulty,
                        options =
                            question.options
                                .sortedBy { it.label }
                                .map { option ->
                                    StudyOptionUiModel(
                                        optionId = option.id,
                                        label = option.label,
                                        markdownText = option.text,
                                        isCorrect = option.isCorrect,
                                    )
                                },
                    )
                }
            val attempt = attemptRepository.startOrResumeStudyAttempt(packId, questions.size)
            val answers =
                attemptRepository.getAnswers(attempt.id).associate { answer ->
                    answer.questionId to
                        StudySavedAnswerUiModel(
                            pickedOptionId = answer.pickedOptionId,
                            isCorrect = answer.isCorrect,
                            timeMs = answer.timeMs,
                        )
                }
            val initialIndex = determineCurrentIndex(attempt.currentQuestionIndex, questions, answers)
            attemptRepository.updateCurrentQuestionIndex(attempt.id, initialIndex)
            val initialQuestion = questions.getOrNull(initialIndex)
            questionStartedAt =
                if (initialQuestion != null && answers[initialQuestion.questionId] == null) {
                    System.currentTimeMillis()
                } else {
                    0L
                }

            _uiState.value =
                StudySessionUiState(
                    isLoading = false,
                    attemptId = attempt.id,
                    packId = packId,
                    packTitle = pack?.title.orEmpty(),
                    questions = questions,
                    currentIndex = initialIndex,
                    // Al reanudar, el progreso parte desde el punto donde se dejó.
                    maxReachedIndex = initialIndex,
                    selectedOptionId = initialQuestion?.let { answers[it.questionId]?.pickedOptionId },
                    answersByQuestionId = answers,
                )
        }
    }

    fun selectOption(optionId: String) {
        val state = _uiState.value
        if (state.isCurrentVerified) return
        _uiState.value = state.copy(selectedOptionId = optionId)
    }

    fun goToPrevious() {
        val state = _uiState.value
        if (!state.canGoPrevious) return
        setCurrentIndex(state.currentIndex - 1)
    }

    fun goToNext() {
        val state = _uiState.value
        if (state.currentIndex >= state.questions.lastIndex) return
        val nextIndex = state.currentIndex + 1
        // maxReachedIndex crece al avanzar hacia territorio nuevo; nunca retrocede.
        _uiState.value = state.copy(maxReachedIndex = maxOf(state.maxReachedIndex, nextIndex))
        setCurrentIndex(nextIndex)
    }

    fun verifyCurrentAnswer() {
        val state = _uiState.value
        val attemptId = state.attemptId ?: return
        val question = state.currentQuestion ?: return
        val selectedOptionId = state.selectedOptionId ?: return
        if (state.isCurrentVerified) return

        val correctOptionId = question.options.firstOrNull { it.isCorrect }?.optionId
        val elapsed = (System.currentTimeMillis() - questionStartedAt).coerceAtLeast(0L)
        val timeMs = elapsed.coerceAtLeast(300L)
        val isCorrect = selectedOptionId == correctOptionId

        viewModelScope.launch {
            attemptRepository.recordAnswer(
                attemptId = attemptId,
                questionId = question.questionId,
                pickedOptionId = selectedOptionId,
                isCorrect = isCorrect,
                timeMs = timeMs,
            )
            val updatedAnswers =
                state.answersByQuestionId + (
                    question.questionId to
                        StudySavedAnswerUiModel(
                            pickedOptionId = selectedOptionId,
                            isCorrect = isCorrect,
                            timeMs = timeMs,
                        )
                )
            _uiState.value =
                state.copy(
                    answersByQuestionId = updatedAnswers,
                    selectedOptionId = selectedOptionId,
                )
        }
    }

    /** Completa el intento activo, calcula la puntuación y lanza la analítica de finalización. */
    suspend fun finishStudy(): String? {
        val state = _uiState.value
        val attemptId = state.attemptId ?: return null
        if (!state.canFinish) return null
        val score =
            if (state.totalQuestions > 0) {
                ((state.correctCount * 100f) / state.totalQuestions).toInt()
            } else {
                0
            }
        attemptRepository.completeAttempt(
            attemptId = attemptId,
            score = score,
            correctAnswers = state.correctCount,
            totalQuestions = state.totalQuestions,
            durationMs = state.effectiveTimeMs,
        )
        finalizeAttemptAnalyticsUseCase(attemptId)
        return attemptId
    }

    /** Persiste el índice de pregunta actual para poder reanudar la sesión desde este punto. */
    suspend fun persistCurrentProgress() {
        val state = _uiState.value
        val attemptId = state.attemptId ?: return
        attemptRepository.updateCurrentQuestionIndex(attemptId, state.currentIndex)
    }

    private fun setCurrentIndex(index: Int) {
        val state = _uiState.value
        val question = state.questions.getOrNull(index) ?: return
        val existingAnswer = state.answersByQuestionId[question.questionId]
        _uiState.value =
            state.copy(
                currentIndex = index,
                selectedOptionId = existingAnswer?.pickedOptionId,
            )
        questionStartedAt = if (existingAnswer == null) System.currentTimeMillis() else 0L

        val attemptId = state.attemptId ?: return
        viewModelScope.launch {
            attemptRepository.updateCurrentQuestionIndex(attemptId, index)
        }
    }

    class Factory(
        private val packRepository: PackRepository,
        private val attemptRepository: AttemptRepository,
        private val finalizeAttemptAnalyticsUseCase: FinalizeAttemptAnalyticsUseCase,
        private val packId: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            StudySessionViewModel(
                packRepository,
                attemptRepository,
                finalizeAttemptAnalyticsUseCase,
                packId,
            ) as T
    }
}

// Si hay preguntas sin responder se mueve a la primera; si todas están respondidas
// se respeta el índice persistido (el usuario puede repasar en cualquier orden).
private fun determineCurrentIndex(
    persistedIndex: Int,
    questions: List<StudyQuestionUiModel>,
    answers: Map<String, StudySavedAnswerUiModel>,
): Int {
    val firstUnanswered = questions.indexOfFirst { answers[it.questionId] == null }
    return when {
        firstUnanswered >= 0 -> firstUnanswered
        questions.isEmpty() -> 0
        else -> persistedIndex.coerceIn(0, questions.lastIndex)
    }
}
