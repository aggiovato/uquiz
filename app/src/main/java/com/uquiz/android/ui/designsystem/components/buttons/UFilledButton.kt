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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.Teal900
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UFilledButton
 *
 * Muestra un botón relleno para acciones principales.
 *
 * @param text Texto visible del botón.
 * @param onClick Se invoca al pulsar el botón.
 * @param tone Tono visual aplicado al botón.
 * @param size Tamaño visual del botón.
 * @param enabled Indica si la acción está disponible.
 * @param leadingIconRes Recurso drawable opcional mostrado antes del texto.
 * @param leadingIconTint Tinte opcional para el icono inicial.
 */
@Composable
fun UFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: UButtonTone = UButtonTone.Brand,
    size: UButtonSize = UButtonSize.Regular,
    enabled: Boolean = true,
    @DrawableRes leadingIconRes: Int? = null,
    leadingIconTint: Color? = null,
) {
    val (containerColor, contentColor) = when (tone) {
        UButtonTone.Brand -> BrandNavy to Color.White
        UButtonTone.Secondary -> Teal900 to Color.White
        UButtonTone.Danger -> Red700 to Color.White
        UButtonTone.Neutral -> Neutral700 to Color.White
    }
    val contentPadding =
        when (size) {
            UButtonSize.Regular -> PaddingValues(horizontal = 30.dp, vertical = 2.dp)
            UButtonSize.Compact -> PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        }
    val minHeight =
        when (size) {
            UButtonSize.Regular -> 48.dp
            UButtonSize.Compact -> 38.dp
        }
    val iconSize =
        when (size) {
            UButtonSize.Regular -> 18.dp
            UButtonSize.Compact -> 16.dp
        }
    val textStyle =
        when (size) {
            UButtonSize.Regular -> MaterialTheme.typography.labelLarge
            UButtonSize.Compact -> MaterialTheme.typography.labelMedium
        }
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.defaultMinSize(minHeight = minHeight),
        shape = RoundedCornerShape(URadius),
        contentPadding = contentPadding,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = Neutral100,
                disabledContentColor = Neutral400,
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
private fun UFilledButtonPreview() {
    UTheme {
        UFilledButton(text = "Acción principal", onClick = {})
    }
}
