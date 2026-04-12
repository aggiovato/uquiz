package com.uquiz.android.ui.feature.study.screens.summary

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.study.components.StudyMetricCard
import com.uquiz.android.ui.feature.study.components.StudyModeBackground
import com.uquiz.android.ui.feature.study.screens.summary.model.StudySummaryUiState
import com.uquiz.android.ui.feature.study.utils.formatStudyDuration
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### StudySummaryRoute
 *
 * Punto de entrada de la pantalla de resumen de sesión de estudio.
 *
 * @param attemptId Identificador del intento finalizado cuyo resumen se muestra.
 * @param attemptRepository Repositorio para leer los datos del intento y sus respuestas.
 * @param packRepository Repositorio para resolver el título del pack asociado.
 * @param onBack Callback invocado al volver atrás en la pila de navegación.
 */
@Composable
fun StudySummaryRoute(
    attemptId: String,
    attemptRepository: AttemptRepository,
    packRepository: PackRepository,
    onBack: () -> Unit,
) {
    val viewModel: StudySummaryViewModel =
        viewModel(
            factory =
                StudySummaryViewModel.Factory(
                    attemptRepository = attemptRepository,
                    packRepository = packRepository,
                    attemptId = attemptId,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = onBack)

    StudySummaryScreen(
        uiState = uiState,
        onBack = onBack,
    )
}

/**
 * ### StudySummaryScreen
 *
 * Pantalla que muestra el resumen de resultados al finalizar una sesión de estudio.
 * Incluye métricas de aciertos, fallos, precisión y tiempo efectivo.
 *
 * @param uiState Estado actual con los datos del resumen.
 * @param onBack Callback invocado al pulsar volver atrás.
 */
@Composable
private fun StudySummaryScreen(
    uiState: StudySummaryUiState,
    onBack: () -> Unit,
) {
    val strings = LocalStrings.current

    StudyModeBackground(
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
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Text(
                    text = strings.common.studySummaryTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                )
                Text(
                    text = uiState.packTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    StudyMetricCard(
                        value = uiState.correctAnswers.toString(),
                        label = strings.common.studyCorrectAnswersLabel,
                        modifier = Modifier.weight(1f),
                    )
                    StudyMetricCard(
                        value = uiState.incorrectAnswers.toString(),
                        label = strings.common.studyIncorrectAnswersLabel,
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    StudyMetricCard(
                        value = "${uiState.accuracyPercent}%",
                        label = strings.common.accuracyStatLabel,
                        modifier = Modifier.weight(1f),
                    )
                    StudyMetricCard(
                        value = formatStudyDuration(uiState.effectiveTimeMs),
                        label = strings.common.studyEffectiveTimeLabel,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.weight(1f))

                UDarkButton(
                    text = strings.common.back,
                    onClick = onBack,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun StudySummaryScreenPreview() {
    UTheme {
        StudySummaryScreen(
            uiState =
                StudySummaryUiState(
                    isLoading = false,
                    attemptId = "preview",
                    packId = "pack-1",
                    packTitle = "Historia Universal",
                    totalQuestions = 20,
                    correctAnswers = 15,
                    incorrectAnswers = 5,
                    accuracyPercent = 75,
                    effectiveTimeMs = 185_000L,
                ),
            onBack = {},
        )
    }
}
