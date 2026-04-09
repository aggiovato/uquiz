package com.uquiz.android.ui.feature.question.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.chips.UToggleChip
import com.uquiz.android.ui.designsystem.components.inputs.UTextArea
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### MarkdownEditorField
 *
 * Campo de edición Markdown con encabezado de sección y botón de vista previa.
 * El botón de preview se activa (filled navy) solo cuando el campo tiene contenido.
 *
 * @param title Etiqueta de la sección mostrada en el encabezado.
 * @param value Texto actual del campo.
 * @param placeholder Texto de ayuda cuando el campo está vacío.
 * @param previewLabel Texto del botón de vista previa.
 * @param onValueChange Se invoca al cambiar el contenido del campo.
 * @param onPreviewClick Se invoca al pulsar el botón de vista previa.
 */
@Composable
fun MarkdownEditorField(
    title: String,
    value: String,
    placeholder: String,
    previewLabel: String,
    onValueChange: (String) -> Unit,
    onPreviewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Neutral700,
            )
            UToggleChip(
                iconRes = UIcons.Actions.See,
                label = previewLabel,
                isActive = value.isNotBlank(),
                onClick = onPreviewClick,
            )
        }
        Spacer(Modifier.height(10.dp))
        UTextArea(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            capitalization = KeyboardCapitalization.Sentences,
        )
    }
}

@UPreview
@Composable
private fun MarkdownEditorFieldPreview() {
    UTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            MarkdownEditorField(
                title = "Question",
                value = "",
                placeholder = "Write the question in Markdown",
                previewLabel = "Preview",
                onValueChange = {},
                onPreviewClick = {},
            )
            MarkdownEditorField(
                title = "Question",
                value = "¿Qué organela produce energía?",
                placeholder = "Write the question in Markdown",
                previewLabel = "Preview",
                onValueChange = {},
                onPreviewClick = {},
            )
        }
    }
}
