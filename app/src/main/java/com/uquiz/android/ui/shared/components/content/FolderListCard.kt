package com.uquiz.android.ui.shared.components.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.projection.FolderWithCounts
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### FolderListCard
 *
 * Renderiza un bloque de carpetas dentro de una tarjeta compartida, separando
 * visualmente cada fila con divisores finos.
 *
 * @param folders Lista ordenada de carpetas junto con sus contadores agregados.
 * @param onFolderClick Callback invocado al pulsar una carpeta.
 * @param onEditFolderClick Callback invocado al pulsar editar en una carpeta.
 * @param onDeleteFolderClick Callback invocado al pulsar eliminar en una carpeta.
 */
@Composable
fun FolderListCard(
    folders: List<FolderWithCounts>,
    onFolderClick: (Folder) -> Unit,
    onEditFolderClick: (Folder) -> Unit,
    onDeleteFolderClick: (Folder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        folders.forEachIndexed { index, item ->
            FolderRow(
                folder = item.folder,
                subfolderCount = item.subfolderCount,
                packCount = item.packCount,
                accentIndex = index,
                onClick = { onFolderClick(item.folder) },
                onEditClick = { onEditFolderClick(item.folder) },
                onDeleteClick = { onDeleteFolderClick(item.folder) },
            )
            if (index < folders.lastIndex) {
                HorizontalDivider(
                    color = Neutral100,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@UPreview
@Composable
private fun FolderListCardPreview() {
    UTheme {
        FolderListCard(
            folders = listOf(
                FolderWithCounts(
                    folder = previewFolder(id = "folder-1", name = "Backend"),
                    subfolderCount = 2,
                    packCount = 4,
                ),
                FolderWithCounts(
                    folder = previewFolder(id = "folder-2", name = "Diseño"),
                    subfolderCount = 0,
                    packCount = 3,
                ),
            ),
            onFolderClick = {},
            onEditFolderClick = {},
            onDeleteFolderClick = {},
        )
    }
}

private fun previewFolder(
    id: String,
    name: String,
) = Folder(
    id = id,
    name = name,
    colorHex = "#134C8F",
    icon = "folder",
    createdAt = 0L,
    updatedAt = 0L,
)
