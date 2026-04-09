package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.feedback.UMarkdownText
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Red500
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.study.screens.session.model.StudyOptionUiModel
import com.uquiz.android.ui.feature.study.screens.session.model.StudySavedAnswerUiModel

/**
 * ### StudyOptionCard
 *
 * Tarjeta interactiva que representa una opción de respuesta en la sesión de estudio.
 *
 * El color de fondo y el badge varían según el estado de verificación, selección y corrección.
 *
 * @param option Modelo de la opción con su etiqueta, texto en Markdown e indicador de corrección.
 * @param savedAnswer Respuesta ya guardada para la pregunta actual, o null si aún no se ha respondido.
 * @param selectedOptionId Identificador de la opción seleccionada actualmente, o null.
 * @param verified Indica si la respuesta ya ha sido verificada.
 * @param onClick Callback invocado al pulsar la opción; ignorado si la respuesta está verificada.
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
    val isCorrect = option.isCorrect
    val isPickedWrong = verified && savedAnswer?.pickedOptionId == option.optionId && !savedAnswer.isCorrect

    val cardBackground =
        when {
            verified && isCorrect -> Teal500
            isPickedWrong -> Red500
            isSelected -> Navy100
            verified -> Color.White.copy(alpha = 0.72f)
            else -> Color.White
        }

    // Color de fondo del badge circular con la letra
    val badgeBackground =
        when {
            verified && isCorrect -> Color.White
            isPickedWrong -> Color.White
            verified -> BrandNavy.copy(alpha = 0.5f)
            else -> BrandNavy
        }

    // Color de la letra dentro del badge
    val badgeLetterColor =
        when {
            verified && isCorrect -> Teal500
            isPickedWrong -> Red500
            else -> Color.White
        }

    val contentColor =
        when {
            verified && isCorrect -> Color.White
            isPickedWrong -> Color.White
            else -> Ink950
        }

    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(AppRadius),
            color = cardBackground,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(32.dp)
                            .background(color = badgeBackground, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.titleSmall,
                        color = badgeLetterColor,
                        fontWeight = FontWeight.Bold,
                    )
                }
                UMarkdownText(
                    markdown = option.markdownText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        // Sin esto, AndroidView (UMarkdownText) intercepta los MotionEvents antes que Compose.
        if (!verified) {
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick,
                        ),
            )
        }
    }
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
