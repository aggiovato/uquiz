package com.uquiz.android.ui.feature.question.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.dialogs.UDialogScaffold
import com.uquiz.android.ui.designsystem.components.feedback.UMarkdownText
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral200
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

data class PreviewOptionUiModel(
    val label: String,
    val markdown: String,
    val isCorrect: Boolean,
)

/**
 * ### PreviewMarkdownDialog
 *
 * Diálogo de vista previa de contenido Markdown con opciones de respuesta.
 * Usa `UDialogScaffold` con cabecera navy y el icono de ojo. El contenido es
 * desplazable si supera la altura máxima.
 *
 * @param title Título mostrado en la cabecera del diálogo.
 * @param markdown Contenido Markdown de la pregunta o explicación.
 * @param options Lista de opciones a mostrar (vacía para el preview de explicación).
 * @param onDismiss Se invoca al cerrar el diálogo.
 */
@Composable
fun PreviewMarkdownDialog(
    title: String,
    markdown: String,
    options: List<PreviewOptionUiModel> = emptyList(),
    onDismiss: () -> Unit,
) {
    val strings = LocalStrings.current

    UDialogScaffold(
        title = title,
        onDismiss = onDismiss,
        headerColor = BrandNavy,
        headerIconRes = UIcons.Actions.See,
        decorativeTint = BrandNavy,
        showDecorativeBackground = false,
        modifier = Modifier.heightIn(max = 560.dp),
        actions = {
            UFilledButton(
                text = strings.common.cancel,
                size = UButtonSize.Compact,
                onClick = onDismiss,
            )
        },
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UMarkdownText(
                markdown = markdown,
                style = MaterialTheme.typography.bodyLarge,
                color = Ink950,
            )

            options.filter { it.markdown.isNotBlank() }.forEach { option ->
                val isCorrect = option.isCorrect
                val bg = if (isCorrect) Teal700 else Color.White
                val border = if (isCorrect) Teal700 else Neutral200
                val labelColor = if (isCorrect) Color.White else BrandNavy
                val textColor = if (isCorrect) Color.White else Ink950

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(bg, RoundedCornerShape(AppRadius))
                            .border(1.dp, border, RoundedCornerShape(AppRadius))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = labelColor,
                    )
                    UMarkdownText(
                        markdown = option.markdown,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                    )
                }
            }
        }
    }
}

@UPreview
@Composable
private fun PreviewMarkdownDialogPreview() {
    UTheme {
        PreviewMarkdownDialog(
            title = "Question preview",
            markdown = "¿Qué **organela** celular se encarga de producir energía?",
            options =
                listOf(
                    PreviewOptionUiModel("A.", "Núcleo", false),
                    PreviewOptionUiModel("B.", "Mitocondria", true),
                    PreviewOptionUiModel("C.", "Ribosoma", false),
                    PreviewOptionUiModel("D.", "Lisosoma", false),
                ),
            onDismiss = {},
        )
    }
}
