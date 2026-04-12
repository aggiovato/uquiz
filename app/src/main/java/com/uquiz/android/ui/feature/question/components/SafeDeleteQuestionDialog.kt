package com.uquiz.android.ui.feature.question.components

import androidx.compose.runtime.Composable
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.dialogs.SafeDeleteEntityDialog

/**
 * ### SafeDeleteQuestionDialog
 *
 * Diálogo de confirmación para eliminar una pregunta de forma segura.
 * Requiere que el usuario escriba la palabra clave para habilitar la acción.
 *
 * @param onDismiss Se invoca al cerrar el diálogo sin confirmar.
 * @param onConfirm Se invoca al confirmar la eliminación.
 */
@Composable
fun SafeDeleteQuestionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val strings = LocalStrings.current

    SafeDeleteEntityDialog(
        title = strings.question.deleteQuestionTitle,
        primaryMessage = strings.question.deleteQuestionPrimaryMessage,
        secondaryMessage = strings.question.deleteQuestionSecondaryMessage,
        requiredKeyword = strings.question.deleteQuestionKeyword,
        keywordInstruction = strings.question.deleteQuestionTypeKeywordInstruction(strings.question.deleteQuestionKeyword),
        headerIconRes = UIcons.Actions.Delete,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
    )
}

@UPreview
@Composable
private fun SafeDeleteQuestionDialogPreview() {
    UTheme {
        SafeDeleteQuestionDialog(
            onDismiss = {},
            onConfirm = {},
        )
    }
}
