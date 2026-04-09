package com.uquiz.android.ui.feature.help

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Orange100
import com.uquiz.android.ui.designsystem.tokens.Orange700
import com.uquiz.android.ui.designsystem.tokens.Purple100
import com.uquiz.android.ui.designsystem.tokens.Purple700
import com.uquiz.android.ui.designsystem.tokens.Teal100
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.Gold100
import com.uquiz.android.ui.designsystem.tokens.Gold700
import com.uquiz.android.ui.designsystem.components.feedback.UMarkdownText
import com.uquiz.android.ui.i18n.LocalStrings

@Composable
fun PackStatsHelpRoute(modifier: Modifier = Modifier) {
    val strings = LocalStrings.current
    val items = listOf(
        HelpTopic(
            iconRes = UIcons.Cards.Question,
            iconTint = Navy500,
            circleColor = Navy100,
            title = strings.questionsStatLabel,
            body = strings.packStatsHelpQuestions
        ),
        HelpTopic(
            iconRes = UIcons.Cards.Check,
            iconTint = Teal700,
            circleColor = Teal100,
            title = strings.accuracyStatLabel,
            body = strings.packStatsHelpAccuracy
        ),
        HelpTopic(
            iconRes = UIcons.Cards.Session,
            iconTint = Gold700,
            circleColor = Gold100,
            title = strings.sessionsStatLabel,
            body = strings.packStatsHelpSessions
        ),
        HelpTopic(
            iconRes = UIcons.Cards.Progress,
            iconTint = Orange700,
            circleColor = Orange100,
            title = strings.progressLabel,
            body = strings.packStatsHelpProgress
        ),
        HelpTopic(
            iconRes = UIcons.Cards.Clock,
            iconTint = Gold700,
            circleColor = Gold100,
            title = strings.averageTimeStatLabel,
            body = strings.packStatsHelpAverageTime
        ),
        HelpTopic(
            iconRes = UIcons.Actions.Details,
            iconTint = Navy500,
            circleColor = Navy100,
            title = strings.packLastSessionLabel,
            body = strings.packStatsHelpLastSession
        ),
        HelpTopic(
            iconRes = UIcons.Actions.Play,
            iconTint = Purple700,
            circleColor = Purple100,
            title = strings.packMostUsedModeLabel,
            body = strings.packStatsHelpMostUsedMode
        ),
        HelpTopic(
            iconRes = UIcons.Cards.Check,
            iconTint = Teal700,
            circleColor = Teal100,
            title = strings.packBestPerformance,
            body = strings.packStatsHelpBestPerformance
        ),
        HelpTopic(
            iconRes = UIcons.Actions.Study,
            iconTint = Orange700,
            circleColor = Orange100,
            title = strings.packStatsHelpMasteryTitle,
            body = strings.packStatsHelpMastery
        ),
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = strings.packStatsHelpTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink950
                )
                UMarkdownText(
                    markdown = strings.packStatsHelpIntro,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral500,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        items(items) { topic ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Neutral100, RoundedCornerShape(URadius))
                    .background(Color.White, RoundedCornerShape(URadius))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(topic.circleColor, CircleShape),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(topic.iconRes),
                            contentDescription = null,
                            tint = topic.iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Ink950,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                UMarkdownText(
                    markdown = topic.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral500,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private data class HelpTopic(
    val iconRes: Int,
    val iconTint: Color,
    val circleColor: Color,
    val title: String,
    val body: String
)
