package com.uquiz.android.ui.designsystem.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Gold50
import com.uquiz.android.ui.designsystem.tokens.Gold700
import com.uquiz.android.ui.designsystem.tokens.Navy50
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Teal50
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/** Tonos disponibles para [UInfoStatCard]. */
enum class UInfoStatTone { Brand, Secondary, Tertiary }

/**
 * ### UInfoStatCard
 *
 * Muestra una tarjeta compacta para una métrica destacada.
 *
 * @param value Valor principal mostrado en la tarjeta.
 * @param label Etiqueta descriptiva de la métrica.
 * @param tone Tono visual aplicado a la tarjeta.
 */
@Composable
fun UInfoStatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    tone: UInfoStatTone = UInfoStatTone.Brand,
) {
    val (containerColor, valueColor) = when (tone) {
        UInfoStatTone.Brand -> Navy50 to Navy500
        UInfoStatTone.Secondary -> Teal50 to Teal700
        UInfoStatTone.Tertiary -> Gold50 to Gold700
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(URadius * 2),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Neutral500,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@UPreview
@Composable
private fun UInfoStatCardPreview() {
    UTheme {
        UInfoStatCard(value = "42", label = "Preguntas")
    }
}
