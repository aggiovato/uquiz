package com.uquiz.android.ui.designsystem.animations.ranks

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import kotlinx.coroutines.launch

/**
 * ### UInitiateMascot
 *
 * Mascota animada del sistema de ranking. Flota y oscila en bucle. Al pulsarla salta
 * y abre la boca con una transición fluida de smile → open que simula una mandíbula.
 * [UInitiateMascot] corresponde al rank de [com.uquiz.android.domain.ranking.enums.UserRank.INITIATE]
 *
 * @param size Tamaño total del componente.
 */
@Composable
fun UInitiateMascot(
    modifier: Modifier = Modifier,
    size: Dp = 144.dp,
) {
    val jumpOffset = remember { Animatable(0f) }
    val mouthOpenProgress = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    val transition = rememberInfiniteTransition(label = "u_node_idle")

    val sway =
        transition.animateFloat(
            initialValue = -2.6f,
            targetValue = 2.6f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 4200
                            (-2.6f) at 0 using FastOutSlowInEasing
                            2.1f at 1100 using FastOutSlowInEasing
                            (-1.2f) at 2200 using FastOutSlowInEasing
                            2.6f at 3250 using FastOutSlowInEasing
                            (-2.6f) at 4200
                        },
                    repeatMode = RepeatMode.Restart,
                ),
            label = "u_node_sway",
        )
    val floatY =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = -5f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 3600
                            0f at 0
                            (-5f) at 1800 using FastOutSlowInEasing
                            0f at 3600 using FastOutSlowInEasing
                        },
                ),
            label = "u_node_float",
        )
    val stemLeft =
        transition.animateFloat(
            initialValue = -8f,
            targetValue = 6f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 2800
                            (-8f) at 0
                            6f at 1400 using FastOutSlowInEasing
                            (-8f) at 2800 using FastOutSlowInEasing
                        },
                ),
            label = "u_node_stem_left",
        )
    val stemBottom =
        transition.animateFloat(
            initialValue = 5f,
            targetValue = -5f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 2600
                            5f at 0
                            (-5f) at 1300 using FastOutSlowInEasing
                            5f at 2600 using FastOutSlowInEasing
                        },
                ),
            label = "u_node_stem_bottom",
        )
    val stemRight =
        transition.animateFloat(
            initialValue = 7f,
            targetValue = -7f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 3000
                            7f at 0
                            (-7f) at 1500 using FastOutSlowInEasing
                            7f at 3000 using FastOutSlowInEasing
                        },
                ),
            label = "u_node_stem_right",
        )
    val blinkScale =
        transition.animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 4600
                            1f at 0
                            1f at 1650
                            0.12f at 1780 using LinearEasing
                            1f at 1920 using LinearEasing
                            1f at 3320
                            0.18f at 3450 using LinearEasing
                            1f at 3580 using LinearEasing
                            1f at 4600
                        },
                ),
            label = "u_node_blink",
        )
    val dotsAlpha =
        transition.animateFloat(
            initialValue = 0.42f,
            targetValue = 0.82f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 3200
                            0.42f at 0
                            0.82f at 1200 using FastOutSlowInEasing
                            0.5f at 2400 using FastOutSlowInEasing
                            0.68f at 3200
                        },
                ),
            label = "u_node_dots_alpha",
        )
    val dotsScale =
        transition.animateFloat(
            initialValue = 0.98f,
            targetValue = 1.02f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 3200
                            0.98f at 0
                            1.02f at 1600 using FastOutSlowInEasing
                            0.98f at 3200 using FastOutSlowInEasing
                        },
                ),
            label = "u_node_dots_scale",
        )

    // Crossfade directo: smile desaparece mientras open aparece, sin huecos ni saltos.
    val p = mouthOpenProgress.value
    val smileAlpha = 1f - p
    val openAlpha = p
    // Ligero "pop" al abrir: escala de 0.88 → 1 con pivote central para no desplazar la posición.
    val openScaleY = 0.88f + openAlpha * 0.12f

    Box(
        modifier =
            modifier
                .size(size)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) {
                    // Salto
                    scope.launch {
                        jumpOffset.stop()
                        jumpOffset.snapTo(0f)
                        jumpOffset.animateTo(
                            targetValue = -18f,
                            animationSpec =
                                keyframes {
                                    durationMillis = 180
                                    (-18f) at 180 using FastOutSlowInEasing
                                },
                        )
                        jumpOffset.animateTo(
                            targetValue = 0f,
                            animationSpec =
                                keyframes {
                                    durationMillis = 240
                                    4f at 120 using FastOutSlowInEasing
                                    0f at 240 using FastOutSlowInEasing
                                },
                        )
                    }
                    // Boca: smile → open → smile
                    scope.launch {
                        if (mouthOpenProgress.value > 0f) return@launch
                        mouthOpenProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
                        )
                        kotlinx.coroutines.delay(320)
                        mouthOpenProgress.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
                        )
                    }
                },
        contentAlignment = Alignment.Center,
    ) {
        LayeredAsset(
            painter = painterResource(R.drawable.u_node_dots),
            modifier =
                Modifier
                    .fillMaxSize()
                    .offset(x = (-10).dp, y = (-20).dp)
                    .graphicsLayer {
                        alpha = dotsAlpha.value
                        scaleX = dotsScale.value
                        scaleY = dotsScale.value
                    },
        )

        Box(
            modifier =
                Modifier
                    .size(size * 0.72f)
                    .graphicsLayer {
                        rotationZ = sway.value
                        translationY = floatY.value + jumpOffset.value
                    },
            contentAlignment = Alignment.Center,
        ) {
            LayeredAsset(
                painter = painterResource(R.drawable.u_node_stem1),
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .offset { IntOffset((3).dp.roundToPx(), (-6.5).dp.roundToPx()) }
                        .size(size * 0.22f)
                        .rotate(-25f)
                        .graphicsLayer {
                            rotationZ = stemLeft.value
                            transformOrigin = TransformOrigin(0.82f, 0.92f)
                        },
            )
            LayeredAsset(
                painter = painterResource(R.drawable.u_node_stem2),
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .offset { IntOffset((-16).dp.roundToPx(), (-3).dp.roundToPx()) }
                        .size(size * 0.28f)
                        .graphicsLayer {
                            rotationZ = stemBottom.value
                            transformOrigin = TransformOrigin(0.9f, 0.15f)
                        },
            )
            LayeredAsset(
                painter = painterResource(R.drawable.u_node_stem3),
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset { IntOffset(5.dp.roundToPx(), (-18).dp.roundToPx()) }
                        .size(size * 0.30f)
                        .graphicsLayer {
                            rotationZ = stemRight.value
                            transformOrigin = TransformOrigin(0.2f, 0.88f)
                        },
            )
            LayeredAsset(
                painter = painterResource(R.drawable.u_node_face),
                modifier =
                    Modifier
                        .size(size * 0.66f)
                        .align(Alignment.Center),
            )
            LayeredAsset(
                painter = painterResource(R.drawable.u_node_eyes),
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(y = (-12).dp)
                        .size(size * 0.34f)
                        .graphicsLayer {
                            scaleY = blinkScale.value
                            transformOrigin = TransformOrigin(0.5f, 0.52f)
                        },
            )

            // Smile — se desvanece al abrir la boca.
            LayeredAsset(
                painter = painterResource(R.drawable.u_node_smile_mouth),
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(x = 2.dp, y = 7.5.dp)
                        .rotate(-2f)
                        .size(size * 0.20f)
                        .graphicsLayer {
                            alpha = smileAlpha
                        },
            )
            // Boca abierta — aparece con un leve "pop" desde el centro sin desplazar la posición.
            LayeredAsset(
                painter = painterResource(R.drawable.u_node_open_mouth),
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(x = 2.dp, y = 7.5.dp)
                        .rotate(-2f)
                        .size(size * 0.20f)
                        .graphicsLayer {
                            alpha = openAlpha
                            scaleY = openScaleY
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        },
            )

            LayeredAsset(
                painter = painterResource(R.drawable.u_node_check),
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(x = (-25).dp, y = 8.dp)
                        .size(size * 0.10f)
                        .graphicsLayer {
                            rotationZ = lerp(-5f, 4f, (sway.value + 2.6f) / 5.2f)
                        },
            )
        }
    }
}

@Composable
private fun LayeredAsset(
    painter: Painter,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

private operator fun Dp.times(factor: Float): Dp = (value * factor).dp

@UPreview
@Composable
private fun UInitiateMascotPreview() {
    UTheme {
        UInitiateMascot()
    }
}
