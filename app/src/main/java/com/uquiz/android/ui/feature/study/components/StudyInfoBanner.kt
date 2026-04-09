package com.uquiz.android.ui.feature.study.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### StudyInfoBanner
 *
 * Banner informativo sobre fondo oscuro que muestra un mensaje de texto con un indicador circular.
 *
 * Se usa en la pantalla de introducción al estudio para indicar el progreso de una sesión previa.
 *
 * @param text Mensaje informativo a mostrar.
 */
@Composable
fun StudyInfoBanner(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(AppRadius * 1.5f),
        color = Color.White.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(9.dp)
                        .background(Teal500, CircleShape),
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
        }
    }
}

@UPreview
@Composable
private fun StudyInfoBannerPreview() {
    UTheme {
        StudyInfoBanner(
            text = "Este es el preview del Study Info Banner",
        )
    }
}
