package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.UserStatsSummary
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Gold100
import com.uquiz.android.ui.designsystem.tokens.Gold500
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Orange100
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Purple100
import com.uquiz.android.ui.designsystem.tokens.Purple500
import com.uquiz.android.ui.designsystem.tokens.Teal100
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.stats.utils.toReadableDuration
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserSummaryGrid
 *
 * Panel de resumen global del usuario. Muestra seis métricas clave en tres filas de dos
 * columnas: sesiones, preguntas, acierto, tiempo total, tiempo medio de respuesta y
 * progreso de packs.
 *
 * @param summary Datos de resumen agregado del usuario.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserSummaryGrid(
    summary: UserStatsSummary,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    StatsPanel(title = strings.statsUser.title, modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CompactStatRow(
                    iconRes = UIcons.Cards.Session,
                    iconBg = Navy100,
                    iconTint = Navy500,
                    value = summary.totalSessions.toString(),
                    label = strings.common.sessionsStatLabel,
                    modifier = Modifier.weight(1f),
                )
                CompactStatRow(
                    iconRes = UIcons.Cards.Question,
                    iconBg = Purple100,
                    iconTint = Purple500,
                    value = summary.answeredQuestions.toString(),
                    label = strings.statsUser.answeredQuestions,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CompactStatRow(
                    iconRes = UIcons.Cards.Check,
                    iconBg = Teal100,
                    iconTint = Teal500,
                    value = summary.accuracyPercent.percentLabel(),
                    label = strings.common.accuracyStatLabel,
                    modifier = Modifier.weight(1f),
                )
                CompactStatRow(
                    iconRes = UIcons.Cards.Clock,
                    iconBg = Orange100,
                    iconTint = Orange500,
                    value = summary.totalStudyTimeMs.toReadableDuration(),
                    label = strings.statsUser.totalTime,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CompactStatRow(
                    iconRes = UIcons.Cards.Clock,
                    iconBg = Gold100,
                    iconTint = Gold500,
                    value = summary.averageAnswerTimeMs?.toReadableDuration() ?: "--",
                    label = strings.statsUser.averageAnswerTime,
                    modifier = Modifier.weight(1f),
                )
                CompactStatRow(
                    iconRes = UIcons.Cards.Progress,
                    iconBg = Navy100,
                    iconTint = Navy500,
                    value = "${summary.completedPacks}/${summary.completedPacks + summary.inProgressPacks}",
                    label = strings.statsUser.packsProgress,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private fun Int?.percentLabel(): String = this?.let { "$it%" } ?: "--"

@UPreview
@Composable
private fun UserSummaryGridPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserSummaryGrid(
            summary = UserStatsSummary(
                totalSessions = 42,
                answeredQuestions = 620,
                accuracyPercent = 78,
                totalStudyTimeMs = 12_400_000L,
                averageAnswerTimeMs = 18_000L,
                completedPacks = 3,
                inProgressPacks = 5,
            ),
            strings = strings,
        )
    }
}
