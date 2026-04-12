package com.uquiz.android.ui.feature.study.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy200
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.shared.components.studygame.MetricCard

/**
 * ### StudyMetricCard
 *
 * Tarjeta de métrica del modo estudio sobre fondo oscuro. Delegado en [MetricCard].
 *
 * @param value      Valor principal a destacar (número, porcentaje, tiempo).
 * @param label      Etiqueta descriptiva del valor.
 * @param valueColor Color aplicado únicamente al texto del valor.
 */
@Composable
fun StudyMetricCard(
    value: String,
    label: String,
    valueColor: Color = Navy200,
    modifier: Modifier = Modifier,
) {
    MetricCard(
        value = value,
        label = label,
        valueColor = valueColor,
        modifier = modifier,
    )
}

@UPreview
@Composable
private fun StudyMetricCardPreview() {
    UTheme {
        StudyMetricCard(
            value = "Valor: 3%",
            label = "Esta es la variable",
        )
    }
}
