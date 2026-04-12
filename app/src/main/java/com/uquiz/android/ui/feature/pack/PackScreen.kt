package com.uquiz.android.ui.feature.pack

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.files.writeDocumentText
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.importexport.projection.ExportedUQuizFile
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.domain.stats.projection.PackOverviewMetrics
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.pack.components.PackDetailContent
import com.uquiz.android.ui.feature.pack.components.PackDialogs
import com.uquiz.android.ui.feature.pack.components.PackStatsBottomSheet
import com.uquiz.android.ui.feature.pack.model.PackDialogState
import com.uquiz.android.ui.feature.pack.model.PackOverviewUiState
import com.uquiz.android.ui.feature.pack.model.QuestionListItemUiModel
import com.uquiz.android.ui.i18n.LocalStrings
import kotlinx.coroutines.launch

/**
 * ### PackRoute
 *
 * Entrada pública del detalle de un pack.
 *
 * Resuelve el [PackViewModel], coordina la exportación del pack y delega el
 * renderizado puro a [PackScreen].
 *
 * @param packId Identificador del pack mostrado.
 * @param packRepository Repositorio del pack.
 * @param packStatsRepository Repositorio de estadísticas asociadas al pack.
 * @param importExportRepository Repositorio de exportación.
 * @param onPackTitleResolved Callback que informa del título cargado del pack.
 * @param onStudyModeClick Acción para iniciar el modo estudio.
 * @param onGameClick Acción para iniciar el modo juego.
 * @param onDetailedStatsClick Acción para abrir el detalle completo de estadísticas.
 * @param onCreateQuestionClick Acción para crear una nueva pregunta.
 * @param onQuestionClick Acción para abrir el detalle de una pregunta.
 * @param onPackDeleted Acción a ejecutar tras borrar el pack.
 */
@Composable
fun PackRoute(
    packId: String,
    packRepository: PackRepository,
    packStatsRepository: PackStatsRepository,
    importExportRepository: ImportExportRepository,
    onPackTitleResolved: (String) -> Unit,
    onStudyModeClick: (String) -> Unit,
    onGameClick: (String) -> Unit,
    onDetailedStatsClick: (String) -> Unit,
    onCreateQuestionClick: (String) -> Unit,
    onQuestionClick: (String, String) -> Unit,
    onPackDeleted: () -> Unit,
) {
    val context = LocalContext.current
    val strings = LocalStrings.current
    val toastController = LocalToastController.current
    val scope = rememberCoroutineScope()
    val viewModel: PackViewModel =
        viewModel(
            factory =
                PackViewModel.Factory(
                    packRepository = packRepository,
                    packStatsRepository = packStatsRepository,
                    packId = packId,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()
    var pendingExport by remember { mutableStateOf<ExportedUQuizFile?>(null) }

    val packExportLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("application/octet-stream"),
        ) { uri ->
            val exportFile = pendingExport ?: return@rememberLauncherForActivityResult
            if (uri == null) return@rememberLauncherForActivityResult
            scope.launch {
                try {
                    context.writeDocumentText(uri, exportFile.content)
                    toastController.show(
                        strings.common.exportUQuizSuccess(
                            uiState.packTitle.ifBlank { exportFile.fileName.removeSuffix(".uquiz") },
                        ),
                        UToastTone.Success,
                    )
                } catch (_: Throwable) {
                    toastController.show(strings.errors.exportWriteErrorMessage, UToastTone.Error)
                } finally {
                    pendingExport = null
                }
            }
        }

    LaunchedEffect(uiState.packTitle) {
        if (uiState.packTitle.isNotBlank()) {
            onPackTitleResolved(uiState.packTitle)
        }
    }

    PackScreen(
        uiState = uiState,
        onStudyModeClick = { onStudyModeClick(packId) },
        onGameClick = { onGameClick(packId) },
        onCreateQuestionClick = { onCreateQuestionClick(packId) },
        onDeletePackClick = viewModel::onDeletePackRequested,
        onDeletePackConfirm = { viewModel.onDeletePackConfirmed(onPackDeleted) },
        onExportPackClick = {
            scope.launch {
                try {
                    val exportFile = importExportRepository.exportPack(packId)
                    pendingExport = exportFile
                    packExportLauncher.launch(exportFile.fileName)
                } catch (_: Throwable) {
                    toastController.show(strings.errors.exportFailedMessage, UToastTone.Error)
                }
            }
        },
        onReorderQuestions = viewModel::reorderQuestions,
        onQuestionClick = { questionId -> onQuestionClick(packId, questionId) },
        onEditPackClick = viewModel::onEditPackRequested,
        onEditPackConfirm = viewModel::onEditPackConfirmed,
        onDialogDismiss = viewModel::onDialogDismissed,
        onStatsClick = viewModel::onStatsRequested,
        onStatsDismiss = viewModel::onStatsDismissed,
        onSeeFullStatsClick = {
            viewModel.onStatsDismissed()
            onDetailedStatsClick(packId)
        },
    )
}

@Composable
private fun PackScreen(
    uiState: PackOverviewUiState,
    onStudyModeClick: () -> Unit,
    onGameClick: () -> Unit,
    onCreateQuestionClick: () -> Unit,
    onDeletePackClick: () -> Unit,
    onDeletePackConfirm: () -> Unit,
    onExportPackClick: () -> Unit,
    onReorderQuestions: (List<String>) -> Unit,
    onQuestionClick: (String) -> Unit,
    onEditPackClick: () -> Unit,
    onEditPackConfirm: (String, String?, String, String) -> Unit,
    onDialogDismiss: () -> Unit,
    onStatsClick: () -> Unit,
    onStatsDismiss: () -> Unit,
    onSeeFullStatsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    // El contenido entra cuando el título del pack está disponible (primera emisión real
    // de Room), solapándose con el fadeIn del NavHost (220ms) para ocultar el estado
    // inicial vacío del stateIn y el glitch de lista vacía durante la transición.
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.packTitle) {
        if (uiState.packTitle.isNotBlank()) contentVisible = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(tween(280)) + slideInVertically(tween(320)) { it / 16 },
        ) {
            PackDetailContent(
                uiState = uiState,
                onStudyModeClick = onStudyModeClick,
                onGameClick = onGameClick,
                onReorderQuestions = onReorderQuestions,
                onQuestionClick = onQuestionClick,
                onEditPackClick = onEditPackClick,
                onStatsClick = onStatsClick,
            )
        }

        UActionsSheetFab(
            actions =
                listOf(
                    UFabActionItem(
                        id = "create_question",
                        label = strings.pack.newQuestion,
                        description = strings.question.createQuestionActionDescription,
                        iconRes = UIcons.Actions.Edit,
                        containerColor = Navy500,
                        contentColor = Color.White,
                        onClick = onCreateQuestionClick,
                    ),
                    UFabActionItem(
                        id = "export_pack",
                        label = strings.common.exportUQuizAction,
                        description = strings.common.exportUQuizActionDescription,
                        iconRes = UIcons.Actions.Download,
                        containerColor = Teal700,
                        contentColor = Color.White,
                        onClick = onExportPackClick,
                    ),
                    UFabActionItem(
                        id = "delete_pack",
                        label = strings.common.deletePack,
                        description = strings.common.deletePackConfirmMessage,
                        iconRes = UIcons.Actions.Delete,
                        kind = com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionKind.Destructive,
                        containerColor = Red700,
                        contentColor = Color.White,
                        onClick = onDeletePackClick,
                    ),
                ),
            modifier = Modifier.fillMaxSize(),
        )
    }

    PackDialogs(
        dialogState = uiState.dialogState,
        onDialogDismiss = onDialogDismiss,
        onEditPackConfirm = onEditPackConfirm,
        onDeletePackConfirm = onDeletePackConfirm,
    )

    if (uiState.showStatsSheet) {
        PackStatsBottomSheet(
            stats = uiState.detailedStats,
            onSeeFullStatsClick = onSeeFullStatsClick,
            onDismiss = onStatsDismiss,
        )
    }
}

