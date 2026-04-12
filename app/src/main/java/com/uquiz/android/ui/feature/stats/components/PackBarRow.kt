package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.UserPackStatsRow
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Orange100
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.stats.utils.toReadableDuration

/**
 * ### PackBarRow
 *
 * Fila de rendimiento de un pack. Muestra el título del pack, el porcentaje de acierto,
 * una barra de progreso de completado y el número de sesiones con la duración media.
 *
 * @param row Datos del pack: título, accuracy, progreso y estadísticas de sesión.
 * @param accuracyLabel Etiqueta para el porcentaje de acierto (e.g. "Acierto").
 * @param progressLabel Etiqueta de la barra de progreso (e.g. "Progreso").
 * @param sessionsLabel Etiqueta para el conteo de sesiones (e.g. "sesiones").
 */
@Composable
fun PackBarRow(
    row: UserPackStatsRow,
    accuracyLabel: String,
    progressLabel: String,
    sessionsLabel: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = row.title,
                style = MaterialTheme.typography.labelLarge,
                color = Ink950,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "$accuracyLabel: ${row.accuracyPercent.percentLabel()}",
                style = MaterialTheme.typography.labelMedium,
                color = Navy500,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Text(
            text = progressLabel,
            style = MaterialTheme.typography.labelSmall,
            color = Neutral500,
        )
        LinearProgressIndicator(
            progress = { row.progressPercent.coerceIn(0, 100) / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Orange500,
            trackColor = Orange100,
            strokeCap = StrokeCap.Round,
        )
        Text(
            text = "${row.sessions} ${sessionsLabel.lowercase()} · ${row.averageDurationMs?.toReadableDuration() ?: "--"}",
            style = MaterialTheme.typography.bodySmall,
            color = Neutral700,
        )
    }
}

private fun Int?.percentLabel(): String = this?.let { "$it%" } ?: "--"

@UPreview
@Composable
private fun PackBarRowPreview() {
    UTheme {
        PackBarRow(
            row = UserPackStatsRow(
                packId = "1",
                title = "Kotlin básico",
                sessions = 12,
                accuracyPercent = 84,
                averageDurationMs = 220_000L,
                progressPercent = 72,
            ),
            accuracyLabel = "Acierto",
            progressLabel = "Progreso",
            sessionsLabel = "sesiones",
        )
    }
}
