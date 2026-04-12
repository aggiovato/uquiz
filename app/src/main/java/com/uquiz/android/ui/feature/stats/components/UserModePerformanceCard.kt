package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.UserModeStats
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserModePerformanceCard
 *
 * Panel de rendimiento por modo de juego. Muestra el porcentaje de acierto en modo estudio
 * y modo juego, la mejor y media puntuación en partida, y el porcentaje de preguntas dominadas.
 * Solo se muestra cuando el filtro de modo es "Todos".
 *
 * @param modeStats Estadísticas de rendimiento por modo del usuario.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserModePerformanceCard(
    modeStats: UserModeStats,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    StatsPanel(title = strings.statsUser.modePerformance, modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ModeStatRow(
                label = "${strings.common.studyModeShort} ${strings.common.accuracyStatLabel.lowercase()}",
                value = modeStats.studyAccuracyPercent.percentLabel(),
            )
            ModeStatRow(
                label = "${strings.common.playModeShort} ${strings.common.accuracyStatLabel.lowercase()}",
                value = modeStats.gameAccuracyPercent.percentLabel(),
            )
            HorizontalDivider(color = Neutral100)
            ModeStatRow(label = strings.statsUser.bestGameScore, value = modeStats.bestGameScore?.toString() ?: "--")
            ModeStatRow(label = strings.statsUser.averageGameScore, value = modeStats
                .averageGameScore?.toString() ?: "--")
            HorizontalDivider(color = Neutral100)
            ModeStatRow(label = strings.statsUser.masteredQuestions, value = modeStats.masteredQuestionPercent
                .percentLabel())
        }
    }
}

@Composable
private fun ModeStatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Neutral700,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = Ink950,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun Int?.percentLabel(): String = this?.let { "$it%" } ?: "--"

@UPreview
@Composable
private fun UserModePerformanceCardPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserModePerformanceCard(
            modeStats = UserModeStats(
                studyAccuracyPercent = 82,
                gameAccuracyPercent = 74,
                bestGameScore = 1420,
                averageGameScore = 860,
                masteredQuestionPercent = 46,
            ),
            strings = strings,
        )
    }
}
