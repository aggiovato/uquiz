package com.uquiz.android.ui.feature.pack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.stats.projection.PackOverviewMetrics
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.feature.pack.model.PackDialogState
import com.uquiz.android.ui.feature.pack.model.PackOverviewUiState
import com.uquiz.android.ui.feature.pack.model.QuestionListItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel del detalle de pack.
 *
 * Combina los metadatos del pack, sus preguntas y sus estadísticas resumidas,
 * además de coordinar reorder, edición, borrado y visibilidad del bottom sheet
 * de estadísticas.
 */
class PackViewModel(
    private val packRepository: PackRepository,
    private val packStatsRepository: PackStatsRepository,
    private val packId: String
) : ViewModel() {

    private val showStatsSheet = MutableStateFlow(false)
    private val dialogState = MutableStateFlow<PackDialogState>(PackDialogState.None)

    init {
        viewModelScope.launch {
            packStatsRepository.refresh(packId)
        }
    }

    val uiState: StateFlow<PackOverviewUiState> = combine(
        packRepository.observeById(packId),
        packRepository.observeWithQuestions(packId),
        packStatsRepository.observeDetailed(packId),
        showStatsSheet,
        dialogState
    ) { pack, questions, detailedStats, isStatsSheetVisible, packDialogState ->
        val items = questions.map {
            QuestionListItemUiModel(
                questionId = it.question.id,
                markdownText = it.question.text,
                difficulty = it.question.difficulty
            )
        }
        val questionCount = items.size
        val overview = PackOverviewMetrics(
            questionCount = questionCount,
            accuracyPercent = detailedStats.averageAccuracyPercent,
            sessionsCount = detailedStats.totalSessions.takeIf { it > 0 },
            progressPercent = if (questionCount > 0) {
                ((detailedStats.dominatedQuestions * 100f) / questionCount).toInt()
            } else {
                0
            },
            dominatedQuestions = detailedStats.dominatedQuestions,
            totalQuestions = questionCount
        )

        PackOverviewUiState(
            packId = packId,
            packTitle = pack?.title.orEmpty(),
            packDescription = pack?.description,
            overview = overview,
            detailedStats = detailedStats.copy(
                totalQuestions = questionCount,
                progressPercent = overview.progressPercent ?: 0
            ),
            questions = items,
            canStartStudy = items.isNotEmpty(),
            canStartGame = items.isNotEmpty(),
            canCreateQuestion = true,
            showStatsSheet = isStatsSheetVisible,
            dialogState = packDialogState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        PackOverviewUiState(packId = packId)
    )

    /** Persiste el nuevo orden de preguntas tras una operación de drag and drop. */
    fun reorderQuestions(orderedQuestionIds: List<String>) {
        viewModelScope.launch {
            packRepository.reorderQuestions(packId, orderedQuestionIds)
        }
    }

    /** Abre el diálogo de edición del pack actual. */
    fun onEditPackRequested() {
        val current = uiState.value
        if (current.packTitle.isBlank()) return
        viewModelScope.launch {
            packRepository.getById(packId)?.let { dialogState.value = PackDialogState.EditPack(it) }
        }
    }

    /** Aplica los cambios confirmados para el pack actual. */
    fun onEditPackConfirmed(title: String, description: String?, colorHex: String, icon: String) {
        viewModelScope.launch {
            val pack = packRepository.getById(packId) ?: return@launch
            packRepository.updatePack(
                pack.copy(
                    title = title,
                    description = description,
                    colorHex = colorHex,
                    icon = icon
                )
            )
            dialogState.value = PackDialogState.None
        }
    }

    /** Cierra cualquier diálogo activo de la pantalla. */
    fun onDialogDismissed() {
        dialogState.value = PackDialogState.None
    }

    /** Muestra el bottom sheet con estadísticas resumidas. */
    fun onStatsRequested() {
        showStatsSheet.value = true
    }

    /** Oculta el bottom sheet de estadísticas. */
    fun onStatsDismissed() {
        showStatsSheet.value = false
    }

    /** Abre el diálogo de borrado del pack actual. */
    fun onDeletePackRequested() {
        viewModelScope.launch {
            packRepository.getById(packId)?.let { dialogState.value = PackDialogState.DeletePack(it) }
        }
    }

    /** Elimina el pack actual y notifica al caller para volver atrás. */
    fun onDeletePackConfirmed(onDeleted: () -> Unit) {
        viewModelScope.launch {
            val pack = packRepository.getById(packId) ?: return@launch
            packRepository.deletePack(pack.id)
            dialogState.value = PackDialogState.None
            onDeleted()
        }
    }

    /** Factory que resuelve las dependencias requeridas por [PackViewModel]. */
    class Factory(
        private val packRepository: PackRepository,
        private val packStatsRepository: PackStatsRepository,
        private val packId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PackViewModel(packRepository, packStatsRepository, packId) as T
        }
    }
}
