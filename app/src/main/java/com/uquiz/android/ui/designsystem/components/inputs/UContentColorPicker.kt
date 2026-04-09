package com.uquiz.android.ui.designsystem.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.ContentColorOption
import com.uquiz.android.ui.designsystem.tokens.contentSelectionBorder
import com.uquiz.android.ui.designsystem.tokens.contentSelectionSurface
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentColorPalette

/**
 * ### UContentColorPicker
 *
 * Muestra un selector horizontal de colores de contenido.
 *
 * @param options Opciones de color disponibles.
 * @param selectedHex Color actualmente seleccionado en formato hex.
 * @param onSelect Se invoca al seleccionar un color.
 */
@Composable
fun UContentColorPicker(
    options: List<ContentColorOption>,
    selectedHex: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        options.forEach { option ->
            val isSelected = option.hex == selectedHex
            val shape = RoundedCornerShape(URadius)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(shape)
                    .background(
                        color = if (isSelected) contentSelectionSurface(option.color) else Color.White,
                        shape = shape,
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) contentSelectionBorder(option.color) else Neutral100,
                        shape = shape,
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onSelect(option.hex) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(UIcons.Inputs.ColorPicker),
                    contentDescription = null,
                    tint = option.color,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@UPreview
@Composable
private fun UContentColorPickerPreview() {
    UTheme {
        UContentColorPicker(
            options = contentColorPalette,
            selectedHex = contentColorPalette.first().hex,
            onSelect = {},
        )
    }
}
