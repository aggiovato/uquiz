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
            val (folders, recentPacks) = content
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
                actions =
                    buildLibraryActionItems(
                        strings = currentStrings,
                        onCreateFolder = ::onCreateFolderRequested,
                        onImport = ::onImportRequested,
                    ),
                dialogState = currentDialog,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LibraryUiState())

    fun updateStrings(appStrings: AppStrings) {
        strings.value = appStrings
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onCreateFolderRequested() {
        dialogState.value = LibraryDialogState.CreateFolder
    }

    fun onEditFolderRequested(folder: Folder) {
        dialogState.value = LibraryDialogState.EditFolder(folder)
    }

    fun onDeleteFolderRequested(folder: Folder) {
        dialogState.value = LibraryDialogState.DeleteFolder(folder)
    }

    fun onEditPackRequested(pack: Pack) {
        dialogState.value = LibraryDialogState.EditPack(pack)
    }

    fun onDeletePackRequested(pack: Pack) {
        dialogState.value = LibraryDialogState.DeletePack(pack)
    }

    fun onDialogDismissed() {
        dialogState.value = LibraryDialogState.None
    }

    fun onImportRequested() {
        viewModelScope.launch {
            events.send(LibraryUiEvent.OpenImportPicker)
        }
    }

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
                        message = strings.value.folderCreatedToast,
                        tone = UToastTone.Success,
                    ),
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

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

    fun onDeleteFolderConfirmed(folder: Folder) {
        viewModelScope.launch {
            try {
                folderRepository.deleteFolder(folder.id)
                dialogState.value = LibraryDialogState.None
                events.send(
                    LibraryUiEvent.ShowToast(
                        message = strings.value.folderDeletedToast,
                        tone = UToastTone.Info,
                    ),
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

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

    fun onDeletePackConfirmed(pack: Pack) {
        viewModelScope.launch {
            try {
                packRepository.deletePack(pack.id)
                dialogState.value = LibraryDialogState.None
                events.send(
                    LibraryUiEvent.ShowToast(
                        message = strings.value.packDeletedToast,
                        tone = UToastTone.Info,
                    ),
                )
            } catch (throwable: Throwable) {
                emitError(throwable)
            }
        }
    }

    fun onImportContentReceived(content: String) {
        viewModelScope.launch {
            try {
                val result = importExportRepository.importIntoLibrary(content)
                dialogState.value = LibraryDialogState.None
                events.send(
                    LibraryUiEvent.ShowToast(
                        message = strings.value.importUQuizSuccess(result.rootName),
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
