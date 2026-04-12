package com.uquiz.android.ui.feature.game.screens.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.core.game.model.GameAnswerInput
import com.uquiz.android.core.game.usecase.BuildGameAttemptPlanUseCase
import com.uquiz.android.core.game.usecase.ComputeGameScoreUseCase
import com.uquiz.android.core.game.usecase.FinalizeGameAttemptUseCase
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.feature.game.screens.session.model.GameOptionUiModel
import com.uquiz.android.ui.feature.game.screens.session.model.GameQuestionUiModel
import com.uquiz.android.ui.feature.game.screens.session.model.GameSessionUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de sesión activa del Game mode.
 *
 * Carga el plan de preguntas, gestiona el temporizador por pregunta, registra las
 * respuestas del usuario y delega la finalización en [FinalizeGameAttemptUseCase].
 * Emite un evento de navegación cuando la sesión termina o se abandona.
 */
class GameSessionViewModel(
    private val packId: String,
    private val buildGameAttemptPlanUseCase: BuildGameAttemptPlanUseCase,
    private val computeGameScoreUseCase: ComputeGameScoreUseCase,
    private val finalizeGameAttemptUseCase: FinalizeGameAttemptUseCase,
    private val attemptRepository: AttemptRepository,
    private val packRepository: PackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameSessionUiState())
    val uiState: StateFlow<GameSessionUiState> = _uiState.asStateFlow()

    private val _navEvents = MutableSharedFlow<GameSessionNavEvent>()
    val navEvents: SharedFlow<GameSessionNavEvent> = _navEvents.asSharedFlow()

    private var timerJob: Job? = null
    private var answerJob: Job? = null

    init {
        viewModelScope.launch {
            val pack = packRepository.getById(packId)
            val session = buildGameAttemptPlanUseCase(packId)
            val questionsByIdMap = packRepository.getWithQuestions(packId)
                .associateBy { it.question.id }

            val questions = session.plan
                .sortedBy { it.orderIndex }
                .mapNotNull { slot ->
                    val qwo = questionsByIdMap[slot.questionId]
                        ?: return@mapNotNull null
                    GameQuestionUiModel(
                        questionId = qwo.question.id,
                        markdownText = qwo.question.text,
                        difficulty = qwo.question.difficulty,
                        timeLimitMs = slot.timeLimitMs,
                        options = qwo.options
                            .sortedBy { it.label }
                            .map { option ->
                                GameOptionUiModel(
                                    optionId = option.id,
                                    label = option.label,
                                    markdownText = option.text,
                                    isCorrect = option.isCorrect,
                                )
                            },
                    )
                }

            // Filtra las preguntas ya respondidas en partidas reanudadas
            val existingAnswers = attemptRepository.getAnswers(session.attempt.id)
                .map { it.questionId }
                .toSet()
            val firstUnansweredIndex = questions.indexOfFirst { it.questionId !in existingAnswers }
                .coerceAtLeast(0)

            _uiState.value = GameSessionUiState(
                isLoading = false,
                packTitle = pack?.title.orEmpty(),
                attemptId = session.attempt.id,
                questions = questions,
                currentIndex = firstUnansweredIndex,
            )

            startTimerForCurrent()
        }
    }

    /** Registra la opción seleccionada por el usuario y detiene el temporizador. */
    fun selectOption(optionId: String) {
        val state = _uiState.value
        if (state.isAnswered || state.isLoading) return
        _uiState.value = state.copy(selectedOptionId = optionId)
        submitAnswer(pickedOptionId = optionId)
    }

    /**
     * Pausa la sesión activa y navega hacia atrás sin modificar el intento.
     *
     * El intento queda en estado IN_PROGRESS y aparecerá en la sección "Continuar"
     * tanto en Game home como en Home, permitiendo reanudarlo más tarde.
     */
    fun pauseAndExit() {
        timerJob?.cancel()
        answerJob?.cancel()
        viewModelScope.launch {
            _navEvents.emit(GameSessionNavEvent.NavigateBack)
        }
    }

    // ─── Lógica interna ───────────────────────────────────────────────────────

    private fun startTimerForCurrent() {
        timerJob?.cancel()
        val question = _uiState.value.currentQuestion ?: return
        val timeLimitMs = question.timeLimitMs

        timerJob = viewModelScope.launch {
            while (true) {
                delay(100)
                val state = _uiState.value
                if (state.isAnswered || state.isLoading) break
                val newElapsed = state.elapsedMs + 100
                _uiState.value = state.copy(elapsedMs = newElapsed)
                // Auto-timeout cuando se agota el tiempo extra (MAX_OVERTIME_MS = 10 s)
                if (newElapsed >= timeLimitMs + MAX_OVERTIME_MS) {
                    submitAnswer(pickedOptionId = null, forceTimeMs = timeLimitMs)
                    break
                }
            }
        }
    }

    private fun submitAnswer(pickedOptionId: String?, forceTimeMs: Long? = null) {
        val state = _uiState.value
        if (state.isAnswered || state.isLoading) return
        val attemptId = state.attemptId ?: return
        val question = state.currentQuestion ?: return

        timerJob?.cancel()
        answerJob?.cancel()

        answerJob = viewModelScope.launch {
            val elapsed = forceTimeMs ?: state.elapsedMs
            val correctOptionId = question.options.firstOrNull { it.isCorrect }?.optionId
            val isCorrect = pickedOptionId != null && pickedOptionId == correctOptionId

            val scoreResult = computeGameScoreUseCase(
                listOf(
                    GameAnswerInput(
                        isCorrect = isCorrect,
                        timeMs = elapsed,
                        timeLimitMs = question.timeLimitMs,
                        difficulty = question.difficulty,
                    ),
                ),
            )
            val questionScore = scoreResult.sessionScore

            attemptRepository.recordAnswer(
                attemptId = attemptId,
                questionId = question.questionId,
                pickedOptionId = pickedOptionId,
                isCorrect = isCorrect,
                timeMs = elapsed,
                timeLimitMs = question.timeLimitMs,
            )

            _uiState.value = _uiState.value.copy(
                isAnswered = true,
                selectedOptionId = pickedOptionId,
                feedbackVisible = true,
                feedbackPositive = questionScore > 0,
                lastQuestionScore = questionScore,
                runningScore = _uiState.value.runningScore + questionScore,
            )

            delay(FEEDBACK_DURATION_MS)

            _uiState.value = _uiState.value.copy(feedbackVisible = false)

            delay(INTER_QUESTION_PAUSE_MS)

            val updatedState = _uiState.value
            val nextIndex = updatedState.currentIndex + 1
            if (nextIndex < updatedState.questions.size) {
                // Avanza a la siguiente pregunta
                _uiState.value = updatedState.copy(
                    currentIndex = nextIndex,
                    elapsedMs = 0L,
                    selectedOptionId = null,
                    isAnswered = false,
                )
                startTimerForCurrent()
            } else {
                // Todas las preguntas respondidas: finaliza la sesión
                finalizeSession(attemptId)
            }
        }
    }

    private suspend fun finalizeSession(attemptId: String) {
        val result = finalizeGameAttemptUseCase(attemptId)
        _navEvents.emit(
            GameSessionNavEvent.NavigateToSummary(
                attemptId = attemptId,
                xpGained = result.xpGained,
                previousRankOrdinal = result.previousRank.ordinal,
                currentRankOrdinal = result.currentRank.ordinal,
                // Codificado como Int (×100) para poder pasarlo como arg de navegación
                previousMmrInt = (result.previousMmr * 100f).toInt(),
            ),
        )
    }

    override fun onCleared() {
        timerJob?.cancel()
        answerJob?.cancel()
    }

    class Factory(
        private val packId: String,
        private val buildGameAttemptPlanUseCase: BuildGameAttemptPlanUseCase,
        private val computeGameScoreUseCase: ComputeGameScoreUseCase,
        private val finalizeGameAttemptUseCase: FinalizeGameAttemptUseCase,
        private val attemptRepository: AttemptRepository,
        private val packRepository: PackRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            GameSessionViewModel(
                packId = packId,
                buildGameAttemptPlanUseCase = buildGameAttemptPlanUseCase,
                computeGameScoreUseCase = computeGameScoreUseCase,
                finalizeGameAttemptUseCase = finalizeGameAttemptUseCase,
                attemptRepository = attemptRepository,
                packRepository = packRepository,
            ) as T
    }

    private companion object {
        const val FEEDBACK_DURATION_MS = 2_000L
        const val INTER_QUESTION_PAUSE_MS = 300L
        // Alineado con GameSessionHeader.MAX_OVERTIME_MS
        const val MAX_OVERTIME_MS = 60_000L
    }
}

/** Eventos de navegación emitidos por [GameSessionViewModel]. */
sealed interface GameSessionNavEvent {
    /** Navega a la pantalla de resumen con los datos de la sesión finalizada. */
    data class NavigateToSummary(
        val attemptId: String,
        val xpGained: Long,
        val previousRankOrdinal: Int,
        val currentRankOrdinal: Int,
        val previousMmrInt: Int,
    ) : GameSessionNavEvent

    /** Navega hacia atrás al Game home tras abandonar la sesión. */
    data object NavigateBack : GameSessionNavEvent
}
