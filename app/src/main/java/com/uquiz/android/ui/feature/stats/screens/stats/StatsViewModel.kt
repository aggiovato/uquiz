package com.uquiz.android.ui.feature.stats.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.ui.feature.stats.screens.stats.model.StatsUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(
    userStatsRepository: UserStatsRepository
) : ViewModel() {

    val uiState = userStatsRepository.observeSnapshot()
        .map { snapshot ->
            StatsUiState(
                isLoading = false,
                dayStreak = snapshot.dayStreak,
                totalPoints = snapshot.totalPoints.toInt(),
                completedSessions = snapshot.completedSessions,
                accuracyPercent = snapshot.accuracyPercent ?: 0
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            StatsUiState()
        )

    class Factory(
        private val userStatsRepository: UserStatsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StatsViewModel(userStatsRepository) as T
        }
    }
}
