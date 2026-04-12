package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UOutlinedButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Gold100
import com.uquiz.android.ui.designsystem.tokens.Gold700
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Orange100
import com.uquiz.android.ui.designsystem.tokens.Orange700
import com.uquiz.android.ui.designsystem.tokens.Purple100
import com.uquiz.android.ui.designsystem.tokens.Purple700
import com.uquiz.android.ui.designsystem.tokens.Teal100
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.feature.stats.utils.accuracyDelta
import com.uquiz.android.ui.feature.stats.utils.asModeLabel
import com.uquiz.android.ui.feature.stats.utils.durationDelta
import com.uquiz.android.ui.feature.stats.utils.recentSessionsDelta
import com.uquiz.android.ui.feature.stats.utils.toDisplayValue
import com.uquiz.android.ui.feature.stats.utils.toReadableDuration
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackDetailedStatsContent
 *
 * Contenido completo de la pantalla de estadísticas de pack. Muestra el resumen de métricas,
 * el desglose por modo, la actividad reciente y el mejor rendimiento histórico.
 *
 * @param packTitle         Título del pack, mostrado como encabezado.
 * @param stats             Datos detallados del pack cargados desde el repositorio.
 * @param onHelpClick       Callback invocado al pulsar el botón de ayuda sobre las métricas.
 * @param onBackToPackClick Callback invocado al pulsar "Volver al pack".
 */
@Composable
fun PackDetailedStatsContent(
    packTitle: String,
    stats: PackDetailedStats,
    onHelpClick: () -> Unit,
    onBackToPackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = packTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = Ink950,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = strings.statsPack.packGeneralSummary,
                style = MaterialTheme.typography.titleMedium,
                color = Ink950,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PackStatsExpandedSummaryCard(
                    iconRes = UIcons.Cards.Session,
                    circleColor = Navy100,
                    iconTint = Navy500,
                    title = strings.common.sessionsStatLabel,
                    value = stats.totalSessions.toString(),
                    delta = recentSessionsDelta(stats.recentActivity, strings),
                    modifier = Modifier.weight(1f),
                )
                PackStatsExpandedSummaryCard(
                    iconRes = UIcons.Cards.Check,
                    circleColor = Teal100,
                    iconTint = Teal700,
                    title = strings.common.accuracyStatLabel,
                    value = stats.averageAccuracyPercent?.let { "$it%" } ?: "--",
                    delta = accuracyDelta(stats.recentActivity),
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PackStatsExpandedSummaryCard(
                    iconRes = UIcons.Cards.Clock,
                    circleColor = Gold100,
                    iconTint = Gold700,
                    title = strings.common.averageTimeStatLabel,
                    value = stats.averageDurationMs?.toReadableDuration() ?: "--",
                    delta = durationDelta(stats.recentActivity),
                    modifier = Modifier.weight(1f),
                )
                PackStatsExpandedSummaryCard(
                    iconRes = UIcons.Cards.Progress,
                    circleColor = Orange100,
                    iconTint = Orange700,
                    title = strings.common.progressLabel,
                    value = "${stats.progressPercent}%",
                    supporting = strings.statsPack.packQuestionsDominated(stats.dominatedQuestions, stats.totalQuestions),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        UOutlinedButton(
            text = strings.statsPack.packStatsHelpAction,
            onClick = onHelpClick,
            leadingIconRes = UIcons.Actions.Details,
            modifier = Modifier.fillMaxWidth(),
            size = UButtonSize.Compact,
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = strings.statsPack.packByMode,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Ink950,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PackStatsModeSummaryCard(
                    iconRes = UIcons.Actions.Study,
                    iconTint = Navy500,
                    title = strings.common.studyModeShort,
                    sessions = stats.studyStats.sessions,
                    supporting = stats.studyStats.accuracyPercent?.let {
                        "$it% ${strings.common.accuracyStatLabel.lowercase()}"
                    },
                    modifier = Modifier.weight(1f),
                )
                PackStatsModeSummaryCard(
                    iconRes = UIcons.Actions.Play,
                    iconTint = Purple700,
                    circleColor = Purple100,
                    title = strings.common.playModeShort,
                    sessions = stats.gameStats.sessions,
                    supporting = stats.gameStats.accuracyPercent?.let {
                        "$it% ${strings.common.accuracyStatLabel.lowercase()}"
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = strings.statsPack.packRecentActivity,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Ink950,
            )
            if (stats.recentActivity.isEmpty()) {
                Text(
                    text = strings.statsPack.packNoSessionsYet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral500,
                )
            } else {
                stats.recentActivity.take(4).forEach { activity ->
                    PackStatsRecentActivityCard(activity)
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = strings.statsPack.packBestPerformance,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Ink950,
            )
            PackStatsBestPerformanceCard(
                value = stats.bestPerformance?.toDisplayValue(strings) ?: "--",
                supporting = stats.bestPerformance?.mode.asModeLabel(strings),
                valueTint = if (stats.bestPerformance == null) Neutral500 else Teal700,
            )
        }

        UOutlinedButton(
            text = strings.common.studyBackToPack,
            onClick = onBackToPackClick,
            modifier = Modifier.fillMaxWidth(),
            size = UButtonSize.Compact,
        )
    }
}

@UPreview
@Composable
private fun PackDetailedStatsContentPreview() {
    UTheme {
        // Vista previa sin datos reales — solo verifica el layout general
        Text("PackDetailedStatsContent — use PackStatsScreenPreview for full preview")
    }
}
