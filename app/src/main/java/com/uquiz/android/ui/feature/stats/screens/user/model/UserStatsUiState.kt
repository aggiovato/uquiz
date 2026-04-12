package com.uquiz.android.ui.feature.stats.screens.user.model

import com.uquiz.android.domain.stats.enums.UserStatsModeFilter
import com.uquiz.android.domain.stats.enums.UserStatsPeriodFilter
import com.uquiz.android.domain.stats.projection.UserStatsDashboard

data class UserStatsUiState(
    val isLoading: Boolean = true,
    val modeFilter: UserStatsModeFilter = UserStatsModeFilter.ALL,
    val periodFilter: UserStatsPeriodFilter = UserStatsPeriodFilter.ALL,
    val dashboard: UserStatsDashboard = UserStatsDashboard(),
)