@UPreview
@Composable
private fun PackScreenPreview() {
    UTheme {
        PackScreen(
            uiState =
                PackOverviewUiState(
                    packId = "pack-1",
                    packTitle = "Geografía europea",
                    packDescription = "Repaso de capitales, ríos y cordilleras.",
                    overview =
                        PackOverviewMetrics(
                            questionCount = 18,
                            accuracyPercent = 70,
                            sessionsCount = 5,
                            progressPercent = 44,
                        ),
                    detailedStats =
                        PackDetailedStats(
                            packId = "pack-1",
                            totalSessions = 5,
                            averageAccuracyPercent = 70,
                            averageDurationMs = 280_000L,
                            progressPercent = 44,
                            dominatedQuestions = 8,
                            totalQuestions = 18,
                        ),
                    questions =
                        listOf(
                            QuestionListItemUiModel(
                                questionId = "q1",
                                markdownText = "¿Cuál es la **capital** de Francia?",
                                difficulty = DifficultyLevel.MEDIUM,
                            ),
                            QuestionListItemUiModel(
                                questionId = "q2",
                                markdownText = "¿Qué río atraviesa Viena?",
                                difficulty = DifficultyLevel.HARD,
                            ),
                        ),
                    canStartStudy = true,
                    canStartGame = true,
                    dialogState = PackDialogState.None,
                ),
            onStudyModeClick = {},
            onGameClick = {},
            onCreateQuestionClick = {},
            onDeletePackClick = {},
            onDeletePackConfirm = {},
            onExportPackClick = {},
            onReorderQuestions = {},
            onQuestionClick = {},
            onEditPackClick = {},
            onEditPackConfirm = { _, _, _, _ -> },
            onDialogDismiss = {},
            onStatsClick = {},
            onStatsDismiss = {},
            onSeeFullStatsClick = {},
        )
    }
}
