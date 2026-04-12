package com.uquiz.android.ui.feature.study.components

import androidx.compose.runtime.Composable
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.studygame.SessionExitDialog

/**
 * ### StudyExitDialog
 *
 * Diálogo de confirmación para salir de la sesión de estudio en curso.
 * Delegado en [SessionExitDialog] con strings específicos del modo estudio.
 *
 * @param onDismiss Callback invocado al cancelar la salida y continuar estudiando.
 * @param onExit    Callback invocado al confirmar la salida de la sesión.
 */
@Composable
fun StudyExitDialog(
    onDismiss: () -> Unit,
    onExit: () -> Unit,
) {
    val strings = LocalStrings.current
    SessionExitDialog(
        title = strings.studySession.studyExitTitle,
        message = strings.studySession.studyExitMessage,
        confirmText = strings.studySession.studyExitStudy,
        dismissText = strings.common.studyContinueStudy,
        onDismiss = onDismiss,
        onConfirm = onExit,
    )
}

@UPreview
@Composable
private fun StudyExitDialogPreview() {
    UTheme {
        StudyExitDialog(
            onDismiss = {},
            onExit = {},
        )
    }
}
