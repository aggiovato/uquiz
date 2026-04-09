package com.uquiz.android.ui.designsystem.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### UDarkIconButton
 *
 * Botón de icono circular para fondos oscuros.
 *
 * Muestra un círculo semitransparente blanco con un icono blanco encima.
 * Usado en cabeceras de pantallas con fondo oscuro, como el modo estudio.
 *
 * @param iconRes Recurso drawable del icono.
 * @param contentDescription Descripción accesible del icono.
 * @param onClick Se invoca al pulsar el botón.
 * @param enabled Indica si la acción está disponible.
 */
@Composable
fun UDarkIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .background(
                color = Color.White.copy(alpha = if (enabled) 0.15f else 0.07f),
                shape = CircleShape,
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = Color.White.copy(alpha = if (enabled) 1f else 0.35f),
        )
    }
}

@UPreview
@Composable
private fun UDarkIconButtonPreview() {
    UTheme {
        Box(
            modifier = Modifier
                .background(BrandNavy)
                .padding(16.dp),
        ) {
            UDarkIconButton(
                iconRes = UIcons.Actions.Leave,
                contentDescription = "Salir",
                onClick = {},
            )
        }
    }
}
