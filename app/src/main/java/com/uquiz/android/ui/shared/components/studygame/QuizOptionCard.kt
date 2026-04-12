package com.uquiz.android.ui.shared.components.studygame

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

/**
 * Estado visual de una tarjeta de opción en los modos estudio y juego.
 *
 * - [Default]: sin seleccionar, interactiva.
 * - [Selected]: seleccionada pero no verificada todavía.
 * - [VerifiedCorrect]: opción correcta revelada tras verificar.
 * - [VerifiedWrongPicked]: opción incorrecta seleccionada por el usuario.
 * - [VerifiedOther]: opción no seleccionada tras verificar (ni correcta ni la elegida incorrecta).
 */
sealed interface QuizOptionState {
    data object Default : QuizOptionState
    data object Selected : QuizOptionState
    data object VerifiedCorrect : QuizOptionState
    data object VerifiedWrongPicked : QuizOptionState
    data object VerifiedOther : QuizOptionState
}

/**
 * ### QuizOptionCard
 *
 * Tarjeta interactiva compartida entre el modo estudio y el modo juego que representa
 * una opción de respuesta.
 *
 * El color de fondo, el badge y el texto varían según el [state] recibido. El click
 * solo está activo en los estados no verificados ([QuizOptionState.Default] y [QuizOptionState.Selected]).
 *
 * @param label         Letra de la opción ("A", "B", "C", "D").
 * @param markdownText  Texto de la opción en formato Markdown.
 * @param state         Estado visual actual de la tarjeta.
 * @param onClick       Callback invocado al pulsar la opción; ignorado en estados verificados.
 */
@Composable
fun QuizOptionCard(
    label: String,
    markdownText: String,
    state: QuizOptionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isVerified = state is QuizOptionState.VerifiedCorrect ||
        state is QuizOptionState.VerifiedWrongPicked ||
        state is QuizOptionState.VerifiedOther

    val cardBackground = when (state) {
        QuizOptionState.VerifiedCorrect -> Teal500
        QuizOptionState.VerifiedWrongPicked -> Red500
        QuizOptionState.Selected -> Navy100
        QuizOptionState.VerifiedOther -> Color.White.copy(alpha = 0.72f)
        QuizOptionState.Default -> Color.White
    }

    // Color de fondo del badge circular con la letra
    val badgeBackground = when (state) {
        QuizOptionState.VerifiedCorrect -> Color.White
        QuizOptionState.VerifiedWrongPicked -> Color.White
        QuizOptionState.VerifiedOther -> BrandNavy.copy(alpha = 0.5f)
        else -> BrandNavy
    }

    // Color de la letra dentro del badge
    val badgeLetterColor = when (state) {
        QuizOptionState.VerifiedCorrect -> Teal500
        QuizOptionState.VerifiedWrongPicked -> Red500
        else -> Color.White
    }

    val contentColor = when (state) {
        QuizOptionState.VerifiedCorrect -> Color.White
        QuizOptionState.VerifiedWrongPicked -> Color.White
        else -> Ink950
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(AppRadius),
            color = cardBackground,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = badgeBackground, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        color = badgeLetterColor,
                        fontWeight = FontWeight.Bold,
                    )
                }
                UMarkdownText(
                    markdown = markdownText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        // Sin esto, AndroidView (UMarkdownText) intercepta los MotionEvents antes que Compose.
        if (!isVerified) {
            Box(
                modifier = Modifier
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
private fun QuizOptionCardPreview() {
    UTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            QuizOptionCard(
                label = "A",
                markdownText = "Opción sin seleccionar",
                state = QuizOptionState.Default,
                onClick = {},
            )
            QuizOptionCard(
                label = "B",
                markdownText = "Opción **seleccionada**",
                state = QuizOptionState.Selected,
                onClick = {},
            )
            QuizOptionCard(
                label = "C",
                markdownText = "Respuesta correcta verificada",
                state = QuizOptionState.VerifiedCorrect,
                onClick = {},
            )
            QuizOptionCard(
                label = "D",
                markdownText = "Respuesta incorrecta elegida",
                state = QuizOptionState.VerifiedWrongPicked,
                onClick = {},
            )
        }
    }
}
