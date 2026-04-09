package com.uquiz.android.ui.feature.folder

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.files.documentDisplayName
import com.uquiz.android.core.files.readDocumentText
import com.uquiz.android.core.files.requireSupportedImportExtension
import com.uquiz.android.core.files.writeDocumentText
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.projection.FolderWithCounts
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.importexport.projection.ExportedUQuizFile
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.ui.common.toUiMessage
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastTone
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.folder.components.FolderDialogs
import com.uquiz.android.ui.feature.folder.model.FolderUiEvent
import com.uquiz.android.ui.feature.folder.model.FolderUiState
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.actions.buildFolderActionItems
import com.uquiz.android.ui.shared.components.content.FolderListCard
import com.uquiz.android.ui.shared.components.content.PackCard
import com.uquiz.android.ui.shared.model.PackListItemUiModel

/**
 * ### FolderRoute
 *
 * Punto de entrada de la pantalla de carpeta.
 *
 * Conecta el ViewModel, registra los launchers de importación y exportación, y
 * reacciona a eventos efímeros como toasts o navegación tras eliminar la carpeta
 * actualmente abierta.
 *
 * @param folderId Identificador de la carpeta que se va a mostrar.
 * @param folderRepository Repositorio para leer y modificar carpetas.
 * @param packRepository Repositorio para leer y modificar packs.
 * @param packStatsRepository Repositorio para resolver progreso activo de packs.
 * @param importExportRepository Repositorio para importar y exportar archivos `.uquiz`.
 * @param onFolderClick Callback invocado al abrir una subcarpeta.
 * @param onPackClick Callback invocado al abrir un pack.
 * @param onFolderDeleted Callback invocado al eliminar la carpeta actual.
 */
@Composable
fun FolderRoute(
    folderId: String,
    folderRepository: FolderRepository,
    packRepository: PackRepository,
    packStatsRepository: PackStatsRepository,
    importExportRepository: ImportExportRepository,
    onFolderClick: (id: String, name: String) -> Unit,
    onPackClick: (id: String, title: String) -> Unit,
    onFolderDeleted: () -> Unit,
) {
    val strings = LocalStrings.current
    val context = LocalContext.current
    val toastController = LocalToastController.current
    var pendingExport by remember { mutableStateOf<ExportedUQuizFile?>(null) }
    var pendingExportSuccessName by remember { mutableStateOf<String?>(null) }

    val viewModel: FolderViewModel =
        viewModel(
            factory =
                FolderViewModel.Factory(
                    folderRepository = folderRepository,
                    packRepository = packRepository,
                    packStatsRepository = packStatsRepository,
                    importExportRepository = importExportRepository,
                    folderId = folderId,
                    initialStrings = strings,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(strings) {
        viewModel.updateStrings(strings)
    }

    val importLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            handleImportSelection(
                uri = uri,
                strings = strings,
                onSuccess = { selectedUri ->
                    requireSupportedImportExtension(context.documentDisplayName(selectedUri))
                    viewModel.onImportContentReceived(context.readDocumentText(selectedUri))
                },
                onError = { message -> toastController.show(message, UToastTone.Error) },
            )
        }

    val exportLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
            handleExportSelection(
                uri = uri,
                strings = strings,
                pendingExport = pendingExport,
                successName = pendingExportSuccessName,
                writeDocument = { selectedUri, content -> context.writeDocumentText(selectedUri, content) },
                onWriteSucceeded = viewModel::onExportWriteSucceeded,
                onWriteFailed = viewModel::onExportWriteFailed,
                onClearPending = {
                    pendingExport = null
                    pendingExportSuccessName = null
                },
            )
        }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is FolderUiEvent.OpenImportPicker -> importLauncher.launch(arrayOf("*/*"))
                is FolderUiEvent.ShowToast -> toastController.show(event.message, event.tone)
                is FolderUiEvent.NavigateBackAfterDelete -> onFolderDeleted()
                is FolderUiEvent.StartExport -> {
                    pendingExport = event.file
                    pendingExportSuccessName = event.successName
                    exportLauncher.launch(event.file.fileName)
                }
            }
        }
    }

    val actions =
        buildFolderActionItems(
            strings = strings,
            canExport = uiState.currentFolder != null,
            canDeleteCurrent = uiState.currentFolder != null,
            onCreatePack = viewModel::onCreatePackRequested,
            onCreateFolder = viewModel::onCreateFolderRequested,
            onImport = viewModel::onImportRequested,
            onExport = viewModel::onExportRequested,
            onDeleteCurrent = viewModel::onDeleteCurrentFolderRequested,
        )

    FolderScreen(
        uiState = uiState,
        actions = actions,
        onFolderClick = { folder -> onFolderClick(folder.id, folder.name) },
        onPackClick = { pack -> onPackClick(pack.id, pack.title) },
        onEditFolderClick = viewModel::onEditFolderRequested,
        onDeleteFolderClick = viewModel::onDeleteFolderRequested,
        onEditPackClick = viewModel::onEditPackRequested,
        onDeletePackClick = viewModel::onDeletePackRequested,
        onDialogDismiss = viewModel::onDialogDismissed,
        onCreateFolderConfirm = viewModel::onCreateFolderConfirmed,
        onCreatePackConfirm = viewModel::onCreatePackConfirmed,
        onEditFolderConfirm = viewModel::onEditFolderConfirmed,
        onDeleteFolderConfirm = viewModel::onDeleteFolderConfirmed,
        onEditPackConfirm = viewModel::onEditPackConfirmed,
        onDeletePackConfirm = viewModel::onDeletePackConfirmed,
        onDeleteCurrentFolderConfirm = viewModel::onDeleteCurrentFolderConfirmed,
    )
}

