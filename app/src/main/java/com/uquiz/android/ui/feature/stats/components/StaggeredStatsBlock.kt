package com.uquiz.android.ui.feature.stats.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### StaggeredStatsBlock
 *
 * Envuelve contenido en un [AnimatedVisibility] con entrada escalonada: fade + deslizamiento
 * vertical suave. Permite crear apariciones progresivas en cascada ajustando [delayMillis].
 *
 * @param visible Cuando pasa de `false` a `true` dispara la animación de entrada.
 * @param delayMillis Retraso en milisegundos antes de que comience la animación.
 * @param content Contenido a animar.
 */
@Composable
fun StaggeredStatsBlock(
    visible: Boolean,
    delayMillis: Int,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 260, delayMillis = delayMillis)) +
            slideInVertically(
                animationSpec = tween(durationMillis = 260, delayMillis = delayMillis),
                initialOffsetY = { it / 3 },
            ),
    ) {
        content()
    }
}

@UPreview
@Composable
private fun StaggeredStatsBlockPreview() {
    UTheme {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }
        StaggeredStatsBlock(visible = visible, delayMillis = 0) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Contenido animado", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
