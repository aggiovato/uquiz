package com.uquiz.android.ui.designsystem.animations.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UEmptyMascot
 *
 * Mascota animada para estados vacíos (sin contenido todavía). Muestra el SVG completo
 * [R.drawable.empty_node] con una flotación vertical suave y un tono neutro/marca de agua
 * para indicar visualmente que la sección aún no tiene ítems.
 *
 * A diferencia de [UNotFoundMascot] (pensada para búsquedas sin resultado), esta mascota
 * se usa cuando la sección existe pero aún no se ha creado ningún contenido.
 *
 * @param size Tamaño del área cuadrada de la imagen.
 */
@Composable
fun UEmptyMascot(
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
) {
    val transition = rememberInfiniteTransition(label = "u_empty_idle")

    // Filtro neutro: saturación reducida al 18 % para efecto marca de agua
    val neutralColorFilter =
        remember {
            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.18f) })
        }

    // Flotación vertical suave: ciclo de 3 200 ms con easing
    val floatY =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = -10f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 3200
                            0f at 0
                            (-10f) at 1600 using FastOutSlowInEasing
                            0f at 3200 using FastOutSlowInEasing
                        },
                ),
            label = "u_empty_float_y",
        )

    // Oscilación lateral suave: amplitud mínima para no distraer
    val sway =
        transition.animateFloat(
            initialValue = -1.5f,
            targetValue = 1.5f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        keyframes {
                            durationMillis = 4400
                            (-1.5f) at 0
                            1.5f at 2200 using FastOutSlowInEasing
                            (-1.5f) at 4400 using FastOutSlowInEasing
                        },
                ),
            label = "u_empty_sway",
        )

    Image(
        painter = painterResource(R.drawable.empty_node),
        contentDescription = null,
        colorFilter = neutralColorFilter,
        contentScale = ContentScale.Fit,
        modifier =
            modifier
                .size(size)
                .graphicsLayer {
                    alpha = 0.72f
                    translationY = floatY.value
                    rotationZ = sway.value
                },
    )
}

@UPreview
@Composable
private fun UEmptyMascotPreview() {
    UTheme {
        UEmptyMascot(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        )
    }
}
