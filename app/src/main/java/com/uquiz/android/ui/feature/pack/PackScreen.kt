package com.uquiz.android.ui.feature.pack

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.files.writeDocumentText
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.designsystem.animations.screens.UNotFoundMascot
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.designsystem.components.chips.UToggleChip
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.designsystem.components.inputs.USearchField
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.contentColorPalette
import com.uquiz.android.ui.designsystem.tokens.packSelectableIconPalette
import com.uquiz.android.ui.feature.pack.components.PackActionButtonsRow
import com.uquiz.android.ui.feature.pack.components.PackOverviewCard
import com.uquiz.android.ui.feature.pack.components.PackStatsBottomSheet
import com.uquiz.android.ui.feature.pack.components.QuestionListItem
import com.uquiz.android.ui.feature.pack.components.QuestionListItemMinHeight
import com.uquiz.android.ui.feature.pack.model.PackDialogState
import com.uquiz.android.ui.feature.pack.model.PackOverviewUiState
import com.uquiz.android.ui.feature.pack.model.QuestionListItemUiModel
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.dialogs.CreatePackDialog
import com.uquiz.android.ui.shared.components.dialogs.SafeDeleteEntityDialog
import kotlinx.coroutines.launch

private val questionItemSpacing = 10.dp

@Composable
fun PackRoute(
    packId: String,
    packRepository: PackRepository,
    packStatsRepository: PackStatsRepository,
    importExportRepository: ImportExportRepository,
    onPackTitleResolved: (String) -> Unit,
    onStudyModeClick: (String) -> Unit,
    onQuickGameClick: (String) -> Unit,
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
    var pendingExport by remember {
        mutableStateOf<com.uquiz.android.domain.importexport.projection.ExportedUQuizFile?>(
            null,
        )
    }
    val packExportLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
            val exportFile = pendingExport ?: return@rememberLauncherForActivityResult
            if (uri == null) {
                pendingExport = null
                return@rememberLauncherForActivityResult
            }
            scope.launch {
                try {
                    context.writeDocumentText(uri, exportFile.content)
                    toastController.show(
                        strings.exportUQuizSuccess(
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
        onQuickGameClick = { onQuickGameClick(packId) },
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
    onQuickGameClick: () -> Unit,
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
    val density = LocalDensity.current
    val itemExtentPx = with(density) { (QuestionListItemMinHeight + questionItemSpacing).toPx() }

    val displayQuestions = remember { mutableStateListOf<QuestionListItemUiModel>() }
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var reorderMode by remember { mutableStateOf(false) }
    var filterMode by remember { mutableStateOf(false) }
    var filterQuery by remember { mutableStateOf("") }
    val visibleQuestions by remember {
        derivedStateOf {
            if (filterMode && filterQuery.isNotBlank()) {
                displayQuestions.filter { it.markdownText.contains(filterQuery, ignoreCase = true) }
            } else {
                displayQuestions.toList()
            }
        }
    }

    LaunchedEffect(uiState.questions) {
        if (draggedIndex == null) {
            displayQuestions.clear()
            displayQuestions.addAll(uiState.questions)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding =
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp,
                    bottom = 96.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(questionItemSpacing),
        ) {
            item {
                PackOverviewCard(
                    title = uiState.packTitle,
                    description = uiState.packDescription,
                    overview = uiState.overview,
                    onEditClick = onEditPackClick,
                    onStatsClick = onStatsClick,
                )
                Spacer(Modifier.height(18.dp))
                PackActionButtonsRow(
                    canStartStudy = uiState.canStartStudy,
                    canStartGame = uiState.canStartGame,
                    onStudyClick = onStudyModeClick,
                    onQuickGameClick = onQuickGameClick,
                )
                Spacer(Modifier.height(22.dp))
                if (uiState.questions.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        SectionHeader(strings.questionsSection(visibleQuestions.size))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            UToggleChip(
                                iconRes = UIcons.Actions.Reorder,
                                label = strings.reorderLabel,
                                isActive = reorderMode,
                                onClick = {
                                    reorderMode = !reorderMode
                                    if (reorderMode) {
                                        filterMode = false
                                        filterQuery = ""
                                    }
                                },
                            )
                            UToggleChip(
                                iconRes = UIcons.Actions.Filter,
                                label = strings.filterLabel,
                                isActive = filterMode,
                                onClick = {
                                    filterMode = !filterMode
                                    if (filterMode) {
                                        reorderMode = false
                                    } else {
                                        filterQuery = ""
                                    }
                                },
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = filterMode,
                        enter = expandVertically(tween(220)) + fadeIn(tween(180)),
                        exit = shrinkVertically(tween(200)) + fadeOut(tween(150)),
                    ) {
                        USearchField(
                            value = filterQuery,
                            onValueChange = { filterQuery = it },
                            placeholder = strings.filterQuestionsHint,
                            leadingIcon = painterResource(UIcons.Actions.Filter),
                            modifier = Modifier.padding(top = 10.dp),
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                } else {
                    Text(
                        text = strings.noQuestionsYet,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Neutral400,
                    )
                }
            }

            if (filterMode && filterQuery.isNotBlank() && visibleQuestions.isEmpty()) {
                item {
                    UNotFoundMascot(modifier = Modifier.fillMaxWidth().wrapContentHeight())
                    Text(
                        text = strings.noSearchResults,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Neutral400,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            itemsIndexed(visibleQuestions, key = { _, item -> item.questionId }) { index, item ->
                val displayIndex = displayQuestions.indexOfFirst { it.questionId == item.questionId }
                QuestionListItem(
                    order = index + 1,
                    markdownText = item.markdownText,
                    difficulty = item.difficulty,
                    translationY = if (draggedIndex == displayIndex) dragOffset else 0f,
                    isDragging = draggedIndex == displayIndex,
                    showDragHandle = reorderMode,
                    onClick = { onQuestionClick(item.questionId) },
                    onDrag = { delta ->
                        if (draggedIndex == null) draggedIndex = displayIndex
                        dragOffset += delta
                        var currentIndex = draggedIndex ?: displayIndex

                        while (dragOffset > itemExtentPx / 2 && currentIndex < displayQuestions.lastIndex) {
                            displayQuestions.swap(currentIndex, currentIndex + 1)
                            currentIndex += 1
                            draggedIndex = currentIndex
                            dragOffset -= itemExtentPx
                        }

                        while (dragOffset < -itemExtentPx / 2 && currentIndex > 0) {
                            displayQuestions.swap(currentIndex, currentIndex - 1)
                            currentIndex -= 1
                            draggedIndex = currentIndex
                            dragOffset += itemExtentPx
                        }
                    },
                    onDragEnd = {
                        if (draggedIndex != null) {
                            onReorderQuestions(displayQuestions.map { it.questionId })
                        }
                        draggedIndex = null
                        dragOffset = 0f
                    },
                    onDragCancel = {
                        draggedIndex = null
                        dragOffset = 0f
                        displayQuestions.clear()
                        displayQuestions.addAll(uiState.questions)
                    },
                )
            }
        }

        UActionsSheetFab(
            actions =
                listOf(
                    UFabActionItem(
                        id = "create_question",
                        label = strings.newQuestion,
                        description = strings.createQuestionActionDescription,
                        iconRes = UIcons.Actions.Edit,
                        containerColor = Navy500,
                        contentColor = Color.White,
                        onClick = onCreateQuestionClick,
                    ),
                    UFabActionItem(
                        id = "export_pack",
                        label = strings.exportUQuizAction,
                        description = strings.exportUQuizActionDescription,
                        iconRes = UIcons.Actions.Download,
                        containerColor = Teal700,
                        contentColor = Color.White,
                        onClick = onExportPackClick,
                    ),
                    UFabActionItem(
                        id = "delete_pack",
                        label = strings.deletePack,
                        description = strings.deletePackConfirmMessage,
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

    when (val dialog = uiState.dialogState) {
        PackDialogState.None -> {
            Unit
        }

        is PackDialogState.EditPack -> {
            CreatePackDialog(
                title = strings.editPack,
                description = strings.editPackDescription,
                confirmLabel = strings.save,
                onDismiss = onDialogDismiss,
                onConfirm = onEditPackConfirm,
                initialTitle = dialog.pack.title,
                initialDescription = dialog.pack.description.orEmpty(),
                initialColorHex = dialog.pack.colorHex ?: contentColorPalette.first().hex,
                initialIcon = dialog.pack.icon ?: packSelectableIconPalette.first().key,
            )
        }

        is PackDialogState.DeletePack -> {
            SafeDeleteEntityDialog(
                title = strings.deletePack,
                primaryMessage = strings.deletePackPrimaryMessage,
                secondaryMessage = strings.deletePackSecondaryMessage,
                requiredKeyword = strings.deletePackKeyword,
                keywordInstruction = strings.deletePackTypeKeywordInstruction(strings.deletePackKeyword),
                headerIconRes = UIcons.Actions.Delete,
                onDismiss = onDialogDismiss,
                onConfirm = onDeletePackConfirm,
            )
        }
    }

    if (uiState.showStatsSheet) {
        PackStatsBottomSheet(
            stats = uiState.detailedStats,
            onSeeFullStatsClick = onSeeFullStatsClick,
            onDismiss = onStatsDismiss,
        )
    }
}

private fun <T> MutableList<T>.swap(
    from: Int,
    to: Int,
) {
    if (from == to) return
    val item = removeAt(from)
    add(to, item)
}
