package com.uquiz.android.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.profile.UserAvatar

/**
 * ### HomeGreetingCard
 *
 * Cabecera de saludo de la pantalla Home con acceso rápido al perfil.
 *
 * @param displayName Nombre visible del usuario actual.
 * @param avatarIcon Clave del icono persistido para el avatar.
 * @param avatarImageUri Uri de la imagen personalizada del avatar.
 * @param onProfileClick Acción al pulsar el avatar.
 */
@Composable
fun HomeGreetingCard(
    displayName: String,
    avatarIcon: String?,
    avatarImageUri: String?,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Neutral100, RoundedCornerShape(URadius))
            .background(Color.White, RoundedCornerShape(URadius))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = strings.home.homeGreeting(displayName),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BrandNavy,
            )
            Text(
                text = strings.home.homeReadyPrompt,
                style = MaterialTheme.typography.bodySmall,
                color = Neutral400,
            )
        }
        UserAvatar(
            avatarIcon = avatarIcon,
            avatarImageUri = avatarImageUri,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onProfileClick,
                ),
        )
    }
}

@UPreview
@Composable
private fun HomeGreetingCardPreview() {
    UTheme {
        HomeGreetingCard(
            displayName = "Ada",
            avatarIcon = null,
            avatarImageUri = null,
            onProfileClick = {},
        )
    }
}
