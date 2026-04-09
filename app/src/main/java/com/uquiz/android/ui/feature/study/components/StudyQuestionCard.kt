package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.ui.designsystem.components.feedback.UMarkdownText
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.study.screens.session.model.StudyQuestionUiModel
import com.uquiz.android.ui.feature.study.utils.mapStudyDifficulty
import com.uquiz.android.ui.shared.components.chips.DifficultyChip
import com.uquiz.android.ui.shared.components.chips.DifficultyChipVariant

private val cardShape = RoundedCornerShape(AppRadius + 6.dp)

/**
 * ### StudyQuestionCard
 *
 * Tarjeta que muestra el texto de una pregunta de estudio.
 *
 * El texto admite scroll vertical si supera la altura máxima del card;
 * un degradado en la parte inferior indica visualmente que hay más contenido.
 * El chip de dificultad flota en la esquina superior derecha con rotación decorativa.
 *
 * @param question Modelo de UI con el texto en Markdown y la dificultad de la pregunta.
 */
@Composable
fun StudyQuestionCard(question: StudyQuestionUiModel) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = cardShape,
            color = Color.White,
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                ) {
                    UMarkdownText(
                        markdown = question.markdownText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Ink950,
                    )
                }
                // Degradado inferior que indica que hay más texto por ver con scroll.
                if (scrollState.canScrollForward) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(22.dp)
                            .align(Alignment.BottomCenter)
                            .clip(RoundedCornerShape(bottomStart = AppRadius + 6.dp, bottomEnd = AppRadius + 6.dp))
                            .background(
                                Brush.verticalGradient(listOf(Color.Transparent, Color.White)),
                            ),
                    )
                }
            }
        }
        DifficultyChip(
            difficulty = mapStudyDifficulty(question.difficulty),
            variant = DifficultyChipVariant.Filled,
            modifier = Modifier
                .align(Alignment.TopEnd)
                // Rotación en sentido horario, esquina más pegada al borde.
                .offset(x = 4.dp, y = (-4).dp)
                .rotate(12f),
        )
    }
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
