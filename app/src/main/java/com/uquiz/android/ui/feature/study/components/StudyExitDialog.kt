package com.uquiz.android.ui.feature.study.components

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
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### StudyExitDialog
 *
 * Diálogo de confirmación para salir de la sesión de estudio en curso.
 *
 * @param onDismiss Callback invocado al cancelar la salida y continuar estudiando.
 * @param onExit Callback invocado al confirmar la salida de la sesión.
 */
@Composable
fun StudyExitDialog(
    onDismiss: () -> Unit,
    onExit: () -> Unit,
) {
    val strings = LocalStrings.current
    UDialogScaffold(
        title = strings.studyExitTitle,
        onDismiss = onDismiss,
        headerColor = BrandNavy,
        headerIconRes = UIcons.Actions.Leave,
        actions = {
            Column(
                modifier =
                    Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                UFilledButton(
                    text = strings.studyExitStudy,
                    onClick = onExit,
                    tone = UButtonTone.Brand,
                    leadingIconRes = UIcons.Actions.Leave,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.padding(start = 12.dp))

                UOutlinedButton(
                    text = strings.studyContinueStudy,
                    onClick = onDismiss,
                    tone = UButtonTone.Neutral,
                    leadingIconRes = UIcons.Select.Book,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) {
        Text(
            text = strings.studyExitMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = Ink950,
        )
    }
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
