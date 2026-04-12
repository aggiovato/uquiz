package com.uquiz.android.ui.shared.components.studygame

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Navy200
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.UTheme

/** Variante de tamaño de [MetricCard]: Regular conserva las proporciones originales, Small reduce padding y tipografía. */
enum class MetricCardSize { Regular, Small }

/**
 * ### MetricCard
 *
 * Tarjeta de métrica compartida entre el modo estudio y el modo juego sobre fondo oscuro.
 *
 * Siempre utiliza el estilo neutro de fondo blanco con alpha para mantener la consistencia
 * visual independientemente de la métrica mostrada.
 *
 * @param value      Valor principal a destacar (número, porcentaje, tiempo).
 * @param label      Etiqueta descriptiva mostrada bajo el valor.
 * @param valueColor Color aplicado únicamente al texto del valor.
 * @param size       Variante de tamaño: Regular (por defecto) o Small para pantallas con espacio reducido.
 */
@Composable
fun MetricCard(
    value: String,
    label: String,
    valueColor: Color = Navy200,
    size: MetricCardSize = MetricCardSize.Regular,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = if (size == MetricCardSize.Small) 12.dp else 16.dp
    val verticalPadding = if (size == MetricCardSize.Small) 10.dp else 18.dp
    val valueStyle = if (size == MetricCardSize.Small) {
        MaterialTheme.typography.titleLarge
    } else {
        MaterialTheme.typography.headlineMedium
    }
    val labelStyle = if (size == MetricCardSize.Small) {
        MaterialTheme.typography.labelSmall
    } else {
        MaterialTheme.typography.bodySmall
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(AppRadius),
        color = Color.White.copy(alpha = 0.09f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = value,
                style = valueStyle,
                fontWeight = FontWeight.Bold,
                color = valueColor,
            )
            Text(
                text = label,
                style = labelStyle,
                color = Neutral100.copy(alpha = 0.86f),
            )
        }
    }
}

@UPreview
@Composable
private fun MetricCardPreview() {
    UTheme {
        MetricCard(
            value = "42",
            label = "Preguntas",
        )
    }
}
