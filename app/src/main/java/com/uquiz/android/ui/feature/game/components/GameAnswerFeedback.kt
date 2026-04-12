package com.uquiz.android.ui.feature.game.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Red500
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### GameAnswerFeedback
 *
 * Etiqueta animada que aparece tras seleccionar una opción en Game mode. Muestra
 * el resultado puntual de la respuesta (acierto, fallo o tiempo agotado) con una
 * entrada de escala+fundido y una salida equivalente.
 *
 * @param visible     Si la etiqueta debe mostrarse.
 * @param text        Texto a mostrar (p. ej. "+12 pts", "−5 pts", "¡Tiempo!").
 * @param isPositive  Si el resultado es favorable (verde) o desfavorable (rojo).
 */
@Composable
fun GameAnswerFeedback(
    visible: Boolean,
    text: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (isPositive) Teal500 else Red500

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = scaleIn(
            initialScale = 0.72f,
            animationSpec = tween(durationMillis = 200),
        ) + fadeIn(animationSpec = tween(durationMillis = 180)),
        exit = scaleOut(
            targetScale = 0.88f,
            animationSpec = tween(durationMillis = 160),
        ) + fadeOut(animationSpec = tween(durationMillis = 140)),
    ) {
        Box(
            modifier = Modifier
                .background(bgColor, RoundedCornerShape(24.dp))
                .padding(horizontal = 18.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}

@UPreview
@Composable
private fun GameAnswerFeedbackCorrectPreview() {
    UTheme {
        GameAnswerFeedback(
            visible = true,
            text = "+12 pts",
            isPositive = true,
        )
    }
}

@UPreview
@Composable
private fun GameAnswerFeedbackIncorrectPreview() {
    UTheme {
        GameAnswerFeedback(
            visible = true,
            text = "−8 pts",
            isPositive = false,
        )
    }
}
