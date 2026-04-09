package com.uquiz.android.ui.feature.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.content.repository.QuestionRepository
import com.uquiz.android.ui.feature.question.components.DifficultySelector
import com.uquiz.android.ui.feature.question.components.MarkdownEditorField
import com.uquiz.android.ui.feature.question.components.OptionsEditorSection
import com.uquiz.android.ui.feature.question.components.PreviewOptionUiModel
import com.uquiz.android.ui.feature.question.components.PreviewMarkdownDialog
import com.uquiz.android.ui.feature.question.components.SafeDeleteQuestionDialog
import com.uquiz.android.ui.feature.question.model.QuestionMode
import com.uquiz.android.ui.feature.question.model.QuestionUiState
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionKind
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.question.model.EditableOptionUiModel
import kotlinx.coroutines.launch

/**
 * ### QuestionRoute
 *
 * Punto de entrada público de la pantalla de pregunta.
 * Conecta el ViewModel, recopila el estado y delega la UI a [QuestionScreen].
 *
 * @param packId Identificador del pack al que pertenece la pregunta.
 * @param questionId Identificador de la pregunta a editar, o `null` para crear una nueva.
 * @param questionRepository Repositorio de preguntas.
 * @param packRepository Repositorio de packs.
 * @param onDone Se invoca al completar o cancelar la edición.
 */
@Composable
fun QuestionRoute(
    packId: String,
    questionId: String?,
    questionRepository: QuestionRepository,
    packRepository: PackRepository,
    onDone: () -> Unit
) {
    val viewModel: QuestionViewModel = viewModel(
        factory = QuestionViewModel.Factory(
            questionRepository = questionRepository,
            packRepository = packRepository,
            packId = packId,
            questionId = questionId
        )
    )
    val uiState by viewModel.uiState.collectAsState()

    QuestionScreen(
        uiState = uiState,
        onQuestionMarkdownChange = viewModel::updateQuestionMarkdown,
        onExplanationMarkdownChange = viewModel::updateExplanationMarkdown,
        onOptionTextChange = viewModel::updateOptionText,
        onOptionSelected = viewModel::selectCorrectOption,
        onRemoveOption = viewModel::removeOption,
        onDifficultySelected = viewModel::updateDifficulty,
        onAddOption = viewModel::addOption,
        onSave = { viewModel.save() },
        onDelete = { viewModel.delete() },
        onCancel = onDone
    )
}

/**
 * ### QuestionScreen
 *
 * Pantalla de edición de pregunta. UI pura sin acceso al ViewModel.
 *
 * @param uiState Estado actual de la pantalla.
 * @param onQuestionMarkdownChange Se invoca al editar el texto de la pregunta.
 * @param onExplanationMarkdownChange Se invoca al editar la explicación.
 * @param onOptionTextChange Se invoca al editar el texto de una opción.
 * @param onOptionSelected Se invoca al marcar una opción como correcta.
 * @param onRemoveOption Se invoca al eliminar una opción.
 * @param onDifficultySelected Se invoca al cambiar la dificultad.
 * @param onAddOption Se invoca al añadir una nueva opción.
 * @param onSave Se invoca al guardar; devuelve `true` si la operación fue exitosa.
 * @param onDelete Se invoca al eliminar; devuelve `true` si la operación fue exitosa.
 * @param onCancel Se invoca al cancelar o al terminar.
 */
