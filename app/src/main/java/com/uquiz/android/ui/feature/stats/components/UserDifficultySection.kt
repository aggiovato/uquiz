package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.stats.projection.UserDifficultyStats
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserDifficultySection
 *
 * Panel de estadísticas por nivel de dificultad. Muestra una fila por cada nivel
 * con el nombre, número de preguntas respondidas, porcentaje de acierto y tiempo
 * medio. Muestra texto vacío si no hay datos disponibles.
 *
 * @param rows Lista de estadísticas por nivel de dificultad.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserDifficultySection(
    rows: List<UserDifficultyStats>,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    StatsPanel(title = strings.statsUser.difficultyPerformance, modifier = modifier) {
        if (rows.isEmpty()) {
            StatsEmptyText(text = strings.statsUser.noData)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                rows.forEach { row ->
                    DifficultyRow(
                        row = row,
                        difficultyLabel = row.difficulty.label(strings),
                        answeredLabel = strings.statsUser.answeredQuestions,
                    )
                }
            }
        }
    }
}

private fun DifficultyLevel.label(strings: AppStrings): String = when (this) {
    DifficultyLevel.EASY -> strings.common.difficultyEasy
    DifficultyLevel.MEDIUM -> strings.common.difficultyMedium
    DifficultyLevel.HARD -> strings.common.difficultyHard
    DifficultyLevel.EXPERT -> strings.common.difficultyExpert
}

@UPreview
@Composable
private fun UserDifficultySectionPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserDifficultySection(
            rows = listOf(
                UserDifficultyStats(DifficultyLevel.EASY, 180, 91, 11_000L),
                UserDifficultyStats(DifficultyLevel.MEDIUM, 260, 78, 17_000L),
                UserDifficultyStats(DifficultyLevel.HARD, 120, 61, 26_000L),
            ),
            strings = strings,
        )
    }
}
