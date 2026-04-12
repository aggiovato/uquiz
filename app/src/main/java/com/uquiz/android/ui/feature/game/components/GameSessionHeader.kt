package com.uquiz.android.ui.feature.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UDarkIconButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral300
import com.uquiz.android.ui.designsystem.tokens.Red500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### GameSessionHeader
 *
 * Encabezado de la sesión de Game mode. Muestra el botón de salida (izquierda), el nombre
 * del pack con el contador de preguntas, el temporizador y la puntuación acumulada (derecha).
 * Bajo ellos, una barra de cuenta atrás que se vacía durante la fase normal y se vuelve roja
 * en el tiempo extra.
 *
 * Fase normal (`elapsedMs < timeLimitMs`): barra verde que se vacía; tiempo muestra segundos
 * restantes en blanco.
 * Fase extra (`elapsedMs ≥ timeLimitMs`): barra roja que crece hasta [MAX_OVERTIME_MS]; tiempo
 * muestra segundos transcurridos en el extra, en rojo con prefijo "+".
 *
 * @param elapsedMs            Tiempo transcurrido desde que se mostró la pregunta actual, en ms.
 * @param timeLimitMs          Tiempo límite de la pregunta actual, en ms.
 * @param runningScore         Puntuación acumulada visible durante la sesión.
 * @param packTitle            Título del pack en curso, mostrado sobre el contador de preguntas.
 * @param currentQuestionIndex Índice base 0 de la pregunta visible (se muestra como base 1).
 * @param totalQuestions       Número total de preguntas en la sesión.
 * @param onExit               Callback invocado al pulsar el botón de salir.
 */
@Composable
fun GameSessionHeader(
    elapsedMs: Long,
    timeLimitMs: Long,
    runningScore: Int,
    packTitle: String,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val isOvertime = elapsedMs >= timeLimitMs

    // Fracción de barra: en normal se vacía de 1→0; en extra crece de 0→1
    val timerProgress = if (!isOvertime) {
        ((timeLimitMs - elapsedMs).toFloat() / timeLimitMs.toFloat()).coerceIn(0f, 1f)
    } else {
        ((elapsedMs - timeLimitMs).toFloat() / MAX_OVERTIME_MS.toFloat()).coerceIn(0f, 1f)
    }

    val barColor by animateColorAsState(
        targetValue = if (isOvertime) Red500 else Teal700,
        animationSpec = tween(durationMillis = 200),
        label = "gameSessionTimerBarColor",
    )

    val scoreColor = if (runningScore >= 0) Color.White else Red500
    val scoreText = if (runningScore >= 0) {
        "+$runningScore ${strings.common.pointsShortLabel}"
    } else {
        "$runningScore ${strings.common.pointsShortLabel}"
    }

    // Tiempo mostrado: segundos restantes en normal, segundos de extra en overtime
    val timeSeconds = if (!isOvertime) {
        ((timeLimitMs - elapsedMs) / 1000L).coerceAtLeast(0L)
    } else {
        (elapsedMs - timeLimitMs) / 1000L
    }
    val timeText = if (!isOvertime) "${timeSeconds}s" else "+${timeSeconds}s"
    val timeColor by animateColorAsState(
        targetValue = if (isOvertime) Red500 else Color.White,
        animationSpec = tween(durationMillis = 200),
        label = "gameSessionTimerTextColor",
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UDarkIconButton(
                iconRes = UIcons.Actions.Leave,
                contentDescription = strings.gameSession.gameExitTitle,
                onClick = onExit,
            )
            Spacer(Modifier.width(8.dp))

            // Título del pack + contador de preguntas
            Column(modifier = Modifier.weight(1f)) {
                if (packTitle.isNotBlank()) {
                    Text(
                        text = packTitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.60f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = "${currentQuestionIndex + 1} / $totalQuestions",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.85f),
                )
            }

            // Temporizador
            Text(
                text = timeText,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = timeColor,
            )
            Spacer(Modifier.width(14.dp))
            // Puntuación acumulada con unidad
            Text(
                text = scoreText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = scoreColor,
            )
        }

        LinearProgressIndicator(
            progress = { timerProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = barColor,
            trackColor = Neutral300.copy(alpha = 0.28f),
        )
    }
}

internal const val MAX_OVERTIME_MS = 60_000L

@UPreview
@Composable
private fun GameSessionHeaderNormalPreview() {
    UTheme {
        GameSessionHeader(
            elapsedMs = 8_000L,
            timeLimitMs = 20_000L,
            runningScore = 42,
            packTitle = "Kotlin Coroutines",
            currentQuestionIndex = 2,
            totalQuestions = 10,
            onExit = {},
        )
    }
}

@UPreview
@Composable
private fun GameSessionHeaderOvertimePreview() {
    UTheme {
        GameSessionHeader(
            elapsedMs = 23_000L,
            timeLimitMs = 20_000L,
            runningScore = -5,
            packTitle = "Historia Universal",
            currentQuestionIndex = 4,
            totalQuestions = 10,
            onExit = {},
        )
    }
}
