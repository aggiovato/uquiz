package com.uquiz.android.ui.shared.components.dialogs

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.buttons.UTextButton
import com.uquiz.android.ui.designsystem.components.dialogs.UDialogScaffold
import com.uquiz.android.ui.designsystem.components.inputs.UTextField
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### SafeDeleteEntityDialog
 *
 * Muestra un diálogo destructivo con confirmación por palabra clave.
 *
 * @param title Título mostrado en la cabecera del diálogo.
 * @param primaryMessage Mensaje principal de confirmación.
 * @param secondaryMessage Mensaje secundario de apoyo.
 * @param requiredKeyword Palabra exacta necesaria para habilitar el borrado.
 * @param keywordInstruction Instrucción mostrada al usuario.
 * @param headerIconRes Recurso drawable del icono de cabecera.
 * @param onDismiss Se invoca al cerrar el diálogo.
 * @param onConfirm Se invoca al confirmar la acción destructiva.
 */
@Composable
fun SafeDeleteEntityDialog(
    title: String,
    primaryMessage: String,
    secondaryMessage: String,
    requiredKeyword: String,
    keywordInstruction: String,
    @DrawableRes headerIconRes: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val strings = LocalStrings.current
    var keywordInput by remember { mutableStateOf("") }
    val canDelete = keywordInput.trim() == requiredKeyword

    UDialogScaffold(
        title = title,
        onDismiss = onDismiss,
        headerColor = Red700,
        headerIconRes = headerIconRes,
        decorativeTint = Red700,
        actions = {
            UTextButton(
                text = strings.cancel,
                onClick = onDismiss,
            )
            Spacer(Modifier.width(12.dp))
            UFilledButton(
                text = strings.delete,
                onClick = onConfirm,
                tone = UButtonTone.Danger,
                enabled = canDelete,
            )
        },
    ) {
        Text(
            text = primaryMessage,
            style = MaterialTheme.typography.titleSmall,
            color = Ink950,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = secondaryMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = Neutral500,
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = keywordInstruction,
            style = MaterialTheme.typography.bodySmall,
            color = Neutral500,
        )
        Spacer(Modifier.height(14.dp))
        UTextField(
            value = keywordInput,
            onValueChange = { },
            placeholder = strings.deleteKeywordHint,
        )
    }
}

@UPreview
@Composable
private fun SafeDeleteEntityDialogPreview() {
    UTheme {
        SafeDeleteEntityDialog(
            title = "Eliminar entity preview",
            primaryMessage =
                "Este es el mensaje principal que se muestra" +
                    " puede ser una pregunta '?'",
            secondaryMessage =
                "Y este es el mensaje secundario con texto" +
                    " más pequeño para dar más información al respecto de la" +
                    " eliminación.",
            requiredKeyword = "preview",
            keywordInstruction = "Escribe 'preview' para eliminar",
            headerIconRes = UIcons.Actions.Delete,
            onDismiss = {},
            onConfirm = {},
        )
    }
}
