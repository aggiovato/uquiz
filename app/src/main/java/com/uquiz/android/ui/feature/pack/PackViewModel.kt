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

    fun reorderQuestions(orderedQuestionIds: List<String>) {
        viewModelScope.launch {
            packRepository.reorderQuestions(packId, orderedQuestionIds)
        }
    }

    fun onEditPackRequested() {
        val current = uiState.value
        if (current.packTitle.isBlank()) return
        viewModelScope.launch {
            packRepository.getById(packId)?.let { dialogState.value = PackDialogState.EditPack(it) }
        }
    }

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

    fun onDialogDismissed() {
        dialogState.value = PackDialogState.None
    }

    fun onStatsRequested() {
        showStatsSheet.value = true
    }

    fun onStatsDismissed() {
        showStatsSheet.value = false
    }

    fun onDeletePackRequested() {
        viewModelScope.launch {
            packRepository.getById(packId)?.let { dialogState.value = PackDialogState.DeletePack(it) }
        }
    }

    fun onDeletePackConfirmed(onDeleted: () -> Unit) {
        viewModelScope.launch {
            val pack = packRepository.getById(packId) ?: return@launch
            packRepository.deletePack(pack.id)
            dialogState.value = PackDialogState.None
            onDeleted()
        }
    }

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
