package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.UserQuestionInsight
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.question.components.PreviewMarkdownDialog
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserQuestionInsightsSection
 *
 * Sección de insights de preguntas destacadas. Muestra dos tarjetas en paralelo con colores
 * sólidos: la pregunta respondida más rápido y la pregunta más fallada. Al pulsar el botón
 * de previsualización se abre un diálogo con el enunciado completo.
 *
 * @param fastestQuestion Datos de la pregunta respondida más rápido, o `null` si no hay datos.
 * @param mostFailedQuestion Datos de la pregunta más fallada, o `null` si no hay datos.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserQuestionInsightsSection(
    fastestQuestion: UserQuestionInsight?,
    mostFailedQuestion: UserQuestionInsight?,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    var previewInsight by remember { mutableStateOf<UserQuestionInsight?>(null) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeader(title = strings.statsUser.questionInsights)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            QuestionInsightCard(
                title = strings.statsUser.fastestQuestion,
                insight = fastestQuestion,
                tone = Teal500,
                iconRes = UIcons.Cards.Clock,
                noDataText = strings.statsUser.noData,
                previewLabel = strings.common.previewLabel,
                onPreview = { fastestQuestion?.let { previewInsight = it } },
                modifier = Modifier.weight(1f),
            )
            QuestionInsightCard(
                title = strings.statsUser.mostFailedQuestion,
                insight = mostFailedQuestion,
                tone = Orange500,
                iconRes = UIcons.Feedback.Error,
                noDataText = strings.statsUser.noData,
                previewLabel = strings.common.previewLabel,
                onPreview = { mostFailedQuestion?.let { previewInsight = it } },
                modifier = Modifier.weight(1f),
            )
        }
    }

    previewInsight?.let { insight ->
        PreviewMarkdownDialog(
            title = strings.common.questionPreview,
            markdown = insight.questionText,
            onDismiss = { previewInsight = null },
        )
    }
}

@UPreview
@Composable
private fun UserQuestionInsightsSectionPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserQuestionInsightsSection(
            fastestQuestion = UserQuestionInsight("q1", "¿Qué keyword declara una clase en Kotlin?", "4s"),
            mostFailedQuestion = UserQuestionInsight("q2", "Explica cuándo usar rememberSaveable.", "6x"),
            strings = strings,
        )
    }
}
