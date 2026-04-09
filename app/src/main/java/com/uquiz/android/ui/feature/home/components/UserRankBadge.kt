package com.uquiz.android.ui.feature.home.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.ui.designsystem.tokens.UIcons

@Composable
fun UserRankBadge(
    rank: UserRank,
    badgeSize: Dp = 96.dp,
    modifier: Modifier = Modifier,
) {
    val topTrim = rank.topTrim()
    val visibleHeight = (badgeSize - topTrim).coerceAtLeast(40.dp)
    val transition = rememberInfiniteTransition(label = "user_rank_badge")
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
            label = "user_rank_float",
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
            label = "user_rank_pulse",
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
            label = "user_rank_shimmer",
        )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .height(visibleHeight)
                    .clipToBounds(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Image(
                painter = painterResource(rank.drawableRes()),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .offset(y = -topTrim)
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
}

private fun UserRank.topTrim(): Dp =
    when (this) {
        UserRank.ACOLYTE -> 10.dp
        else -> 0.dp
    }

private fun UserRank.drawableRes(): Int =
    when (this) {
        UserRank.INITIATE -> UIcons.Ranks.Initiate
        UserRank.NEOPHYTE -> UIcons.Ranks.Neophyte
        UserRank.ACOLYTE -> UIcons.Ranks.Acolyte
        UserRank.DISCIPLE -> UIcons.Ranks.Disciple
        UserRank.ADEPT -> UIcons.Ranks.Adept
        UserRank.VIRTUOSO -> UIcons.Ranks.Virtuoso
        UserRank.ARCHON -> UIcons.Ranks.Archon
        UserRank.PARAGON -> UIcons.Ranks.Paragon
    }
