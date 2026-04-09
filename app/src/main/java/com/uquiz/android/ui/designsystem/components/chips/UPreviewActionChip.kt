package com.uquiz.android.ui.designsystem.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.uTonalButtonContainerColor
import com.uquiz.android.ui.designsystem.components.buttons.uTonalButtonContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UPreviewActionChip
 *
 * Muestra un chip tonal para acciones secundarias en línea.
 *
 * @param icon Icono mostrado antes del texto.
 * @param text Texto visible del chip.
 * @param onClick Se invoca al pulsar el chip.
 * @param enabled Indica si la acción está disponible.
 * @param tone Tono visual aplicado al chip.
 */
@Composable
fun UPreviewActionChip(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tone: UButtonTone = UButtonTone.Brand,
) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = modifier
            .shadow(elevation = if (enabled) 6.dp else 0.dp, shape = shape, clip = false)
            .clip(shape)
            .background(uTonalButtonContainerColor(tone), shape)
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = uTonalButtonContentColor(tone),
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = uTonalButtonContentColor(tone),
        )
    }
}

@UPreview
@Composable
private fun UPreviewActionChipPreview() {
    UTheme {
        UPreviewActionChip(
            icon = Icons.Default.Add,
            text = "Acción",
            onClick = {},
        )
    }
}
