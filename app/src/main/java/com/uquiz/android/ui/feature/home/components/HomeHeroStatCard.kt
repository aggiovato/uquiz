package com.uquiz.android.ui.feature.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Orange500

/**
 * ### HomeHeroStatCard
 *
 * Tarjeta destacada de métrica principal en la cabecera de Home.
 *
 * @param value Valor principal que se quiere destacar.
 * @param label Etiqueta descriptiva de la métrica.
 * @param iconRes Recurso del icono asociado.
 * @param containerColor Color de fondo de la tarjeta.
 * @param iconTint Color aplicado al icono dentro del badge.
 */
@Composable
fun HomeHeroStatCard(
    value: String,
    label: String,
    @DrawableRes iconRes: Int,
    containerColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(URadius * 1.5f))
            .border(1.dp, containerColor.copy(alpha = 0.9f), RoundedCornerShape(URadius * 2))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(5.dp, CircleShape, clip = false)
                .background(Color.White, CircleShape)
                .border(1.dp, Neutral100, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.84f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@UPreview
@Composable
private fun HomeHeroStatCardPreview() {
    UTheme {
        HomeHeroStatCard(
            value = "240",
            label = "Puntos totales",
            iconRes = UIcons.Menu.Stats,
            containerColor = Navy500,
            iconTint = Orange500,
        )
    }
}
