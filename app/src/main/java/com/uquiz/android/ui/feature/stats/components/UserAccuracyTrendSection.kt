package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.UserAccuracyTrendPoint
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserAccuracyTrendSection
 *
 * Panel que muestra la evolución del porcentaje de acierto a lo largo del tiempo.
 * Incluye el gráfico de línea y una etiqueta del eje X que indica qué representa
 * cada punto horizontal.
 *
 * @param points Lista de puntos ordenada cronológicamente. Mínimo 2 para mostrar la línea.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserAccuracyTrendSection(
    points: List<UserAccuracyTrendPoint>,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    StatsPanel(title = strings.statsUser.accuracyTrend, modifier = modifier) {
        AccuracyLineChart(points = points)
        // Etiqueta del eje X alineada a la derecha, dejando espacio para el eje Y
        Text(
            text = "← ${strings.common.sessionsStatLabel.lowercase()}",
            style = MaterialTheme.typography.labelSmall,
            color = Neutral500,
            modifier = Modifier
                .align(Alignment.End)
                .padding(start = 40.dp),
        )
    }
}

@UPreview
@Composable
private fun UserAccuracyTrendSectionPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserAccuracyTrendSection(
            points = listOf(
                UserAccuracyTrendPoint(1L, 62),
                UserAccuracyTrendPoint(2L, 70),
                UserAccuracyTrendPoint(3L, 68),
                UserAccuracyTrendPoint(4L, 76),
                UserAccuracyTrendPoint(5L, 82),
            ),
            strings = strings,
        )
    }
}
