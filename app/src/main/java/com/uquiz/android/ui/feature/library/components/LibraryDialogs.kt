package com.uquiz.android.ui.feature.library.components

import androidx.compose.runtime.Composable
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentColorPalette
import com.uquiz.android.ui.designsystem.tokens.folderSelectableIconPalette
import com.uquiz.android.ui.designsystem.tokens.packSelectableIconPalette
import com.uquiz.android.ui.feature.library.model.LibraryDialogState
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.dialogs.CreateFolderDialog
import com.uquiz.android.ui.shared.components.dialogs.CreatePackDialog
import com.uquiz.android.ui.shared.components.dialogs.SafeDeleteEntityDialog

/**
 * ### LibraryDialogs
 *
 * Renderiza el diálogo activo asociado a la pantalla de biblioteca.
 *
 * @param dialogState Estado de diálogo actualmente visible.
 * @param onDialogDismiss Acción común para cerrar el diálogo activo.
 * @param onCreateFolderConfirm Acción al confirmar la creación de una carpeta.
 * @param onEditFolderConfirm Acción al confirmar la edición de una carpeta.
 * @param onDeleteFolderConfirm Acción al confirmar el borrado de una carpeta.
 * @param onEditPackConfirm Acción al confirmar la edición de un pack.
 * @param onDeletePackConfirm Acción al confirmar el borrado de un pack.
 */
@Composable
fun LibraryDialogs(
    dialogState: LibraryDialogState,
    onDialogDismiss: () -> Unit,
    onCreateFolderConfirm: (String, String, String) -> Unit,
    onEditFolderConfirm: (Folder, String, String, String) -> Unit,
    onDeleteFolderConfirm: (Folder) -> Unit,
    onEditPackConfirm: (Pack, String, String?, String, String) -> Unit,
    onDeletePackConfirm: (Pack) -> Unit,
) {
    val strings = LocalStrings.current

    when (val dialog = dialogState) {
        LibraryDialogState.None -> Unit
        LibraryDialogState.CreateFolder -> {
            CreateFolderDialog(
                title = strings.common.newFolder,
                description = strings.common.createFolderDescription,
                confirmLabel = strings.common.create,
                onDismiss = onDialogDismiss,
                onConfirm = onCreateFolderConfirm,
            )
        }

        is LibraryDialogState.EditFolder -> {
            CreateFolderDialog(
                title = strings.common.editFolder,
                description = strings.common.editFolderDescription,
                confirmLabel = strings.common.save,
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
                title = strings.common.deleteFolder,
                primaryMessage = strings.common.deleteFolderPrimaryMessage,
                secondaryMessage = strings.common.deleteFolderSecondaryMessage,
                requiredKeyword = strings.common.deleteFolderKeyword,
                keywordInstruction = strings.common.deleteFolderTypeKeywordInstruction(strings.common.deleteFolderKeyword),
                headerIconRes = UIcons.Content.Folder.Error,
                onDismiss = onDialogDismiss,
                onConfirm = { onDeleteFolderConfirm(dialog.folder) },
            )
        }

        is LibraryDialogState.EditPack -> {
            CreatePackDialog(
                title = strings.common.editPack,
                description = strings.common.editPackDescription,
                confirmLabel = strings.common.save,
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
                title = strings.common.deletePack,
                primaryMessage = strings.common.deletePackPrimaryMessage,
                secondaryMessage = strings.common.deletePackSecondaryMessage,
                requiredKeyword = strings.common.deletePackKeyword,
                keywordInstruction = strings.common.deletePackTypeKeywordInstruction(strings.common.deletePackKeyword),
                headerIconRes = UIcons.Content.Pack.Error,
                onDismiss = onDialogDismiss,
                onConfirm = { onDeletePackConfirm(dialog.pack) },
            )
        }
    }
}

@UPreview
@Composable
private fun LibraryDialogsPreview() {
    UTheme {
        LibraryDialogs(
            dialogState = LibraryDialogState.CreateFolder,
            onDialogDismiss = {},
            onCreateFolderConfirm = { _, _, _ -> },
            onEditFolderConfirm = { _, _, _, _ -> },
            onDeleteFolderConfirm = {},
            onEditPackConfirm = { _, _, _, _, _ -> },
            onDeletePackConfirm = {},
        )
    }
}
