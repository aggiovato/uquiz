package com.uquiz.android.ui.feature.stats.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.UserQuestionInsight
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### QuestionInsightCard
 *
 * Tarjeta de insight de pregunta con badge de icono, valor destacado y vista previa del
 * texto. Incluye un botón de previsualización que abre un diálogo con el enunciado completo.
 *
 * @param title Etiqueta del tipo de insight (e.g. "Pregunta más rápida").
 * @param insight Datos de la pregunta destacada, o `null` si no hay datos disponibles.
 * @param tone Color temático del insight: tint del badge, label y botón.
 * @param iconRes Recurso drawable del icono del badge.
 * @param noDataText Texto mostrado cuando [insight] es `null`.
 * @param previewLabel Texto del botón de previsualización (e.g. "Preview").
 * @param onPreview Se invoca al pulsar el botón de previsualización. Solo visible si [insight] no es `null`.
 */
@Composable
fun QuestionInsightCard(
    title: String,
    insight: UserQuestionInsight?,
    tone: Color,
    @DrawableRes iconRes: Int,
    noDataText: String,
    previewLabel: String,
    onPreview: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .heightIn(min = 148.dp)
            .background(tone, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.20f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.90f),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
        Text(
            text = insight?.valueLabel ?: "--",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = insight?.questionText ?: noDataText,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.82f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (insight != null) {
            Row(
                modifier = Modifier
                    .clickable(onClick = onPreview)
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(UIcons.Actions.See),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.80f),
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = previewLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.80f),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun QuestionInsightCardPreview() {
    UTheme {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            QuestionInsightCard(
                title = "Pregunta más rápida",
                insight = UserQuestionInsight("q1", "¿Qué keyword declara una clase en Kotlin?", "4s"),
                tone = Teal500,
                iconRes = UIcons.Cards.Clock,
                noDataText = "Aún no hay datos",
                previewLabel = "Preview",
                onPreview = {},
                modifier = Modifier.weight(1f),
            )
            QuestionInsightCard(
                title = "Más fallada",
                insight = null,
                tone = com.uquiz.android.ui.designsystem.tokens.Orange500,
                iconRes = UIcons.Feedback.Error,
                noDataText = "Aún no hay datos",
                previewLabel = "Preview",
                onPreview = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}
