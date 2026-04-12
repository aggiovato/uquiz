package com.uquiz.android.ui.feature.library.model

import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack

/**
 * Estado de diálogos visibles en la pantalla de biblioteca.
 */
sealed interface LibraryDialogState {
    data object None : LibraryDialogState
    data object CreateFolder : LibraryDialogState
    data class EditFolder(val folder: Folder) : LibraryDialogState
    data class DeleteFolder(val folder: Folder) : LibraryDialogState
    data class EditPack(val pack: Pack) : LibraryDialogState
    data class DeletePack(val pack: Pack) : LibraryDialogState
}