@Composable
private fun QuestionScreen(
    uiState: QuestionUiState,
    onQuestionMarkdownChange: (String) -> Unit,
    onExplanationMarkdownChange: (String) -> Unit,
    onOptionTextChange: (String, String) -> Unit,
    onOptionSelected: (String) -> Unit,
    onRemoveOption: (String) -> Unit,
    onDifficultySelected: (DifficultyLevel) -> Unit,
    onAddOption: () -> Unit,
    onSave: suspend () -> Boolean,
    onDelete: suspend () -> Boolean,
    onCancel: () -> Unit
) {
    val strings = LocalStrings.current
    val toastController = LocalToastController.current
    val scope = rememberCoroutineScope()
    var showQuestionPreview by remember { mutableStateOf(false) }
    var showExplanationPreview by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val fabActions = buildList {
        add(
            UFabActionItem(
                id = "save",
                label = strings.save,
                description = strings.saveActionDescription,
                iconRes = UIcons.Actions.Edit,
                enabled = uiState.canSave,
                containerColor = Navy500,
                contentColor = androidx.compose.ui.graphics.Color.White,
                onClick = {}
            )
        )
        add(
            UFabActionItem(
                id = "cancel",
                label = strings.cancel,
                description = strings.cancelActionDescription,
                iconRes = UIcons.Actions.Close,
                containerColor = Neutral500,
                contentColor = androidx.compose.ui.graphics.Color.White,
                onClick = onCancel
            )
        )
        if (uiState.mode == QuestionMode.EDIT) {
            add(
                UFabActionItem(
                    id = "delete",
                    label = strings.delete,
                    description = strings.deleteQuestionActionDescription,
                    iconRes = UIcons.Actions.Delete,
                    kind = UFabActionKind.Destructive,
                    containerColor = com.uquiz.android.ui.designsystem.tokens.Red700,
                    contentColor = androidx.compose.ui.graphics.Color.White,
                    onClick = {}
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                MarkdownEditorField(
                    title = strings.questionMarkdownLabel,
                    value = uiState.questionMarkdown,
                    placeholder = strings.questionMarkdownHint,
                    previewLabel = strings.previewLabel,
                    onValueChange = onQuestionMarkdownChange,
                    onPreviewClick = { showQuestionPreview = true }
                )

                OptionsEditorSection(
                    options = uiState.options,
                    onOptionTextChange = onOptionTextChange,
                    onOptionSelected = onOptionSelected,
                    onRemoveOption = onRemoveOption,
                    onAddOption = onAddOption
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = strings.difficultySectionTitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = Neutral500
                    )
                    DifficultySelector(
                        selected = uiState.difficulty,
                        onSelected = onDifficultySelected
                    )
                }

                MarkdownEditorField(
                    title = strings.explanationMarkdownLabel,
                    value = uiState.explanationMarkdown,
                    placeholder = strings.explanationMarkdownHint,
                    previewLabel = strings.previewLabel,
                    onValueChange = onExplanationMarkdownChange,
                    onPreviewClick = { showExplanationPreview = true }
                )

                Spacer(Modifier.height(88.dp))
            }
        }

        UActionsSheetFab(
            actions = fabActions.map { action ->
                when (action.id) {
                    "save" -> action.copy(
                        onClick = {
                            scope.launch {
                                if (onSave()) {
                                    if (uiState.mode == QuestionMode.CREATE) {
                                        toastController.show(strings.questionCreatedToast, UToastTone.Success)
                                    }
                                    onCancel()
                                }
                            }
                        }
                    )
                    "delete" -> action.copy(
                        onClick = { showDeleteDialog = true }
                    )
                    else -> action
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    if (showQuestionPreview) {
        PreviewMarkdownDialog(
            title = strings.questionPreview,
            markdown = uiState.questionMarkdown,
            options = uiState.options
                .filter { it.text.isNotBlank() }
                .mapIndexed { index, option ->
                    PreviewOptionUiModel(
                        label = "${buildPreviewOptionLabel(index)}.",
                        markdown = option.text,
                        isCorrect = option.isCorrect
                    )
                },
            onDismiss = { showQuestionPreview = false }
        )
    }

    if (showExplanationPreview) {
        PreviewMarkdownDialog(
            title = strings.explanationPreview,
            markdown = uiState.explanationMarkdown,
            onDismiss = { showExplanationPreview = false }
        )
    }

    if (showDeleteDialog) {
        SafeDeleteQuestionDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                scope.launch {
                    if (onDelete()) {
                        toastController.show(strings.questionDeletedToast, UToastTone.Info)
                        onCancel()
                    }
                }
            }
        )
    }
}

private fun buildPreviewOptionLabel(index: Int): String {
    var number = index
    val builder = StringBuilder()
    do {
        builder.append(('A'.code + (number % 26)).toChar())
        number = number / 26 - 1
    } while (number >= 0)
    return builder.reverse().toString()
}

@UPreview
@Composable
private fun QuestionScreenPreview() {
    UTheme {
        QuestionScreen(
            uiState = QuestionUiState(
                mode = QuestionMode.CREATE,
                packId = "preview",
                isLoading = false,
                questionMarkdown = "¿Qué organela celular se encarga de producir energía?",
                options = listOf(
                    EditableOptionUiModel(id = "o1", text = "Núcleo"),
                    EditableOptionUiModel(id = "o2", text = "Mitocondria", isCorrect = true),
                    EditableOptionUiModel(id = "o3", text = "Ribosoma"),
                    EditableOptionUiModel(id = "o4", text = "Lisosoma"),
                ),
                difficulty = DifficultyLevel.MEDIUM,
            ),
            onQuestionMarkdownChange = {},
            onExplanationMarkdownChange = {},
            onOptionTextChange = { _, _ -> },
            onOptionSelected = {},
            onRemoveOption = {},
            onDifficultySelected = {},
            onAddOption = {},
            onSave = { false },
            onDelete = { false },
            onCancel = {},
        )
    }
}
