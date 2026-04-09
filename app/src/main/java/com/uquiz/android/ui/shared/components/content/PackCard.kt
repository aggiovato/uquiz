package com.uquiz.android.ui.shared.components.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### PackCard
 *
 * Renderiza un pack dentro de una tarjeta con borde, delegando el contenido de
 * la fila a [PackRow].
 *
 * @param pack Pack que se va a representar.
 * @param questionCount Número total de preguntas del pack.
 * @param answeredCount Número de preguntas respondidas en una sesión activa.
 * @param progress Progreso opcional del pack en formato `0..1`.
 * @param accentIndex Índice usado para elegir un color de respaldo.
 * @param onClick Callback invocado al pulsar la tarjeta.
 * @param onEditClick Callback invocado al pulsar editar.
 * @param onDeleteClick Callback invocado al pulsar eliminar.
 */
@Composable
fun PackCard(
    pack: Pack,
    questionCount: Int = 0,
    answeredCount: Int = 0,
    progress: Float? = null,
    accentIndex: Int,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Neutral100),
    ) {
        PackRow(
            pack = pack,
            questionCount = questionCount,
            answeredCount = answeredCount,
            progress = progress,
            accentIndex = accentIndex,
            onClick = onClick,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
        )
    }
}

@UPreview
@Composable
private fun PackCardPreview() {
    UTheme {
        PackCard(
            pack = previewPack(),
            questionCount = 24,
            answeredCount = 9,
            progress = 0.38f,
            accentIndex = 0,
            onClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}

private fun previewPack() = Pack(
    id = "pack-preview",
    title = "Kotlin Coroutines",
    description = "Repaso rápido",
    folderId = "folder-preview",
    icon = "file",
    colorHex = "#176B87",
    createdAt = 0L,
    updatedAt = 0L,
)
