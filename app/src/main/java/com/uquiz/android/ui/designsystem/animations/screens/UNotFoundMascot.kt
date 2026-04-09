package com.uquiz.android.ui.designsystem.animations.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UNotFoundMascot
 *
 * Mascota animada para estados de búsqueda sin resultados. El cuerpo flota y oscila en bucle
 * con la misma cadencia que UInitiateMascot. La lupa tiene un bob independiente más lento.
 * En la escena inferior flotan las barras de búsqueda, el polvo y la cookie decorativa.
 * El conjunto lleva un tono neutro/marca de agua para indicar visualmente la ausencia de contenido.
 *
 * @param size Tamaño base del área de la mascota. La escena inferior escala proporcionalmente.
 */
@Composable
fun UNotFoundMascot(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
) {
    val transition = rememberInfiniteTransition(label = "u_not_found_idle")
    // Filtro neutro: saturación reducida para efecto marca de agua
    val neutralColorFilter =
        remember {
            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.18f) })
        }

    // Flotación vertical del cuerpo
    val floatY =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = -8f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 3600
                            0f at 0
                            (-8f) at 1800 using FastOutSlowInEasing
                            0f at 3600 using FastOutSlowInEasing
                        },
                ),
            label = "u_not_found_float",
        )

    // Oscilación lateral del cuerpo
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
            label = "u_not_found_sway",
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
            label = "u_not_found_stem_left",
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
            label = "u_not_found_stem_bottom",
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
            label = "u_not_found_stem_right",
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
            label = "u_not_found_blink",
        )

    // Bob independiente de la lupa: ciclo más corto y amplitud menor que el cuerpo
    val loupeFloat =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = -5f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 2900
                            0f at 0
                            (-5f) at 1450 using FastOutSlowInEasing
                            0f at 2900 using FastOutSlowInEasing
                        },
                ),
            label = "u_not_found_loupe_float",
        )

    // Flotación de las barras de búsqueda, desfasada respecto al cuerpo
    val searchFloat =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = -4f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 4400
                            0f at 0
                            (-4f) at 2200 using FastOutSlowInEasing
                            0f at 4400 using FastOutSlowInEasing
                        },
                ),
            label = "u_not_found_search_float",
        )

    // Deriva horizontal del polvo
    val dustDrift =
        transition.animateFloat(
            initialValue = -2f,
            targetValue = 2f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 5200
                            (-2f) at 0
                            2f at 2600 using FastOutSlowInEasing
                            (-2f) at 5200 using FastOutSlowInEasing
                        },
                ),
            label = "u_not_found_dust_drift",
        )

    // Flotación de la cookie decorativa
    val cookieFloat =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = -4f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 3800
                            0f at 0
                            (-4f) at 1900 using FastOutSlowInEasing
                            0f at 3800 using FastOutSlowInEasing
                        },
                ),
            label = "u_not_found_cookie_float",
        )

    Column(
        modifier =
            modifier
                .graphicsLayer { alpha = 0.72f },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Área de la mascota
        Box(
            modifier =
                Modifier
                    .padding(top = 50.dp, bottom = 0.dp)
                    .size(size),
            contentAlignment = Alignment.Center,
        ) {
            // Cuerpo animado: sway + float aplicados al contenedor interno
            Box(
                modifier =
                    Modifier
                        .size(size * 0.65f)
                        .graphicsLayer {
                            rotationZ = sway.value
                            translationY = floatY.value
                        },
                contentAlignment = Alignment.Center,
            ) {
                NeutralAsset(
                    painter = painterResource(R.drawable.not_found_stem1),
                    colorFilter = neutralColorFilter,
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .offset { IntOffset(-5.dp.roundToPx(), (5).dp.roundToPx()) }
                            .size(size * 0.30f)
                            .rotate(-10f)
                            .graphicsLayer {
                                rotationZ = stemLeft.value
                                transformOrigin = TransformOrigin(0.62f, 0.72f)
                            },
                )
                NeutralAsset(
                    painter = painterResource(R.drawable.u_node_stem2),
                    colorFilter = neutralColorFilter,
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .offset { IntOffset((-24).dp.roundToPx(), (-10).dp.roundToPx()) }
                            .size(size * 0.28f)
                            .graphicsLayer {
                                rotationZ = stemBottom.value
                                transformOrigin = TransformOrigin(0.9f, 0.15f)
                            },
                )
                NeutralAsset(
                    painter = painterResource(R.drawable.u_node_stem3),
                    colorFilter = neutralColorFilter,
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .offset { IntOffset(10.dp.roundToPx(), (-20).dp.roundToPx()) }
                            .size(size * 0.30f)
                            .graphicsLayer {
                                rotationZ = stemRight.value
                                transformOrigin = TransformOrigin(0.2f, 0.88f)
                            },
                )

                // Cara base
                NeutralAsset(
                    painter = painterResource(R.drawable.u_node_face),
                    colorFilter = neutralColorFilter,
                    modifier =
                        Modifier
                            .size(size * 0.66f)
                            .align(Alignment.Center),
                )

                // Ojos con parpadeo periódico
                NeutralAsset(
                    painter = painterResource(R.drawable.u_node_eyes),
                    colorFilter = neutralColorFilter,
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

                // Cejas levantadas: expresión de búsqueda / sorpresa
                NeutralAsset(
                    painter = painterResource(R.drawable.not_found_eyebrow),
                    colorFilter = neutralColorFilter,
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .offset(x = (-18).dp, y = (-23).dp)
                            .size(size * 0.10f),
                )

                // Boca plana/neutra (sin smile ni open mouth)
                NeutralAsset(
                    painter = painterResource(R.drawable.not_found_mouth),
                    colorFilter = neutralColorFilter,
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .offset(x = (-2).dp, y = 12.dp)
                            .size(size * 0.10f),
                )

                // Lupa superpuesta en el lado derecho de la cara, con bob independiente
                NeutralAsset(
                    painter = painterResource(R.drawable.not_found_loupe),
                    colorFilter = neutralColorFilter,
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 12.dp, y = 16.dp)
                            .size(size * 0.42f)
                            .graphicsLayer {
                                translationY = loupeFloat.value
                            },
                )
            }
        }

        // Escena inferior: barras de búsqueda, polvo y cookie
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 80.dp)
                    .height(size * 0.5f),
            contentAlignment = Alignment.Center,
        ) {
            // Barras de búsqueda centradas, flotando de forma independiente
            NeutralAsset(
                painter = painterResource(R.drawable.not_found_search),
                colorFilter = neutralColorFilter,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .size(size * 0.38f)
                        .graphicsLayer {
                            translationY = searchFloat.value
                        },
            )
            // Polvo izquierdo: deriva horizontal suave
            NeutralAsset(
                painter = painterResource(R.drawable.not_found_dust),
                colorFilter = neutralColorFilter,
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = (20).dp, y = 40.dp)
                        .size(size * 0.26f)
                        .graphicsLayer {
                            translationX = dustDrift.value
                        },
            )
            // Polvo derecho: deriva en sentido contrario
            NeutralAsset(
                painter = painterResource(R.drawable.not_found_dust2),
                colorFilter = neutralColorFilter,
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-20).dp, y = 45.dp)
                        .size(size * 0.36f)
                        .graphicsLayer {
                            translationX = -dustDrift.value
                        },
            )
            // Cookie decorativa en la esquina inferior derecha
            NeutralAsset(
                painter = painterResource(R.drawable.not_found_cookie),
                colorFilter = neutralColorFilter,
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 90.dp, y = 60.dp)
                        .size(size * 0.20f)
                        .graphicsLayer {
                            translationY = cookieFloat.value
                        },
            )
        }
    }
}

@Composable
private fun NeutralAsset(
    painter: Painter,
    colorFilter: ColorFilter,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = colorFilter,
        contentScale = ContentScale.Fit,
        modifier = modifier,
    )
}

private operator fun Dp.times(factor: Float): Dp = (value * factor).dp

@UPreview
@Composable
private fun UNotFoundMascotPreview() {
    UTheme {
        UNotFoundMascot(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        )
    }
}
