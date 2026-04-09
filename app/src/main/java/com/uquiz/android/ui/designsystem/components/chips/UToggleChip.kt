package com.uquiz.android.ui.designsystem.components.chips

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral200
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Neutral50
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

private val chipShape = RoundedCornerShape(20.dp)
private val colorSpec = tween<Color>(durationMillis = 180)

/**
 * ### UToggleChip
 *
 * Muestra un chip seleccionable con icono y texto.
 *
 * @param iconRes Recurso drawable del icono. Si es `null`, no se muestra icono.
 * @param label Texto visible del chip.
 * @param isActive Indica si el chip está activo.
 * @param onClick Se invoca al pulsar el chip.
 */
@Composable
fun UToggleChip(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isActive) Navy500 else Neutral50,
        animationSpec = colorSpec,
        label = "chipBg",
    )
    val contentColor by animateColorAsState(
        targetValue = if (isActive) Color.White else Neutral500,
        animationSpec = colorSpec,
        label = "chipContent",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isActive) Navy500 else Neutral200,
        animationSpec = colorSpec,
        label = "chipBorder",
    )

    Row(
        modifier = modifier
            .clip(chipShape)
            .drawBehind { drawRect(bgColor) }
            .border(width = 1.dp, color = borderColor, shape = chipShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

@UPreview
@Composable
private fun UToggleChipPreview() {
    UTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            UToggleChip(
                iconRes = UIcons.Select.Book,
                label = "Activo",
                isActive = true,
                onClick = {},
            )
            UToggleChip(
                iconRes = UIcons.Select.Book,
                label = "Inactivo",
                isActive = false,
                onClick = {},
            )
            UToggleChip(
                label = "Sin icono",
                isActive = true,
                onClick = {},
            )
        }
    }
}