/**
 * ### FolderScreen
 *
 * Pantalla principal del feature de carpeta.
 *
 * Renderiza el contenido directo de la carpeta actual, el estado vacío o de
 * carga, el panel de acciones rápidas y los diálogos del flujo.
 *
 * @param uiState Estado actual de la pantalla.
 * @param actions Acciones rápidas mostradas en el `fab` expandible.
 * @param onFolderClick Callback invocado al pulsar una subcarpeta.
 * @param onPackClick Callback invocado al pulsar un pack.
 * @param onEditFolderClick Callback invocado al editar una subcarpeta.
 * @param onDeleteFolderClick Callback invocado al pedir el borrado de una subcarpeta.
 * @param onEditPackClick Callback invocado al editar un pack.
 * @param onDeletePackClick Callback invocado al pedir el borrado de un pack.
 * @param onDialogDismiss Callback invocado al cerrar el diálogo activo.
 * @param onCreateFolderConfirm Callback invocado al confirmar la creación de carpeta.
 * @param onCreatePackConfirm Callback invocado al confirmar la creación de pack.
 * @param onEditFolderConfirm Callback invocado al confirmar la edición de carpeta.
 * @param onDeleteFolderConfirm Callback invocado al confirmar el borrado de carpeta.
 * @param onEditPackConfirm Callback invocado al confirmar la edición de pack.
 * @param onDeletePackConfirm Callback invocado al confirmar el borrado de pack.
 * @param onDeleteCurrentFolderConfirm Callback invocado al confirmar el borrado de la carpeta actual.
 */
