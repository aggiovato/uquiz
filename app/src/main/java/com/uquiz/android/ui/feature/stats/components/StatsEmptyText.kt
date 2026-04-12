package com.uquiz.android.ui.feature.stats.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### StatsEmptyText
 *
 * Texto de estado vacío para secciones de estadísticas sin datos disponibles.
 *
 * @param text Mensaje a mostrar cuando no hay datos.
 */
@Composable
fun StatsEmptyText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = Neutral700,
        modifier = modifier,
    )
}

@UPreview
@Composable
private fun StatsEmptyTextPreview() {
    UTheme {
        StatsEmptyText(text = "Aún no hay datos disponibles")
    }
}
