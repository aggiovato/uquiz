package com.uquiz.android.ui.feature.preferences.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.buttons.UOutlinedButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.shared.components.profile.UserAvatar

/**
 * ### AvatarEditor
 *
 * Permite elegir o eliminar la foto de perfil del usuario dentro de preferencias.
 *
 * Muestra el avatar actual (foto o icono genérico de usuario) a la izquierda y,
 * a la derecha, los botones de acción en columna. Al pulsar el avatar con foto
 * activa se abre un diálogo de previsualización en tamaño mayor. Sin foto, pulsar
 * el avatar equivale a pulsar "Elegir foto".
 *
 * @param avatarImageUri Uri de la foto activa, o `null` si no hay foto seleccionada.
 * @param onChoosePhotoClick Acción para abrir el selector de imagen del sistema.
 * @param onDeletePhotoClick Acción para eliminar la foto y volver al icono de usuario por defecto.
 */
@Composable
fun AvatarEditor(
    avatarImageUri: String?,
    onChoosePhotoClick: () -> Unit,
    onDeletePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val hasPhoto = !avatarImageUri.isNullOrBlank()
    var showPreviewDialog by remember { mutableStateOf(false) }

    if (showPreviewDialog && hasPhoto) {
        AvatarPreviewDialog(
            avatarImageUri = avatarImageUri,
            onDismiss = { showPreviewDialog = false },
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar pulsable: previsualiza si tiene foto, abre selector si no.
        UserAvatar(
            avatarIcon = null,
            avatarImageUri = avatarImageUri,
            size = 150.dp,
            modifier =
                Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    if (hasPhoto) showPreviewDialog = true else onChoosePhotoClick()
                },
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            UFilledButton(
                text = strings.preferences.avatarChoosePhoto,
                onClick = onChoosePhotoClick,
                modifier = Modifier.width(172.dp),
                size = UButtonSize.Compact,
                tone = UButtonTone.Brand,
                leadingIconRes = UIcons.Actions.Image,
            )
            UOutlinedButton(
                text = strings.preferences.avatarDeletePhoto,
                onClick = onDeletePhotoClick,
                modifier = Modifier.width(172.dp),
                size = UButtonSize.Compact,
                tone = if(hasPhoto) UButtonTone.Danger else UButtonTone.Neutral,
                enabled = hasPhoto,
                leadingIconRes = UIcons.Actions.Delete,
            )
        }
    }
}

@Composable
private fun AvatarPreviewDialog(
    avatarImageUri: String,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        AsyncImage(
            model = avatarImageUri,
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss,
                    ),
            contentScale = ContentScale.Crop,
        )
    }
}

@UPreview
@Composable
private fun AvatarEditorNoPhotoPreview() {
    UTheme {
        AvatarEditor(
            avatarImageUri = null,
            onChoosePhotoClick = {},
            onDeletePhotoClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@UPreview
@Composable
private fun AvatarEditorWithPhotoPreview() {
    UTheme {
        AvatarEditor(
            avatarImageUri = "https://example.com/avatar.jpg",
            onChoosePhotoClick = {},
            onDeletePhotoClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
