package com.uquiz.android.ui.feature.stats.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.stats.enums.UserStatsModeFilter
import com.uquiz.android.domain.stats.enums.UserStatsPeriodFilter
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.ui.feature.stats.screens.user.model.UserStatsUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class UserStatsViewModel(
    private val userStatsRepository: UserStatsRepository,
) : ViewModel() {

    private val modeFilter = MutableStateFlow(UserStatsModeFilter.ALL)
    private val periodFilter = MutableStateFlow(UserStatsPeriodFilter.ALL)

    val uiState = combine(modeFilter, periodFilter, ::Pair)
        .flatMapLatest { (mode, period) ->
            userStatsRepository.observeDashboard(mode, period)
                .map { dashboard ->
                    UserStatsUiState(
                        isLoading = false,
                        modeFilter = mode,
                        periodFilter = period,
                        dashboard = dashboard,
                    )
                }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UserStatsUiState(),
        )

    fun onModeFilterSelected(filter: UserStatsModeFilter) {
        modeFilter.value = filter
    }

    fun onPeriodFilterSelected(filter: UserStatsPeriodFilter) {
        periodFilter.value = filter
    }

    class Factory(
        private val userStatsRepository: UserStatsRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserStatsViewModel(userStatsRepository) as T
        }
    }
}
