package com.uquiz.android.ui.feature.preferences.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.buttons.UOutlinedButton
import com.uquiz.android.ui.designsystem.components.inputs.UContentIconCarouselPicker
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.packSelectableIconPalette
import com.uquiz.android.ui.feature.home.components.HomeUserAvatar
import com.uquiz.android.ui.feature.preferences.model.AvatarSource
import com.uquiz.android.ui.i18n.LocalStrings

@Composable
fun AvatarSourceSelector(
    displayName: String,
    avatarSource: AvatarSource,
    avatarIcon: String?,
    avatarImageUri: String?,
    onChoosePhotoClick: () -> Unit,
    onUseIconClick: () -> Unit,
    onIconSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeUserAvatar(
                avatarIcon = avatarIcon,
                avatarImageUri = if (avatarSource == AvatarSource.Image) avatarImageUri else null,
                size = 72.dp,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = displayName.ifBlank { strings.displayNameHint },
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = if (avatarSource == AvatarSource.Image) strings.avatarChoosePhoto else strings.avatarLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            UFilledButton(
                text = strings.avatarChoosePhoto,
                onClick = onChoosePhotoClick,
                modifier = Modifier.weight(1f),
                size = UButtonSize.Compact,
                tone = if (avatarSource == AvatarSource.Image) UButtonTone.Brand else UButtonTone.Secondary,
                leadingIconRes = UIcons.Actions.Image,
            )
            UOutlinedButton(
                text = strings.avatarUseIcon,
                onClick = onUseIconClick,
                modifier = Modifier.weight(1f),
                size = UButtonSize.Compact,
                tone = UButtonTone.Brand,
                leadingIconRes = UIcons.Cards.User,
            )
        }

        if (avatarSource == AvatarSource.Icon) {
            UContentIconCarouselPicker(
                options = packSelectableIconPalette,
                selectedKey = avatarIcon.orEmpty(),
                tintColor = Navy500,
                onSelect = { key -> onIconSelect(key.ifBlank { null }) },
            )
        }
    }
}
