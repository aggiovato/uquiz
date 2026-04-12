package com.uquiz.android.ui.feature.pack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.stats.projection.PackOverviewMetrics
import com.uquiz.android.ui.designsystem.components.buttons.UIconActionButton
import com.uquiz.android.ui.designsystem.components.buttons.UPlainIconButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Gold500
import com.uquiz.android.ui.designsystem.tokens.Navy400
import com.uquiz.android.ui.designsystem.tokens.Navy700
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Orange700
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackOverviewCard
 *
 * Tarjeta superior del detalle de pack con metadatos, acciones y métricas resumidas.
 *
 * @param title Título visible del pack.
 * @param description Descripción opcional del pack.
 * @param overview Métricas resumidas mostradas en la zona inferior.
 * @param onEditClick Acción para editar el pack.
 * @param onStatsClick Acción para abrir el resumen de estadísticas.
 */
@Composable
fun PackOverviewCard(
    title: String,
    description: String?,
    overview: PackOverviewMetrics,
    onEditClick: () -> Unit,
    onStatsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    var isExpanded by remember(description) { mutableStateOf(false) }
    val trimmedDescription = description?.trim().orEmpty()
    val canExpand = trimmedDescription.length > 110
    val collapsedDescription = remember(trimmedDescription) {
        if (trimmedDescription.length <= 110) trimmedDescription
        else trimmedDescription.take(107).trimEnd() + "..."
    }
    val statPages = remember(overview) {
        listOf(
            PackOverviewStatPageItem(
                iconRes = UIcons.Cards.Question,
                iconTint = Navy500,
                value = overview.questionCount.toString(),
                label = strings.common.questionsStatLabel
            ),
            PackOverviewStatPageItem(
                iconRes = UIcons.Cards.Check,
                iconTint = Teal700,
                value = overview.accuracyPercent?.let { "$it%" } ?: "--",
                label = strings.common.accuracyStatLabel
            ),
            PackOverviewStatPageItem(
                iconRes = UIcons.Cards.Session,
                iconTint = Gold500,
                value = overview.sessionsCount?.toString() ?: "0",
                label = strings.common.sessionsStatLabel
            ),
            PackOverviewStatPageItem(
                iconRes = UIcons.Cards.Progress,
                iconTint = Orange700,
                value = "${overview.progressPercent ?: 0}%",
                label = strings.common.progressLabel
            )
        ).chunked(2)
    }
    val pagerState = rememberPagerState(pageCount = { statPages.size })

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Neutral100, RoundedCornerShape(URadius))
            .background(Color.White, RoundedCornerShape(URadius))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f, fill = false),
                    style = MaterialTheme.typography.titleMedium,
                    color = Navy700,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                UPlainIconButton(
                    iconRes = UIcons.Actions.Edit,
                    contentDescription = strings.common.editPack,
                    onClick = onEditClick,
                    tint = Navy400,
                    hitSize = 18.dp,
                    iconSize = 15.dp
                )
            }
            UIconActionButton(
                iconRes = UIcons.Actions.Stats,
                contentDescription = strings.nav.navStats,
                onClick = onStatsClick,
                containerColor = Navy500,
                contentColor = Color.White,
                elevated = false
            )
        }

        if (!description.isNullOrBlank()) {
            Spacer(Modifier.height(8.dp))
            if (canExpand) {
                Text(
                    text = buildAnnotatedString {
                        append(if (isExpanded) trimmedDescription else collapsedDescription)
                        append(" ")
                        pushStyle(
                            SpanStyle(
                                color = Navy400,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        append(if (isExpanded) strings.common.seeLess else strings.common.seeMore)
                        pop()
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isExpanded = !isExpanded }
                )
            } else {
                Text(
                    text = trimmedDescription,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { pageIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                statPages[pageIndex].forEach { item ->
                    PackOverviewStatCard(
                        iconRes = item.iconRes,
                        iconTint = item.iconTint,
                        value = item.value,
                        label = item.label,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (statPages.size > 1) {
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(statPages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .background(
                                color = if (isSelected) Navy500 else Neutral100,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@UPreview
@Composable
private fun PackOverviewCardPreview() {
    UTheme {
        PackOverviewCard(
            title = "Geografía europea",
            description = "Repaso de capitales, ríos y cordilleras del continente.",
            overview = PackOverviewMetrics(
                questionCount = 24,
                accuracyPercent = 72,
                sessionsCount = 8,
                progressPercent = 50,
            ),
            onEditClick = {},
            onStatsClick = {},
        )
    }
}

private data class PackOverviewStatPageItem(
    val iconRes: Int,
    val iconTint: Color,
    val value: String,
    val label: String
)
