package com.uquiz.android.ui.feature.study.screens.session

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.analytics.usecase.FinalizeAttemptAnalyticsUseCase
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButton
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButtonVariant
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.study.components.StudyExitDialog
import com.uquiz.android.ui.feature.study.components.StudyExplanationDialog
import com.uquiz.android.ui.feature.study.components.StudyModeBackground
import com.uquiz.android.ui.feature.study.components.StudyOptionCard
import com.uquiz.android.ui.feature.study.components.StudyQuestionCard
import com.uquiz.android.ui.feature.study.components.StudySessionHeader
import com.uquiz.android.ui.feature.study.screens.session.model.StudyOptionUiModel
import com.uquiz.android.ui.feature.study.screens.session.model.StudyQuestionUiModel
import com.uquiz.android.ui.feature.study.screens.session.model.StudySessionUiState
import com.uquiz.android.ui.i18n.LocalStrings
import kotlinx.coroutines.launch

/**
 * ### StudySessionRoute
 *
 * Punto de entrada de la pantalla de sesión de estudio.
 *
 * @param packId Identificador del pack cuyas preguntas se estudian.
 * @param packRepository Repositorio para cargar el pack y sus preguntas.
 * @param attemptRepository Repositorio para crear, reanudar y completar intentos.
 * @param finalizeAttemptAnalyticsUseCase Caso de uso que registra los eventos de analítica al finalizar.
 * @param externalExitRequested Señal externa para mostrar el diálogo de salida (top bar, bottom nav).
 * @param onExternalExitConsumed Callback para indicar que la señal ha sido procesada.
 * @param onExitToPack Callback invocado al salir de la sesión sin completarla.
 * @param onFinished Callback invocado al completar la sesión, recibe el attemptId resultante.
 */
