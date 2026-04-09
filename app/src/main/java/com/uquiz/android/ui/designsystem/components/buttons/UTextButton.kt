package com.uquiz.android.ui.designsystem.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Red100
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UTextButton
 *
 * Muestra un botón de texto para acciones de baja jerarquía visual.
 *
 * @param text Texto visible del botón.
 * @param onClick Se invoca al pulsar el botón.
 * @param tone Tono visual aplicado al botón.
 * @param size Tamaño visual del botón.
 * @param leadingIconRes Recurso drawable opcional mostrado antes del texto.
 * @param leadingIconTint Tinte opcional para el icono inicial.
 */
@Composable
fun UTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: UButtonTone = UButtonTone.Neutral,
    size: UButtonSize = UButtonSize.Regular,
    @DrawableRes leadingIconRes: Int? = null,
    leadingIconTint: Color? = null,
) {
    val contentColor = uTonalButtonContentColor(tone)
    val contentPadding = when (size) {
        UButtonSize.Regular -> PaddingValues(horizontal = 18.dp, vertical = 6.dp)
        UButtonSize.Compact -> PaddingValues(horizontal = 4.dp, vertical = 2.dp)
    }
    val minHeight = when (size) {
        UButtonSize.Regular -> 40.dp
        UButtonSize.Compact -> 24.dp
    }
    val textStyle = when (size) {
        UButtonSize.Regular -> MaterialTheme.typography.labelLarge
        UButtonSize.Compact -> MaterialTheme.typography.labelMedium
    }
    val iconSize = when (size) {
        UButtonSize.Regular -> 18.dp
        UButtonSize.Compact -> 14.dp
    }
    TextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = minHeight),
        shape = RoundedCornerShape(URadius),
        contentPadding = contentPadding,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            containerColor = if (tone == UButtonTone.Danger) Red100.copy(alpha = 0.5f) else Color.Transparent,
        ),
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            if (leadingIconRes != null) {
                Icon(
                    painter = painterResource(leadingIconRes),
                    contentDescription = null,
                    tint = leadingIconTint ?: contentColor,
                    modifier = Modifier.size(iconSize),
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(text = text, style = textStyle)
        }
    }
}

@UPreview
@Composable
private fun UTextButtonPreview() {
    UTheme {
        UTextButton(text = "Cancelar", onClick = {})
    }
}
