package com.uquiz.android.ui.feature.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.files.documentDisplayName
import com.uquiz.android.core.files.readDocumentText
import com.uquiz.android.core.files.requireSupportedImportExtension
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.common.toUiMessage
import com.uquiz.android.ui.designsystem.animations.screens.UNotFoundMascot
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.designsystem.components.inputs.USearchField
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.contentColorPalette
import com.uquiz.android.ui.designsystem.tokens.folderSelectableIconPalette
import com.uquiz.android.ui.designsystem.tokens.packSelectableIconPalette
import com.uquiz.android.ui.feature.library.model.LibraryDialogState
import com.uquiz.android.ui.feature.library.model.LibraryUiEvent
import com.uquiz.android.ui.feature.library.model.LibraryUiState
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.content.FolderListCard
import com.uquiz.android.ui.shared.components.content.PackCard
import com.uquiz.android.ui.shared.components.dialogs.CreateFolderDialog
import com.uquiz.android.ui.shared.components.dialogs.CreatePackDialog
import com.uquiz.android.ui.shared.components.dialogs.SafeDeleteEntityDialog

@Composable
fun LibraryRoute(
    folderRepository: FolderRepository,
    packRepository: PackRepository,
    packStatsRepository: PackStatsRepository,
    importExportRepository: ImportExportRepository,
    onFolderClick: (id: String, name: String) -> Unit,
    onPackClick: (id: String, title: String) -> Unit,
) {
    val strings = LocalStrings.current
    val context = LocalContext.current
    val toastController = LocalToastController.current
    val viewModel: LibraryViewModel =
        viewModel(
            factory =
                LibraryViewModel.Factory(
                    folderRepository = folderRepository,
                    packRepository = packRepository,
                    packStatsRepository = packStatsRepository,
                    importExportRepository = importExportRepository,
                    initialStrings = strings,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(strings) {
        viewModel.updateStrings(strings)
    }

    val importLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            try {
                requireSupportedImportExtension(context.documentDisplayName(uri))
                viewModel.onImportContentReceived(context.readDocumentText(uri))
            } catch (throwable: Throwable) {
                toastController.show(throwable.toUiMessage(strings.errors), UToastTone.Error)
            }
        }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is LibraryUiEvent.OpenImportPicker -> importLauncher.launch(arrayOf("*/*"))
                is LibraryUiEvent.ShowToast -> toastController.show(event.message, event.tone)
            }
        }
    }

    LibraryScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onFolderClick = { folder -> onFolderClick(folder.id, folder.name) },
        onPackClick = { pack -> onPackClick(pack.id, pack.title) },
        onEditFolderClick = viewModel::onEditFolderRequested,
        onDeleteFolderClick = viewModel::onDeleteFolderRequested,
        onEditPackClick = viewModel::onEditPackRequested,
        onDeletePackClick = viewModel::onDeletePackRequested,
        onDialogDismiss = viewModel::onDialogDismissed,
        onCreateFolderConfirm = viewModel::onCreateFolderConfirmed,
        onEditFolderConfirm = viewModel::onEditFolderConfirmed,
        onDeleteFolderConfirm = viewModel::onDeleteFolderConfirmed,
        onEditPackConfirm = viewModel::onEditPackConfirmed,
        onDeletePackConfirm = viewModel::onDeletePackConfirmed,
    )
}

