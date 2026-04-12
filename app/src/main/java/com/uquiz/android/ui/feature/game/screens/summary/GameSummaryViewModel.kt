package com.uquiz.android.ui.feature.game.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.ui.feature.game.screens.summary.model.GameSummaryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de resumen del Game mode.
 *
 * Carga los datos del intento finalizado para calcular métricas de sesión,
 * y lee el MMR actual del usuario para posicionar el arco de progreso de rango.
 * Los datos de XP ganada y cambio de rango llegan directamente desde los args de navegación.
 */
class GameSummaryViewModel(
    private val attemptId: String,
    private val xpGained: Long,
    private val previousRankOrdinal: Int,
    private val currentRankOrdinal: Int,
    private val previousMmrInt: Int,
    private val attemptRepository: AttemptRepository,
    private val userRankRepository: UserRankRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameSummaryUiState(
            xpGained = xpGained,
            previousRank = UserRank.entries.getOrElse(previousRankOrdinal) { UserRank.INITIATE },
            currentRank = UserRank.entries.getOrElse(currentRankOrdinal) { UserRank.INITIATE },
            rankChanged = previousRankOrdinal != currentRankOrdinal,
            previousMmr = previousMmrInt / 100f,
        ),
    )
    val uiState: StateFlow<GameSummaryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val attempt = attemptRepository.getById(attemptId)
            val answers = attemptRepository.getAnswers(attemptId)
            val rankState = userRankRepository.getCurrent()

            val totalQuestions = attempt?.totalQuestions ?: answers.size
            val correctAnswers = attempt?.correctAnswers ?: answers.count { it.isCorrect }
            val incorrectAnswers = (totalQuestions - correctAnswers).coerceAtLeast(0)
            val accuracy = if (totalQuestions > 0) {
                ((correctAnswers * 100f) / totalQuestions).toInt()
            } else {
                0
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                packId = attempt?.primaryPackId,
                sessionScore = attempt?.score ?: 0,
                correctAnswers = correctAnswers,
                incorrectAnswers = incorrectAnswers,
                accuracyPercent = accuracy,
                durationMs = answers.sumOf { it.timeMs },
                mmr = rankState.mmr,
            )
        }
    }

    class Factory(
        private val attemptId: String,
        private val xpGained: Long,
        private val previousRankOrdinal: Int,
        private val currentRankOrdinal: Int,
        private val previousMmrInt: Int,
        private val attemptRepository: AttemptRepository,
        private val userRankRepository: UserRankRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            GameSummaryViewModel(
                attemptId = attemptId,
                xpGained = xpGained,
                previousRankOrdinal = previousRankOrdinal,
                currentRankOrdinal = currentRankOrdinal,
                previousMmrInt = previousMmrInt,
                attemptRepository = attemptRepository,
                userRankRepository = userRankRepository,
            ) as T
    }
}
