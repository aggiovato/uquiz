package com.uquiz.android.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.R
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.home.components.HomeContinuePackCard
import com.uquiz.android.ui.feature.home.components.HomeGreetingCard
import com.uquiz.android.ui.feature.home.components.HomeHighlightsSection
import com.uquiz.android.ui.feature.home.components.HomeModeShortcutCard
import com.uquiz.android.ui.feature.home.components.HomeScoreProgressCard
import com.uquiz.android.ui.feature.home.model.ContinuePackUiModel
import com.uquiz.android.ui.feature.home.model.HomeUiEvent
import com.uquiz.android.ui.feature.home.model.HomeUiState
import com.uquiz.android.ui.i18n.LocalStrings
import kotlinx.coroutines.flow.collectLatest

/**
 * ### HomeRoute
 *
 * Entrada pública de la pantalla Home.
 *
 * Resuelve el [HomeViewModel], observa su estado y delega el renderizado puro a
 * [HomeScreen].
 *
 * @param userProfileRepository Repositorio del perfil actual.
 * @param userRankRepository Repositorio del rango del usuario.
 * @param userStatsRepository Repositorio de estadísticas globales.
 * @param attemptRepository Repositorio de intentos activos.
 * @param packRepository Repositorio de packs para completar los resúmenes.
 * @param onContinuePlayingClick Navega al pack con progreso de juego activo.
 * @param onContinueStudyingClick Navega al pack con progreso de estudio activo.
 * @param onProfileClick Abre la pantalla o acción de perfil.
 */
@Composable
fun HomeRoute(
    userProfileRepository: UserProfileRepository,
    userRankRepository: UserRankRepository,
    userStatsRepository: UserStatsRepository,
    attemptRepository: AttemptRepository,
    packRepository: PackRepository,
    onContinuePlayingClick: (String) -> Unit,
    onContinueStudyingClick: (String) -> Unit,
    onProfileClick: () -> Unit,
) {
    val viewModel: HomeViewModel =
        viewModel(
            factory =
                HomeViewModel.Factory(
                    userProfileRepository,
                    userRankRepository,
                    userStatsRepository,
                    attemptRepository,
                    packRepository,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is HomeUiEvent.OpenRandomGamePack -> onContinuePlayingClick(event.packId)
                is HomeUiEvent.OpenRandomStudyPack -> onContinueStudyingClick(event.packId)
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onContinuePlayingClick = onContinuePlayingClick,
        onContinueStudyingClick = onContinueStudyingClick,
        onRandomGameClick = viewModel::onRandomGameRequested,
        onRandomStudyClick = viewModel::onRandomStudyRequested,
        onProfileClick = onProfileClick,
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onContinuePlayingClick: (String) -> Unit,
    onContinueStudyingClick: (String) -> Unit,
    onRandomGameClick: () -> Unit,
    onRandomStudyClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) contentVisible = true
    }

    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize())
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                HomeGreetingCard(
                    displayName = uiState.displayName,
                    avatarIcon = uiState.avatarIcon,
                    avatarImageUri = uiState.avatarImageUri,
                    onProfileClick = onProfileClick,
                )
                HomeHighlightsSection(
                    currentRank = uiState.currentRank,
                    totalXp = uiState.totalXp,
                    dayStreak = uiState.dayStreak,
                    visible = contentVisible,
                )
            }
        }

        item {
            HomeScoreProgressCard(
                score = uiState.score,
                mmr = uiState.scoreMmr,
                currentRank = uiState.currentRank,
                visible = contentVisible,
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                HomeModeShortcutCard(
                    backgroundRes = R.drawable.game_card,
                    buttonText = strings.home.homeRandomPlay,
                    visible = contentVisible,
                    enabled = uiState.hasPlayablePacks,
                    animationDelayMillis = 520,
                    onClick = onRandomGameClick,
                    modifier = Modifier.weight(1f),
                )
                HomeModeShortcutCard(
                    backgroundRes = R.drawable.study_card,
                    buttonText = strings.home.homeRandomStudy,
                    visible = contentVisible,
                    enabled = uiState.hasPlayablePacks,
                    animationDelayMillis = 620,
                    onClick = onRandomStudyClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        if (uiState.continuePlaying.isNotEmpty()) {
            item { SectionHeader(strings.home.homeContinuePlaying) }
            itemsIndexed(uiState.continuePlaying, key = { _, item -> "play-${item.packId}" }) { index, item ->
                HomeContinuePackCard(
                    item = item,
                    accentIndex = index,
                    onClick = { onContinuePlayingClick(item.packId) },
                )
            }
        }

        if (uiState.continueStudying.isNotEmpty()) {
            item {
                if (uiState.continuePlaying.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                }
                SectionHeader(strings.home.homeContinueStudying)
            }
            itemsIndexed(uiState.continueStudying, key = { _, item -> "study-${item.packId}" }) { index, item ->
                HomeContinuePackCard(
                    item = item,
                    accentIndex = index,
                    onClick = { onContinueStudyingClick(item.packId) },
                )
            }
        }
    }
}

@UPreview
@Composable
private fun HomeScreenPreview() {
    UTheme {
        HomeScreen(
            uiState =
                HomeUiState(
                    isLoading = false,
                    displayName = "Ada",
                    currentRank = UserRank.NEOPHYTE,
                    dayStreak = 5,
                    score = 480,
                    scoreMmr = 480f,
                    totalXp = 240L,
                    continuePlaying =
                        listOf(
                            ContinuePackUiModel(
                                packId = "play-pack",
                                title = "Capítulos de biología",
                                answeredCount = 6,
                                totalQuestions = 15,
                                progressFraction = 0.4f,
                                icon = null,
                                colorHex = null,
                            ),
                        ),
                    continueStudying =
                        listOf(
                            ContinuePackUiModel(
                                packId = "study-pack",
                                title = "Historia de Roma",
                                answeredCount = 3,
                                totalQuestions = 12,
                                progressFraction = 0.25f,
                                icon = null,
                                colorHex = null,
                            ),
                        ),
                ),
            onContinuePlayingClick = {},
            onContinueStudyingClick = {},
            onRandomGameClick = {},
            onRandomStudyClick = {},
            onProfileClick = {},
        )
    }
}
