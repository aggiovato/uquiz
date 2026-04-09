package com.uquiz.android.ui.feature.study.screens.intro

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
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.study.components.StudyInfoBanner
import com.uquiz.android.ui.feature.study.components.StudyMetricCard
import com.uquiz.android.ui.feature.study.components.StudyModeBackground
import com.uquiz.android.ui.feature.study.screens.intro.model.StudyIntroUiState
import com.uquiz.android.ui.feature.study.utils.studyDifficultyInfo
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### StudyIntroRoute
 *
 * Punto de entrada de la pantalla de introducción al modo estudio.
 *
 * @param packId Identificador del pack cuyas preguntas se van a estudiar.
 * @param packRepository Repositorio para leer el pack y sus preguntas.
 * @param attemptRepository Repositorio para comprobar si existe un intento activo.
 * @param onBack Callback invocado al pulsar retroceder.
 * @param onOpenSession Callback invocado al iniciar o reanudar la sesión de estudio.
 */
@Composable
fun StudyIntroRoute(
    packId: String,
    packRepository: PackRepository,
    attemptRepository: AttemptRepository,
    onBack: () -> Unit,
    onOpenSession: () -> Unit,
) {
    val viewModel: StudyIntroViewModel =
        viewModel(
            factory =
                StudyIntroViewModel.Factory(
                    packRepository = packRepository,
                    attemptRepository = attemptRepository,
                    packId = packId,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()

    StudyIntroScreen(
        uiState = uiState,
        onBack = onBack,
        onStartOrResume = onOpenSession,
    )
}

/**
 * ### StudyIntroScreen
 *
 * Pantalla de introducción al modo estudio. Muestra el título del pack, métricas clave
 * (número de preguntas y dificultad media) y el botón para iniciar o reanudar la sesión.
 *
 * @param uiState Estado actual de la pantalla.
 * @param onBack Callback invocado al pulsar el botón de retroceso.
 * @param onStartOrResume Callback invocado al pulsar iniciar o reanudar la sesión.
 */
@Composable
private fun StudyIntroScreen(
    uiState: StudyIntroUiState,
    onBack: () -> Unit,
    onStartOrResume: () -> Unit,
) {
    val strings = LocalStrings.current
    var contentVisible by remember { mutableStateOf(false) }
    val metricCardScale by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 1.04f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "studyIntroMetricCardScale",
    )

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) contentVisible = true
    }

    StudyModeBackground(includeStatusBarsPadding = false) {
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
                        enter =
                            fadeIn(animationSpec = tween(400)) +
                                slideInVertically(animationSpec = tween(500)) { -it / 5 },
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Surface(
                                color = Color.White.copy(alpha = 0.14f),
                                shape = RoundedCornerShape(999.dp),
                                tonalElevation = 0.dp,
                                shadowElevation = 0.dp,
                            ) {
                                Text(
                                    text = strings.studyModeTitle,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Neutral100,
                                    modifier =
                                        Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp,
                                        ),
                                )
                            }

                            Text(
                                text = uiState.packTitle,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                            )
                            Text(
                                text = strings.studyReadyDescription,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.78f),
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = contentVisible,
                        enter =
                            fadeIn(animationSpec = tween(500, delayMillis = 120)) +
                                slideInVertically(
                                    animationSpec =
                                        tween(
                                            520,
                                            delayMillis = 120,
                                        ),
                                ) { it / 4 },
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                StudyMetricCard(
                                    value = uiState.questionCount.toString(),
                                    label = strings.questionsStatLabel,
                                    valueColor = Navy200,
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .scale(metricCardScale),
                                )
                                uiState.averageDifficulty?.let { difficulty ->
                                    val (value, color) = studyDifficultyInfo(difficulty, strings)
                                    StudyMetricCard(
                                        value = value,
                                        label = strings.studyAverageDifficultyLabel,
                                        valueColor = color,
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                                .scale(metricCardScale),
                                    )
                                }
                            }

                            if (uiState.hasActiveAttempt) {
                                StudyInfoBanner(
                                    text =
                                        strings.studyResumeProgress(
                                            uiState.activeProgress,
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
                        fadeIn(animationSpec = tween(500, delayMillis = 240)) +
                            slideInVertically(
                                animationSpec =
                                    tween(
                                        560,
                                        delayMillis = 240,
                                    ),
                            ) { it / 3 } +
                            scaleIn(
                                initialScale = 0.96f,
                                animationSpec = tween(560, delayMillis = 240),
                            ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        UDarkButton(
                            text = if (uiState.hasActiveAttempt) strings.studyResumeStudy else strings.studyStartStudy,
                            onClick = onStartOrResume,
                            enabled = uiState.questionCount > 0,
                        )
                        UDarkButton(
                            text = strings.back,
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
private fun StudyIntroScreenPreview() {
    UTheme {
        StudyIntroScreen(
            uiState =
                StudyIntroUiState(
                    isLoading = false,
                    packId = "preview",
                    packTitle = "Sintaxis Kotlin",
                    questionCount = 42,
                    averageDifficulty = DifficultyLevel.HARD,
                    hasActiveAttempt = true,
                    activeProgress = 12,
                ),
            onBack = {},
            onStartOrResume = {},
        )
    }
}
