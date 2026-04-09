package com.uquiz.android.ui.designsystem.components.actionsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### UActionsSheetContent
 *
 * Muestra el contenido del panel de acciones asociado al FAB.
 *
 * @param actions Acciones visibles en el panel.
 * @param onActionClick Se invoca al pulsar una acción.
 */
@Composable
fun UActionsSheetContent(
    actions: List<UFabActionItem>,
    onActionClick: (UFabActionItem) -> Unit,
) {
    val strings = LocalStrings.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 2.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = strings.actionsLabel,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        actions.forEach { action ->
            UActionSheetRow(
                action = action,
                onClick = { onActionClick(action) },
            )
        }
    }
}

@UPreview
@Composable
private fun UActionsSheetContentPreview() {
    UTheme {
        UActionsSheetContent(
            actions = listOf(
                UFabActionItem(
                    id = "editar",
                    label = "Editar",
                    description = "Modifica este elemento",
                    iconRes = UIcons.Actions.Edit,
                    onClick = {},
                ),
            ),
            onActionClick = {},
        )
    }
}
