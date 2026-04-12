package com.uquiz.android.ui.feature.preferences.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.user.enums.AppThemeMode
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy300
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### ThemeModeSelector
 *
 * Selector segmentado de tres opciones para cambiar el tema de la aplicación.
 *
 * @param selected Tema actualmente seleccionado.
 * @param onSelect Acción invocada al elegir un tema distinto.
 */
@Composable
fun ThemeModeSelector(
    selected: AppThemeMode,
    onSelect: (AppThemeMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ThemeModeOption.entries.forEach { option ->
            val isSelected = option.mode == selected
            Surface(
                onClick = { onSelect(option.mode) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(AppRadius),
                color = if (isSelected) Navy100 else Color.White,
                border = BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) Navy300 else Neutral100
                ),
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(option.iconRes),
                        contentDescription = null,
                        tint = if (isSelected) BrandNavy else Neutral400,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) Ink950 else Neutral400
                    )
                }
            }
        }
    }
}

private enum class ThemeModeOption(val mode: AppThemeMode, val label: String, val iconRes: Int) {
    Light(AppThemeMode.LIGHT, "Light", UIcons.Actions.LightMode),
    Dark(AppThemeMode.DARK, "Dark", UIcons.Actions.DarkMode),
    System(AppThemeMode.SYSTEM, "System", UIcons.Actions.Settings)
}

@UPreview
@Composable
private fun ThemeModeSelectorPreview() {
    UTheme {
        ThemeModeSelector(
            selected = AppThemeMode.SYSTEM,
            onSelect = {},
        )
    }
}
