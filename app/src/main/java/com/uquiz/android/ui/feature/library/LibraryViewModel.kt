package com.uquiz.android.ui.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.common.toUiMessage
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.feature.library.model.LibraryDialogState
import com.uquiz.android.ui.feature.library.model.LibraryUiEvent
import com.uquiz.android.ui.feature.library.model.LibraryUiState
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.shared.actions.buildLibraryActionItems
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
 * ViewModel de la pantalla de biblioteca.
 *
 * Agrupa el contenido raíz, aplica filtrado local y coordina acciones de creación,
 * edición, borrado e importación.
 */
class LibraryViewModel(
    private val folderRepository: FolderRepository,
    private val packRepository: PackRepository,
    private val packStatsRepository: PackStatsRepository,
    private val importExportRepository: ImportExportRepository,
    initialStrings: AppStrings,
) : ViewModel() {
    private val strings = MutableStateFlow(initialStrings)
    private val searchQuery = MutableStateFlow("")
    private val dialogState = MutableStateFlow<LibraryDialogState>(LibraryDialogState.None)
    private val events = Channel<LibraryUiEvent>(Channel.BUFFERED)

    val uiEvents = events.receiveAsFlow()

    private val contentState =
        combine(
            folderRepository.observeByParentWithCounts(null),
            packRepository.observeAllWithQuestionCounts(),
            packStatsRepository.observeActiveStudyProgress(),
        ) { folders, packs, activeProgress ->
            folders to packs.toPackListItems(activeProgress)
        }

    val uiState: StateFlow<LibraryUiState> =
        combine(
            contentState,
            searchQuery,
            dialogState,
            strings,
        ) { content, query, currentDialog, currentStrings ->
            val (folders, allPacks) = content

            // Packs con sesión activa (paused/abandoned), del más antiguo al más reciente.
            val inProgress = allPacks
                .filter { it.progress != null }
                .sortedBy { it.pack.updatedAt }

            // Resto de packs sin actividad, del más nuevo al más antiguo por fecha de creación.
            val fresh = allPacks
                .filter { it.progress == null }
                .sortedByDescending { it.pack.createdAt }

            // Máximo 5 en total: primero los activos, luego los recientes hasta completar 5.
            val recentPacks = (inProgress + fresh).take(5)

            val filteredFolders =
                if (query.isBlank()) {
                    folders
                } else {
                    folders.filter { it.folder.name.contains(query, ignoreCase = true) }
                }
            val filteredPacks =
                if (query.isBlank()) {
                    recentPacks
                } else {
                    recentPacks.filter { it.pack.title.contains(query, ignoreCase = true) }
                }

            LibraryUiState(
                isLoading = false,
                searchQuery = query,
                folders = folders,
                recentPacks = recentPacks,
                filteredFolders = filteredFolders,
                filteredPacks = filteredPacks,
                hasStudiablePacks = recentPacks.any { it.questionCount > 0 },
                actions =
                    buildLibraryActionItems(
                        strings = currentStrings,
                        onCreateFolder = ::onCreateFolderRequested,
                        onImport = ::onImportRequested,
                    ),
                dialogState = currentDialog,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LibraryUiState())

    /** Actualiza las cadenas activas para reconstruir acciones localizadas del FAB. */
    fun updateStrings(appStrings: AppStrings) {
        strings.value = appStrings
    }

    /** Actualiza la búsqueda local sobre carpetas y packs visibles. */
    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    /** Abre el diálogo de creación de carpeta raíz. */
    fun onCreateFolderRequested() {
        dialogState.value = LibraryDialogState.CreateFolder
    }

    /** Abre el diálogo de edición para la carpeta indicada. */
    fun onEditFolderRequested(folder: Folder) {
        dialogState.value = LibraryDialogState.EditFolder(folder)
    }

    /** Abre el diálogo de confirmación de borrado de carpeta. */
    fun onDeleteFolderRequested(folder: Folder) {
        dialogState.value = LibraryDialogState.DeleteFolder(folder)
    }

    /** Abre el diálogo de edición para el pack indicado. */
    fun onEditPackRequested(pack: Pack) {
        dialogState.value = LibraryDialogState.EditPack(pack)
    }

    /** Abre el diálogo de confirmación de borrado del pack indicado. */
    fun onDeletePackRequested(pack: Pack) {
        dialogState.value = LibraryDialogState.DeletePack(pack)
    }

    /** Cierra cualquier diálogo visible en la pantalla. */
    fun onDialogDismissed() {
        dialogState.value = LibraryDialogState.None
    }

    /** Selecciona un pack con preguntas al azar y solicita abrir su flujo de estudio. */
    fun onRandomStudyRequested() {
        val randomPack = uiState.value.recentPacks
            .filter { it.questionCount > 0 }
            .randomOrNull()
            ?: return
        viewModelScope.launch {
            events.send(LibraryUiEvent.OpenRandomStudy(randomPack.pack.id))
        }
    }

    /** Solicita a la pantalla abrir el selector de archivos de importación. */
    fun onImportRequested() {
        viewModelScope.launch {
            events.send(LibraryUiEvent.OpenImportPicker)
        }
    }

    /** Crea una carpeta raíz con los valores confirmados en el diálogo. */
    fun onCreateFolderConfirmed(
        name: String,
        colorHex: String,
        icon: String,
    ) {
        viewModelScope.launch {
            try {
                folderRepository.createFolder(
                    name = name,
                    parentId = null,
                    colorHex = colorHex,
                    icon = icon,
                )
                dialogState.value = LibraryDialogState.None
                events.send(
                    LibraryUiEvent.ShowToast(
                        message = strings.value.common.folderCreatedToast,
                        tone = UToastTone.Success,
                    ),
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Actualiza una carpeta existente con los cambios confirmados por la UI. */
    fun onEditFolderConfirmed(
        folder: Folder,
        name: String,
        colorHex: String,
        icon: String,
    ) {
        viewModelScope.launch {
            try {
                folderRepository.updateFolder(
                    folder.copy(name = name, colorHex = colorHex, icon = icon),
                )
                dialogState.value = LibraryDialogState.None
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Elimina la carpeta indicada y emite feedback al usuario. */
    fun onDeleteFolderConfirmed(folder: Folder) {
        viewModelScope.launch {
            try {
                folderRepository.deleteFolder(folder.id)
                dialogState.value = LibraryDialogState.None
                events.send(
                    LibraryUiEvent.ShowToast(
                        message = strings.value.common.folderDeletedToast,
                        tone = UToastTone.Info,
                    ),
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Actualiza los metadatos del pack indicado. */
    fun onEditPackConfirmed(
        pack: Pack,
        title: String,
        description: String?,
        colorHex: String,
        icon: String,
    ) {
        viewModelScope.launch {
            try {
                packRepository.updatePack(
                    pack.copy(
                        title = title,
                        description = description,
                        colorHex = colorHex,
                        icon = icon,
                    ),
                )
                dialogState.value = LibraryDialogState.None
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Elimina el pack indicado y emite feedback al usuario. */
    fun onDeletePackConfirmed(pack: Pack) {
        viewModelScope.launch {
            try {
                packRepository.deletePack(pack.id)
                dialogState.value = LibraryDialogState.None
                events.send(
                    LibraryUiEvent.ShowToast(
                        message = strings.value.common.packDeletedToast,
                        tone = UToastTone.Info,
                    ),
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    /** Importa contenido de biblioteca desde un documento ya leído en memoria. */
    fun onImportContentReceived(content: String) {
        viewModelScope.launch {
            try {
                val result = importExportRepository.importIntoLibrary(content)
                dialogState.value = LibraryDialogState.None
                events.send(
                    LibraryUiEvent.ShowToast(
                        message = strings.value.common.importUQuizSuccess(result.rootName),
                        tone = UToastTone.Success,
                    ),
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    private suspend fun emitError(throwable: Throwable) {
        events.send(
            LibraryUiEvent.ShowToast(
                message = throwable.toUiMessage(strings.value.errors),
                tone = UToastTone.Error,
            ),
        )
    }

    /** Factory que resuelve las dependencias requeridas por [LibraryViewModel]. */
    class Factory(
        private val folderRepository: FolderRepository,
        private val packRepository: PackRepository,
        private val packStatsRepository: PackStatsRepository,
        private val importExportRepository: ImportExportRepository,
        private val initialStrings: AppStrings,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LibraryViewModel(
                folderRepository = folderRepository,
                packRepository = packRepository,
                packStatsRepository = packStatsRepository,
                importExportRepository = importExportRepository,
                initialStrings = initialStrings,
            ) as T
    }
}
