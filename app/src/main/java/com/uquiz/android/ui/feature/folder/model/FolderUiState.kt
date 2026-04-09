package com.uquiz.android.ui.feature.folder.model

import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.projection.FolderWithCounts
import com.uquiz.android.ui.shared.model.PackListItemUiModel

/**
 * ### FolderUiState
 *
 * Estado de UI de la pantalla de carpeta.
 *
 * Reúne la carpeta actual, sus hijos directos, los packs visibles y el diálogo
 * activo para que la screen pueda renderizar toda la experiencia desde un único
 * snapshot inmutable.
 */
data class FolderUiState(
    val isLoading: Boolean = true,
    val currentFolder: Folder? = null,
    val childFolders: List<FolderWithCounts> = emptyList(),
    val packs: List<PackListItemUiModel> = emptyList(),
    val isEmpty: Boolean = true,
    val dialogState: FolderDialogState = FolderDialogState.None,
)