@Composable
private fun LibraryScreen(
    uiState: LibraryUiState,
    onSearchQueryChange: (String) -> Unit,
    onFolderClick: (Folder) -> Unit,
    onPackClick: (Pack) -> Unit,
    onEditFolderClick: (Folder) -> Unit,
    onDeleteFolderClick: (Folder) -> Unit,
    onEditPackClick: (Pack) -> Unit,
    onDeletePackClick: (Pack) -> Unit,
    onDialogDismiss: () -> Unit,
    onCreateFolderConfirm: (String, String, String) -> Unit,
    onEditFolderConfirm: (Folder, String, String, String) -> Unit,
    onDeleteFolderConfirm: (Folder) -> Unit,
    onEditPackConfirm: (Pack, String, String?, String, String) -> Unit,
    onDeletePackConfirm: (Pack) -> Unit,
) {
    val strings = LocalStrings.current

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
        ) {
            item {
                USearchField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = LocalStrings.current.searchPlaceholder,
                    leadingIcon = rememberVectorPainter(Icons.Outlined.Search),
                )
            }

            if (uiState.searchQuery.isNotBlank() && uiState.filteredFolders.isEmpty() &&
                uiState.filteredPacks.isEmpty()
            ) {
                item {
                    UNotFoundMascot(modifier = Modifier.fillMaxWidth().wrapContentHeight())
                    Text(
                        text = strings.noSearchResults,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Neutral400,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            if (uiState.filteredFolders.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(24.dp))
                    SectionHeader(strings.foldersSection)
                    Spacer(Modifier.height(8.dp))
                    FolderListCard(
                        folders = uiState.filteredFolders,
                        onFolderClick = onFolderClick,
                        onEditFolderClick = onEditFolderClick,
                        onDeleteFolderClick = onDeleteFolderClick,
                    )
                }
            }

            if (uiState.filteredPacks.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(24.dp))
                    SectionHeader(strings.recentPacksSection)
                    Spacer(Modifier.height(8.dp))
                }
                itemsIndexed(uiState.filteredPacks, key = { _, item -> item.pack.id }) { index, item ->
                    PackCard(
                        pack = item.pack,
                        questionCount = item.questionCount,
                        answeredCount = item.answeredCount,
                        progress = item.progress,
                        accentIndex = index,
                        onClick = { onPackClick(item.pack) },
                        onEditClick = { onEditPackClick(item.pack) },
                        onDeleteClick = { onDeletePackClick(item.pack) },
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }

        UActionsSheetFab(
            actions = uiState.actions,
            modifier = Modifier.fillMaxSize(),
        )
    }

    when (val dialog = uiState.dialogState) {
        LibraryDialogState.None -> {
            Unit
        }

        LibraryDialogState.CreateFolder -> {
            CreateFolderDialog(
                title = strings.newFolder,
                description = strings.createFolderDescription,
                confirmLabel = strings.create,
                onDismiss = onDialogDismiss,
                onConfirm = onCreateFolderConfirm,
            )
        }

        is LibraryDialogState.EditFolder -> {
            CreateFolderDialog(
                title = strings.editFolder,
                description = strings.editFolderDescription,
                confirmLabel = strings.save,
                onDismiss = onDialogDismiss,
                onConfirm = { name, colorHex, icon ->
                    onEditFolderConfirm(dialog.folder, name, colorHex, icon)
                },
                initialName = dialog.folder.name,
                initialColorHex = dialog.folder.colorHex ?: contentColorPalette.first().hex,
                initialIcon = dialog.folder.icon ?: folderSelectableIconPalette.first().key,
            )
        }

        is LibraryDialogState.DeleteFolder -> {
            SafeDeleteEntityDialog(
                title = strings.deleteFolder,
                primaryMessage = strings.deleteFolderPrimaryMessage,
                secondaryMessage = strings.deleteFolderSecondaryMessage,
                requiredKeyword = strings.deleteFolderKeyword,
                keywordInstruction = strings.deleteFolderTypeKeywordInstruction(strings.deleteFolderKeyword),
                headerIconRes = UIcons.Content.Folder.Error,
                onDismiss = onDialogDismiss,
                onConfirm = { onDeleteFolderConfirm(dialog.folder) },
            )
        }

        is LibraryDialogState.EditPack -> {
            CreatePackDialog(
                title = strings.editPack,
                description = strings.editPackDescription,
                confirmLabel = strings.save,
                onDismiss = onDialogDismiss,
                onConfirm = { title, description, colorHex, icon ->
                    onEditPackConfirm(dialog.pack, title, description, colorHex, icon)
                },
                initialTitle = dialog.pack.title,
                initialDescription = dialog.pack.description.orEmpty(),
                initialColorHex = dialog.pack.colorHex ?: contentColorPalette.first().hex,
                initialIcon = dialog.pack.icon ?: packSelectableIconPalette.first().key,
            )
        }

        is LibraryDialogState.DeletePack -> {
            SafeDeleteEntityDialog(
                title = strings.deletePack,
                primaryMessage = strings.deletePackPrimaryMessage,
                secondaryMessage = strings.deletePackSecondaryMessage,
                requiredKeyword = strings.deletePackKeyword,
                keywordInstruction = strings.deletePackTypeKeywordInstruction(strings.deletePackKeyword),
                headerIconRes = UIcons.Content.Pack.Error,
                onDismiss = onDialogDismiss,
                onConfirm = { onDeletePackConfirm(dialog.pack) },
            )
        }
    }
}
