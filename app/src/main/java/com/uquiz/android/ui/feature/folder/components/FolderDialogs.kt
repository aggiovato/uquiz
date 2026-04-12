package com.uquiz.android.ui.feature.folder.components

import androidx.compose.runtime.Composable
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentColorPalette
import com.uquiz.android.ui.designsystem.tokens.folderSelectableIconPalette
import com.uquiz.android.ui.designsystem.tokens.packSelectableIconPalette
import com.uquiz.android.ui.feature.folder.model.FolderDialogState
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.dialogs.CreateFolderDialog
import com.uquiz.android.ui.shared.components.dialogs.CreatePackDialog
import com.uquiz.android.ui.shared.components.dialogs.SafeDeleteEntityDialog

/**
 * ### FolderDialogs
 *
 * Renderiza el modal activo del feature de carpeta según [dialogState].
 *
 * Este componente centraliza los flujos de creación, edición y borrado de
 * carpetas y packs para mantener la screen principal más ligera.
 *
 * @param dialogState Estado de diálogo actualmente activo.
 * @param onDialogDismiss Callback invocado al cerrar el diálogo visible.
 * @param onCreateFolderConfirm Callback invocado al confirmar la creación de carpeta.
 * @param onCreatePackConfirm Callback invocado al confirmar la creación de pack.
 * @param onEditFolderConfirm Callback invocado al confirmar la edición de carpeta.
 * @param onDeleteFolderConfirm Callback invocado al confirmar el borrado de carpeta.
 * @param onEditPackConfirm Callback invocado al confirmar la edición de pack.
 * @param onDeletePackConfirm Callback invocado al confirmar el borrado de pack.
 * @param onDeleteCurrentFolderConfirm Callback invocado al confirmar el borrado de la carpeta actual.
 */
@Composable
internal fun FolderDialogs(
    dialogState: FolderDialogState,
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

    when (dialogState) {
        FolderDialogState.None -> Unit
        FolderDialogState.CreateFolder -> {
            CreateFolderDialog(
                title = strings.common.newFolder,
                description = strings.common.createFolderDescription,
                confirmLabel = strings.common.create,
                onDismiss = onDialogDismiss,
                onConfirm = onCreateFolderConfirm,
            )
        }

        FolderDialogState.CreatePack -> {
            CreatePackDialog(
                title = strings.common.newPack,
                description = strings.common.createPackDescription,
                confirmLabel = strings.common.create,
                onDismiss = onDialogDismiss,
                onConfirm = onCreatePackConfirm,
            )
        }

        is FolderDialogState.EditFolder -> {
            CreateFolderDialog(
                title = strings.common.editFolder,
                description = strings.common.editFolderDescription,
                confirmLabel = strings.common.save,
                onDismiss = onDialogDismiss,
                onConfirm = { name, colorHex, icon ->
                    onEditFolderConfirm(dialogState.folder, name, colorHex, icon)
                },
                initialName = dialogState.folder.name,
                initialColorHex = dialogState.folder.colorHex ?: contentColorPalette.first().hex,
                initialIcon = dialogState.folder.icon ?: folderSelectableIconPalette.first().key,
            )
        }

        is FolderDialogState.DeleteFolder -> {
            SafeDeleteEntityDialog(
                title = strings.common.deleteFolder,
                primaryMessage = strings.common.deleteFolderPrimaryMessage,
                secondaryMessage = strings.common.deleteFolderSecondaryMessage,
                requiredKeyword = strings.common.deleteFolderKeyword,
                keywordInstruction = strings.common.deleteFolderTypeKeywordInstruction(strings.common.deleteFolderKeyword),
                headerIconRes = UIcons.Content.Folder.Error,
                onDismiss = onDialogDismiss,
                onConfirm = { onDeleteFolderConfirm(dialogState.folder) },
            )
        }

        is FolderDialogState.EditPack -> {
            CreatePackDialog(
                title = strings.common.editPack,
                description = strings.common.editPackDescription,
                confirmLabel = strings.common.save,
                onDismiss = onDialogDismiss,
                onConfirm = { title, description, colorHex, icon ->
                    onEditPackConfirm(dialogState.pack, title, description, colorHex, icon)
                },
                initialTitle = dialogState.pack.title,
                initialDescription = dialogState.pack.description.orEmpty(),
                initialColorHex = dialogState.pack.colorHex ?: contentColorPalette.first().hex,
                initialIcon = dialogState.pack.icon ?: packSelectableIconPalette.first().key,
            )
        }

        is FolderDialogState.DeletePack -> {
            SafeDeleteEntityDialog(
                title = strings.common.deletePack,
                primaryMessage = strings.common.deletePackPrimaryMessage,
                secondaryMessage = strings.common.deletePackSecondaryMessage,
                requiredKeyword = strings.common.deletePackKeyword,
                keywordInstruction = strings.common.deletePackTypeKeywordInstruction(strings.common.deletePackKeyword),
                headerIconRes = UIcons.Content.Pack.Error,
                onDismiss = onDialogDismiss,
                onConfirm = { onDeletePackConfirm(dialogState.pack) },
            )
        }

        FolderDialogState.DeleteCurrentFolder -> {
            SafeDeleteEntityDialog(
                title = strings.common.deleteFolder,
                primaryMessage = strings.common.deleteFolderPrimaryMessage,
                secondaryMessage = strings.common.deleteFolderSecondaryMessage,
                requiredKeyword = strings.common.deleteFolderKeyword,
                keywordInstruction = strings.common.deleteFolderTypeKeywordInstruction(strings.common.deleteFolderKeyword),
                headerIconRes = UIcons.Content.Folder.Error,
                onDismiss = onDialogDismiss,
                onConfirm = onDeleteCurrentFolderConfirm,
            )
        }
    }
}

@UPreview
@Composable
private fun FolderDialogsPreview() {
    UTheme {
        FolderDialogs(
            dialogState = FolderDialogState.CreateFolder,
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
