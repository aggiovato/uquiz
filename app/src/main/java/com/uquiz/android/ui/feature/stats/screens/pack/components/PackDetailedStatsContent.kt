package com.uquiz.android.ui.feature.stats.screens.pack.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.stats.projection.PackBestPerformance
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.domain.stats.projection.PackRecentActivity
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UOutlinedButton
import com.uquiz.android.ui.designsystem.tokens.Gold100
import com.uquiz.android.ui.designsystem.tokens.Gold700
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Orange100
import com.uquiz.android.ui.designsystem.tokens.Orange700
import com.uquiz.android.ui.designsystem.tokens.Purple100
import com.uquiz.android.ui.designsystem.tokens.Purple700
import com.uquiz.android.ui.designsystem.tokens.Teal100
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.i18n.LocalStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun PackDetailedStatsContent(
    packTitle: String,
    stats: PackDetailedStats,
    onHelpClick: () -> Unit,
    onBackToPackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = packTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = Ink950,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = strings.packGeneralSummary,
                style = MaterialTheme.typography.titleMedium,
                color = Ink950,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExpandedSummaryCard(
                    iconRes = UIcons.Cards.Session,
                    circleColor = Navy100,
                    iconTint = Navy500,
                    title = strings.sessionsStatLabel,
                    value = stats.totalSessions.toString(),
                    delta = recentSessionsDelta(stats.recentActivity, strings),
                    modifier = Modifier.weight(1f)
                )
                ExpandedSummaryCard(
                    iconRes = UIcons.Cards.Check,
                    circleColor = Teal100,
                    iconTint = Teal700,
                    title = strings.accuracyStatLabel,
                    value = stats.averageAccuracyPercent?.let { "$it%" } ?: "--",
                    delta = accuracyDelta(stats.recentActivity),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExpandedSummaryCard(
                    iconRes = UIcons.Cards.Clock,
                    circleColor = Gold100,
                    iconTint = Gold700,
                    title = strings.averageTimeStatLabel,
                    value = stats.averageDurationMs?.toReadableDuration() ?: "--",
                    delta = durationDelta(stats.recentActivity),
                    modifier = Modifier.weight(1f)
                )
                ExpandedSummaryCard(
                    iconRes = UIcons.Cards.Progress,
                    circleColor = Orange100,
                    iconTint = Orange700,
                    title = strings.progressLabel,
                    value = "${stats.progressPercent}%",
                    supporting = strings.packQuestionsDominated(stats.dominatedQuestions, stats.totalQuestions),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        UOutlinedButton(
            text = strings.packStatsHelpAction,
            onClick = onHelpClick,
            leadingIconRes = UIcons.Actions.Details,
            modifier = Modifier.fillMaxWidth(),
            size = UButtonSize.Compact
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = strings.packByMode,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Ink950
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModeSummaryCard(
                    iconRes = UIcons.Actions.Study,
                    iconTint = Navy500,
                    title = strings.studyModeShort,
                    sessions = stats.studyStats.sessions,
                    supporting = stats.studyStats.accuracyPercent?.let {
                        "$it% ${strings.accuracyStatLabel.lowercase()}"
                    },
                    modifier = Modifier.weight(1f)
                )
                ModeSummaryCard(
                    iconRes = UIcons.Actions.Play,
                    iconTint = Purple700,
                    circleColor = Purple100,
                    title = strings.playModeShort,
                    sessions = stats.gameStats.sessions,
                    supporting = stats.gameStats.accuracyPercent?.let {
                        "$it% ${strings.accuracyStatLabel.lowercase()}"
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = strings.packRecentActivity,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Ink950
            )
            if (stats.recentActivity.isEmpty()) {
                Text(
                    text = strings.packNoSessionsYet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral500
                )
            } else {
                stats.recentActivity.take(4).forEach { activity ->
                    RecentActivityCard(activity)
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = strings.packBestPerformance,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Ink950
            )
            BestPerformanceCard(
                value = stats.bestPerformance?.toDisplayValue(strings) ?: "--",
                supporting = stats.bestPerformance?.mode.asModeLabel(strings),
                valueTint = if (stats.bestPerformance == null) Neutral500 else Teal700
            )
        }

        UOutlinedButton(
            text = strings.studyBackToPack,
            onClick = onBackToPackClick,
            modifier = Modifier.fillMaxWidth(),
            size = UButtonSize.Compact
        )
    }
}

@Composable
private fun ExpandedSummaryCard(
    @DrawableRes iconRes: Int,
    circleColor: Color,
    iconTint: Color,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    delta: StatDelta? = null,
    supporting: String? = null
) {
    Card(
        modifier = modifier
            .heightIn(min = 156.dp)
            .border(1.dp, Neutral100, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = iconTint
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = iconTint,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = iconTint,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            when {
                delta != null -> DeltaRow(delta)
                !supporting.isNullOrBlank() -> {
                    Text(
                        text = supporting,
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral700,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun DeltaRow(delta: StatDelta) {
    val tint = if (delta.improved) Teal700 else Color(0xFFE04F5F)
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(UIcons.Cards.ArrowUp),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(14.dp)
                .rotate(if (delta.improved) 0f else 180f)
        )
        Text(
            text = delta.label,
            style = MaterialTheme.typography.bodySmall,
            color = tint,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ModeSummaryCard(
    @DrawableRes iconRes: Int,
    iconTint: Color,
    circleColor: Color = Navy100,
    title: String,
    sessions: Int,
    supporting: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(min = 128.dp)
            .border(1.dp, Neutral100, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = iconTint
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Ink950,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (sessions == 1) {
                    "1 ${LocalStrings.current.sessionsStatLabel.lowercase()}"
                } else {
                    "$sessions ${LocalStrings.current.sessionsStatLabel.lowercase()}"
                },
                color = Neutral700,
                style = MaterialTheme.typography.bodyMedium
            )
            supporting?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun RecentActivityCard(activity: PackRecentActivity) {
    Card(
        modifier = Modifier.border(1.dp, Neutral100, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(if (activity.mode == AttemptMode.STUDY) Navy100 else Purple100, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (activity.mode == AttemptMode.STUDY) UIcons.Actions.Study else UIcons.Actions.Play
                    ),
                    contentDescription = null,
                    tint = if (activity.mode == AttemptMode.STUDY) Navy500 else Purple700
                )
            }
            Spacer(Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (activity.mode == AttemptMode.STUDY) LocalStrings.current.studyMode else LocalStrings.current.playMode,
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink950,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${activity.completedAt.formatAsSessionTime()} · ${activity.durationMs?.toReadableDuration() ?: "--"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500
                )
            }
            Text(
                text = activity.accuracyPercent?.let { "$it%" } ?: "--",
                style = MaterialTheme.typography.titleMedium,
                color = Navy500,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BestPerformanceCard(
    value: String,
    supporting: String,
    valueTint: Color
) {
    Card(
        modifier = Modifier.border(1.dp, Neutral100, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Gold100, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(UIcons.Cards.Trophy),
                    contentDescription = null,
                    tint = Gold700
                )
            }
            Spacer(Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = LocalStrings.current.packBestPerformance,
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink950,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (supporting.isBlank()) LocalStrings.current.packNoSessionsYet else supporting,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = valueTint,
                fontWeight = FontWeight.Bold
            )
        }
    }
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

private fun PackBestPerformance.toDisplayValue(strings: AppStrings): String = when (mode) {
    AttemptMode.GAME -> numericScore?.let { "$it ${strings.pointsShortLabel}" }
        ?: "$scoreLabel ${strings.pointsShortLabel}"
    AttemptMode.STUDY -> numericScore?.let { "$it%" } ?: scoreLabel
}

private data class StatDelta(
    val label: String,
    val improved: Boolean
)

private fun recentSessionsDelta(activity: List<PackRecentActivity>, strings: AppStrings): StatDelta? {
    if (activity.isEmpty()) return null
    val now = System.currentTimeMillis()
    val last7 = activity.count { now - it.completedAt <= 7L * 24L * 60L * 60L * 1000L }
    val previous7 = activity.count {
        val diff = now - it.completedAt
        diff > 7L * 24L * 60L * 60L * 1000L && diff <= 14L * 24L * 60L * 60L * 1000L
    }
    val delta = last7 - previous7
    return when {
        delta > 0 -> StatDelta("+${strings.packThisWeekLabel(delta)}", true)
        delta < 0 -> StatDelta("-${strings.packThisWeekLabel(abs(delta))}", false)
        else -> StatDelta(strings.packThisWeekLabel(last7), true)
    }
}

private fun accuracyDelta(activity: List<PackRecentActivity>): StatDelta? {
    val latest = activity.take(3).mapNotNull { it.accuracyPercent }
    val previous = activity.drop(3).take(3).mapNotNull { it.accuracyPercent }
    if (latest.isEmpty() || previous.isEmpty()) return null
    val delta = latest.average() - previous.average()
    return StatDelta(
        label = if (delta >= 0) "+${delta.toInt()}pp" else "${delta.toInt()}pp",
        improved = delta >= 0
    )
}

private fun durationDelta(activity: List<PackRecentActivity>): StatDelta? {
    val latest = activity.take(3).mapNotNull { it.durationMs }
    val previous = activity.drop(3).take(3).mapNotNull { it.durationMs }
    if (latest.isEmpty() || previous.isEmpty()) return null
    val deltaSeconds = ((latest.average() - previous.average()) / 1000.0).toInt()
    return StatDelta(
        label = if (deltaSeconds >= 0) "+${deltaSeconds}s" else "${deltaSeconds}s",
        improved = deltaSeconds <= 0
    )
}
