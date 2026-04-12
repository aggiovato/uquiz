package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.shared.components.studygame.ModeBackground

/**
 * ### StudyModeBackground
 *
 * Contenedor base de la interfaz del modo estudio. Delegado en [ModeBackground].
 *
 * @param contentPadding Padding interior aplicado al área de contenido.
 * @param includeStatusBarsPadding Indica si se debe añadir el padding de la barra de estado.
 * @param content Contenido visible sobre el fondo.
 */
@Composable
fun StudyModeBackground(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 22.dp, vertical = 24.dp),
    includeStatusBarsPadding: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    ModeBackground(
        modifier = modifier,
        contentPadding = contentPadding,
        includeStatusBarsPadding = includeStatusBarsPadding,
        content = content,
    )
}

@UPreview
@Composable
private fun StudyModeBackgroundPreview() {
    UTheme {
        StudyModeBackground {}
    }
}
