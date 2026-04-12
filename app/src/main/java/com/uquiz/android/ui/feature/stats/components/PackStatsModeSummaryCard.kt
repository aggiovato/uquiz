package com.uquiz.android.ui.feature.stats.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackStatsModeSummaryCard
 *
 * Tarjeta compacta que resume las sesiones de un modo de juego concreto (Study o Game).
 * Muestra el número de sesiones realizadas y, opcionalmente, un texto de apoyo con la
 * precisión media.
 *
 * @param iconRes      Recurso drawable representativo del modo.
 * @param iconTint     Color del icono.
 * @param circleColor  Color de fondo del círculo que enmarca el icono.
 * @param title        Nombre del modo de juego.
 * @param sessions     Número de sesiones completadas en este modo.
 * @param supporting   Texto de apoyo opcional (p.ej. "82% precisión").
 */
@Composable
internal fun PackStatsModeSummaryCard(
    @DrawableRes iconRes: Int,
    iconTint: Color,
    title: String,
    sessions: Int,
    supporting: String?,
    modifier: Modifier = Modifier,
    circleColor: Color = Navy100,
) {
    val strings = LocalStrings.current
    Card(
        modifier = modifier
            .heightIn(min = 128.dp)
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
                    .size(44.dp)
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
                style = MaterialTheme.typography.titleSmall,
                color = Ink950,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = if (sessions == 1) {
                    "1 ${strings.common.sessionsStatLabel.lowercase()}"
                } else {
                    "$sessions ${strings.common.sessionsStatLabel.lowercase()}"
                },
                color = Neutral700,
                style = MaterialTheme.typography.bodyMedium,
            )
            supporting?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun PackStatsModeSummaryCardPreview() {
    UTheme {
        PackStatsModeSummaryCard(
            iconRes = UIcons.Actions.Study,
            iconTint = Navy500,
            title = "Study",
            sessions = 8,
            supporting = "79% precisión",
        )
    }
}
