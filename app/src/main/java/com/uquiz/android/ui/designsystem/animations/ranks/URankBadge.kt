package com.uquiz.android.ui.designsystem.animations.ranks

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * ### URankBadge
 *
 * Animador interno del badge de rango. Recibe el drawable ya resuelto y aplica
 * las animaciones de flotación, pulso y shimmer.
 *
 * Este componente es `internal` — solo debe ser invocado desde [UMascot].
 *
 * @param badge Recurso drawable del label/badge a animar.
 * @param badgeSize Tamaño base del badge.
 */
@Composable
internal fun URankBadge(
    @DrawableRes badge: Int,
    badgeSize: Dp = 100.dp,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "u_rank_badge")
    val floatY =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = -3f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 2600
                            0f at 0
                            (-3f) at 1300 using FastOutSlowInEasing
                            0f at 2600 using FastOutSlowInEasing
                        },
                    repeatMode = RepeatMode.Restart,
                ),
            label = "u_rank_badge_float",
        )
    val pulse =
        transition.animateFloat(
            initialValue = 0.96f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 2600
                            0.96f at 0
                            1f at 1300 using FastOutSlowInEasing
                            0.96f at 2600 using FastOutSlowInEasing
                        },
                    repeatMode = RepeatMode.Restart,
                ),
            label = "u_rank_badge_pulse",
        )
    val shimmer =
        transition.animateFloat(
            initialValue = -0.4f,
            targetValue = 1.4f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 4200
                            (-0.4f) at 0
                            (-0.4f) at 2200
                            1.4f at 4200 using FastOutSlowInEasing
                        },
                    repeatMode = RepeatMode.Restart,
                ),
            label = "u_rank_badge_shimmer",
        )

    Box(
        modifier = modifier.height(badgeSize).clipToBounds(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            painter = painterResource(badge),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier =
                Modifier
                    .offset(y = 0.dp)
                    .size(badgeSize)
                    .graphicsLayer {
                        translationY = floatY.value
                        scaleX = pulse.value
                        scaleY = pulse.value
                        compositingStrategy = CompositingStrategy.Offscreen
                    }.drawWithContent {
                        drawContent()
                        val startX = size.width * (shimmer.value - 0.28f)
                        val endX = size.width * shimmer.value
                        drawRect(
                            brush =
                                Brush.linearGradient(
                                    colors =
                                        listOf(
                                            Color.Transparent,
                                            Color.White.copy(alpha = 0.32f),
                                            Color.Transparent,
                                        ),
                                    start =
                                        androidx.compose.ui.geometry
                                            .Offset(startX, 0f),
                                    end =
                                        androidx.compose.ui.geometry
                                            .Offset(endX, size.height),
                                ),
                            blendMode = BlendMode.SrcAtop,
                        )
                    },
        )
    }
}
