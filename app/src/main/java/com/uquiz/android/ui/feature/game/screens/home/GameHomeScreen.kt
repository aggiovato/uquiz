package com.uquiz.android.ui.feature.game.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.UEmptyContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.stats.projection.PackGameCard
import com.uquiz.android.ui.designsystem.animations.screens.UNotFoundMascot
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.components.inputs.USearchField
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.game.components.GameModeBanner
import com.uquiz.android.ui.feature.game.components.GamePackCard
import com.uquiz.android.ui.feature.game.screens.home.model.GameHomeUiEvent
import com.uquiz.android.ui.feature.game.screens.home.model.GameHomeUiState
import com.uquiz.android.ui.feature.stats.components.StaggeredStatsBlock
import com.uquiz.android.ui.i18n.LocalStrings
import kotlinx.coroutines.flow.collectLatest

/**
 * ### GameHomeRoute
 *
 * Punto de entrada de la pantalla principal del Game mode.
 *
 * @param packRepository Repositorio para observar los packs disponibles con sus conteos.
 * @param attemptRepository Repositorio para observar el progreso de partidas activas.
 * @param onPackClick Callback invocado al seleccionar un pack para jugar.
 */
@Composable
fun GameHomeRoute(
    packRepository: PackRepository,
    attemptRepository: AttemptRepository,
    onPackClick: (String) -> Unit,
) {
    val viewModel: GameHomeViewModel = viewModel(
        factory = GameHomeViewModel.Factory(
            packRepository = packRepository,
            attemptRepository = attemptRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is GameHomeUiEvent.OpenRandomPack -> onPackClick(event.packId)
            }
        }
    }

    GameHomeScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onPackClick = onPackClick,
        onRandomPlayClick = viewModel::onRandomPlayRequested,
    )
}

/**
 * ### GameHomeScreen
 *
 * Pantalla principal del Game mode. Muestra la lista de packs disponibles agrupados
 * en partidas activas y packs recientes, con búsqueda integrada en tiempo real.
 *
 * @param uiState Estado actual con packs, partidas activas y resultados de búsqueda.
 * @param onSearchQueryChange Callback invocado al cambiar el texto de búsqueda.
 * @param onPackClick Callback invocado al seleccionar un pack.
 * @param onRandomPlayClick Callback invocado al pedir una partida aleatoria.
 */
@Composable
private fun GameHomeScreen(
    uiState: GameHomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onPackClick: (String) -> Unit,
    onRandomPlayClick: () -> Unit,
) {
    val strings = LocalStrings.current
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) contentVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
        ) {
            item {
                GameModeBanner(
                    visible = contentVisible,
                    enabled = uiState.hasPlayablePacks,
                    onRandomPlayClick = onRandomPlayClick,
                )
                Spacer(Modifier.height(16.dp))
            }

            item {
                USearchField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = strings.common.searchPlaceholder,
                    leadingIcon = rememberVectorPainter(Icons.Outlined.Search),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
            }

            if (uiState.searchQuery.isNotBlank()) {
                if (uiState.searchResults.isEmpty()) {
                    item {
                        UNotFoundMascot(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        )
                    }
                } else {
                    itemsIndexed(uiState.searchResults) { index, card ->
                        StaggeredStatsBlock(
                            visible = contentVisible,
                            delayMillis = minOf(index, 4) * 50,
                        ) {
                            Column {
                                GamePackCard(
                                    packGameCard = card,
                                    accentIndex = index,
                                    onPlayClick = { onPackClick(card.pack.id) },
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            } else if (uiState.activeGames.isEmpty() && uiState.recentPacks.isEmpty() && !uiState.isLoading) {
                item {
                    UEmptyContent(
                        message = strings.common.nothingHereYet,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                    )
                }
            } else {
                if (uiState.activeGames.isNotEmpty()) {
                    item {
                        StaggeredStatsBlock(visible = contentVisible, delayMillis = 60) {
                            SectionHeader(strings.gameHome.gameContinueButton)
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    itemsIndexed(uiState.activeGames) { index, card ->
                        StaggeredStatsBlock(
                            visible = contentVisible,
                            delayMillis = 60 + minOf(index, 3) * 60,
                        ) {
                            Column {
                                GamePackCard(
                                    packGameCard = card,
                                    accentIndex = index,
                                    onPlayClick = { onPackClick(card.pack.id) },
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                    item { Spacer(Modifier.height(4.dp)) }
                }
                item {
                    StaggeredStatsBlock(visible = contentVisible, delayMillis = 120) {
                        SectionHeader(strings.gameHome.gameAllPacksSection)
                    }
                    Spacer(Modifier.height(8.dp))
                }
                itemsIndexed(uiState.recentPacks) { index, card ->
                    StaggeredStatsBlock(
                        visible = contentVisible,
                        delayMillis = 120 + minOf(index, 4) * 60,
                    ) {
                        Column {
                            GamePackCard(
                                packGameCard = card,
                                accentIndex = index + uiState.activeGames.size,
                                onPlayClick = { onPackClick(card.pack.id) },
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@UPreview
@Composable
private fun GameHomeScreenPreview() {
    UTheme {
        GameHomeScreen(
            uiState = GameHomeUiState(
                isLoading = false,
                activeGames = listOf(
                    PackGameCard(
                        pack = Pack(
                            id = "pack-1",
                            title = "Historia Universal",
                            description = null,
                            folderId = "folder-1",
                            icon = "brain",
                            colorHex = "#134C8F",
                            createdAt = 0L,
                            updatedAt = 0L,
                        ),
                        questionCount = 15,
                        averageDifficulty = DifficultyLevel.MEDIUM,
                        expectedPlayTimeMs = 300_000L,
                        activeAttemptId = "attempt-1",
                        answeredCount = 7,
                    ),
                ),
                recentPacks = listOf(
                    PackGameCard(
                        pack = Pack(
                            id = "pack-2",
                            title = "Kotlin Coroutines",
                            description = null,
                            folderId = "folder-1",
                            icon = "code",
                            colorHex = "#00957F",
                            createdAt = 0L,
                            updatedAt = 0L,
                        ),
                        questionCount = 20,
                        averageDifficulty = DifficultyLevel.HARD,
                        expectedPlayTimeMs = 500_000L,
                    ),
                ),
            ),
            onSearchQueryChange = {},
            onPackClick = {},
            onRandomPlayClick = {},
        )
    }
}