@Composable
fun StudySessionRoute(
    packId: String,
    packRepository: PackRepository,
    attemptRepository: AttemptRepository,
    finalizeAttemptAnalyticsUseCase: FinalizeAttemptAnalyticsUseCase,
    externalExitRequested: Boolean,
    onExternalExitConsumed: () -> Unit,
    onExitToPack: () -> Unit,
    onFinished: (String) -> Unit,
) {
    val viewModel: StudySessionViewModel =
        viewModel(
            factory =
                StudySessionViewModel.Factory(
                    packRepository = packRepository,
                    attemptRepository = attemptRepository,
                    finalizeAttemptAnalyticsUseCase = finalizeAttemptAnalyticsUseCase,
                    packId = packId,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showExitDialog by remember { mutableStateOf(false) }

    // Intercepta la señal de salida que llega desde el top bar o el bottom nav.
    LaunchedEffect(externalExitRequested) {
        if (externalExitRequested) {
            showExitDialog = true
            onExternalExitConsumed()
        }
    }

    StudySessionScreen(
        uiState = uiState,
        onSelectOption = viewModel::selectOption,
        onVerify = viewModel::verifyCurrentAnswer,
        onPrevious = viewModel::goToPrevious,
        onNext = viewModel::goToNext,
        onRequestExit = { showExitDialog = true },
        onFinish = {
            scope.launch {
                viewModel.finishStudy()?.let(onFinished)
            }
        },
    )

    if (showExitDialog) {
        StudyExitDialog(
            onDismiss = { showExitDialog = false },
            onExit = {
                showExitDialog = false
                scope.launch {
                    viewModel.persistCurrentProgress()
                    onExitToPack()
                }
            },
        )
    }
}

/**
 * ### StudySessionScreen
 *
 * Pantalla principal de la sesión de estudio. Muestra la pregunta actual, las opciones de
 * respuesta, la barra de progreso y los botones de navegación y verificación.
 *
 * @param uiState Estado actual de la sesión con pregunta, opciones y progreso.
 * @param onSelectOption Callback invocado al seleccionar una opción de respuesta.
 * @param onVerify Callback invocado al verificar la respuesta seleccionada.
 * @param onPrevious Callback invocado al retroceder a la pregunta anterior.
 * @param onNext Callback invocado al avanzar a la siguiente pregunta.
 * @param onRequestExit Callback invocado al solicitar salir de la sesión.
 * @param onFinish Callback invocado al finalizar la sesión en la última pregunta.
 */
@Composable
private fun StudySessionScreen(
    uiState: StudySessionUiState,
    onSelectOption: (String) -> Unit,
    onVerify: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onRequestExit: () -> Unit,
    onFinish: () -> Unit,
) {
    val strings = LocalStrings.current
    var showExplanationDialog by remember { mutableStateOf(false) }
    val optionsScrollState = rememberScrollState()

    BackHandler(enabled = !uiState.isLoading) {
        onRequestExit()
    }

    StudyModeBackground(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 22.dp),
        includeStatusBarsPadding = false,
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
            )
        } else {
            val question = uiState.currentQuestion

            Column(modifier = Modifier.fillMaxSize()) {
                StudySessionHeader(
                    counter =
                        strings.studySession.studyQuestionCounter(
                            uiState.currentIndex + 1,
                            uiState.totalQuestions,
                        ),
                    packTitle = uiState.packTitle,
                    progress = uiState.progressFraction,
                    hasExplanation = !question?.explanationMarkdown.isNullOrBlank() && uiState.isCurrentVerified,
                    onExit = onRequestExit,
                    onExplanation = { showExplanationDialog = true },
                )

                if (question != null) {
                    Spacer(Modifier.height(14.dp))

                    StudyQuestionCard(question = question)

                    Spacer(Modifier.height(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(optionsScrollState),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            question.options.forEach { option ->
                                StudyOptionCard(
                                    option = option,
                                    savedAnswer = uiState.currentSavedAnswer,
                                    selectedOptionId = uiState.selectedOptionId,
                                    verified = uiState.isCurrentVerified,
                                    onClick = { onSelectOption(option.optionId) },
                                )
                            }
                        }
                        // Degradado inferior que indica que hay más opciones por ver con scroll.
                        if (optionsScrollState.canScrollForward) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(28.dp)
                                        .align(Alignment.BottomCenter)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, BrandNavy),
                                            ),
                                        ),
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    if (!uiState.isCurrentVerified) {
                        UDarkButton(
                            text = strings.common.studyVerifyAnswer,
                            onClick = onVerify,
                            variant = UDarkButtonVariant.Secondary,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.canVerify,
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            if (uiState.canGoPrevious) {
                                UDarkButton(
                                    text = strings.studySession.studyPrevious,
                                    onClick = onPrevious,
                                    variant = UDarkButtonVariant.Secondary,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            UDarkButton(
                                text = if (uiState.isLastQuestion) strings.studySession.studyFinish else strings.studySession.studyNext,
                                onClick = { if (uiState.isLastQuestion) onFinish() else onNext() },
                                variant = UDarkButtonVariant.Secondary,
                                modifier = Modifier.weight(1f),
                                enabled = if (uiState.isLastQuestion) uiState.canFinish else true,
                            )
                        }
                    }
                }
            }
        }
    }

    if (showExplanationDialog) {
        val markdown = uiState.currentQuestion?.explanationMarkdown
        if (!markdown.isNullOrBlank()) {
            StudyExplanationDialog(
                markdown = markdown,
                onDismiss = { showExplanationDialog = false },
            )
        }
    }
}

@UPreview
@Composable
private fun StudySessionScreenPreview() {
    UTheme {
        StudySessionScreen(
            uiState =
                StudySessionUiState(
                    isLoading = false,
                    packId = "preview",
                    packTitle = "Historia Universal",
                    questions =
                        listOf(
                            StudyQuestionUiModel(
                                questionId = "q1",
                                markdownText =
                                    "¿En qué año comenzó la **Primera Guerra Mundial**?" +
                                        "\n\nEsta fue una guerra que afectó a toda Europa.",
                                explanationMarkdown = "El archiduque Francisco Fernando fue asesinado en 1914.",
                                difficulty = DifficultyLevel.MEDIUM,
                                options =
                                    listOf(
                                        StudyOptionUiModel("o1", "A", "1912", false),
                                        StudyOptionUiModel("o2", "B", "1914", true),
                                        StudyOptionUiModel("o3", "C", "1916", false),
                                        StudyOptionUiModel("o4", "D", "1918", false),
                                    ),
                            ),
                        ),
                    currentIndex = 0,
                ),
            onSelectOption = {},
            onVerify = {},
            onPrevious = {},
            onNext = {},
            onRequestExit = {},
            onFinish = {},
        )
    }
}
