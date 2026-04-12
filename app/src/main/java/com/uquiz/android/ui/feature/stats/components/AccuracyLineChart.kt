package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uquiz.android.domain.stats.projection.UserAccuracyTrendPoint
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Orange100
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Orange700
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### AccuracyLineChart
 *
 * Gráfico de línea que representa la evolución del porcentaje de acierto a lo largo de
 * sesiones. Muestra etiquetas del eje Y (0 %, 50 %, 100 %) y el valor exacto encima de
 * cada punto. Requiere al menos 2 puntos para renderizar la línea; si hay menos, muestra "--".
 *
 * @param points Lista de puntos ordenada cronológicamente. Mínimo 2 para mostrar la línea.
 */
@Composable
fun AccuracyLineChart(
    points: List<UserAccuracyTrendPoint>,
    modifier: Modifier = Modifier,
) {
    if (points.size < 2) {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "--", color = Neutral500, style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    val orange700Argb = Orange700.toArgb()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        // Etiquetas del eje Y
        Column(
            modifier =
                Modifier
                    .width(40.dp)
                    .height(160.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "100%", style = MaterialTheme.typography.labelSmall, color = Neutral500)
            Text(text = "50%", style = MaterialTheme.typography.labelSmall, color = Neutral500)
            Text(text = "0%", style = MaterialTheme.typography.labelSmall, color = Neutral500)
        }
        Canvas(
            modifier =
                Modifier
                    .weight(1f)
                    .height(160.dp),
        ) {
            val stepX = size.width / (points.size - 1).coerceAtLeast(1)
            val chartHeight = size.height
            val offsets =
                points.mapIndexed { index, point ->
                    Offset(
                        x = index * stepX,
                        y = chartHeight - (chartHeight * (point.accuracyPercent.coerceIn(0, 100) / 100f)),
                    )
                }
            // Líneas de guía horizontales
            repeat(4) { index ->
                val y = chartHeight * (index + 1) / 5f
                drawLine(
                    color = Neutral100,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx(),
                )
            }
            // Línea del gráfico
            offsets.zipWithNext().forEach { (start, end) ->
                drawLine(
                    color = Orange500,
                    start = start,
                    end = end,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
            // Puntos con etiqueta de valor exacto
            val labelPaint =
                android.graphics.Paint().apply {
                    isAntiAlias = true
                    textSize = 9.sp.toPx()
                    color = orange700Argb
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                }
            offsets.forEachIndexed { i, point ->
                drawCircle(color = Orange100, radius = 6.dp.toPx(), center = point)
                drawCircle(color = Orange500, radius = 4.dp.toPx(), center = point)
                val labelY = (point.y - 8.dp.toPx()).coerceAtLeast(labelPaint.textSize)
                drawContext.canvas.nativeCanvas.drawText(
                    "${points[i].accuracyPercent}%",
                    point.x,
                    labelY,
                    labelPaint,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun AccuracyLineChartPreview() {
    UTheme {
        AccuracyLineChart(
            points =
                listOf(
                    UserAccuracyTrendPoint(1L, 62),
                    UserAccuracyTrendPoint(2L, 70),
                    UserAccuracyTrendPoint(3L, 55),
                    UserAccuracyTrendPoint(4L, 80),
                    UserAccuracyTrendPoint(5L, 90),
                ),
        )
    }
}
