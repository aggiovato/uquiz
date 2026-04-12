package com.uquiz.android.ui.feature.preferences.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy300
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### DayOfWeekSelector
 *
 * Selector de días para configurar la repetición semanal del recordatorio.
 *
 * @param selectedDays Conjunto actual de claves de día seleccionadas.
 * @param onToggle Acción invocada al alternar un día concreto.
 */
@Composable
fun DayOfWeekSelector(
    selectedDays: Set<String>,
    onToggle: (String) -> Unit,
) {
    val strings = LocalStrings.current
    val dayKeys =
        listOf(
            "MON" to strings.preferences.dayShortMon,
            "TUE" to strings.preferences.dayShortTue,
            "WED" to strings.preferences.dayShortWed,
            "THU" to strings.preferences.dayShortThu,
            "FRI" to strings.preferences.dayShortFri,
            "SAT" to strings.preferences.dayShortSat,
            "SUN" to strings.preferences.dayShortSun,
        )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        dayKeys.forEach { (key, label) ->
            val isSelected = key in selectedDays
            Surface(
                onClick = { onToggle(key) },
                modifier = Modifier.weight(1f),
                shape = CircleShape,
                color = if (isSelected) Navy500 else Color.White,
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) Navy300 else Neutral100,
                    ),
                tonalElevation = 0.dp,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Neutral400,
                    modifier =
                        Modifier
                            .padding(vertical = 8.dp)
                            .then(Modifier) // fills width via weight
                            .run { this },
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun DayOfWeekSelectorPreview() {
    UTheme {
        DayOfWeekSelector(
            selectedDays = setOf("MON", "WED", "FRI"),
            onToggle = {},
        )
    }
}
