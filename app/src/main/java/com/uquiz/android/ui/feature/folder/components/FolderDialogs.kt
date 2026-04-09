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

    when (val dialog = dialogState) {
        FolderDialogState.None -> Unit
        FolderDialogState.CreateFolder -> {
            CreateFolderDialog(
                title = strings.newFolder,
                description = strings.createFolderDescription,
                confirmLabel = strings.create,
                onDismiss = onDialogDismiss,
                onConfirm = onCreateFolderConfirm,
            )
        }

        FolderDialogState.CreatePack -> {
            CreatePackDialog(
                title = strings.newPack,
                description = strings.createPackDescription,
                confirmLabel = strings.create,
                onDismiss = onDialogDismiss,
                onConfirm = onCreatePackConfirm,
            )
        }

        is FolderDialogState.EditFolder -> {
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

        is FolderDialogState.DeleteFolder -> {
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

        is FolderDialogState.EditPack -> {
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

        is FolderDialogState.DeletePack -> {
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

        FolderDialogState.DeleteCurrentFolder -> {
            SafeDeleteEntityDialog(
                title = strings.deleteFolder,
                primaryMessage = strings.deleteFolderPrimaryMessage,
                secondaryMessage = strings.deleteFolderSecondaryMessage,
                requiredKeyword = strings.deleteFolderKeyword,
                keywordInstruction = strings.deleteFolderTypeKeywordInstruction(strings.deleteFolderKeyword),
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