@Composable
private fun FolderScreen(
    uiState: FolderUiState,
    actions: List<UFabActionItem>,
    onFolderClick: (Folder) -> Unit,
    onPackClick: (Pack) -> Unit,
    onEditFolderClick: (Folder) -> Unit,
    onDeleteFolderClick: (Folder) -> Unit,
    onEditPackClick: (Pack) -> Unit,
    onDeletePackClick: (Pack) -> Unit,
    onDialogDismiss: () -> Unit,
    onCreateFolderConfirm: (String, String, String) -> Unit,
    onCreatePackConfirm: (String, String?, String, String) -> Unit,
    onEditFolderConfirm: (Folder, String, String, String) -> Unit,
    onDeleteFolderConfirm: (Folder) -> Unit,
    onEditPackConfirm: (Pack, String, String?, String, String) -> Unit,
    onDeletePackConfirm: (Pack) -> Unit,
    onDeleteCurrentFolderConfirm: () -> Unit,
) {
    val strings = LocalStrings.current

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isEmpty) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(strings.nothingHereYet, color = Neutral400)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            ) {
                if (uiState.childFolders.isNotEmpty()) {
                    item {
                        SectionHeader(strings.subfoldersSection)
                        Spacer(Modifier.height(8.dp))
                        FolderListCard(
                            folders = uiState.childFolders,
                            onFolderClick = onFolderClick,
                            onEditFolderClick = onEditFolderClick,
                            onDeleteFolderClick = onDeleteFolderClick,
                        )
                    }
                }

                if (uiState.packs.isNotEmpty()) {
                    item {
                        if (uiState.childFolders.isNotEmpty()) {
                            Spacer(Modifier.height(24.dp))
                        }
                        SectionHeader(strings.packsInFolderSection)
                        Spacer(Modifier.height(8.dp))
                    }
                    itemsIndexed(uiState.packs, key = { _, item -> item.pack.id }) { index, item ->
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

        UActionsSheetFab(
            actions = actions,
            modifier = Modifier.fillMaxSize(),
        )
    }

    FolderDialogs(
        dialogState = uiState.dialogState,
        onDialogDismiss = onDialogDismiss,
        onCreateFolderConfirm = onCreateFolderConfirm,
        onCreatePackConfirm = onCreatePackConfirm,
        onEditFolderConfirm = onEditFolderConfirm,
        onDeleteFolderConfirm = onDeleteFolderConfirm,
        onEditPackConfirm = onEditPackConfirm,
        onDeletePackConfirm = onDeletePackConfirm,
        onDeleteCurrentFolderConfirm = onDeleteCurrentFolderConfirm,
    )
}

private fun handleImportSelection(
    uri: Uri?,
    strings: AppStrings,
    onSuccess: (Uri) -> Unit,
    onError: (String) -> Unit,
) {
    if (uri == null) return
    try {
        onSuccess(uri)
    } catch (throwable: Throwable) {
        onError(throwable.toUiMessage(strings.errors))
    }
}

private fun handleExportSelection(
    uri: Uri?,
    strings: AppStrings,
    pendingExport: ExportedUQuizFile?,
    successName: String?,
    writeDocument: (Uri, String) -> Unit,
    onWriteSucceeded: (String) -> Unit,
    onWriteFailed: (Throwable) -> Unit,
    onClearPending: () -> Unit,
) {
    if (pendingExport == null || successName == null) return
    if (uri == null) {
        onClearPending()
        return
    }
    try {
        writeDocument(uri, pendingExport.content)
        onWriteSucceeded(successName)
    } catch (throwable: Throwable) {
        onWriteFailed(RuntimeException(strings.errors.exportWriteErrorMessage))
    } finally {
        onClearPending()
    }
}

@UPreview
@Composable
private fun FolderScreenPreview() {
    UTheme {
        FolderScreen(
            uiState = FolderUiState(
                isLoading = false,
                currentFolder = previewFolder(),
                childFolders = listOf(
                    FolderWithCounts(
                        folder = previewFolder(id = "child-folder", name = "Backend"),
                        subfolderCount = 1,
                        packCount = 3,
                    ),
                ),
                packs = listOf(
                    PackListItemUiModel(
                        pack = previewPack(),
                        questionCount = 18,
                        answeredCount = 7,
                        progress = 0.39f,
                    ),
                ),
                isEmpty = false,
            ),
            actions = emptyList(),
            onFolderClick = {},
            onPackClick = {},
            onEditFolderClick = {},
            onDeleteFolderClick = {},
            onEditPackClick = {},
            onDeletePackClick = {},
            onDialogDismiss = {},
            onCreateFolderConfirm = { _, _, _ -> },
            onCreatePackConfirm = { _, _, _, _ -> },
            onEditFolderConfirm = { _, _, _, _ -> },
            onDeleteFolderConfirm = {},
            onEditPackConfirm = { _, _, _, _, _ -> },
            onDeletePackConfirm = {},
            onDeleteCurrentFolderConfirm = {},
        )
    }
}

private fun previewFolder(
    id: String = "folder-preview",
    name: String = "Mobile",
) = Folder(
    id = id,
    name = name,
    colorHex = "#134C8F",
    icon = "folder",
    createdAt = 0L,
    updatedAt = 0L,
)

private fun previewPack() = Pack(
    id = "pack-preview",
    title = "Jetpack Compose",
    description = "UI declarativa",
    folderId = "folder-preview",
    icon = "file",
    colorHex = "#176B87",
    createdAt = 0L,
    updatedAt = 0L,
)
