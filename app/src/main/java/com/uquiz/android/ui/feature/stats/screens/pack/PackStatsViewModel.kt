package com.uquiz.android.ui.feature.stats.screens.pack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.feature.stats.screens.pack.model.PackStatsUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PackStatsViewModel(
    packRepository: PackRepository,
    private val packStatsRepository: PackStatsRepository,
    private val packId: String
) : ViewModel() {

    init {
        viewModelScope.launch {
            packStatsRepository.refresh(packId)
        }
    }

    val uiState: StateFlow<PackStatsUiState> = combine(
        packRepository.observeById(packId),
        packStatsRepository.observeDetailed(packId)
    ) { pack, stats ->
        PackStatsUiState(
            isLoading = false,
            packTitle = pack?.title.orEmpty(),
            stats = stats
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        PackStatsUiState()
    )

    class Factory(
        private val packRepository: PackRepository,
        private val packStatsRepository: PackStatsRepository,
        private val packId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PackStatsViewModel(
                packRepository = packRepository,
                packStatsRepository = packStatsRepository,
                packId = packId
            ) as T
        }
    }
}
