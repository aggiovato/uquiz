package com.uquiz.android.ui.feature.game.screens.session

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.game.usecase.BuildGameAttemptPlanUseCase
import com.uquiz.android.core.game.usecase.ComputeGameScoreUseCase
import com.uquiz.android.core.game.usecase.FinalizeGameAttemptUseCase
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.game.components.GameAnswerFeedback
import com.uquiz.android.ui.feature.game.components.GameSessionHeader
import com.uquiz.android.ui.feature.game.screens.session.model.GameOptionUiModel
import com.uquiz.android.ui.feature.game.screens.session.model.GameQuestionUiModel
import com.uquiz.android.ui.feature.game.screens.session.model.GameSessionUiState
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.studygame.ModeBackground
import com.uquiz.android.ui.shared.components.studygame.QuizOptionCard
import com.uquiz.android.ui.shared.components.studygame.QuizOptionState
import com.uquiz.android.ui.shared.components.studygame.QuizQuestionCard
import com.uquiz.android.ui.shared.components.studygame.SessionExitDialog

/**
 * ### GameSessionRoute
 *
 * Punto de entrada de la pantalla de sesión activa del Game mode.
 *
 * @param packId Identificador del pack que se está jugando.
 * @param buildGameAttemptPlanUseCase Caso de uso que construye o reanuda el plan de preguntas.
 * @param computeGameScoreUseCase Caso de uso que calcula el score por pregunta.
 * @param finalizeGameAttemptUseCase Caso de uso que finaliza la sesión y actualiza el ranking.
 * @param attemptRepository Repositorio para registrar respuestas y abandonar intentos.
 * @param packRepository Repositorio para cargar el pack y sus preguntas.
 * @param externalExitRequested Señal externa que muestra el diálogo de salida.
 * @param onExternalExitConsumed Callback para indicar que la señal ha sido procesada.
 * @param onExitToGame Callback invocado al confirmar la salida hacia el Game home.
 * @param onFinished Callback invocado al finalizar la sesión con los datos de la sesión.
 */
