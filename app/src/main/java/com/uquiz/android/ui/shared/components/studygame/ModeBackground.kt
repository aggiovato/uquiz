package com.uquiz.android.ui.shared.components.studygame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### ModeBackground
 *
 * Contenedor base compartido entre el modo estudio y el modo juego.
 * Aplica el fondo oscuro `BrandNavy`, círculos decorativos semitransparentes
 * y una imagen de fondo tintada sobre la que se sitúa el contenido.
 *
 * @param contentPadding Padding interior aplicado al área de contenido.
 * @param includeStatusBarsPadding Indica si se debe añadir el padding de la barra de estado.
 * @param content Contenido visible sobre el fondo.
 */
@Composable
fun ModeBackground(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 22.dp, vertical = 24.dp),
    includeStatusBarsPadding: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandNavy),
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.TopEnd)
                .offset(x = 72.dp, y = (-72).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 96.dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape),
        )
        Image(
            painter = painterResource(R.drawable.bck_image),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Navy100.copy(alpha = 0.96f)),
            alpha = 0.12f,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .scale(2.2f)
                .rotate(20f)
                .fillMaxSize(0.92f)
                .offset(x = (-100).dp, y = 120.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (includeStatusBarsPadding) Modifier.statusBarsPadding() else Modifier)
                .padding(contentPadding),
            content = content,
        )
    }
}

@UPreview
@Composable
private fun ModeBackgroundPreview() {
    UTheme {
        ModeBackground {}
    }
}
