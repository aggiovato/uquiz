package com.uquiz.android.ui.feature.folder.model

import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack

/**
 * ### FolderDialogState
 *
 * Estado de diálogo del feature de carpeta.
 *
 * Modela los distintos modales que puede abrir la pantalla para crear, editar o
 * eliminar carpetas y packs.
 */
sealed interface FolderDialogState {
    data object None : FolderDialogState
    data object CreateFolder : FolderDialogState
    data object CreatePack : FolderDialogState
    data class EditFolder(val folder: Folder) : FolderDialogState
    data class DeleteFolder(val folder: Folder) : FolderDialogState
    data class EditPack(val pack: Pack) : FolderDialogState
    data class DeletePack(val pack: Pack) : FolderDialogState
    data object DeleteCurrentFolder : FolderDialogState
}