@Composable
fun GameSessionRoute(
    packId: String,
    buildGameAttemptPlanUseCase: BuildGameAttemptPlanUseCase,
    computeGameScoreUseCase: ComputeGameScoreUseCase,
    finalizeGameAttemptUseCase: FinalizeGameAttemptUseCase,
    attemptRepository: AttemptRepository,
    packRepository: PackRepository,
    externalExitRequested: Boolean,
    onExternalExitConsumed: () -> Unit,
    onExitToGame: () -> Unit,
    onFinished: (attemptId: String, xpGained: Long, previousRankOrdinal: Int, currentRankOrdinal: Int, previousMmrInt: Int) -> Unit,
) {
    val viewModel: GameSessionViewModel = viewModel(
        factory = GameSessionViewModel.Factory(
            packId = packId,
            buildGameAttemptPlanUseCase = buildGameAttemptPlanUseCase,
            computeGameScoreUseCase = computeGameScoreUseCase,
            finalizeGameAttemptUseCase = finalizeGameAttemptUseCase,
            attemptRepository = attemptRepository,
            packRepository = packRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    // Recoge los eventos de navegación del ViewModel
    LaunchedEffect(Unit) {
        viewModel.navEvents.collect { event ->
            when (event) {
                is GameSessionNavEvent.NavigateToSummary -> onFinished(
                    event.attemptId,
                    event.xpGained,
                    event.previousRankOrdinal,
                    event.currentRankOrdinal,
                    event.previousMmrInt,
                )
                GameSessionNavEvent.NavigateBack -> onExitToGame()
            }
        }
    }

    // Intercepta la señal de salida que llega desde el top bar o el bottom nav
    LaunchedEffect(externalExitRequested) {
        if (externalExitRequested) {
            showExitDialog = true
            onExternalExitConsumed()
        }
    }

    GameSessionScreen(
        uiState = uiState,
        onSelectOption = viewModel::selectOption,
        onRequestExit = { showExitDialog = true },
    )

    if (showExitDialog) {
        val strings = LocalStrings.current
        SessionExitDialog(
            title = strings.gameSession.gameExitTitle,
            message = strings.gameSession.gameExitMessage,
            confirmText = strings.common.back,
            dismissText = strings.common.studyVerifyAnswer,
            onDismiss = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                viewModel.pauseAndExit()
            },
        )
    }
}

/**
 * ### GameSessionScreen
 *
 * Pantalla de sesión activa del Game mode. Muestra la pregunta actual con su temporizador,
 * las opciones de respuesta interactivas y el overlay de feedback tras responder.
 *
 * @param uiState Estado actual de la sesión.
 * @param onSelectOption Callback invocado al pulsar una opción de respuesta.
 * @param onRequestExit Callback invocado al solicitar salir de la sesión.
 */
@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
private fun GameSessionScreen(
    uiState: GameSessionUiState,
    onSelectOption: (String) -> Unit,
    onRequestExit: () -> Unit,
) {
    val strings = LocalStrings.current

    BackHandler(enabled = !uiState.isLoading) {
        onRequestExit()
    }

    ModeBackground(
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    GameSessionHeader(
                        elapsedMs = uiState.elapsedMs,
                        timeLimitMs = question?.timeLimitMs ?: 20_000L,
                        runningScore = uiState.runningScore,
                        packTitle = uiState.packTitle,
                        currentQuestionIndex = uiState.currentIndex,
                        totalQuestions = uiState.questions.size,
                        onExit = onRequestExit,
                    )

                    // Feedback superpuesto sobre el header
                    // Nota: lastQuestionScore puede ser negativo; el string de fallo ya incluye
                    // el signo "−", por lo que se pasa el valor absoluto para evitar doble signo.
                    val feedbackText = if (uiState.feedbackPositive) {
                        strings.gameSession.gameCorrectFeedback(uiState.lastQuestionScore)
                    } else {
                        strings.gameSession.gameIncorrectFeedback(kotlin.math.abs(uiState.lastQuestionScore))
                    }
                    GameAnswerFeedback(
                        visible = uiState.feedbackVisible,
                        text = feedbackText,
                        isPositive = uiState.feedbackPositive,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }

                if (question != null) {
                    Spacer(Modifier.height(14.dp))

                    // Transición fluida entre preguntas: slide + fade con curva de aceleración
                    AnimatedContent(
                        targetState = uiState.currentIndex,
                        transitionSpec = {
                            (slideInHorizontally(
                                animationSpec = tween(380, easing = FastOutSlowInEasing),
                            ) { it } + fadeIn(tween(260))) togetherWith
                                (slideOutHorizontally(
                                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                                ) { -it } + fadeOut(tween(200)))
                        },
                        label = "gameSessionQuestionTransition",
                    ) { _ ->
                        QuizQuestionCard(
                            markdownText = question.markdownText,
                            difficulty = question.difficulty,
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        question.options.forEach { option ->
                            QuizOptionCard(
                                label = option.label,
                                markdownText = option.markdownText,
                                state = optionStateFor(option, uiState),
                                onClick = { if (!uiState.isAnswered) onSelectOption(option.optionId) },
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Resuelve el estado visual de una opción según si la pregunta ya ha sido respondida.
 *
 * Antes de responder: Default o Selected según la selección previa del usuario.
 * Después de responder: las opciones revelan correcta/incorrecta/otra según el resultado.
 */
private fun optionStateFor(option: GameOptionUiModel, state: GameSessionUiState): QuizOptionState =
    when {
        !state.isAnswered && state.selectedOptionId == option.optionId -> QuizOptionState.Selected
        !state.isAnswered -> QuizOptionState.Default
        option.isCorrect -> QuizOptionState.VerifiedCorrect
        state.selectedOptionId == option.optionId -> QuizOptionState.VerifiedWrongPicked
        else -> QuizOptionState.VerifiedOther
    }

@UPreview
@Composable
private fun GameSessionScreenPreview() {
    UTheme {
        GameSessionScreen(
            uiState = GameSessionUiState(
                isLoading = false,
                packTitle = "Kotlin Coroutines",
                currentIndex = 0,
                elapsedMs = 8_500L,
                runningScore = 15,
                questions = listOf(
                    GameQuestionUiModel(
                        questionId = "q1",
                        markdownText = "¿Qué función se usa para lanzar una corrutina que retorna un resultado?",
                        difficulty = DifficultyLevel.MEDIUM,
                        timeLimitMs = 20_000L,
                        options = listOf(
                            GameOptionUiModel("o1", "A", "launch", false),
                            GameOptionUiModel("o2", "B", "async", true),
                            GameOptionUiModel("o3", "C", "runBlocking", false),
                            GameOptionUiModel("o4", "D", "withContext", false),
                        ),
                    ),
                ),
            ),
            onSelectOption = {},
            onRequestExit = {},
        )
    }
}
