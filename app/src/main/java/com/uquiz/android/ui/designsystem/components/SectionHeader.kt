package com.uquiz.android.ui.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### SectionHeader
 *
 * Muestra un encabezado ligero para separar bloques de contenido.
 *
 * @param title Texto del encabezado.
 */
@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = Ink950,
        modifier = modifier,
    )
}

@UPreview
@Composable
private fun SectionHeaderPreview() {
    UTheme {
        SectionHeader(title = "Sección de ejemplo")
    }
}
