package com.uquiz.android.ui.feature.preferences.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.buttons.UTextButton
import com.uquiz.android.ui.designsystem.components.dialogs.UDialogScaffold
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### TimePickerRow
 *
 * Muestra la hora configurada para el recordatorio y abre un selector al pulsarla.
 *
 * @param hour Hora actual en formato 24h.
 * @param minute Minuto actual.
 * @param onConfirm Acción invocada al confirmar la nueva hora.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerRow(
    hour: Int,
    minute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val strings = LocalStrings.current
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        onClick = { showDialog = true },
        shape = RoundedCornerShape(AppRadius),
        color = Color.White,
        border = BorderStroke(1.dp, Neutral100),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(UIcons.Cards.Clock),
                    contentDescription = null,
                    tint = BrandNavy,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = strings.preferences.reminderTimeLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral500
                )
            }
            Text(
                text = "%02d:%02d".format(hour, minute),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Ink950
            )
        }
    }

    if (showDialog) {
        val pickerState: TimePickerState = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute,
            is24Hour = true
        )

        UDialogScaffold(
            title = strings.preferences.reminderTimePickerTitle,
            onDismiss = { showDialog = false },
            headerColor = BrandNavy,
            headerIconRes = UIcons.Cards.Clock,
            actions = {
                UTextButton(
                    text = strings.common.cancel,
                    onClick = { showDialog = false }
                )
                UFilledButton(
                    text = strings.preferences.confirm,
                    onClick = {
                        onConfirm(pickerState.hour, pickerState.minute)
                        showDialog = false
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MaterialTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(primary = BrandNavy)
                ) {
                    TimePicker(state = pickerState)
                }
            }
        }
    }
}

@UPreview
@Composable
private fun TimePickerRowPreview() {
    UTheme {
        TimePickerRow(
            hour = 20,
            minute = 30,
            onConfirm = { _, _ -> },
        )
    }
}
