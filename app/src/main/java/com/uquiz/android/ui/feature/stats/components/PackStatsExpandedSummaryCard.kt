package com.uquiz.android.ui.feature.stats.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.feature.stats.utils.StatDelta

/**
 * ### PackStatsExpandedSummaryCard
 *
 * Tarjeta de resumen ampliada para una métrica clave de pack. Muestra un icono con fondo
 * circular de color, el título de la métrica, su valor principal y opcionalmente un delta
 * de comparación respecto al período anterior o un texto de apoyo.
 *
 * @param iconRes     Recurso drawable del icono representativo de la métrica.
 * @param circleColor Color de fondo del círculo que enmarca el icono.
 * @param iconTint    Color del icono y del título de la métrica.
 * @param title       Etiqueta descriptiva de la métrica.
 * @param value       Valor principal formateado (número, porcentaje, duración…).
 * @param delta       Delta opcional respecto al período anterior. Tiene prioridad sobre [supporting].
 * @param supporting  Texto de apoyo secundario, mostrado solo si [delta] es null.
 */
@Composable
internal fun PackStatsExpandedSummaryCard(
    @DrawableRes iconRes: Int,
    circleColor: Color,
    iconTint: Color,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    delta: StatDelta? = null,
    supporting: String? = null,
) {
    Card(
        modifier = modifier
            .heightIn(min = 156.dp)
            .border(1.dp, Neutral100, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = iconTint,
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = iconTint,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = iconTint,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.weight(1f))
            when {
                delta != null -> PackStatsDeltaRow(delta)
                !supporting.isNullOrBlank() -> {
                    Text(
                        text = supporting,
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral700,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@UPreview
@Composable
private fun PackStatsExpandedSummaryCardPreview() {
    UTheme {
        PackStatsExpandedSummaryCard(
            iconRes = UIcons.Cards.Session,
            circleColor = Navy100,
            iconTint = Navy500,
            title = "Sesiones",
            value = "12",
            delta = StatDelta(label = "+3 esta semana", improved = true),
        )
    }
}
