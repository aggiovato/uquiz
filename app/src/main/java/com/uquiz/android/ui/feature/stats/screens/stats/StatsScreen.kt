package com.uquiz.android.ui.feature.stats.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.ui.designsystem.components.cards.UInfoStatCard
import com.uquiz.android.ui.designsystem.components.cards.UInfoStatTone
import com.uquiz.android.ui.i18n.LocalStrings

@Composable
fun StatsRoute(userStatsRepository: UserStatsRepository) {
    val viewModel: StatsViewModel =
        viewModel(
            factory = StatsViewModel.Factory(userStatsRepository),
        )
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                UInfoStatCard(
                    value = uiState.dayStreak.toString(),
                    label = strings.homeDayStreakLabel,
                    tone = UInfoStatTone.Brand,
                    modifier = Modifier.weight(1f),
                )
                UInfoStatCard(
                    value = uiState.totalPoints.toString(),
                    label = strings.homeTotalPointsLabel,
                    tone = UInfoStatTone.Secondary,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                UInfoStatCard(
                    value = uiState.completedSessions.toString(),
                    label = strings.sessionsStatLabel,
                    tone = UInfoStatTone.Tertiary,
                    modifier = Modifier.weight(1f),
                )
                UInfoStatCard(
                    value = "${uiState.accuracyPercent}%",
                    label = strings.accuracyStatLabel,
                    tone = UInfoStatTone.Brand,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
