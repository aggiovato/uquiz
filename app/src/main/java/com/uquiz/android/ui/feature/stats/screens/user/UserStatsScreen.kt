package com.uquiz.android.ui.feature.stats.screens.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.stats.enums.UserStatsModeFilter
import com.uquiz.android.domain.stats.enums.UserStatsPeriodFilter
import com.uquiz.android.domain.stats.projection.UserAccuracyTrendPoint
import com.uquiz.android.domain.stats.projection.UserAnswerSplit
import com.uquiz.android.domain.stats.projection.UserDifficultyStats
import com.uquiz.android.domain.stats.projection.UserModeStats
import com.uquiz.android.domain.stats.projection.UserPackStatsRow
import com.uquiz.android.domain.stats.projection.UserQuestionInsight
import com.uquiz.android.domain.stats.projection.UserStatsDashboard
import com.uquiz.android.domain.stats.projection.UserStatsSummary
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.stats.components.StaggeredStatsBlock
import com.uquiz.android.ui.feature.stats.components.UserAccuracyTrendSection
import com.uquiz.android.ui.feature.stats.components.UserAnswerSplitSection
import com.uquiz.android.ui.feature.stats.components.UserStatsBanner
import com.uquiz.android.ui.feature.stats.components.UserDifficultySection
import com.uquiz.android.ui.feature.stats.components.UserModePerformanceCard
import com.uquiz.android.ui.feature.stats.components.UserPackBarsSection
import com.uquiz.android.ui.feature.stats.components.UserQuestionInsightsSection
import com.uquiz.android.ui.feature.stats.components.UserSummaryGrid
import com.uquiz.android.ui.feature.stats.screens.user.model.UserStatsUiState
import com.uquiz.android.ui.i18n.LocalStrings

@Composable
fun UserStatsRoute(userStatsRepository: UserStatsRepository) {
    val viewModel: UserStatsViewModel = viewModel(
        factory = UserStatsViewModel.Factory(userStatsRepository),
    )
    val uiState by viewModel.uiState.collectAsState()
    UserStatsScreen(
        uiState = uiState,
        onModeFilterSelected = viewModel::onModeFilterSelected,
        onPeriodFilterSelected = viewModel::onPeriodFilterSelected,
    )
}

@Composable
private fun UserStatsScreen(
    uiState: UserStatsUiState,
    onModeFilterSelected: (UserStatsModeFilter) -> Unit,
    onPeriodFilterSelected: (UserStatsPeriodFilter) -> Unit,
) {
    // Se inicializa directamente con el estado actual para evitar el frame en blanco
    // que ocurría cuando el early return eliminaba el árbol y LaunchedEffect tardaba
    // un frame más en activar contentVisible.
    var contentVisible by remember { mutableStateOf(!uiState.isLoading) }
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) contentVisible = true
    }
    val strings = LocalStrings.current

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                UserStatsBanner(
                    visible = contentVisible,
                    modeFilter = uiState.modeFilter,
                    periodFilter = uiState.periodFilter,
                    onModeFilterSelected = onModeFilterSelected,
                    onPeriodFilterSelected = onPeriodFilterSelected,
                )
            }
            item {
                StaggeredStatsBlock(visible = contentVisible, delayMillis = 60) {
                    UserSummaryGrid(summary = uiState.dashboard.summary, strings = strings)
                }
            }
            if (uiState.modeFilter == UserStatsModeFilter.ALL) {
                item {
                    StaggeredStatsBlock(visible = contentVisible, delayMillis = 120) {
                        UserModePerformanceCard(modeStats = uiState.dashboard.modeStats, strings = strings)
                    }
                }
            }
            item {
                StaggeredStatsBlock(visible = contentVisible, delayMillis = 180) {
                    UserAccuracyTrendSection(points = uiState.dashboard.accuracyTrend, strings = strings)
                }
            }
            item {
                StaggeredStatsBlock(visible = contentVisible, delayMillis = 240) {
                    UserAnswerSplitSection(split = uiState.dashboard.answerSplit, strings = strings)
                }
            }
            item {
                StaggeredStatsBlock(visible = contentVisible, delayMillis = 300) {
                    UserPackBarsSection(rows = uiState.dashboard.packRows, strings = strings)
                }
            }
            item {
                StaggeredStatsBlock(visible = contentVisible, delayMillis = 360) {
                    UserDifficultySection(rows = uiState.dashboard.difficultyStats, strings = strings)
                }
            }
            item {
                StaggeredStatsBlock(visible = contentVisible, delayMillis = 420) {
                    UserQuestionInsightsSection(
                        fastestQuestion = uiState.dashboard.fastestQuestion,
                        mostFailedQuestion = uiState.dashboard.mostFailedQuestion,
                        strings = strings,
                    )
                }
            }
        }

        // El spinner hace fadeOut suave mientras el contenido hace fadeIn escalonado,
        // evitando el salto brusco que causaba el early return anterior.
        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(tween(160)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@UPreview
@Composable
private fun UserStatsScreenPreview() {
    UTheme {
        UserStatsScreen(
            uiState = UserStatsUiState(
                isLoading = false,
                dashboard = UserStatsDashboard(
                    summary = UserStatsSummary(
                        totalSessions = 42,
                        answeredQuestions = 620,
                        accuracyPercent = 78,
                        totalStudyTimeMs = 12_400_000L,
                        averageAnswerTimeMs = 18_000L,
                        completedPacks = 3,
                        inProgressPacks = 5,
                    ),
                    modeStats = UserModeStats(
                        studyAccuracyPercent = 82,
                        gameAccuracyPercent = 74,
                        bestGameScore = 1420,
                        averageGameScore = 860,
                        masteredQuestionPercent = 46,
                    ),
                    answerSplit = UserAnswerSplit(correctAnswers = 484, incorrectAnswers = 136),
                    accuracyTrend = listOf(
                        UserAccuracyTrendPoint(1L, 62),
                        UserAccuracyTrendPoint(2L, 70),
                        UserAccuracyTrendPoint(3L, 68),
                        UserAccuracyTrendPoint(4L, 76),
                        UserAccuracyTrendPoint(5L, 82),
                    ),
                    packRows = listOf(
                        UserPackStatsRow("1", "Kotlin básico", 12, 84, 220_000L, 72),
                        UserPackStatsRow("2", "Android UI", 9, 76, 300_000L, 48),
                    ),
                    difficultyStats = listOf(
                        UserDifficultyStats(DifficultyLevel.EASY, 180, 91, 11_000L),
                        UserDifficultyStats(DifficultyLevel.MEDIUM, 260, 78, 17_000L),
                        UserDifficultyStats(DifficultyLevel.HARD, 120, 61, 26_000L),
                    ),
                    fastestQuestion = UserQuestionInsight("q1", "Qué keyword declara una clase en Kotlin?", "4s"),
                    mostFailedQuestion = UserQuestionInsight("q2", "Explica cuándo usar rememberSaveable.", "6x"),
                ),
            ),
            onModeFilterSelected = {},
            onPeriodFilterSelected = {},
        )
    }
}
