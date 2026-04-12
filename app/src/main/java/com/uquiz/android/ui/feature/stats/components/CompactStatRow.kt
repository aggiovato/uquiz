package com.uquiz.android.ui.feature.stats.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### CompactStatRow
 *
 * Fila compacta de métrica con badge circular de icono, valor principal en negrita y
 * etiqueta descriptiva. Diseñada para mostrar 6 métricas dentro de un [StatsPanel].
 *
 * @param iconRes Recurso drawable del icono del badge.
 * @param iconBg Color de fondo del badge circular.
 * @param iconTint Color aplicado al icono.
 * @param value Texto del valor principal (número, porcentaje, duración).
 * @param label Etiqueta descriptiva del valor.
 */
@Composable
fun CompactStatRow(
    @DrawableRes iconRes: Int,
    iconBg: Color,
    iconTint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBg, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Ink950,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Neutral500,
            )
        }
    }
}

@UPreview
@Composable
private fun CompactStatRowPreview() {
    UTheme {
        CompactStatRow(
            iconRes = UIcons.Cards.Session,
            iconBg = Navy100,
            iconTint = Navy500,
            value = "42",
            label = "Sesiones",
        )
    }
}
