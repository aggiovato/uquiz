package com.uquiz.android.ui.feature.stats.screens.pack.model

import com.uquiz.android.domain.stats.projection.PackDetailedStats

data class PackStatsUiState(
    val isLoading: Boolean = true,
    val packTitle: String = "",
    val stats: PackDetailedStats = PackDetailedStats(packId = "")
)
