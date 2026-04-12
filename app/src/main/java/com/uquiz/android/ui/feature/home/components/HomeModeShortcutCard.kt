package com.uquiz.android.ui.feature.home.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.UTheme
import kotlinx.coroutines.delay

/**
 * ### HomeModeShortcutCard
 *
 * Card vertical con imagen de fondo y CTA inferior para abrir un modo aleatorio.
 *
 * @param backgroundRes Imagen usada como fondo de la card.
 * @param buttonText Texto visible del CTA inferior.
 * @param visible Controla la animación de entrada de la card y su CTA.
 * @param enabled Indica si existe al menos un pack jugable para abrir.
 * @param animationDelayMillis Retraso usado para escalonar la entrada entre cards.
 * @param onClick Acción al pulsar el CTA.
 */
@Composable
fun HomeModeShortcutCard(
    @DrawableRes backgroundRes: Int,
    buttonText: String,
    visible: Boolean,
    enabled: Boolean,
    animationDelayMillis: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var buttonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            delay((animationDelayMillis + 180).toLong())
            buttonVisible = true
        } else {
            buttonVisible = false
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(520, delayMillis = animationDelayMillis)) +
            slideInVertically(tween(560, delayMillis = animationDelayMillis)) { it / 5 },
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(AppRadius),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(backgroundRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x66121A25),
                                    Color(0xD9071221),
                                ),
                            ),
                        ),
                )
                HomeModeShortcutButton(
                    visible = buttonVisible,
                    delayMillis = 0,
                    buttonText = buttonText,
                    enabled = enabled,
                    onClick = onClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 14.dp),
                )
            }
        }
    }
}

@Composable
private fun HomeModeShortcutButton(
    visible: Boolean,
    delayMillis: Int,
    buttonText: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(420, delayMillis = delayMillis)) +
            slideInVertically(tween(440, delayMillis = delayMillis)) { it / 2 },
        modifier = modifier,
    ) {
        UDarkButton(
            text = buttonText,
            onClick = onClick,
            size = UButtonSize.Tiny,
            fullWidth = false,
            enabled = enabled,
        )
    }
}

@UPreview
@Composable
private fun HomeModeShortcutCardPreview() {
    UTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            HomeModeShortcutCard(
                backgroundRes = R.drawable.game_card,
                buttonText = "Random Play",
                visible = true,
                enabled = true,
                animationDelayMillis = 0,
                onClick = {},
            )
        }
    }
}
