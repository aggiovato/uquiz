package com.uquiz.android.ui.feature.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.common.toUiMessage
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.feature.folder.model.FolderDialogState
import com.uquiz.android.ui.feature.folder.model.FolderUiEvent
import com.uquiz.android.ui.feature.folder.model.FolderUiState
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.shared.model.toPackListItems
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de carpeta.
 *
 * Observa la carpeta actual y su contenido directo, coordina los diálogos de
 * creación, edición y borrado, y expone eventos efímeros para importación,
 * exportación, toasts y navegación de vuelta tras eliminar la carpeta visible.
 */
class FolderViewModel(
    private val folderRepository: FolderRepository,
    private val packRepository: PackRepository,
    private val packStatsRepository: PackStatsRepository,
    private val importExportRepository: ImportExportRepository,
    private val folderId: String,
    initialStrings: AppStrings
) : ViewModel() {

    private val strings = MutableStateFlow(initialStrings)
    private val dialogState = MutableStateFlow<FolderDialogState>(FolderDialogState.None)
    private val events = Channel<FolderUiEvent>(Channel.BUFFERED)

    val uiEvents = events.receiveAsFlow()

    private val contentState = combine(
        folderRepository.observeById(folderId),
        folderRepository.observeByParentWithCounts(folderId),
        packRepository.observeByFolderWithQuestionCounts(folderId),
        packStatsRepository.observeActiveStudyProgress()
    ) { folder, childFolders, packs, activeProgress ->
        Triple(folder, childFolders, packs.toPackListItems(activeProgress))
    }

    val uiState: StateFlow<FolderUiState> = combine(
        contentState,
        dialogState,
    ) { content, currentDialog ->
        val (folder, childFolders, packItems) = content
        FolderUiState(
            isLoading = false,
            currentFolder = folder,
            childFolders = childFolders,
            packs = packItems,
            isEmpty = childFolders.isEmpty() && packItems.isEmpty(),
            dialogState = currentDialog,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FolderUiState())

    /** Actualiza las strings activas para que los eventos efímeros usen el idioma visible. */
    fun updateStrings(appStrings: AppStrings) {
        strings.value = appStrings
    }

    /** Abre el diálogo de creación de subcarpeta. */
    fun onCreateFolderRequested() {
        dialogState.value = FolderDialogState.CreateFolder
    }

    /** Abre el diálogo de creación de pack dentro de la carpeta actual. */
    fun onCreatePackRequested() {
        dialogState.value = FolderDialogState.CreatePack
    }

    /** Abre el diálogo de edición para la carpeta indicada. */
    fun onEditFolderRequested(folder: Folder) {
        dialogState.value = FolderDialogState.EditFolder(folder)
    }

    /** Abre el diálogo de confirmación de borrado para la carpeta indicada. */
    fun onDeleteFolderRequested(folder: Folder) {
        dialogState.value = FolderDialogState.DeleteFolder(folder)
    }

    /** Abre el diálogo de edición para el pack indicado. */
    fun onEditPackRequested(pack: Pack) {
        dialogState.value = FolderDialogState.EditPack(pack)
    }

    /** Abre el diálogo de confirmación de borrado para el pack indicado. */
    fun onDeletePackRequested(pack: Pack) {
        dialogState.value = FolderDialogState.DeletePack(pack)
    }

    /** Abre la confirmación de borrado de la carpeta actualmente visible. */
    fun onDeleteCurrentFolderRequested() {
        dialogState.value = FolderDialogState.DeleteCurrentFolder
    }

    /** Cierra cualquier diálogo actualmente abierto por el feature. */
    fun onDialogDismissed() {
        dialogState.value = FolderDialogState.None
    }

    /** Solicita a la UI abrir el selector de documentos para importar contenido. */
    fun onImportRequested() {
        viewModelScope.launch {
            events.send(FolderUiEvent.OpenImportPicker)
        }
    }

    /** Crea una subcarpeta y notifica el resultado mediante eventos de UI. */
    fun onCreateFolderConfirmed(name: String, colorHex: String, icon: String) {
        viewModelScope.launch {
            try {
                folderRepository.createFolder(
                    name = name,
                    parentId = folderId,
                    colorHex = colorHex,
                    icon = icon
                )
                dialogState.value = FolderDialogState.None
                events.send(
                    FolderUiEvent.ShowToast(
                        message = strings.value.common.folderCreatedToast,
                        tone = UToastTone.Success
                    )
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Crea un pack dentro de la carpeta actual y notifica el resultado en UI. */
    fun onCreatePackConfirmed(title: String, description: String?, colorHex: String, icon: String) {
        viewModelScope.launch {
            try {
                packRepository.createPack(
                    title = title,
                    description = description,
                    folderId = folderId,
                    icon = icon,
                    colorHex = colorHex
                )
                dialogState.value = FolderDialogState.None
                events.send(
                    FolderUiEvent.ShowToast(
                        message = strings.value.common.packCreatedToast,
                        tone = UToastTone.Success
                    )
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Persiste los cambios de una carpeta ya existente. */
    fun onEditFolderConfirmed(folder: Folder, name: String, colorHex: String, icon: String) {
        viewModelScope.launch {
            try {
                folderRepository.updateFolder(
                    folder.copy(name = name, colorHex = colorHex, icon = icon)
                )
                dialogState.value = FolderDialogState.None
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Elimina una subcarpeta concreta y muestra feedback al usuario. */
    fun onDeleteFolderConfirmed(folder: Folder) {
        viewModelScope.launch {
            try {
                folderRepository.deleteFolder(folder.id)
                dialogState.value = FolderDialogState.None
                events.send(
                    FolderUiEvent.ShowToast(
                        message = strings.value.common.folderDeletedToast,
                        tone = UToastTone.Info
                    )
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Persiste los cambios de un pack ya existente. */
    fun onEditPackConfirmed(pack: Pack, title: String, description: String?, colorHex: String, icon: String) {
        viewModelScope.launch {
            try {
                packRepository.updatePack(pack.copy(title = title, description = description, colorHex = colorHex, icon = icon))
                dialogState.value = FolderDialogState.None
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Elimina un pack concreto y muestra feedback al usuario. */
    fun onDeletePackConfirmed(pack: Pack) {
        viewModelScope.launch {
            try {
                packRepository.deletePack(pack.id)
                dialogState.value = FolderDialogState.None
                events.send(
                    FolderUiEvent.ShowToast(
                        message = strings.value.common.packDeletedToast,
                        tone = UToastTone.Info
                    )
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Elimina la carpeta abierta y emite navegación de vuelta al completarse. */
    fun onDeleteCurrentFolderConfirmed() {
        viewModelScope.launch {
            val folder = uiState.value.currentFolder ?: return@launch
            try {
                folderRepository.deleteFolder(folder.id)
                dialogState.value = FolderDialogState.None
                events.send(
                    FolderUiEvent.ShowToast(
                        message = strings.value.common.folderDeletedToast,
                        tone = UToastTone.Info
                    )
                )
                events.send(FolderUiEvent.NavigateBackAfterDelete)
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Procesa el contenido importado y lo aplica dentro de la carpeta actual. */
    fun onImportContentReceived(content: String) {
        viewModelScope.launch {
            try {
                val result = importExportRepository.importIntoFolder(folderId, content)
                dialogState.value = FolderDialogState.None
                events.send(
                    FolderUiEvent.ShowToast(
                        message = strings.value.common.importUQuizSuccess(result.rootName),
                        tone = UToastTone.Success
                    )
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Solicita la exportación de la carpeta actual como archivo `.uquiz`. */
    fun onExportRequested() {
        viewModelScope.launch {
            try {
                val file = importExportRepository.exportFolderSubtree(folderId)
                events.send(
                    FolderUiEvent.StartExport(
                        file = file,
                        successName = uiState.value.currentFolder?.name ?: file.fileName.removeSuffix(".uquiz")
                    )
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Emite el toast de éxito tras confirmar la escritura del archivo exportado. */
    fun onExportWriteSucceeded(successName: String) {
        viewModelScope.launch {
            events.send(
                FolderUiEvent.ShowToast(
                    message = strings.value.common.exportUQuizSuccess(successName),
                    tone = UToastTone.Success
                )
            )
        }
    }

    /** Reenvía un error de escritura de exportación al canal de feedback de UI. */
    fun onExportWriteFailed(throwable: Throwable) {
        viewModelScope.launch {
            emitError(throwable)
        }
    }

    private suspend fun emitError(throwable: Throwable) {
        events.send(
            FolderUiEvent.ShowToast(
                message = throwable.toUiMessage(strings.value.errors),
                tone = UToastTone.Error
            )
        )
    }

    /** Factory usada por Compose para construir el ViewModel con dependencias explícitas. */
    class Factory(
        private val folderRepository: FolderRepository,
        private val packRepository: PackRepository,
        private val packStatsRepository: PackStatsRepository,
        private val importExportRepository: ImportExportRepository,
        private val folderId: String,
        private val initialStrings: AppStrings,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FolderViewModel(
                folderRepository = folderRepository,
                packRepository = packRepository,
                packStatsRepository = packStatsRepository,
                importExportRepository = importExportRepository,
                folderId = folderId,
                initialStrings = initialStrings,
            ) as T
    }
}
