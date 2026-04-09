package com.uquiz.android.ui.designsystem.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral300
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UTextArea
 *
 * Muestra un área de texto multilínea con el estilo compartido de la app.
 *
 * @param value Texto actual del área.
 * @param onValueChange Se invoca al editar el contenido.
 * @param placeholder Texto mostrado cuando el área está vacía.
 * @param minHeight Altura mínima antes de crecer con el contenido.
 * @param capitalization Preferencia de capitalización para el teclado.
 * @param maxLength Límite máximo de caracteres permitido.
 */
@Composable
fun UTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: Int = 112,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    maxLength: Int? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = { new -> if (maxLength == null || new.length <= maxLength) onValueChange(new) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Ink950),
            cursorBrush = SolidColor(Navy500),
            keyboardOptions = KeyboardOptions(capitalization = capitalization),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight.dp)
                        .background(Color.White, RoundedCornerShape(URadius))
                        .border(1.dp, Neutral300, RoundedCornerShape(URadius))
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Neutral400,
                        )
                    }
                    innerTextField()
                }
            },
        )

        if (maxLength != null) {
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "${value.length}/$maxLength",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (value.length >= maxLength) Red700 else Neutral400,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun UTextAreaPreview() {
    UTheme {
        UTextArea(
            value = "",
            onValueChange = {},
            placeholder = "Escribe aquí...",
        )
    }
}
