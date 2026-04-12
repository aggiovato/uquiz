package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.dialogs.UDialogScaffold
import com.uquiz.android.ui.designsystem.components.feedback.UMarkdownText
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Gold400
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### StudyExplanationDialog
 *
 * Diálogo que muestra la explicación de la pregunta actual en formato Markdown.
 *
 * @param markdown Contenido de la explicación en formato Markdown.
 * @param onDismiss Callback invocado al cerrar el diálogo.
 */
@Composable
fun StudyExplanationDialog(
    markdown: String,
    onDismiss: () -> Unit,
) {
    val strings = LocalStrings.current
    UDialogScaffold(
        title = strings.studySession.studyExplanationLabel,
        onDismiss = onDismiss,
        headerColor = BrandNavy,
        headerIconRes = UIcons.Select.Idea,
        headerIconColor = Gold400,
        showDecorativeBackground = false,
        actions = {
            Column(
                modifier =
                    Modifier
                        .padding(top = 25.dp)
                        .fillMaxWidth(),
            ) {
                UFilledButton(
                    text = strings.common.cancel,
                    onClick = onDismiss,
                    tone = UButtonTone.Brand,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) {
        UMarkdownText(
            markdown = markdown,
            style = MaterialTheme.typography.bodyMedium,
            color = Ink950,
        )
    }
}

@UPreview
@Composable
private fun StudyExplanationDialogPreview() {
    UTheme {
        StudyExplanationDialog(
            markdown =
                "Este es un ejemplo de *texto* en ***markdown***" +
                    " para verificar como este se renderiza" +
                    "\n Además se puede hacer varias cosas como mostrar `inline code`." +
                    "\n\n\n y varios saltos de línea",
            onDismiss = {},
        )
    }
}
