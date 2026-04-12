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
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackActionButtonsRow
 *
 * Fila principal de acciones para iniciar estudio o juego desde un pack.
 *
 * @param canStartStudy Indica si el botón de estudio debe estar habilitado.
 * @param canStartGame Indica si el botón de juego debe estar habilitado.
 * @param onStudyClick Acción para entrar al modo estudio.
 * @param onGameClick Acción para entrar al modo juego.
 */
@Composable
fun PackActionButtonsRow(
    canStartStudy: Boolean,
    canStartGame: Boolean,
    onStudyClick: () -> Unit,
    onGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        UFilledButton(
            text = strings.common.studyAction,
            onClick = onStudyClick,
            enabled = canStartStudy,
            leadingIconRes = UIcons.Actions.Study,
            size = UButtonSize.Compact,
            modifier = Modifier.weight(1f)
        )
        UOutlinedButton(
            text = strings.common.playMode,
            onClick = onGameClick,
            enabled = canStartGame,
            leadingIconRes = UIcons.Actions.Play,
            size = UButtonSize.Compact,
            modifier = Modifier.weight(1f)
        )
    }
}

@UPreview
@Composable
private fun PackActionButtonsRowPreview() {
    UTheme {
        PackActionButtonsRow(
            canStartStudy = true,
            canStartGame = true,
            onStudyClick = {},
            onGameClick = {},
        )
    }
}
