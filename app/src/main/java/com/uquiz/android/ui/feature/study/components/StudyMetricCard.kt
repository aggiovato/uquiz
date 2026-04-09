package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Navy200
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### StudyMetricCard
 *
 * Displays a study mode metric card on a dark background.
 *
 * The card container always uses a neutral white-alpha style (same as [StudyInfoBanner])
 * so visual consistency is independent of the metric being shown.
 *
 * @param value      Main value to highlight (number, percentage, time).
 * @param label      Descriptive label shown below the value.
 * @param valueColor Color applied to the value text only; does not affect the card border.
 */
@Composable
fun StudyMetricCard(
    value: String,
    label: String,
    valueColor: Color = Navy200,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(AppRadius),
        color = Color.White.copy(alpha = 0.09f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = valueColor,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Neutral100.copy(alpha = 0.86f),
            )
        }
    }
}

@UPreview
@Composable
private fun StudyMetricCardPreview() {
    UTheme {
        StudyMetricCard(
            value = "Valor: 3%",
            label = "Esta es la variable",
        )
    }
}
