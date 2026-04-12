package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Gold100
import com.uquiz.android.ui.designsystem.tokens.Gold700
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackStatsBestPerformanceCard
 *
 * Tarjeta trofeo que muestra el mejor resultado histórico del usuario en un pack.
 * Cuando no hay datos disponibles, el valor se muestra en gris neutro.
 *
 * @param value      Valor formateado del mejor rendimiento (p.ej. "87%" o "245 pts").
 * @param supporting Texto de apoyo con el modo en que se consiguió, o el literal de "sin sesiones".
 * @param valueTint  Color del texto del valor; cambia a neutro cuando no hay datos.
 */
@Composable
internal fun PackStatsBestPerformanceCard(
    value: String,
    supporting: String,
    valueTint: Color,
) {
    val strings = LocalStrings.current
    Card(
        modifier = Modifier.border(1.dp, Neutral100, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Gold100, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(UIcons.Cards.Trophy),
                    contentDescription = null,
                    tint = Gold700,
                )
            }
            Spacer(Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = strings.statsPack.packBestPerformance,
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink950,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = if (supporting.isBlank()) strings.statsPack.packNoSessionsYet else supporting,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = valueTint,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@UPreview
@Composable
private fun PackStatsBestPerformanceCardWithDataPreview() {
    UTheme {
        PackStatsBestPerformanceCard(
            value = "87%",
            supporting = "Study",
            valueTint = Teal700,
        )
    }
}

@UPreview
@Composable
private fun PackStatsBestPerformanceCardEmptyPreview() {
    UTheme {
        PackStatsBestPerformanceCard(
            value = "--",
            supporting = "",
            valueTint = Neutral500,
        )
    }
}
