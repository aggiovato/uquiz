package com.uquiz.android.ui.designsystem.components.actionsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral300
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### UActionSheetRow
 *
 * Muestra una fila de acción dentro del panel de acciones del FAB.
 *
 * @param action Metadatos de la acción representada.
 * @param onClick Se invoca al pulsar una acción habilitada.
 */
@Composable
fun UActionSheetRow(
    action: UFabActionItem,
    onClick: () -> Unit,
) {
    val accentColor = when {
        !action.enabled -> Neutral300
        action.kind == UFabActionKind.Destructive -> Red700
        action.containerColor != null -> action.containerColor
        else -> Navy500
    }
    val titleColor = if (action.enabled) Ink950 else Neutral400
    val descriptionColor = if (action.enabled) Neutral400 else Neutral300
    val iconTint = if (action.enabled) action.contentColor else Color.White
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = action.enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(accentColor, RoundedCornerShape(URadius)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(action.iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = action.label,
                style = MaterialTheme.typography.bodyMedium,
                color = titleColor,
            )
            Text(
                text = action.description,
                style = MaterialTheme.typography.labelSmall,
                color = descriptionColor,
            )
        }
    }
}

@UPreview
@Composable
private fun UActionSheetRowPreview() {
    UTheme {
        UActionSheetRow(
            action = UFabActionItem(
                id = "eliminar",
                label = "Eliminar",
                description = "Borrar este elemento",
                iconRes = UIcons.Actions.Delete,
                kind = UFabActionKind.Destructive,
                onClick = {},
            ),
            onClick = {},
        )
    }
}
