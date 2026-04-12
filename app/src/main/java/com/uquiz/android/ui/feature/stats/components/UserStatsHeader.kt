package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UserStatsHeader
 *
 * Encabezado de la pantalla de estadísticas del usuario. Muestra el título principal
 * y el subtítulo descriptivo de la sección.
 *
 * @param title Título principal de la pantalla.
 * @param subtitle Texto descriptivo bajo el título.
 */
@Composable
fun UserStatsHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = Ink950,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Neutral700,
        )
    }
}

@UPreview
@Composable
private fun UserStatsHeaderPreview() {
    UTheme {
        UserStatsHeader(
            title = "My stats",
            subtitle = "Discover your performance and improvement over time",
        )
    }
}
