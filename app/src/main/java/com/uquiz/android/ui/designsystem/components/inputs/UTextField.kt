package com.uquiz.android.ui.designsystem.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UTextField
 *
 * Muestra un campo de texto de una sola línea con el estilo compartido de la app.
 *
 * @param value Texto actual del campo.
 * @param onValueChange Se invoca al editar el contenido.
 * @param label Etiqueta opcional mostrada encima del campo.
 * @param placeholder Texto mostrado cuando el campo está vacío.
 * @param singleLine Indica si el campo debe limitarse a una sola línea.
 * @param capitalization Preferencia de capitalización para el teclado.
 * @param maxLength Límite máximo de caracteres permitido.
 */
@Composable
fun UTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    maxLength: Int? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Neutral500,
            )
            androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = { new -> if (maxLength == null || new.length <= maxLength) onValueChange(new) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Ink950),
            cursorBrush = SolidColor(Navy500),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(capitalization = capitalization),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(URadius))
                        .border(1.dp, Neutral300, RoundedCornerShape(URadius))
                        .padding(horizontal = 14.dp, vertical = 13.dp),
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
            androidx.compose.foundation.layout.Spacer(Modifier.height(4.dp))
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
private fun UTextFieldPreview() {
    UTheme {
        UTextField(
            value = "",
            onValueChange = {},
            label = "Campo",
            placeholder = "Escribe aquí...",
        )
    }
}
