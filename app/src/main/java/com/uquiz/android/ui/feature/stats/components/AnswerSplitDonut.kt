package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uquiz.android.domain.stats.projection.UserAnswerSplit
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### AnswerSplitDonut
 *
 * Donut chart que compara respuestas correctas e incorrectas. El arco teal representa los
 * aciertos y el arco naranja los errores. El porcentaje de acierto y su etiqueta se
 * superponen en el centro del donut.
 *
 * @param split Conteo acumulado de respuestas correctas e incorrectas.
 * @param accuracyLabel Etiqueta de contexto mostrada bajo el porcentaje central (e.g. "Accuracy").
 * @param correctLabel Texto de la leyenda para los aciertos.
 * @param incorrectLabel Texto de la leyenda para los errores.
 */
@Composable
fun AnswerSplitDonut(
    split: UserAnswerSplit,
    accuracyLabel: String,
    correctLabel: String,
    incorrectLabel: String,
    modifier: Modifier = Modifier,
) {
    val total = split.correctAnswers + split.incorrectAnswers
    val correctSweep = if (total > 0) 360f * split.correctAnswers / total.toFloat() else 0f
    val accuracyPercent = if (total > 0) split.correctAnswers * 100 / total else 0
    val incorrectPercent = if (total > 0) 100 - accuracyPercent else 0
    val ink950Argb = Ink950.toArgb()
    val neutral500Argb = Neutral500.toArgb()

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val stroke = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            val inset = 8.dp.toPx()
            val arcSize = Size(size.width - inset * 2, size.height - inset * 2)

            // Arco de errores (fondo naranja claro)
            drawArc(
                color = Orange500,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = stroke,
            )
            // Arco de aciertos (teal)
            if (correctSweep > 0f) {
                drawArc(
                    color = Teal500,
                    startAngle = -90f,
                    sweepAngle = correctSweep,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = stroke,
                )
            }
            // Porcentaje central
            val valuePaint =
                android.graphics.Paint().apply {
                    isAntiAlias = true
                    textSize = 22.sp.toPx()
                    color = ink950Argb
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                }
            drawContext.canvas.nativeCanvas.drawText(
                "$accuracyPercent%",
                center.x,
                center.y + 4.dp.toPx(),
                valuePaint,
            )
            // Etiqueta de contexto bajo el porcentaje
            val labelPaint =
                android.graphics.Paint().apply {
                    isAntiAlias = true
                    textSize = 10.sp.toPx()
                    color = neutral500Argb
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = android.graphics.Typeface.DEFAULT
                }
            drawContext.canvas.nativeCanvas.drawText(
                accuracyLabel,
                center.x,
                center.y + valuePaint.textSize * 0.9f + 4.dp.toPx(),
                labelPaint,
            )
        }
    }

    // Leyenda: correctas e incorrectas con porcentaje
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        DonutLegendItem(
            color = Teal500,
            label = "$correctLabel: ${split.correctAnswers} ($accuracyPercent%)",
        )
        DonutLegendItem(
            color = Orange500,
            label = "$incorrectLabel: ${split.incorrectAnswers} ($incorrectPercent%)",
        )
    }
}

@Composable
private fun DonutLegendItem(
    color: Color,
    label: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(8.dp)
                    .background(color, CircleShape),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Neutral700,
        )
    }
}

@UPreview
@Composable
private fun AnswerSplitDonutPreview() {
    UTheme {
        AnswerSplitDonut(
            split = UserAnswerSplit(correctAnswers = 484, incorrectAnswers = 136),
            accuracyLabel = "Accuracy",
            correctLabel = "Correct",
            incorrectLabel = "Incorrect",
        )
    }
}
