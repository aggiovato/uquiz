package com.uquiz.android.ui.feature.folder.model

import com.uquiz.android.domain.importexport.projection.ExportedUQuizFile
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone

/**
 * ### FolderUiEvent
 *
 * Eventos efímeros emitidos por el feature de carpeta para coordinar efectos de
 * UI como selección de archivos, toasts y navegación puntual.
 */
sealed interface FolderUiEvent {
    data object OpenImportPicker : FolderUiEvent
    data class StartExport(val file: ExportedUQuizFile, val successName: String) : FolderUiEvent
    data class ShowToast(val message: String, val tone: UToastTone) : FolderUiEvent
    data object NavigateBackAfterDelete : FolderUiEvent
}
