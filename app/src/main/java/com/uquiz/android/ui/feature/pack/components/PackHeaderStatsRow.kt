package com.uquiz.android.ui.feature.pack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.cards.UInfoStatCard
import com.uquiz.android.ui.designsystem.components.cards.UInfoStatTone
import com.uquiz.android.ui.i18n.LocalStrings

@Composable
fun PackHeaderStatsRow(
    questionCount: Int,
    accuracyPercent: Int?,
    sessionsCount: Int?,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val cards = buildList {
        add(Triple(questionCount.toString(), strings.questionsStatLabel, UInfoStatTone.Brand))
        if (accuracyPercent != null) add(Triple("$accuracyPercent%", strings.accuracyStatLabel, UInfoStatTone.Secondary))
        if (sessionsCount != null) add(Triple(sessionsCount.toString(), strings.sessionsStatLabel, UInfoStatTone.Tertiary))
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        cards.forEach { (value, label, tone) ->
            UInfoStatCard(
                value = value,
                label = label,
                tone = tone,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
