package com.uquiz.android.ui.feature.preferences.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### PreferencesSection
 *
 * Agrupa un bloque de contenido etiquetado dentro de la pantalla de preferencias.
 *
 * @param title Título principal de la sección.
 * @param subtitle Subtítulo opcional con contexto adicional.
 * @param content Contenido renderizado dentro de la sección.
 */
@Composable
fun PreferencesSection(
    title: String,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Ink950,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
            }
        }
        content()
    }
}

@UPreview
@Composable
private fun PreferencesSectionPreview() {
    UTheme {
        PreferencesSection(
            title = "Perfil",
            subtitle = "Tu nombre y avatar",
        ) {
            Text("Contenido de ejemplo")
        }
    }
}
