package com.uquiz.android.ui.feature.pack.components

import androidx.compose.runtime.Composable
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentColorPalette
import com.uquiz.android.ui.designsystem.tokens.packSelectableIconPalette
import com.uquiz.android.ui.feature.pack.model.PackDialogState
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.dialogs.CreatePackDialog
import com.uquiz.android.ui.shared.components.dialogs.SafeDeleteEntityDialog

/**
 * ### PackDialogs
 *
 * Renderiza el diálogo activo asociado al detalle de un pack.
 *
 * @param dialogState Estado de diálogo actual.
 * @param onDialogDismiss Acción para cerrar el diálogo activo.
 * @param onEditPackConfirm Acción al confirmar la edición del pack.
 * @param onDeletePackConfirm Acción al confirmar el borrado del pack.
 */
@Composable
fun PackDialogs(
    dialogState: PackDialogState,
    onDialogDismiss: () -> Unit,
    onEditPackConfirm: (String, String?, String, String) -> Unit,
    onDeletePackConfirm: () -> Unit,
) {
    val strings = LocalStrings.current

    when (val dialog = dialogState) {
        PackDialogState.None -> Unit
        is PackDialogState.EditPack -> {
            CreatePackDialog(
                title = strings.common.editPack,
                description = strings.common.editPackDescription,
                confirmLabel = strings.common.save,
                onDismiss = onDialogDismiss,
                onConfirm = onEditPackConfirm,
                initialTitle = dialog.pack.title,
                initialDescription = dialog.pack.description.orEmpty(),
                initialColorHex = dialog.pack.colorHex ?: contentColorPalette.first().hex,
                initialIcon = dialog.pack.icon ?: packSelectableIconPalette.first().key,
            )
        }

        is PackDialogState.DeletePack -> {
            SafeDeleteEntityDialog(
                title = strings.common.deletePack,
                primaryMessage = strings.common.deletePackPrimaryMessage,
                secondaryMessage = strings.common.deletePackSecondaryMessage,
                requiredKeyword = strings.common.deletePackKeyword,
                keywordInstruction = strings.common.deletePackTypeKeywordInstruction(strings.common.deletePackKeyword),
                headerIconRes = UIcons.Actions.Delete,
                onDismiss = onDialogDismiss,
                onConfirm = onDeletePackConfirm,
            )
        }
    }
}

@UPreview
@Composable
private fun PackDialogsPreview() {
    UTheme {
        PackDialogs(
            dialogState = PackDialogState.EditPack(
                Pack(
                    id = "pack-1",
                    title = "Geografía europea",
                    description = "Repaso de países y capitales",
                    folderId = "folder-1",
                    createdAt = 0L,
                    updatedAt = 0L,
                ),
            ),
            onDialogDismiss = {},
            onEditPackConfirm = { _, _, _, _ -> },
            onDeletePackConfirm = {},
        )
    }
}
