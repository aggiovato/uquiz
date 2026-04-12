package com.uquiz.android.ui.feature.game.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.domain.ranking.enums.progressFraction
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy200
import com.uquiz.android.ui.designsystem.tokens.Neutral300
import com.uquiz.android.ui.designsystem.tokens.Red500
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings
import kotlin.math.cos
import kotlin.math.sin

/**
 * ### GameCircularRankProgress
 *
 * Arco circular animado que representa el progreso del usuario dentro del tramo
 * de rango actual. Muestra dentro del arco: nombre del rango, XP ganada, score
 * de sesión y su etiqueta.
 *
 * La animación del arco arranca desde 0 al entrar en composición, disparada via
 * [LaunchedEffect], para que sea visible incluso cuando el componente entra dentro
 * de un [AnimatedVisibility].
 *
 * @param currentRank  Rango actual del usuario.
 * @param previousMmr  MMR antes de la sesión — posición de inicio de la animación del arco.
 * @param mmr          MMR tras la sesión — posición final de la animación del arco.
 * @param xpGained     XP ganada en la sesión, mostrada bajo el nombre del rango.
 * @param sessionScore Score total de la sesión, mostrado dentro del círculo.
 * @param size         Diámetro del componente.
 */
@Composable
fun GameCircularRankProgress(
    currentRank: UserRank,
    previousMmr: Float,
    mmr: Float,
    xpGained: Long,
    sessionScore: Int,
    modifier: Modifier = Modifier,
    size: Dp = 190.dp,
) {
    val strings = LocalStrings.current
    val startFraction = currentRank.progressFraction(previousMmr)
    val endFraction = currentRank.progressFraction(mmr)

    // Arranca en la fracción previa y anima hasta la fracción actual.
    var animTarget by remember { mutableFloatStateOf(startFraction) }
    val animatedFraction by animateFloatAsState(
        targetValue = animTarget,
        animationSpec = tween(durationMillis = 1_200),
        label = "gameCircularRankProgressFraction",
    )
    LaunchedEffect(endFraction) {
        animTarget = endFraction
    }

    val scoreColor = if (sessionScore >= 0) Color.White else Red500
    val scoreText = if (sessionScore >= 0) "+$sessionScore" else "$sessionScore"

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = size.toPx() * 0.09f
            val inset = strokeWidth / 2f
            val arcSize = Size(this.size.width - strokeWidth, this.size.height - strokeWidth)
            val topLeft = Offset(inset, inset)

            // Arco de pista (fondo gris)
            drawArc(
                color = Neutral300.copy(alpha = 0.28f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )

            // Arco de progreso (teal)
            if (animatedFraction > 0f) {
                drawArc(
                    color = Teal500,
                    startAngle = 135f,
                    sweepAngle = 270f * animatedFraction,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                // Brillo en la punta del arco
                val tipAngleDeg = 135f + 270f * animatedFraction
                val tipAngleRad = Math.toRadians(tipAngleDeg.toDouble())
                val radius = (this.size.width - strokeWidth) / 2f
                val centerX = this.size.width / 2f
                val centerY = this.size.height / 2f
                val tipX = centerX + radius * cos(tipAngleRad).toFloat()
                val tipY = centerY + radius * sin(tipAngleRad).toFloat()
                drawCircle(
                    color = Color.White.copy(alpha = 0.55f),
                    radius = strokeWidth * 0.42f,
                    center = Offset(tipX, tipY),
                )
            }
        }

        // Contenido interior: rango, XP ganada, score de sesión y su etiqueta
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = currentRank.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Navy200,
            )
            if (xpGained > 0) {
                Text(
                    text = "+$xpGained XP",
                    style = MaterialTheme.typography.labelSmall,
                    color = Teal500,
                )
            }
            Text(
                text = scoreText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = scoreColor,
            )
            Text(
                text = strings.gameSummary.gameTotalScoreLabel,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.60f),
            )
        }
    }
}

@UPreview
@Composable
private fun GameCircularRankProgressPreview() {
    UTheme {
        GameCircularRankProgress(
            currentRank = UserRank.DISCIPLE,
            previousMmr = 1087f,
            mmr = 1150f,
            xpGained = 88L,
            sessionScore = 63,
        )
    }
}

@UPreview
@Composable
private fun GameCircularRankProgressNegativePreview() {
    UTheme {
        GameCircularRankProgress(
            currentRank = UserRank.ACOLYTE,
            previousMmr = 740f,
            mmr = 720f,
            xpGained = 0L,
            sessionScore = -12,
        )
    }
}
