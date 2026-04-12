package com.uquiz.android.ui.shared.components.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UIconActionButton
import com.uquiz.android.ui.designsystem.components.cards.UIconBadge
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentAccents
import com.uquiz.android.ui.designsystem.tokens.contentColorFromHex
import com.uquiz.android.ui.designsystem.tokens.contentIconResForKey
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### FolderRow
 *
 * Muestra una fila interactiva de carpeta con su icono, subtítulo de contenido
 * y acciones de edición y borrado.
 *
 * @param folder Carpeta que se va a representar.
 * @param subfolderCount Número de subcarpetas directas mostrado en el subtítulo.
 * @param packCount Número de packs directos mostrado en el subtítulo.
 * @param accentIndex Índice usado para elegir un color de respaldo cuando la
 * carpeta no tiene color persistido.
 * @param onClick Callback invocado al pulsar la fila.
 * @param onEditClick Callback invocado al pulsar editar.
 * @param onDeleteClick Callback invocado al pulsar eliminar.
 */
@Composable
fun FolderRow(
    folder: Folder,
    subfolderCount: Int = 0,
    packCount: Int = 0,
    accentIndex: Int = 0,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, iconColor) = remember(folder.colorHex, accentIndex) {
        contentColorFromHex(folder.colorHex, contentAccents[accentIndex % contentAccents.size])
    }
    val strings = LocalStrings.current
    val subtitleParts = buildList {
        if (subfolderCount > 0) add(strings.common.subfoldersLabel(subfolderCount))
        if (packCount > 0) add(strings.common.packsLabel(packCount))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UIconBadge(
            iconRes = contentIconResForKey(folder.icon, fallback = UIcons.Select.Folder),
            backgroundColor = backgroundColor,
            contentColor = iconColor,
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = folder.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Ink950,
            )
            if (subtitleParts.isNotEmpty()) {
                Text(
                    text = subtitleParts.joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            UIconActionButton(
                iconRes = UIcons.Actions.Edit,
                contentDescription = strings.common.editFolder,
                onClick = onEditClick,
                tone = UButtonTone.Brand,
                elevated = false,
            )
            Spacer(Modifier.width(8.dp))
            UIconActionButton(
                iconRes = UIcons.Actions.Delete,
                contentDescription = strings.common.deleteFolder,
                onClick = onDeleteClick,
                tone = UButtonTone.Danger,
                elevated = false,
            )
        }
    }
}

@UPreview
@Composable
private fun FolderRowPreview() {
    UTheme {
        FolderRow(
            folder = Folder(
                id = "folder-preview",
                name = "Arquitectura",
                colorHex = "#134C8F",
                icon = "folder",
                createdAt = 0L,
                updatedAt = 0L,
            ),
            subfolderCount = 2,
            packCount = 5,
            onClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
