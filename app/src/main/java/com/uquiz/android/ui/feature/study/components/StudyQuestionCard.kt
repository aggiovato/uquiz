package com.uquiz.android.ui.feature.study.components

import androidx.compose.runtime.Composable
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.study.screens.session.model.StudyQuestionUiModel
import com.uquiz.android.ui.feature.study.utils.mapStudyDifficulty
import com.uquiz.android.ui.shared.components.studygame.QuizQuestionCard

/**
 * ### StudyQuestionCard
 *
 * Tarjeta que muestra el texto de una pregunta de estudio. Delegado en [QuizQuestionCard].
 *
 * El chip de dificultad usa [mapStudyDifficulty] para colapsar EXPERT a HARD,
 * ya que el chip no tiene variante EXPERT en el modo estudio.
 *
 * @param question Modelo de UI con el texto en Markdown y la dificultad de la pregunta.
 */
@Composable
fun StudyQuestionCard(question: StudyQuestionUiModel) {
    QuizQuestionCard(
        markdownText = question.markdownText,
        // EXPERT se colapsa a HARD porque el chip visual no tiene variante EXPERT en estudio.
        difficulty = mapStudyDifficulty(question.difficulty),
    )
}

@UPreview
@Composable
private fun StudyQuestionCardPreview() {
    UTheme {
        StudyQuestionCard(
            question = StudyQuestionUiModel(
                questionId = "q1",
                markdownText = "¿En qué año comenzó la **Primera Guerra Mundial**?" +
                    "\n\nFue un conflicto de escala global que involucró a las principales potencias europeas.",
                explanationMarkdown = "Comenzó en 1914 con el asesinato del archiduque Francisco Fernando.",
                difficulty = DifficultyLevel.MEDIUM,
                options = emptyList(),
            ),
        )
    }
}
