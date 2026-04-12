package com.uquiz.android.ui.feature.library.model

import com.uquiz.android.domain.content.projection.FolderWithCounts
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.shared.model.PackListItemUiModel

/** Estado de UI de la pantalla raíz de biblioteca. */
data class LibraryUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val folders: List<FolderWithCounts> = emptyList(),
    val recentPacks: List<PackListItemUiModel> = emptyList(),
    val filteredFolders: List<FolderWithCounts> = emptyList(),
    val filteredPacks: List<PackListItemUiModel> = emptyList(),
    val hasStudiablePacks: Boolean = false,
    val actions: List<UFabActionItem> = emptyList(),
    val dialogState: LibraryDialogState = LibraryDialogState.None
)
