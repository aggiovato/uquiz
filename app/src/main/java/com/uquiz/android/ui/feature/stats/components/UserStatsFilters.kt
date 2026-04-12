package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.uquiz.android.domain.stats.enums.UserStatsModeFilter
import com.uquiz.android.domain.stats.enums.UserStatsPeriodFilter
import com.uquiz.android.ui.designsystem.components.chips.UToggleChip
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserStatsFilters
 *
 * Barra de filtros de la pantalla de estadísticas del usuario. Muestra chips de modo
 * (Todos, Estudio, Juego) y un botón de icono que abre un menú desplegable estilizado
 * para seleccionar el período de tiempo.
 *
 * @param modeFilter Filtro de modo actualmente seleccionado.
 * @param periodFilter Filtro de período actualmente seleccionado.
 * @param onModeFilterSelected Callback al seleccionar un nuevo filtro de modo.
 * @param onPeriodFilterSelected Callback al seleccionar un nuevo filtro de período.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserStatsFilters(
    modeFilter: UserStatsModeFilter,
    periodFilter: UserStatsPeriodFilter,
    onModeFilterSelected: (UserStatsModeFilter) -> Unit,
    onPeriodFilterSelected: (UserStatsPeriodFilter) -> Unit,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserStatsModeFilter.entries.forEach { filter ->
            UToggleChip(
                label = filter.label(strings),
                isActive = filter == modeFilter,
                onClick = { onModeFilterSelected(filter) },
                iconRes = filter.iconRes(),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        PeriodFilterButton(
            periodFilter = periodFilter,
            onPeriodFilterSelected = onPeriodFilterSelected,
            strings = strings,
        )
    }
}

@Composable
private fun PeriodFilterButton(
    periodFilter: UserStatsPeriodFilter,
    onPeriodFilterSelected: (UserStatsPeriodFilter) -> Unit,
    strings: AppStrings,
) {
    var showMenu by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    // Desplazamiento vertical para mostrar el menú justo bajo el botón (48dp)
    val offsetY = with(density) { 48.dp.roundToPx() }

    Box {
        IconButton(onClick = { showMenu = !showMenu }) {
            Icon(
                painter = painterResource(UIcons.Actions.Filter),
                contentDescription = null,
                tint = if (periodFilter != UserStatsPeriodFilter.ALL) Teal500 else Neutral500,
                modifier = Modifier.size(20.dp),
            )
        }
        if (showMenu) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(0, offsetY),
                onDismissRequest = { showMenu = false },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(URadius))
                        .background(BrandNavy)
                        .padding(vertical = 4.dp),
                ) {
                    UserStatsPeriodFilter.entries.forEach { filter ->
                        val isSelected = filter == periodFilter
                        Row(
                            modifier = Modifier
                                .width(172.dp)
                                .clickable {
                                    onPeriodFilterSelected(filter)
                                    showMenu = false
                                }
                                .background(
                                    if (isSelected) Color.White.copy(alpha = 0.12f) else Color.Transparent,
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = filter.label(strings),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun UserStatsModeFilter.label(strings: AppStrings): String = when (this) {
    UserStatsModeFilter.ALL -> strings.statsUser.allFilter
    UserStatsModeFilter.STUDY -> strings.common.studyModeShort
    UserStatsModeFilter.GAME -> strings.common.playModeShort
}

private fun UserStatsModeFilter.iconRes(): Int? = when (this) {
    UserStatsModeFilter.ALL -> null
    UserStatsModeFilter.STUDY -> UIcons.Actions.Study
    UserStatsModeFilter.GAME -> UIcons.Actions.Play
}

private fun UserStatsPeriodFilter.label(strings: AppStrings): String = when (this) {
    UserStatsPeriodFilter.ALL -> strings.statsUser.allFilter
    UserStatsPeriodFilter.LAST_7_DAYS -> strings.statsUser.last7DaysFilter
    UserStatsPeriodFilter.LAST_30_DAYS -> strings.statsUser.last30DaysFilter
}

@UPreview
@Composable
private fun UserStatsFiltersPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserStatsFilters(
            modeFilter = UserStatsModeFilter.ALL,
            periodFilter = UserStatsPeriodFilter.LAST_7_DAYS,
            onModeFilterSelected = {},
            onPeriodFilterSelected = {},
            strings = strings,
        )
    }
}
