package com.uquiz.android.ui.feature.preferences.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### ReminderToggleRow
 *
 * Fila de preferencias que activa o desactiva el recordatorio diario.
 *
 * @param label Texto principal de la preferencia.
 * @param checked Indica si el recordatorio está activo.
 * @param onCheckedChange Acción invocada al cambiar el estado del switch.
 */
@Composable
fun ReminderToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(AppRadius),
        color = Color.White,
        border = BorderStroke(1.dp, Neutral100),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (checked) Ink950 else Neutral400,
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Navy500,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Neutral100,
                    uncheckedBorderColor = Neutral400,
                ),
            )
        }
    }
}

@UPreview
@Composable
private fun ReminderToggleRowPreview() {
    UTheme {
        ReminderToggleRow(
            label = "Activar recordatorio diario",
            checked = true,
            onCheckedChange = {},
        )
    }
}
