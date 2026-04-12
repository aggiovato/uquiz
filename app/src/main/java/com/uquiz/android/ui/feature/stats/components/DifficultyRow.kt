package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.stats.projection.UserDifficultyStats
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Red500
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.stats.utils.toReadableDuration

/**
 * ### DifficultyRow
 *
 * Fila de estadísticas por nivel de dificultad. Muestra un badge circular con la inicial
 * del nivel, el nombre completo, el número de preguntas respondidas, el porcentaje de acierto
 * y el tiempo medio de respuesta.
 *
 * @param row Datos de estadísticas del nivel de dificultad.
 * @param difficultyLabel Nombre localizado del nivel de dificultad (e.g. "Fácil").
 * @param answeredLabel Etiqueta del conteo de preguntas respondidas (e.g. "preguntas respondidas").
 */
@Composable
fun DifficultyRow(
    row: UserDifficultyStats,
    difficultyLabel: String,
    answeredLabel: String,
    modifier: Modifier = Modifier,
) {
    val tone = row.difficulty.toneColor()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Neutral100, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(tone.copy(alpha = 0.14f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = difficultyLabel.take(1),
                style = MaterialTheme.typography.titleMedium,
                color = tone,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = difficultyLabel,
                style = MaterialTheme.typography.titleSmall,
                color = Ink950,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${row.answeredCount} ${answeredLabel.lowercase()}",
                style = MaterialTheme.typography.bodySmall,
                color = Neutral700,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = row.accuracyPercent.percentLabel(),
                style = MaterialTheme.typography.titleMedium,
                color = tone,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = row.averageTimeMs?.toReadableDuration() ?: "--",
                style = MaterialTheme.typography.bodySmall,
                color = Neutral700,
            )
        }
    }
}

private fun Int?.percentLabel(): String = this?.let { "$it%" } ?: "--"

private fun DifficultyLevel.toneColor(): Color = when (this) {
    DifficultyLevel.EASY -> Teal500
    DifficultyLevel.MEDIUM -> com.uquiz.android.ui.designsystem.tokens.Gold500
    DifficultyLevel.HARD -> Orange500
    DifficultyLevel.EXPERT -> Red500
}

@UPreview
@Composable
private fun DifficultyRowPreview() {
    UTheme {
        DifficultyRow(
            row = UserDifficultyStats(
                difficulty = DifficultyLevel.MEDIUM,
                answeredCount = 260,
                accuracyPercent = 78,
                averageTimeMs = 17_000L,
            ),
            difficultyLabel = "Medium",
            answeredLabel = "questions answered",
        )
    }
}
