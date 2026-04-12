package com.uquiz.android.ui.feature.game.screens.intro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButton
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButtonVariant
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy200
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.game.screens.intro.model.GameIntroUiState
import com.uquiz.android.ui.feature.game.utils.formatExpectedPlayTime
import com.uquiz.android.ui.feature.study.components.StudyInfoBanner
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.studygame.MetricCard
import com.uquiz.android.ui.shared.components.studygame.ModeBackground

/**
 * ### GameIntroRoute
 *
 * Punto de entrada de la pantalla de introducción al Game mode para un pack concreto.
 *
 * @param packId Identificador del pack seleccionado para jugar.
 * @param packRepository Repositorio para cargar el pack y sus preguntas.
 * @param attemptRepository Repositorio para comprobar si existe una partida activa.
 * @param onBack Callback invocado al pulsar retroceder.
 * @param onStartGame Callback invocado al iniciar o reanudar la sesión de juego.
 */
@Composable
fun GameIntroRoute(
    packId: String,
    packRepository: PackRepository,
    attemptRepository: AttemptRepository,
    onBack: () -> Unit,
    onStartGame: () -> Unit,
) {
    val viewModel: GameIntroViewModel =
        viewModel(
            factory =
                GameIntroViewModel.Factory(
                    packRepository = packRepository,
                    attemptRepository = attemptRepository,
                    packId = packId,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()

    GameIntroScreen(
        uiState = uiState,
        onBack = onBack,
        onStartOrResume = onStartGame,
    )
}

/**
 * ### GameIntroScreen
 *
 * Pantalla de introducción al Game mode. Muestra el título del pack, métricas clave
 * (preguntas, dificultad media y tiempo estimado) y el botón para iniciar o reanudar.
 *
 * @param uiState Estado actual de la pantalla.
 * @param onBack Callback invocado al pulsar el botón de retroceso.
 * @param onStartOrResume Callback invocado al iniciar o reanudar la partida.
 */
@Composable
private fun GameIntroScreen(
    uiState: GameIntroUiState,
    onBack: () -> Unit,
    onStartOrResume: () -> Unit,
) {
    val strings = LocalStrings.current
    var contentVisible by remember { mutableStateOf(false) }
    val metricCardScale by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 1.04f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "gameIntroMetricCardScale",
    )

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) contentVisible = true
    }

    ModeBackground(includeStatusBarsPadding = false) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    AnimatedVisibility(
                        visible = contentVisible,
                        enter = fadeIn(tween(400)) + slideInVertically(tween(500)) { -it / 5 },
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Surface(
                                color = Color.White.copy(alpha = 0.14f),
                                shape = RoundedCornerShape(999.dp),
                                tonalElevation = 0.dp,
                                shadowElevation = 0.dp,
                            ) {
                                Text(
                                    text = strings.nav.navGame,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Neutral100,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                )
                            }

                            Text(
                                text = uiState.packTitle,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = contentVisible,
                        enter =
                            fadeIn(tween(500, delayMillis = 120)) +
                                slideInVertically(tween(520, delayMillis = 120)) { it / 4 },
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                MetricCard(
                                    value = uiState.questionCount.toString(),
                                    label = strings.common.questionsStatLabel,
                                    valueColor = Navy200,
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .scale(metricCardScale),
                                )
                                val diffColor =
                                    when (uiState.averageDifficulty) {
                                        DifficultyLevel.EASY -> Teal500
                                        DifficultyLevel.MEDIUM -> Navy200
                                        DifficultyLevel.HARD, DifficultyLevel.EXPERT -> Color.White
                                    }
                                MetricCard(
                                    value =
                                        when (uiState.averageDifficulty) {
                                            DifficultyLevel.EASY -> strings.common.difficultyEasy
                                            DifficultyLevel.MEDIUM -> strings.common.difficultyMedium
                                            DifficultyLevel.HARD -> strings.common.difficultyHard
                                            DifficultyLevel.EXPERT -> strings.common.difficultyHard
                                        },
                                    label = strings.common.studyAverageDifficultyLabel,
                                    valueColor = diffColor,
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .scale(metricCardScale),
                                )
                            }

                            MetricCard(
                                value = formatExpectedPlayTime(uiState.expectedPlayTimeMs),
                                label = strings.gameIntro.gameEstimatedTimeLabel,
                                valueColor = Navy200,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .scale(metricCardScale),
                            )

                            if (uiState.hasActiveAttempt) {
                                StudyInfoBanner(
                                    text =
                                        strings.gameIntro.gameAnsweredSoFar(
                                            uiState.answeredCount,
                                            uiState.questionCount,
                                        ),
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = contentVisible,
                    enter =
                        fadeIn(tween(500, delayMillis = 240)) +
                            slideInVertically(tween(560, delayMillis = 240)) { it / 3 } +
                            scaleIn(initialScale = 0.96f, animationSpec = tween(560, delayMillis = 240)),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        UDarkButton(
                            text = if (uiState.hasActiveAttempt) strings.gameIntro.gameResumeGame else strings.gameIntro.gameStartGame,
                            onClick = onStartOrResume,
                            enabled = uiState.questionCount > 0,
                        )
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
private fun GameIntroScreenPreview() {
    UTheme {
        GameIntroScreen(
            uiState =
                GameIntroUiState(
                    isLoading = false,
                    packId = "preview",
                    packTitle = "Kotlin Coroutines",
                    questionCount = 20,
                    averageDifficulty = DifficultyLevel.HARD,
                    expectedPlayTimeMs = 500_000L,
                    hasActiveAttempt = true,
                    answeredCount = 8,
                ),
            onBack = {},
            onStartOrResume = {},
        )
    }
}
