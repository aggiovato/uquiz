package com.uquiz.android.ui.feature.home.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Navy700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.contentIconResForKey

/**
 * ## HomeUserAvatar
 * Circular avatar displayed in the home screen greeting row.
 *
 * Renders a navy circle with a darker border and a subtle shadow. When the user
 * has selected an avatar icon it is shown in full white; otherwise the generic
 * user icon is shown at reduced opacity so it reads as a silhouette.
 *
 * @param avatarIcon Persisted icon key from [contentIconResForKey], or null for the default.
 * @param modifier Modifier applied to the root [Box].
 */
@Composable
fun HomeUserAvatar(
    avatarIcon: String?,
    avatarImageUri: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp
) {
    val hasCustomIcon = !avatarIcon.isNullOrBlank()
    val iconRes = if (hasCustomIcon) contentIconResForKey(avatarIcon) else UIcons.Cards.User
    val iconTint = if (hasCustomIcon) Color.White else Color.White.copy(alpha = 0.60f)
    val iconSize = if (hasCustomIcon) 20.dp else 22.dp

    Box(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = CircleShape, clip = false)
            .size(size)
            .background(Navy500, CircleShape)
            .border(1.5.dp, Navy700, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!avatarImageUri.isNullOrBlank()) {
            AsyncImage(
                model = avatarImageUri,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
