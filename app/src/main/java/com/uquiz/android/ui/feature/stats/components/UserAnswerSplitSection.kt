package com.uquiz.android.ui.feature.stats.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uquiz.android.domain.stats.projection.UserAnswerSplit
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserAnswerSplitSection
 *
 * Panel que muestra la distribución de respuestas correctas e incorrectas mediante
 * un gráfico de dona. La leyenda y los porcentajes se muestran dentro del propio donut.
 *
 * @param split Conteo acumulado de respuestas correctas e incorrectas.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserAnswerSplitSection(
    split: UserAnswerSplit,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    StatsPanel(title = strings.statsUser.answerSplit, modifier = modifier) {
        AnswerSplitDonut(
            split = split,
            accuracyLabel = strings.common.accuracyStatLabel,
            correctLabel = strings.common.studyCorrectAnswersLabel,
            incorrectLabel = strings.common.studyIncorrectAnswersLabel,
        )
    }
}

@UPreview
@Composable
private fun UserAnswerSplitSectionPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserAnswerSplitSection(
            split = UserAnswerSplit(correctAnswers = 484, incorrectAnswers = 136),
            strings = strings,
        )
    }
}
