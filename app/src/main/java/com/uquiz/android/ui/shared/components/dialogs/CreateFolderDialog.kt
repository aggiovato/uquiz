package com.uquiz.android.ui.shared.components.dialogs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.error.CONTENT_NAME_MAX_LENGTH
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.buttons.UTextButton
import com.uquiz.android.ui.designsystem.components.dialogs.UDialogScaffold
import com.uquiz.android.ui.designsystem.components.inputs.UContentColorPicker
import com.uquiz.android.ui.designsystem.components.inputs.UContentIconCarouselPicker
import com.uquiz.android.ui.designsystem.components.inputs.UTextField
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentColorPalette
import com.uquiz.android.ui.designsystem.tokens.folderSelectableIconPalette
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### CreateFolderDialog
 *
 * Muestra el diálogo para crear o editar una carpeta.
 *
 * @param title Título mostrado en la cabecera.
 * @param description Texto descriptivo del diálogo.
 * @param confirmLabel Etiqueta de la acción principal.
 * @param onDismiss Se invoca al cerrar el diálogo.
 * @param onConfirm Se invoca con el nombre, color e icono seleccionados.
 * @param initialName Nombre inicial de la carpeta.
 * @param initialColorHex Color inicial seleccionado.
 * @param initialIcon Icono inicial seleccionado.
 */
@Composable
fun CreateFolderDialog(
    title: String,
    description: String,
    confirmLabel: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, colorHex: String, icon: String) -> Unit,
    initialName: String = "",
    initialColorHex: String = contentColorPalette.first().hex,
    initialIcon: String = folderSelectableIconPalette.first().key,
) {
    val strings = LocalStrings.current
    var name by remember(initialName) { mutableStateOf(initialName) }
    var selectedHex by remember(initialColorHex) { mutableStateOf(initialColorHex) }
    var selectedIcon by remember(initialIcon) { mutableStateOf(initialIcon) }
    val canCreate = name.isNotBlank()

    UDialogScaffold(
        title = title,
        onDismiss = onDismiss,
        headerColor = BrandNavy,
        headerIconRes = UIcons.Content.Folder.Add,
        actions = {
            UTextButton(
                text = strings.cancel,
                onClick = onDismiss,
            )
            Spacer(Modifier.width(12.dp))
            UFilledButton(
                text = confirmLabel,
                onClick = { if (canCreate) onConfirm(name.trim(), selectedHex, selectedIcon) },
                enabled = canCreate,
            )
        },
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Neutral500,
        )
        Spacer(Modifier.height(16.dp))

        UTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = strings.folderNameHint,
            capitalization = KeyboardCapitalization.Words,
            maxLength = CONTENT_NAME_MAX_LENGTH,
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = strings.folderColorLabel,
            style = MaterialTheme.typography.labelMedium,
            color = Neutral500,
        )
        Spacer(Modifier.height(12.dp))
        UContentColorPicker(
            options = contentColorPalette,
            selectedHex = selectedHex,
            onSelect = { selectedHex = it },
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = strings.folderIconLabel,
            style = MaterialTheme.typography.labelMedium,
            color = Neutral500,
        )
        Spacer(Modifier.height(12.dp))

        val selectedColor = contentColorPalette.firstOrNull { it.hex == selectedHex }?.color ?: BrandNavy
        UContentIconCarouselPicker(
            options = folderSelectableIconPalette,
            selectedKey = selectedIcon,
            tintColor = selectedColor,
            onSelect = { selectedIcon = it },
        )
    }
}

@UPreview
@Composable
private fun CreateFolderDialogPreview() {
    UTheme {
        CreateFolderDialog(
            title = "Crear carpeta preview",
            description =
                "Esta es la descripción sobre lo que hace este dialog" +
                    " de confirmación. Informa sobre como crear la carpeta",
            confirmLabel = "Crear carpeta",
            initialName = "",
            initialColorHex = contentColorPalette.first().hex,
            initialIcon = folderSelectableIconPalette.first().key,
            onConfirm = { _, _, _ -> },
            onDismiss = {},
        )
    }
}
