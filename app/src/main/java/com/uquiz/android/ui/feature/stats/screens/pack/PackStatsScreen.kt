package com.uquiz.android.ui.feature.stats.screens.pack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.stats.components.PackDetailedStatsContent
import com.uquiz.android.ui.feature.stats.screens.pack.model.PackStatsUiState

@Composable
fun PackStatsRoute(
    packId: String,
    packRepository: PackRepository,
    packStatsRepository: PackStatsRepository,
    onBackToPack: () -> Unit,
    onHelpClick: () -> Unit,
) {
    val viewModel: PackStatsViewModel = viewModel(
        factory = PackStatsViewModel.Factory(
            packRepository = packRepository,
            packStatsRepository = packStatsRepository,
            packId = packId,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()
    PackStatsScreen(
        uiState = uiState,
        onBackToPack = onBackToPack,
        onHelpClick = onHelpClick,
    )
}

@Composable
private fun PackStatsScreen(
    uiState: PackStatsUiState,
    onBackToPack: () -> Unit,
    onHelpClick: () -> Unit,
) {
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            PackDetailedStatsContent(
                packTitle = uiState.packTitle,
                stats = uiState.stats,
                onHelpClick = onHelpClick,
                onBackToPackClick = onBackToPack,
            )
        }
    }
}

@UPreview
@Composable
private fun PackStatsScreenPreview() {
    UTheme {
        PackStatsScreen(
            uiState = PackStatsUiState(
                isLoading = false,
                packTitle = "Biología celular",
                stats = PackDetailedStats(packId = "preview"),
            ),
            onBackToPack = {},
            onHelpClick = {},
        )
    }
}
