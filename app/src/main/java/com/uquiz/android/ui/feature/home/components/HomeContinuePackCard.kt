package com.uquiz.android.ui.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.cards.UIconBadge
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral200
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.home.model.ContinuePackUiModel
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.designsystem.tokens.contentAccents
import com.uquiz.android.ui.designsystem.tokens.contentColorFromHex
import com.uquiz.android.ui.designsystem.tokens.contentIconResForKey

/**
 * ### HomeContinuePackCard
 *
 * Tarjeta resumida de un pack con progreso pendiente desde la pantalla Home.
 *
 * @param item Modelo de UI con título, conteos y progreso del pack.
 * @param accentIndex Índice usado para alternar el acento visual de la tarjeta.
 * @param onClick Acción al pulsar la tarjeta.
 */
@Composable
fun HomeContinuePackCard(
    item: ContinuePackUiModel,
    accentIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val (backgroundColor, contentColor) = contentColorFromHex(
        item.colorHex,
        contentAccents[accentIndex % contentAccents.size]
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Neutral100),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UIconBadge(
                    iconRes = contentIconResForKey(item.icon, fallback = UIcons.Select.File),
                    backgroundColor = backgroundColor,
                    contentColor = contentColor
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Ink950
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${item.answeredCount}/${item.totalQuestions}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.common.progressLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500
                )
                Text(
                    text = "${(item.progressFraction * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Teal700
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { item.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = Teal700,
                trackColor = Neutral200
            )
        }
    }
}

@UPreview
@Composable
private fun HomeContinuePackCardPreview() {
    UTheme {
        HomeContinuePackCard(
            item = ContinuePackUiModel(
                packId = "pack-1",
                title = "Historia moderna",
                answeredCount = 8,
                totalQuestions = 20,
                progressFraction = 0.4f,
                icon = null,
                colorHex = null,
            ),
            accentIndex = 0,
            onClick = {},
        )
    }
}
