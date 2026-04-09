package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UDarkIconButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### StudySessionHeader
 *
 * Encabezado de la pantalla de sesión de estudio.
 *
 * Muestra el botón de salida (izquierda), el contador de pregunta y título del pack (centro),
 * el botón de explicación (derecha) y la barra de progreso.
 *
 * @param counter Texto que indica la pregunta actual sobre el total (p.ej. "3 / 10").
 * @param packTitle Título del pack que se está estudiando.
 * @param progress Fracción de progreso de la sesión, entre 0.0 y 1.0.
 * @param hasExplanation Indica si la pregunta actual tiene explicación disponible.
 * @param onExit Callback invocado al pulsar el botón de salir.
 * @param onExplanation Callback invocado al pulsar el botón de explicación.
 */
@Composable
fun StudySessionHeader(
    counter: String,
    packTitle: String,
    progress: Float,
    hasExplanation: Boolean,
    onExit: () -> Unit,
    onExplanation: () -> Unit,
) {
    val strings = LocalStrings.current
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UDarkIconButton(
                iconRes = UIcons.Actions.Leave,
                contentDescription = strings.studyExitStudy,
                onClick = onExit,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = counter,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                )
                Text(
                    text = packTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.74f),
                )
            }
            UDarkIconButton(
                iconRes = UIcons.Select.Idea,
                contentDescription = strings.studyExplanationLabel,
                onClick = onExplanation,
                enabled = hasExplanation,
            )
        }

        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = Teal700,
            trackColor = Color.White.copy(alpha = 0.16f),
        )
    }
}

@UPreview
@Composable
private fun StudySessionHeaderPreview() {
    UTheme {
        StudySessionHeader(
            counter = "3 / 10",
            packTitle = "Historia Universal",
            progress = 0.3f,
            hasExplanation = true,
            onExit = {},
            onExplanation = {},
        )
    }
}
