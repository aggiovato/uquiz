package com.uquiz.android.ui.shared.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Navy700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentIconResForKey

/**
 * ### UserAvatar
 *
 * Muestra el avatar circular del usuario usando una imagen personalizada o un icono.
 *
 * Renderiza un fondo navy con borde y sombra sutil para mantener contraste tanto en
 * contextos de saludo como en editores de perfil.
 *
 * @param avatarIcon Clave persistida del icono del avatar cuando no hay imagen.
 * @param avatarImageUri Uri persistida de la imagen elegida por el usuario.
 * @param size Tamaño cuadrado aplicado al avatar.
 */
@Composable
fun UserAvatar(
    avatarIcon: String?,
    avatarImageUri: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
) {
    val hasCustomIcon = !avatarIcon.isNullOrBlank()
    val iconRes = if (hasCustomIcon) contentIconResForKey(avatarIcon) else UIcons.Cards.User
    val iconTint = if (hasCustomIcon) Color.White else Color.White.copy(alpha = 0.60f)
    val iconSize = (
        if (hasCustomIcon) {
            ((size / 2f) - 2.dp)
        } else {
            (size / 1.7f)
        }
    )

    Box(
        modifier =
            modifier
                .shadow(elevation = 4.dp, shape = CircleShape, clip = false)
                .size(size)
                .background(Navy500, CircleShape)
                .border(1.5.dp, Navy700, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (!avatarImageUri.isNullOrBlank()) {
            AsyncImage(
                model = avatarImageUri,
                contentDescription = null,
                modifier =
                    Modifier
                        .matchParentSize()
                        .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@UPreview
@Composable
private fun UserAvatarPreview() {
    UTheme {
        UserAvatar(
            avatarIcon = null,
            avatarImageUri = null,
            size = 56.dp,
        )
    }
}
