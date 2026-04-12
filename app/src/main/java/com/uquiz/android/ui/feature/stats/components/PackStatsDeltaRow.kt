package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.feature.stats.utils.StatDelta

/**
 * ### PackStatsDeltaRow
 *
 * Fila compacta que muestra el cambio de una métrica respecto al período anterior.
 * La flecha rota 180° y cambia de color cuando el cambio es desfavorable.
 *
 * @param delta Datos del delta, incluyendo etiqueta formateada y sentido del cambio.
 */
@Composable
internal fun PackStatsDeltaRow(delta: StatDelta) {
    val tint = if (delta.improved) Teal700 else Color(0xFFE04F5F)
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(UIcons.Cards.ArrowUp),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(14.dp)
                .rotate(if (delta.improved) 0f else 180f),
        )
        Text(
            text = delta.label,
            style = MaterialTheme.typography.bodySmall,
            color = tint,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@UPreview
@Composable
private fun PackStatsDeltaRowPositivePreview() {
    UTheme {
        PackStatsDeltaRow(delta = StatDelta(label = "+3 esta semana", improved = true))
    }
}

@UPreview
@Composable
private fun PackStatsDeltaRowNegativePreview() {
    UTheme {
        PackStatsDeltaRow(delta = StatDelta(label = "-5pp", improved = false))
    }
}
