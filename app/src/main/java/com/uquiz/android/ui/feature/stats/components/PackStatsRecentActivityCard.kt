package com.uquiz.android.ui.feature.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.stats.projection.PackRecentActivity
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Purple100
import com.uquiz.android.ui.designsystem.tokens.Purple700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.feature.stats.utils.formatAsSessionTime
import com.uquiz.android.ui.feature.stats.utils.toReadableDuration
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackStatsRecentActivityCard
 *
 * Tarjeta que muestra el resumen de una sesión reciente: modo (Study o Game), fecha/hora,
 * duración y porcentaje de acierto.
 *
 * @param activity Datos de la actividad reciente a mostrar.
 */
@Composable
internal fun PackStatsRecentActivityCard(activity: PackRecentActivity) {
    val strings = LocalStrings.current
    val isStudy = activity.mode == AttemptMode.STUDY
    Card(
        modifier = Modifier.border(1.dp, Neutral100, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(if (isStudy) Navy100 else Purple100, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(
                        if (isStudy) UIcons.Actions.Study else UIcons.Actions.Play,
                    ),
                    contentDescription = null,
                    tint = if (isStudy) Navy500 else Purple700,
                )
            }
            Spacer(Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isStudy) strings.common.studyMode else strings.common.playMode,
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink950,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "${activity.completedAt.formatAsSessionTime()} · ${activity.durationMs?.toReadableDuration() ?: "--"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
            }
            Text(
                text = activity.accuracyPercent?.let { "$it%" } ?: "--",
                style = MaterialTheme.typography.titleMedium,
                color = Navy500,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@UPreview
@Composable
private fun PackStatsRecentActivityCardPreview() {
    UTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PackStatsRecentActivityCard(
                activity = PackRecentActivity(
                    attemptId = "a1",
                    mode = AttemptMode.STUDY,
                    completedAt = System.currentTimeMillis() - 86_400_000L,
                    durationMs = 540_000L,
                    accuracyPercent = 82,
                ),
            )
            PackStatsRecentActivityCard(
                activity = PackRecentActivity(
                    attemptId = "a2",
                    mode = AttemptMode.GAME,
                    completedAt = System.currentTimeMillis() - 172_800_000L,
                    durationMs = 360_000L,
                    accuracyPercent = 71,
                ),
            )
        }
    }
}
