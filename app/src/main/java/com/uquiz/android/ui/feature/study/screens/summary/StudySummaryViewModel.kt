package com.uquiz.android.ui.feature.study.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.feature.study.screens.summary.model.StudySummaryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de resumen de sesión de estudio.
 *
 * Carga el intento finalizado y sus respuestas para calcular las métricas de
 * rendimiento: aciertos, errores, porcentaje de precisión y tiempo efectivo.
 */
class StudySummaryViewModel(
    private val attemptRepository: AttemptRepository,
    private val packRepository: PackRepository,
    private val attemptId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudySummaryUiState(attemptId = attemptId))
    val uiState: StateFlow<StudySummaryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val attempt = attemptRepository.getById(attemptId)
            val answers = attemptRepository.getAnswers(attemptId)
            val pack = attempt?.primaryPackId?.let { packRepository.getById(it) }
            val totalQuestions = attempt?.totalQuestions ?: answers.size
            val correctAnswers = attempt?.correctAnswers ?: answers.count { it.isCorrect }
            val incorrectAnswers = (totalQuestions - correctAnswers).coerceAtLeast(0)
            val effectiveTime = answers.sumOf { it.timeMs }
            val accuracy = if (totalQuestions > 0) {
                ((correctAnswers * 100f) / totalQuestions).toInt()
            } else {
                0
            }

            _uiState.value = StudySummaryUiState(
                isLoading = false,
                attemptId = attemptId,
                packId = attempt?.primaryPackId,
                packTitle = pack?.title.orEmpty(),
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                incorrectAnswers = incorrectAnswers,
                accuracyPercent = accuracy,
                effectiveTimeMs = effectiveTime
            )
        }
    }

    class Factory(
        private val attemptRepository: AttemptRepository,
        private val packRepository: PackRepository,
        private val attemptId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StudySummaryViewModel(attemptRepository, packRepository, attemptId) as T
        }
    }
}
