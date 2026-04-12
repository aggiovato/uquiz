package com.uquiz.android.ui.shared.components.studygame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.buttons.UOutlinedButton
import com.uquiz.android.ui.designsystem.components.dialogs.UDialogScaffold
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### SessionExitDialog
 *
 * Diálogo de confirmación genérico para abandonar una sesión activa (estudio o juego).
 *
 * @param title       Título del diálogo.
 * @param message     Cuerpo del diálogo con la advertencia de pérdida de progreso.
 * @param confirmText Texto del botón de confirmación (salir de la sesión).
 * @param dismissText Texto del botón de cancelación (continuar la sesión).
 * @param onDismiss   Callback invocado al cancelar y continuar la sesión.
 * @param onConfirm   Callback invocado al confirmar la salida.
 */
@Composable
fun SessionExitDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    UDialogScaffold(
        title = title,
        onDismiss = onDismiss,
        headerColor = BrandNavy,
        headerIconRes = UIcons.Actions.Leave,
        showDecorativeBackground = false,
        actions = {
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                UFilledButton(
                    text = confirmText,
                    onClick = onConfirm,
                    tone = UButtonTone.Brand,
                    leadingIconRes = UIcons.Actions.Leave,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.padding(start = 12.dp))

                UOutlinedButton(
                    text = dismissText,
                    onClick = onDismiss,
                    tone = UButtonTone.Neutral,
                    leadingIconRes = UIcons.Select.Book,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Ink950,
        )
    }
}

@UPreview
@Composable
private fun SessionExitDialogPreview() {
    UTheme {
        SessionExitDialog(
            title = "¿Salir de la sesión?",
            message = "Si sales ahora, el progreso de esta sesión se perderá.",
            confirmText = "Salir",
            dismissText = "Continuar",
            onDismiss = {},
            onConfirm = {},
        )
    }
}
