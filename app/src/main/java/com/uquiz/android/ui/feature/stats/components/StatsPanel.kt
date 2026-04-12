package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### StatsPanel
 *
 * Contenedor de sección para estadísticas. Muestra un título, opcionalmente un subtítulo
 * descriptivo, y el contenido dentro de una card con elevación suave y esquinas redondeadas.
 *
 * @param title Título de la sección mostrado como [SectionHeader].
 * @param subtitle Descripción corta opcional mostrada bajo el título en color neutro.
 * @param content Contenido composable de la sección.
 */
@Composable
fun StatsPanel(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                SectionHeader(title = title)
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Neutral500,
                    )
                }
            }
            content()
        }
    }
}

@UPreview
@Composable
private fun StatsPanelPreview() {
    UTheme {
        StatsPanel(
            title = "Rendimiento general",
            subtitle = "Últimas 10 sesiones",
        ) {
            Text(text = "Contenido de la sección")
        }
    }
}
