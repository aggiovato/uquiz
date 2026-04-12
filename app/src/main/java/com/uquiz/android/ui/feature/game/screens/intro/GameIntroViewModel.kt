package com.uquiz.android.ui.feature.game.screens.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.feature.game.screens.intro.model.GameIntroUiState
import com.uquiz.android.ui.feature.game.utils.computeAverageDifficulty
import com.uquiz.android.ui.feature.game.utils.computeExpectedPlayTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de introducción al Game mode.
 *
 * Carga el pack, calcula la dificultad media y el tiempo estimado de partida, y comprueba
 * si existe una sesión activa del mismo pack para ofrecer la opción de reanudar.
 */
class GameIntroViewModel(
    private val packRepository: PackRepository,
    private val attemptRepository: AttemptRepository,
    private val packId: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameIntroUiState(packId = packId))
    val uiState: StateFlow<GameIntroUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val pack = packRepository.getById(packId)
            val questions = packRepository.getWithQuestions(packId)
            val activeAttempt = attemptRepository.getActiveGameAttempt(packId)
            val answeredCount = activeAttempt
                ?.let { attemptRepository.getAnswers(it.id).size }
                ?: 0

            _uiState.value = GameIntroUiState(
                isLoading = false,
                packId = packId,
                packTitle = pack?.title.orEmpty(),
                questionCount = questions.size,
                averageDifficulty = computeAverageDifficulty(questions),
                expectedPlayTimeMs = computeExpectedPlayTime(questions),
                hasActiveAttempt = activeAttempt != null,
                answeredCount = answeredCount,
            )
        }
    }

    class Factory(
        private val packRepository: PackRepository,
        private val attemptRepository: AttemptRepository,
        private val packId: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            GameIntroViewModel(packRepository, attemptRepository, packId) as T
    }
}
