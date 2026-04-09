package com.uquiz.android.ui.feature.stats.screens.stats.model

data class StatsUiState(
    val isLoading: Boolean = true,
    val dayStreak: Int = 0,
    val totalPoints: Int = 0,
    val completedSessions: Int = 0,
    val accuracyPercent: Int = 0
)
