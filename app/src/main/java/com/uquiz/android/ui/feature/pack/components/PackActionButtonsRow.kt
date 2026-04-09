package com.uquiz.android.ui.feature.pack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UOutlinedButton
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.LocalStrings

@Composable
fun PackActionButtonsRow(
    canStartStudy: Boolean,
    canStartGame: Boolean,
    onStudyClick: () -> Unit,
    onQuickGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        UFilledButton(
            text = strings.studyAction,
            onClick = onStudyClick,
            enabled = canStartStudy,
            leadingIconRes = UIcons.Actions.Study,
            size = UButtonSize.Compact,
            modifier = Modifier.weight(1f)
        )
        UOutlinedButton(
            text = strings.playMode,
            onClick = onQuickGameClick,
            enabled = canStartGame,
            leadingIconRes = UIcons.Actions.Play,
            size = UButtonSize.Compact,
            modifier = Modifier.weight(1f)
        )
    }
}
