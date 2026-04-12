package com.uquiz.android.ui.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.domain.ranking.enums.mmrToNextRank
import com.uquiz.android.domain.ranking.enums.nextRank
import com.uquiz.android.domain.ranking.enums.progressFraction
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral200
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Orange100
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Orange900
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * ### HomeScoreProgressCard
 *
 * Card horizontal de score competitivo con progreso circular hacia el siguiente rango.
 *
 * @param score Valor entero visible del MMR competitivo.
 * @param mmr MMR exacto usado para calcular el progreso del tramo actual.
 * @param currentRank Rango actual del usuario.
 * @param visible Controla la animación de entrada y llenado del progreso.
 */
@Composable
fun HomeScoreProgressCard(
    score: Int,
    mmr: Float,
    currentRank: UserRank,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val nextRank = currentRank.nextRank()
    val pointsToNext = currentRank.mmrToNextRank(mmr)
    val progress = currentRank.progressFraction(mmr)

    AnimatedVisibility(
        visible = visible,
        enter =
            fadeIn(tween(420, delayMillis = 360)) +
                slideInVertically(tween(460, delayMillis = 360)) { it / 5 },
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(URadius * 1.5f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .border(1.dp, Neutral100, RoundedCornerShape(URadius * 1.5f))
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(44.dp)
                            .shadow(6.dp, CircleShape, clip = false, ambientColor = Orange900)
                            .background(Orange500, CircleShape)
                            .border(1.dp, Orange500.copy(alpha = 0.24f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(UIcons.Cards.Coins),
                        contentDescription = null,
                        tint = Orange100,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Column(
                    modifier = Modifier.width(84.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = score.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Navy500,
                        fontWeight = FontWeight.Black,
                    )
                    Text(
                        text = strings.home.homeScoreLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                if (nextRank == null) {
                    Text(
                        text = strings.home.homeMaxRankUnlocked,
                        style = MaterialTheme.typography.bodySmall,
                        color = Navy500.copy(alpha = 0.78f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                    ) {
                        Text(
                            text = strings.home.homeNextLevelLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = Neutral500,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                        )
                        Text(
                            text = "U-${nextRank.name}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Orange900,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                        )
                        Text(
                            text = strings.home.homePointsToUnlockShort(pointsToNext),
                            style = MaterialTheme.typography.labelSmall,
                            color = Navy500.copy(alpha = 0.72f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                HomeScoreProgressRing(
                    progress = progress,
                    currentRank = currentRank,
                    visible = visible,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun HomeScoreProgressRing(
    progress: Float,
    currentRank: UserRank,
    visible: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 58.dp,
) {
    // key(currentRank) destruye y recrea el subárbol cuando el rango cambia,
    // reseteando targetProgress a 0f para que el arco vuelva a animarse desde el inicio.
    key(currentRank) {
        var targetProgress by remember { mutableFloatStateOf(0f) }
        val animatedProgress by animateFloatAsState(
            targetValue = targetProgress,
            animationSpec = tween(durationMillis = 900, delayMillis = 480),
            label = "homeScoreProgressRing",
        )

        LaunchedEffect(progress, visible) {
            targetProgress = if (visible) progress else 0f
        }

        Box(
            modifier = modifier.size(size),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val strokeWidth = size.toPx() * 0.12f
                val inset = strokeWidth / 2f
                val arcSize = Size(this.size.width - strokeWidth, this.size.height - strokeWidth)
                val topLeft = Offset(inset, inset)

                drawArc(
                    color = Neutral200,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                if (animatedProgress > 0f) {
                    drawArc(
                        color = Orange500,
                        startAngle = 135f,
                        sweepAngle = 270f * animatedProgress,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )

                    val tipAngleDeg = 135f + 270f * animatedProgress
                    val tipAngleRad = Math.toRadians(tipAngleDeg.toDouble())
                    val radius = (this.size.width - strokeWidth) / 2f
                    val centerX = this.size.width / 2f
                    val centerY = this.size.height / 2f
                    val tipX = centerX + radius * cos(tipAngleRad).toFloat()
                    val tipY = centerY + radius * sin(tipAngleRad).toFloat()
                    drawCircle(
                        color = Color.White.copy(alpha = 0.78f),
                        radius = strokeWidth * 0.38f,
                        center = Offset(tipX, tipY),
                    )
                }
            }

            Text(
                text = "${(animatedProgress * 100f).roundToInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = Navy500,
                fontWeight = FontWeight.Black,
            )
        }
    } // key(currentRank)
}

@UPreview
@Composable
private fun HomeScoreProgressCardPreview() {
    UTheme {
        HomeScoreProgressCard(
            score = 1114,
            mmr = 1114f,
            currentRank = UserRank.DISCIPLE,
            visible = true,
            modifier = Modifier.padding(16.dp),
        )
    }
}
