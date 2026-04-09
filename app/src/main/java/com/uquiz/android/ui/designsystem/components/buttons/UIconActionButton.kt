package com.uquiz.android.ui.designsystem.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### UIconActionButton
 *
 * Muestra un botón tonal con icono vectorial.
 *
 * @param icon Icono mostrado dentro del botón.
 * @param contentDescription Descripción accesible del icono.
 * @param onClick Se invoca al pulsar el botón.
 * @param tone Tono visual del botón.
 * @param enabled Indica si la acción está disponible.
 * @param elevated Indica si debe renderizar elevación.
 */
@Composable
fun UIconActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: UButtonTone = UButtonTone.Neutral,
    enabled: Boolean = true,
    elevated: Boolean = false,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier =
            modifier
                .size(28.dp)
                .shadow(elevation = if (enabled && elevated) 4.dp else 0.dp, shape = shape, clip = false)
                .clip(shape)
                .background(if (enabled) uTonalButtonContainerColor(tone) else Neutral100, shape)
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) uTonalButtonContentColor(tone) else Neutral500,
            modifier = Modifier.size(14.dp),
        )
    }
}

/**
 * ### UPlainIconButton
 *
 * Muestra un botón de icono sin fondo tonal.
 *
 * @param iconRes Recurso drawable del icono.
 * @param contentDescription Descripción accesible del icono.
 * @param onClick Se invoca al pulsar el botón.
 * @param tint Color aplicado al icono.
 * @param enabled Indica si la acción está disponible.
 * @param hitSize Tamaño del área táctil.
 * @param iconSize Tamaño visual del icono.
 */
@Composable
fun UPlainIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color,
    enabled: Boolean = true,
    hitSize: Dp = 20.dp,
    iconSize: Dp = 16.dp,
) {
    Box(
        modifier =
            modifier
                .size(hitSize)
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
            tint = if (enabled) tint else Neutral500,
            modifier = Modifier.size(iconSize),
        )
    }
}

/**
 * ### UIconActionButton
 *
 * Muestra un botón tonal con icono basado en drawable.
 *
 * @param iconRes Recurso drawable del icono.
 * @param contentDescription Descripción accesible del icono.
 * @param onClick Se invoca al pulsar el botón.
 * @param tone Tono visual del botón.
 * @param containerColor Color de fondo opcional.
 * @param contentColor Color de contenido opcional.
 * @param enabled Indica si la acción está disponible.
 * @param elevated Indica si debe renderizar elevación.
 */
@Composable
fun UIconActionButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: UButtonTone = UButtonTone.Neutral,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    elevated: Boolean = false,
) {
    val resolvedContainerColor =
        if (containerColor != Color.Unspecified) {
            containerColor
        } else {
            uTonalButtonContainerColor(tone)
        }
    val resolvedContentColor =
        if (contentColor != Color.Unspecified) {
            contentColor
        } else {
            uTonalButtonContentColor(tone)
        }
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier =
            modifier
                .size(28.dp)
                .shadow(elevation = if (enabled && elevated) 4.dp else 0.dp, shape = shape, clip = false)
                .clip(shape)
                .background(if (enabled) resolvedContainerColor else Neutral100, shape)
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
            tint = if (enabled) resolvedContentColor else Neutral500,
            modifier = Modifier.size(14.dp),
        )
    }
}

@UPreview
@Composable
private fun UIconActionButtonPreview() {
    UTheme {
        UIconActionButton(
            iconRes = UIcons.Actions.Edit,
            contentDescription = "Editar",
            onClick = {},
        )
    }
}
