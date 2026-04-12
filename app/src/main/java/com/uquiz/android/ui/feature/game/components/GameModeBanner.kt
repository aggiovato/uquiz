package com.uquiz.android.ui.feature.game.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### GameModeBanner
 *
 * Banner hero del modo juego con ilustración de fondo y acceso rápido a una partida aleatoria.
 *
 * @param visible Indica si deben reproducirse las animaciones de entrada del contenido.
 * @param enabled Indica si existe al menos un pack jugable para la acción aleatoria.
 * @param onRandomPlayClick Acción al pulsar el CTA de partida aleatoria.
 */
@Composable
fun GameModeBanner(
    visible: Boolean,
    enabled: Boolean,
    onRandomPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp),
        shape = RoundedCornerShape(AppRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.game_mode_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xD9071221),
                                Color(0xA61A2433),
                                Color(0x401A2433),
                            ),
                        ),
                    ),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 120)) +
                        slideInHorizontally(tween(520, delayMillis = 120)) { -it / 4 },
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = strings.gameHome.gameBannerLetsTest,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.graphicsLayer { rotationZ = -4f },
                        )
                        Text(
                            text = strings.gameHome.gameBannerYour,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.92f),
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        Text(
                            text = strings.gameHome.gameBannerBrain,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.graphicsLayer { rotationZ = 2f },
                        )
                    }
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 240)) +
                        slideInHorizontally(tween(560, delayMillis = 240)) { -it / 5 },
                ) {
                    UDarkButton(
                        text = strings.gameHome.gameRandomPlay,
                        onClick = onRandomPlayClick,
                        size = UButtonSize.Tiny,
                        fullWidth = false,
                        enabled = enabled,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }
    }
}

@UPreview
@Composable
private fun GameModeBannerPreview() {
    UTheme {
        Box(
            modifier = Modifier
                .background(Color(0xFF071221))
                .padding(16.dp),
        ) {
            GameModeBanner(
                visible = true,
                enabled = true,
                onRandomPlayClick = {},
            )
        }
    }
}
