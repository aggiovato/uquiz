package com.uquiz.android.ui.feature.stats.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.uquiz.android.R
import com.uquiz.android.domain.stats.enums.UserStatsModeFilter
import com.uquiz.android.domain.stats.enums.UserStatsPeriodFilter
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Orange900
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings

private val statsBannerChipShape = RoundedCornerShape(18.dp)
private val statsBannerColorSpec = tween<Color>(durationMillis = 180)
private val statsBannerMenuColor = Color(0xFF4C220A)

/**
 * ### UserStatsBanner
 *
 * Banner hero de la pantalla de estadísticas del usuario con imagen de fondo,
 * mensaje animado y filtros superpuestos sobre la ilustración.
 *
 * @param visible Indica si deben reproducirse las animaciones de entrada del contenido.
 * @param modeFilter Filtro de modo actualmente seleccionado.
 * @param periodFilter Filtro de período actualmente seleccionado.
 * @param onModeFilterSelected Acción al seleccionar un filtro de modo.
 * @param onPeriodFilterSelected Acción al seleccionar un filtro de período.
 */
@Composable
@OptIn(ExperimentalLayoutApi::class)
fun UserStatsBanner(
    visible: Boolean,
    modeFilter: UserStatsModeFilter,
    periodFilter: UserStatsPeriodFilter,
    onModeFilterSelected: (UserStatsModeFilter) -> Unit,
    onPeriodFilterSelected: (UserStatsPeriodFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .height(176.dp),
        shape = RoundedCornerShape(AppRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.stats_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 35.dp, end = 50.dp)
                        .scale(1.6f),
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors =
                                    listOf(
                                        Color(0xEE280E01),
                                        Color(0xCC2F1301),
                                        Color(0x66501903),
                                        Color(0x1AB03805),
                                    ),
                            ),
                        ),
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter =
                        fadeIn(tween(500, delayMillis = 120)) +
                            slideInHorizontally(tween(520, delayMillis = 120)) { -it / 4 },
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = strings.statsUser.bannerLine1,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier.graphicsLayer { rotationZ = -4f },
                        )
                        Text(
                            text = strings.statsUser.bannerLine2,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.92f),
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        Text(
                            text = strings.statsUser.bannerLine3,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            modifier = Modifier.graphicsLayer { rotationZ = 2f },
                        )
                    }
                }

                AnimatedVisibility(
                    visible = visible,
                    enter =
                        fadeIn(tween(500, delayMillis = 240)) +
                            slideInHorizontally(tween(560, delayMillis = 240)) { -it / 5 },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                        FlowRow(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            UserStatsModeFilter.entries.forEach { filter ->
                                StatsBannerModeChip(
                                    label = filter.label(strings),
                                    isActive = filter == modeFilter,
                                    iconRes = filter.iconRes(),
                                    onClick = { onModeFilterSelected(filter) },
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        StatsBannerPeriodButton(
                            periodFilter = periodFilter,
                            onPeriodFilterSelected = onPeriodFilterSelected,
                            strings = strings,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsBannerModeChip(
    label: String,
    isActive: Boolean,
    iconRes: Int?,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) Color.White.copy(alpha = 0.94f) else Color.White.copy(alpha = 0.10f),
        animationSpec = statsBannerColorSpec,
        label = "statsBannerModeChipBackground",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isActive) Color.White.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.20f),
        animationSpec = statsBannerColorSpec,
        label = "statsBannerModeChipBorder",
    )
    val contentColor by animateColorAsState(
        targetValue = if (isActive) Orange900 else Color.White,
        animationSpec = statsBannerColorSpec,
        label = "statsBannerModeChipContent",
    )

    Row(
        modifier =
            Modifier
                .clip(statsBannerChipShape)
                .background(backgroundColor)
                .border(1.dp, borderColor, statsBannerChipShape)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

@Composable
private fun StatsBannerPeriodButton(
    periodFilter: UserStatsPeriodFilter,
    onPeriodFilterSelected: (UserStatsPeriodFilter) -> Unit,
    strings: AppStrings,
) {
    var showMenu by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val offsetY = with(density) { 44.dp.roundToPx() }
    val isActive = periodFilter != UserStatsPeriodFilter.ALL
    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) Color.White.copy(alpha = 0.94f) else Color.White.copy(alpha = 0.10f),
        animationSpec = statsBannerColorSpec,
        label = "statsBannerPeriodButtonBackground",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isActive) Color.White.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.20f),
        animationSpec = statsBannerColorSpec,
        label = "statsBannerPeriodButtonBorder",
    )
    val iconColor by animateColorAsState(
        targetValue = if (isActive) Orange900 else Color.White,
        animationSpec = statsBannerColorSpec,
        label = "statsBannerPeriodButtonIcon",
    )

    Box {
        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(statsBannerChipShape)
                    .background(backgroundColor)
                    .border(1.dp, borderColor, statsBannerChipShape)
                    .clickable { showMenu = !showMenu },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(UIcons.Actions.Filter),
                contentDescription = null,
                tint = if (isActive) iconColor else Color.White,
                modifier = Modifier.size(16.dp),
            )
        }

        if (showMenu) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(45, offsetY + 26),
                onDismissRequest = { showMenu = false },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(AppRadius))
                            .background(statsBannerMenuColor)
                            .padding(vertical = 4.dp),
                ) {
                    UserStatsPeriodFilter.entries.forEach { filter ->
                        val isSelected = filter == periodFilter
                        Row(
                            modifier =
                                Modifier
                                    .width(172.dp)
                                    .clickable {
                                        onPeriodFilterSelected(filter)
                                        showMenu = false
                                    }.background(
                                        if (isSelected) Color.White.copy(alpha = 0.14f) else Color.Transparent,
                                    ).padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
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

private fun UserStatsModeFilter.label(strings: AppStrings): String =
    when (this) {
        UserStatsModeFilter.ALL -> strings.statsUser.allFilter
        UserStatsModeFilter.STUDY -> strings.common.studyModeShort
        UserStatsModeFilter.GAME -> strings.common.playModeShort
    }

private fun UserStatsModeFilter.iconRes(): Int? =
    when (this) {
        UserStatsModeFilter.ALL -> null
        UserStatsModeFilter.STUDY -> UIcons.Actions.Study
        UserStatsModeFilter.GAME -> UIcons.Actions.Play
    }

private fun UserStatsPeriodFilter.label(strings: AppStrings): String =
    when (this) {
        UserStatsPeriodFilter.ALL -> strings.statsUser.allFilter
        UserStatsPeriodFilter.LAST_7_DAYS -> strings.statsUser.last7DaysFilter
        UserStatsPeriodFilter.LAST_30_DAYS -> strings.statsUser.last30DaysFilter
    }

@UPreview
@Composable
private fun UserStatsBannerPreview() {
    UTheme {
        Box(
            modifier =
                Modifier
                    .background(Color(0xFF4A1E07))
                    .padding(PaddingValues(16.dp)),
        ) {
            UserStatsBanner(
                visible = true,
                modeFilter = UserStatsModeFilter.ALL,
                periodFilter = UserStatsPeriodFilter.LAST_7_DAYS,
                onModeFilterSelected = {},
                onPeriodFilterSelected = {},
            )
        }
    }
}
