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
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral500

/**
 * ## PreferencesSection
 * Labelled content group used to partition the preferences screen.
 *
 * Renders a bold title, an optional subtitle, and a vertically arranged content
 * slot. Spacing between sections is the caller's responsibility.
 *
 * @param title Section heading shown in titleMedium style.
 * @param subtitle Optional muted description below the heading.
 * @param content Section body content.
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
