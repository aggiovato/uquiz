package com.uquiz.android.ui.feature.pack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UOutlinedButton
import com.uquiz.android.ui.designsystem.components.buttons.UPlainIconButton
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy400
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Orange700
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackStatsBottomSheet(
    stats: PackDetailedStats,
    onSeeFullStatsClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = Color.White,
        contentColor = BrandNavy,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = URadius * 2, topEnd = URadius * 2),
        scrimColor = BrandNavy.copy(alpha = 0.34f),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .size(width = 42.dp, height = 4.dp)
                    .background(
                        color = Neutral500,
                        shape = RoundedCornerShape(percent = 100)
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 2.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.packStatsTitle,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Ink950
                )
                UPlainIconButton(
                    iconRes = UIcons.Actions.Close,
                    contentDescription = strings.cancel,
                    onClick = onDismiss,
                    tint = Navy400,
                    hitSize = 22.dp,
                    iconSize = 18.dp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryMiniStat(
                    iconRes = UIcons.Cards.Session,
                    iconTint = Navy500,
                    value = stats.totalSessions.toString(),
                    label = strings.sessionsStatLabel,
                    modifier = Modifier.weight(1f)
                )
                SummaryMiniStat(
                    iconRes = UIcons.Cards.Check,
                    iconTint = Teal700,
                    value = stats.averageAccuracyPercent?.let { "$it%" } ?: "--",
                    label = strings.accuracyStatLabel,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryMiniStat(
                    iconRes = UIcons.Cards.Clock,
                    iconTint = Navy500,
                    value = stats.averageDurationMs?.toReadableDuration() ?: "--",
                    label = strings.averageTimeStatLabel,
                    modifier = Modifier.weight(1f)
                )
                SummaryMiniStat(
                    iconRes = UIcons.Cards.Progress,
                    iconTint = Orange700,
                    value = "${stats.progressPercent}%",
                    label = strings.progressLabel,
                    modifier = Modifier.weight(1f)
                )
            }

            MetadataRow(
                label = strings.packLastSessionLabel,
                value = stats.lastSessionAt?.formatAsSessionTime() ?: strings.packNoSessionsYet
            )
            MetadataRow(
                label = strings.packMostUsedModeLabel,
                value = stats.mostUsedMode.asModeLabel(strings)
            )

            UOutlinedButton(
                text = strings.packSeeFullStats,
                onClick = onSeeFullStatsClick,
                leadingIconRes = UIcons.Actions.Details,
                modifier = Modifier.fillMaxWidth(),
                size = UButtonSize.Compact
            )

            Spacer(Modifier.size(12.dp))
        }
    }
}

@Composable
private fun SummaryMiniStat(
    iconRes: Int,
    iconTint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    PackOverviewStatCard(
        iconRes = iconRes,
        iconTint = iconTint,
        value = value,
        label = label,
        modifier = modifier
    )
}

@Composable
private fun MetadataRow(
    label: String,
    value: String
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Neutral500,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append("$label: ")
            }
            withStyle(
                SpanStyle(
                    color = Neutral700,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append(value)
            }
        },
        style = MaterialTheme.typography.bodySmall
    )
}

private fun Long.formatAsSessionTime(): String =
    SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(this))

private fun Long.toReadableDuration(): String {
    val totalSeconds = (this / 1000L).coerceAtLeast(0L)
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return if (minutes > 0) "${minutes}m ${seconds}s" else "${seconds}s"
}

private fun AttemptMode?.asModeLabel(strings: AppStrings): String = when (this) {
    AttemptMode.STUDY -> strings.studyMode
    AttemptMode.GAME -> strings.playMode
    null -> strings.packNoSessionsYet
}
