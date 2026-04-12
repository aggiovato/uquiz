package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.study.screens.session.model.StudyOptionUiModel
import com.uquiz.android.ui.feature.study.screens.session.model.StudySavedAnswerUiModel
import com.uquiz.android.ui.shared.components.studygame.QuizOptionCard
import com.uquiz.android.ui.shared.components.studygame.QuizOptionState

/**
 * ### StudyOptionCard
 *
 * Tarjeta interactiva de opción de respuesta para el modo estudio.
 * Delegado en [QuizOptionCard] tras mapear los parámetros de estudio a [QuizOptionState].
 *
 * @param option          Modelo de la opción con su etiqueta, texto e indicador de corrección.
 * @param savedAnswer     Respuesta guardada para la pregunta actual, o null si aún no se respondió.
 * @param selectedOptionId Identificador de la opción seleccionada actualmente, o null.
 * @param verified        Indica si la respuesta ya ha sido verificada.
 * @param onClick         Callback invocado al pulsar la opción; ignorado si está verificada.
 */
@Composable
fun StudyOptionCard(
    option: StudyOptionUiModel,
    savedAnswer: StudySavedAnswerUiModel?,
    selectedOptionId: String?,
    verified: Boolean,
    onClick: () -> Unit,
) {
    val isSelected = option.optionId == selectedOptionId
    val isPickedWrong = verified && savedAnswer?.pickedOptionId == option.optionId && !savedAnswer.isCorrect

    val state = when {
        verified && option.isCorrect -> QuizOptionState.VerifiedCorrect
        isPickedWrong -> QuizOptionState.VerifiedWrongPicked
        verified -> QuizOptionState.VerifiedOther
        isSelected -> QuizOptionState.Selected
        else -> QuizOptionState.Default
    }

    QuizOptionCard(
        label = option.label,
        markdownText = option.markdownText,
        state = state,
        onClick = onClick,
    )
}

@UPreview
@Composable
private fun StudyOptionCardPreview() {
    UTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StudyOptionCard(
                option = StudyOptionUiModel("o1", "A", "Opción sin seleccionar", false),
                savedAnswer = null,
                selectedOptionId = null,
                verified = false,
                onClick = {},
            )
            StudyOptionCard(
                option = StudyOptionUiModel("o2", "B", "Opción **seleccionada**", false),
                savedAnswer = null,
                selectedOptionId = "o2",
                verified = false,
                onClick = {},
            )
            StudyOptionCard(
                option = StudyOptionUiModel("o3", "C", "Respuesta correcta verificada", true),
                savedAnswer = StudySavedAnswerUiModel("o3", true, 1200L),
                selectedOptionId = "o3",
                verified = true,
                onClick = {},
            )
        }
    }
}
