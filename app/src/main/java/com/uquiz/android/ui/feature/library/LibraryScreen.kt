package com.uquiz.android.ui.feature.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.uquiz.android.domain.content.projection.FolderWithCounts
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.common.toUiMessage
import com.uquiz.android.ui.designsystem.animations.screens.UNotFoundMascot
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.components.UEmptyContent
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.designsystem.components.inputs.USearchField
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.library.components.LibraryDialogs
import com.uquiz.android.ui.feature.library.components.LibraryModeBanner
import com.uquiz.android.ui.feature.library.model.LibraryDialogState
import com.uquiz.android.ui.feature.library.model.LibraryUiEvent
import com.uquiz.android.ui.feature.library.model.LibraryUiState
import com.uquiz.android.ui.feature.stats.components.StaggeredStatsBlock
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.content.FolderListCard
import com.uquiz.android.ui.shared.components.content.PackCard
import com.uquiz.android.ui.shared.model.PackListItemUiModel

/**
 * ### LibraryRoute
 *
 * Entrada pública de la pantalla principal de biblioteca.
 *
 * Resuelve el [LibraryViewModel], coordina el selector de importación y delega el
 * renderizado puro a [LibraryScreen].
 *
 * @param folderRepository Repositorio de carpetas.
 * @param packRepository Repositorio de packs.
 * @param packStatsRepository Repositorio de estadísticas de pack.
 * @param importExportRepository Repositorio de importación/exportación.
 * @param onFolderClick Navega al detalle de una carpeta.
 * @param onPackClick Navega al detalle de un pack.
 * @param onRandomStudyClick Navega al flujo de estudio del pack indicado.
 */
@Composable
fun LibraryRoute(
    folderRepository: FolderRepository,
    packRepository: PackRepository,
    packStatsRepository: PackStatsRepository,
    importExportRepository: ImportExportRepository,
    onFolderClick: (id: String, name: String) -> Unit,
    onPackClick: (id: String, title: String) -> Unit,
    onRandomStudyClick: (packId: String) -> Unit,
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
                is LibraryUiEvent.OpenRandomStudy -> onRandomStudyClick(event.packId)
                is LibraryUiEvent.ShowToast -> toastController.show(event.message, event.tone)
            }
        }
    }

    LibraryScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onRandomStudyClick = viewModel::onRandomStudyRequested,
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
    onRandomStudyClick: () -> Unit,
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
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) contentVisible = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
        ) {
            item {
                LibraryModeBanner(
                    visible = contentVisible,
                    enabled = uiState.hasStudiablePacks,
                    onRandomStudyClick = onRandomStudyClick,
                )
                Spacer(Modifier.height(16.dp))
                USearchField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = strings.common.searchPlaceholder,
                    leadingIcon = rememberVectorPainter(Icons.Outlined.Search),
                )
            }

            if (uiState.searchQuery.isBlank() &&
                uiState.filteredFolders.isEmpty() &&
                uiState.filteredPacks.isEmpty() &&
                !uiState.isLoading
            ) {
                // Biblioteca vacía: el usuario no ha creado ningún contenido todavía.
                item {
                    UEmptyContent(
                        message = strings.common.nothingHereYet,
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    )
                }
            }

            if (uiState.searchQuery.isNotBlank() &&
                uiState.filteredFolders.isEmpty() &&
                uiState.filteredPacks.isEmpty()
            ) {
                // Sin resultados de búsqueda.
                item {
                    UNotFoundMascot(modifier = Modifier.fillMaxWidth().wrapContentHeight())
                    Text(
                        text = strings.common.noSearchResults,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Neutral400,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            if (uiState.filteredFolders.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(24.dp))
                    StaggeredStatsBlock(visible = contentVisible, delayMillis = 60) {
                        Column {
                            SectionHeader(strings.library.foldersSection)
                            Spacer(Modifier.height(8.dp))
                            FolderListCard(
                                folders = uiState.filteredFolders,
                                onFolderClick = onFolderClick,
                                onEditFolderClick = onEditFolderClick,
                                onDeleteFolderClick = onDeleteFolderClick,
                            )
                        }
                    }
                }
            }

            if (uiState.filteredPacks.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(24.dp))
                    StaggeredStatsBlock(visible = contentVisible, delayMillis = 120) {
                        SectionHeader(strings.library.recentPacksSection)
                    }
                    Spacer(Modifier.height(8.dp))
                }
                itemsIndexed(uiState.filteredPacks, key = { _, item -> item.pack.id }) { index, item ->
                    StaggeredStatsBlock(
                        visible = contentVisible,
                        delayMillis = 120 + minOf(index, 4) * 60,
                    ) {
                        Column {
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
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }

        UActionsSheetFab(
            actions = uiState.actions,
            modifier = Modifier.fillMaxSize(),
        )
    }

    LibraryDialogs(
        dialogState = uiState.dialogState,
        onDialogDismiss = onDialogDismiss,
        onCreateFolderConfirm = onCreateFolderConfirm,
        onEditFolderConfirm = onEditFolderConfirm,
        onDeleteFolderConfirm = onDeleteFolderConfirm,
        onEditPackConfirm = onEditPackConfirm,
        onDeletePackConfirm = onDeletePackConfirm,
    )
}

@UPreview
@Composable
private fun LibraryScreenPreview() {
    UTheme {
        LibraryScreen(
            uiState = LibraryUiState(
                isLoading = false,
                hasStudiablePacks = true,
                searchQuery = "",
                filteredFolders = listOf(
                    FolderWithCounts(
                        folder = Folder(
                            id = "folder-1",
                            name = "Idiomas",
                            createdAt = 0L,
                            updatedAt = 0L,
                        ),
                        subfolderCount = 2,
                        packCount = 4,
                    ),
                ),
                filteredPacks = listOf(
                    PackListItemUiModel(
                        pack = Pack(
                            id = "pack-1",
                            title = "Verbos irregulares",
                            description = null,
                            folderId = "folder-1",
                            createdAt = 0L,
                            updatedAt = 0L,
                        ),
                        questionCount = 24,
                        answeredCount = 10,
                        progress = 0.42f,
                    ),
                ),
                dialogState = LibraryDialogState.None,
            ),
            onSearchQueryChange = {},
            onRandomStudyClick = {},
            onFolderClick = {},
            onPackClick = {},
            onEditFolderClick = {},
            onDeleteFolderClick = {},
            onEditPackClick = {},
            onDeletePackClick = {},
            onDialogDismiss = {},
            onCreateFolderConfirm = { _, _, _ -> },
            onEditFolderConfirm = { _, _, _, _ -> },
            onDeleteFolderConfirm = {},
            onEditPackConfirm = { _, _, _, _, _ -> },
            onDeletePackConfirm = {},
        )
    }
}
