package com.uquiz.android.ui.feature.game.screens.summary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.domain.ranking.enums.mmrToNextRank
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButton
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButtonVariant
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.game.components.GameCircularRankProgress
import com.uquiz.android.ui.feature.game.screens.summary.model.GameSummaryUiState
import com.uquiz.android.ui.feature.game.utils.formatGameDuration
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.studygame.MetricCard
import com.uquiz.android.ui.shared.components.studygame.MetricCardSize
import com.uquiz.android.ui.shared.components.studygame.ModeBackground

/**
 * ### GameSummaryRoute
 *
 * Punto de entrada de la pantalla de resumen del Game mode.
 *
 * @param attemptId Identificador del intento finalizado.
 * @param xpGained XP ganada durante la sesión.
 * @param previousRankOrdinal Ordinal del rango anterior.
 * @param currentRankOrdinal Ordinal del rango actual tras la sesión.
 * @param attemptRepository Repositorio para leer los datos del intento.
 * @param userRankRepository Repositorio para leer el MMR actual del usuario.
 * @param onPlayAgain Callback invocado al pulsar "Jugar de nuevo", recibe el packId.
 * @param onBack Callback invocado al pulsar el botón de volver atrás.
 */
@Composable
fun GameSummaryRoute(
    attemptId: String,
    xpGained: Long,
    previousRankOrdinal: Int,
    currentRankOrdinal: Int,
    previousMmrInt: Int,
    attemptRepository: AttemptRepository,
    userRankRepository: UserRankRepository,
    onPlayAgain: (String) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel: GameSummaryViewModel = viewModel(
        factory = GameSummaryViewModel.Factory(
            attemptId = attemptId,
            xpGained = xpGained,
            previousRankOrdinal = previousRankOrdinal,
            currentRankOrdinal = currentRankOrdinal,
            previousMmrInt = previousMmrInt,
            attemptRepository = attemptRepository,
            userRankRepository = userRankRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()

    GameSummaryScreen(
        uiState = uiState,
        onPlayAgain = { uiState.packId?.let(onPlayAgain) },
        onBack = onBack,
    )
}

/**
 * ### GameSummaryScreen
 *
 * Pantalla de resumen al finalizar una sesión del Game mode. Muestra el rango actual
 * con su arco de progreso, el score, métricas de sesión y las acciones disponibles.
 *
 * @param uiState Estado actual con los datos de la sesión finalizada.
 * @param onPlayAgain Callback invocado al pulsar "Jugar de nuevo".
 * @param onBack Callback invocado al pulsar el botón de volver atrás.
 */
@Composable
private fun GameSummaryScreen(
    uiState: GameSummaryUiState,
    onPlayAgain: () -> Unit,
    onBack: () -> Unit,
) {
    val strings = LocalStrings.current
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) contentVisible = true
    }

    ModeBackground(
        contentPadding = PaddingValues(20.dp),
        includeStatusBarsPadding = false,
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                // Grupo 1: arco de rango y rango actual
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(400)) + slideInVertically(tween(500)) { -it / 5 },
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        GameCircularRankProgress(
                            currentRank = uiState.currentRank,
                            previousMmr = uiState.previousMmr,
                            mmr = uiState.mmr,
                            xpGained = uiState.xpGained,
                            sessionScore = uiState.sessionScore,
                        )

                        if (uiState.rankChanged) {
                            Text(
                                text = strings.gameSummary.gameRankUpTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                        }

                        // MMR que falta para el siguiente rango (no se muestra en PARAGON)
                        val toNext = uiState.currentRank.mmrToNextRank(uiState.mmr)
                        if (toNext > 0) {
                            Text(
                                text = strings.gameSummary.gameNextRankLabel(toNext),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.60f),
                            )
                        }
                    }
                }

                // Grupo 2: métricas
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(500, delayMillis = 120)) +
                        slideInVertically(tween(520, delayMillis = 120)) { it / 4 },
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            MetricCard(
                                value = uiState.correctAnswers.toString(),
                                label = strings.common.studyCorrectAnswersLabel,
                                size = MetricCardSize.Small,
                                modifier = Modifier.weight(1f),
                            )
                            MetricCard(
                                value = uiState.incorrectAnswers.toString(),
                                label = strings.common.studyIncorrectAnswersLabel,
                                size = MetricCardSize.Small,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            MetricCard(
                                value = "${uiState.accuracyPercent}%",
                                label = strings.common.accuracyStatLabel,
                                size = MetricCardSize.Small,
                                modifier = Modifier.weight(1f),
                            )
                            MetricCard(
                                value = formatGameDuration(uiState.durationMs),
                                label = strings.common.studyEffectiveTimeLabel,
                                size = MetricCardSize.Small,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Grupo 3: botones de acción
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(500, delayMillis = 240)) +
                        slideInVertically(tween(560, delayMillis = 240)) { it / 3 } +
                        scaleIn(initialScale = 0.96f, animationSpec = tween(560, delayMillis = 240)),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        if (uiState.packId != null) {
                            UDarkButton(
                                text = strings.gameSummary.gamePlayAgain,
                                onClick = onPlayAgain,
                            )
                        }
                        UDarkButton(
                            text = strings.common.back,
                            onClick = onBack,
                            variant = UDarkButtonVariant.Secondary,
                        )
                    }
                }
            }
        }
    }
}

@UPreview
@Composable
private fun GameSummaryScreenPreview() {
    UTheme {
        GameSummaryScreen(
            uiState = GameSummaryUiState(
                isLoading = false,
                packId = "pack-1",
                sessionScore = 87,
                correctAnswers = 14,
                incorrectAnswers = 3,
                accuracyPercent = 82,
                durationMs = 285_000L,
                xpGained = 124L,
                previousRank = UserRank.NEOPHYTE,
                currentRank = UserRank.ACOLYTE,
                rankChanged = true,
                previousMmr = 633f,
                mmr = 720f,
            ),
            onPlayAgain = {},
            onBack = {},
        )
    }
}
