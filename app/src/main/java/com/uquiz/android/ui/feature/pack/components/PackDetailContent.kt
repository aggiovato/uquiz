package com.uquiz.android.ui.feature.pack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.domain.stats.projection.PackOverviewMetrics
import com.uquiz.android.ui.designsystem.animations.screens.UNotFoundMascot
import com.uquiz.android.ui.designsystem.components.UEmptyContent
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.pack.model.PackOverviewUiState
import com.uquiz.android.ui.feature.pack.model.QuestionListItemUiModel
import com.uquiz.android.ui.i18n.LocalStrings

private val questionItemSpacing = 10.dp

/**
 * ### PackDetailContent
 *
 * Contenido desplazable del detalle de pack con resumen, acciones y listado de preguntas.
 *
 * Mantiene localmente el estado efímero de filtro y reorder para evitar que el
 * [PackScreen] tenga que gestionar lógica de interacción de bajo nivel.
 *
 * @param uiState Estado visible del detalle de pack.
 * @param onStudyModeClick Acción para iniciar el modo estudio.
 * @param onGameClick Acción para iniciar el modo juego.
 * @param onReorderQuestions Acción para persistir el nuevo orden de preguntas.
 * @param onQuestionClick Acción para abrir una pregunta del listado.
 * @param onEditPackClick Acción para editar el pack.
 * @param onStatsClick Acción para abrir el resumen de estadísticas.
 */
@Composable
fun PackDetailContent(
    uiState: PackOverviewUiState,
    onStudyModeClick: () -> Unit,
    onGameClick: () -> Unit,
    onReorderQuestions: (List<String>) -> Unit,
    onQuestionClick: (String) -> Unit,
    onEditPackClick: () -> Unit,
    onStatsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val density = LocalDensity.current
    val itemExtentPx = with(density) { (QuestionListItemMinHeight + questionItemSpacing).toPx() }

    // Inicializar con las preguntas ya disponibles en el primer frame para evitar
    // el flash de lista vacía durante la transición de navegación.
    val displayQuestions = remember {
        mutableStateListOf<QuestionListItemUiModel>().also { it.addAll(uiState.questions) }
    }
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

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 96.dp),
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
                onGameClick = onGameClick,
            )
            Spacer(Modifier.height(22.dp))

            if (uiState.questions.isNotEmpty()) {
                PackQuestionToolbar(
                    questionCount = visibleQuestions.size,
                    reorderMode = reorderMode,
                    filterMode = filterMode,
                    filterQuery = filterQuery,
                    onReorderToggle = {
                        reorderMode = !reorderMode
                        if (reorderMode) {
                            filterMode = false
                            filterQuery = ""
                        }
                    },
                    onFilterToggle = {
                        filterMode = !filterMode
                        if (filterMode) {
                            reorderMode = false
                        } else {
                            filterQuery = ""
                        }
                    },
                    onFilterQueryChange = { filterQuery = it },
                )
                Spacer(Modifier.height(2.dp))
            } else {
                UEmptyContent(
                    message = strings.pack.noQuestionsYet,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        if (filterMode && filterQuery.isNotBlank() && visibleQuestions.isEmpty()) {
            item {
                UNotFoundMascot(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
                Text(
                    text = strings.common.noSearchResults,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Neutral400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
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
}

private fun <T> MutableList<T>.swap(from: Int, to: Int) {
    if (from == to) return
    val item = removeAt(from)
    add(to, item)
}

@UPreview
@Composable
private fun PackDetailContentPreview() {
    UTheme {
        Box {
            PackDetailContent(
                uiState = PackOverviewUiState(
                    packId = "pack-1",
                    packTitle = "Geografía europea",
                    packDescription = "Repaso de capitales, ríos y cordilleras.",
                    overview = PackOverviewMetrics(
                        questionCount = 18,
                        accuracyPercent = 70,
                        sessionsCount = 5,
                        progressPercent = 44,
                    ),
                    detailedStats = PackDetailedStats(
                        packId = "pack-1",
                        totalSessions = 5,
                        averageAccuracyPercent = 70,
                        averageDurationMs = 280_000L,
                        progressPercent = 44,
                        dominatedQuestions = 8,
                        totalQuestions = 18,
                    ),
                    questions = listOf(
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
                ),
                onStudyModeClick = {},
                onGameClick = {},
                onReorderQuestions = {},
                onQuestionClick = {},
                onEditPackClick = {},
                onStatsClick = {},
            )
        }
    }
}
