package com.uquiz.android.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### PlaceholderScreen
 *
 * Muestra una pantalla temporal para rutas todavía no implementadas.
 *
 * @param title Título principal mostrado en el centro.
 */
@Composable
fun PlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = LocalStrings.current.comingSoon,
                style = MaterialTheme.typography.bodyMedium,
                color = Neutral500,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@UPreview
@Composable
private fun PlaceholderScreenPreview() {
    UTheme {
        PlaceholderScreen(title = "Próximamente")
    }
}
