package com.uquiz.android.ui.feature.pack.model

import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.domain.stats.projection.PackOverviewMetrics

/** Estado de UI del detalle de un pack. */
data class PackOverviewUiState(
    val packId: String,
    val packTitle: String = "",
    val packDescription: String? = null,
    val overview: PackOverviewMetrics = PackOverviewMetrics(),
    val detailedStats: PackDetailedStats = PackDetailedStats(packId = packId),
    val questions: List<QuestionListItemUiModel> = emptyList(),
    val canStartStudy: Boolean = false,
    val canStartGame: Boolean = false,
    val canCreateQuestion: Boolean = true,
    val showStatsSheet: Boolean = false,
    val dialogState: PackDialogState = PackDialogState.None
)

/** Modelo de UI usado para mostrar una pregunta dentro del listado del pack. */
data class QuestionListItemUiModel(
    val questionId: String,
    val markdownText: String,
    val difficulty: DifficultyLevel
)
