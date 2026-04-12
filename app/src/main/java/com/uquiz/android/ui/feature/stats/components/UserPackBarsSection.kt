package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.UserPackStatsRow
import com.uquiz.android.ui.designsystem.components.inputs.USearchField
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral200
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### UserPackBarsSection
 *
 * Panel de rendimiento por pack. Muestra una lista de barras de progreso y métricas
 * por cada pack estudiado con altura fija y scroll interno. Incluye un campo de búsqueda
 * para filtrar por nombre de pack, divisores sutiles entre filas y texto vacío si no hay datos.
 *
 * @param rows Lista de estadísticas por pack ordenada por relevancia.
 * @param strings Cadenas de localización de la aplicación.
 */
@Composable
fun UserPackBarsSection(
    rows: List<UserPackStatsRow>,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredRows = if (searchQuery.isBlank()) rows
    else rows.filter { it.title.contains(searchQuery, ignoreCase = true) }

    StatsPanel(
        title = strings.statsUser.packBars,
        subtitle = strings.statsUser.packBarsDescription,
        modifier = modifier,
    ) {
        USearchField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = strings.common.searchPlaceholder,
            leadingIcon = rememberVectorPainter(Icons.Outlined.Search),
        )

        when {
            rows.isEmpty() -> StatsEmptyText(text = strings.statsUser.noData)
            filteredRows.isEmpty() -> StatsEmptyText(text = strings.common.noSearchResults)
            else -> {
                Column(
                    modifier = Modifier
                        .height(240.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    filteredRows.forEachIndexed { index, row ->
                        if (index > 0) {
                            HorizontalDivider(
                                color = Neutral200,
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 10.dp),
                            )
                        }
                        PackBarRow(
                            row = row,
                            accuracyLabel = strings.common.accuracyStatLabel,
                            progressLabel = strings.common.progressLabel,
                            sessionsLabel = strings.common.sessionsStatLabel,
                        )
                    }
                }
            }
        }
    }
}

@UPreview
@Composable
private fun UserPackBarsSectionPreview() {
    UTheme {
        val strings = LocalStrings.current
        UserPackBarsSection(
            rows = listOf(
                UserPackStatsRow("1", "Kotlin básico", 12, 84, 220_000L, 72),
                UserPackStatsRow("2", "Android UI", 9, 76, 300_000L, 48),
                UserPackStatsRow("3", "Coroutines avanzado", 5, 61, 410_000L, 30),
            ),
            strings = strings,
        )
    }
}
