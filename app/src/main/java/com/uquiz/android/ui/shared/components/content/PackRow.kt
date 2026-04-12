package com.uquiz.android.ui.shared.components.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UIconActionButton
import com.uquiz.android.ui.designsystem.components.cards.UIconBadge
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral200
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentAccents
import com.uquiz.android.ui.designsystem.tokens.contentColorFromHex
import com.uquiz.android.ui.designsystem.tokens.contentIconResForKey
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackRow
 *
 * Muestra una fila de pack con icono, título, contador de preguntas, acciones
 * y una sección opcional de progreso.
 *
 * @param pack Pack que se va a representar.
 * @param questionCount Número total de preguntas mostrado como subtítulo.
 * @param answeredCount Número de preguntas respondidas usado en la sección de progreso.
 * @param progress Progreso opcional en formato `0..1`.
 * @param accentIndex Índice usado para elegir un color de respaldo.
 * @param onClick Callback invocado al pulsar la fila.
 * @param onEditClick Callback invocado al pulsar editar.
 * @param onDeleteClick Callback invocado al pulsar eliminar.
 */
@Composable
fun PackRow(
    pack: Pack,
    questionCount: Int = 0,
    answeredCount: Int = 0,
    progress: Float? = null,
    accentIndex: Int = 0,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, iconColor) =
        contentColorFromHex(pack.colorHex, contentAccents[accentIndex % contentAccents.size])
    val strings = LocalStrings.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UIconBadge(
                iconRes = contentIconResForKey(pack.icon, fallback = UIcons.Select.File),
                backgroundColor = backgroundColor,
                contentColor = iconColor,
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pack.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Ink950,
                )
                if (questionCount > 0) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = strings.common.questionsLabel(questionCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                UIconActionButton(
                    iconRes = UIcons.Actions.Edit,
                    contentDescription = strings.common.editPack,
                    onClick = onEditClick,
                    tone = UButtonTone.Brand,
                    elevated = false,
                )
                Spacer(Modifier.width(8.dp))
                UIconActionButton(
                    iconRes = UIcons.Actions.Delete,
                    contentDescription = strings.common.deletePack,
                    onClick = onDeleteClick,
                    tone = UButtonTone.Danger,
                    elevated = false,
                )
            }
        }

        if (progress != null) {
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = strings.common.progressLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$answeredCount / $questionCount",
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Teal700,
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = Teal700,
                trackColor = Neutral200,
            )
        }
    }
}

@UPreview
@Composable
private fun PackRowPreview() {
    UTheme {
        PackRow(
            pack = Pack(
                id = "pack-row-preview",
                title = "HTTP Essentials",
                description = null,
                folderId = "folder-preview",
                icon = "file",
                colorHex = "#176B87",
                createdAt = 0L,
                updatedAt = 0L,
            ),
            questionCount = 18,
            answeredCount = 6,
            progress = 0.33f,
            onClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
