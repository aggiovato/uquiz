package com.uquiz.android.ui.feature.library.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UDarkButton
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### LibraryModeBanner
 *
 * Banner hero de la pantalla de biblioteca con ilustración de fondo en modo estudio
 * y acceso rápido a una sesión de estudio aleatoria.
 *
 * @param visible Indica si deben reproducirse las animaciones de entrada del contenido.
 * @param enabled Indica si existe al menos un pack con preguntas para la acción aleatoria.
 * @param onRandomStudyClick Acción al pulsar el CTA de estudio aleatorio.
 */
@Composable
fun LibraryModeBanner(
    visible: Boolean,
    enabled: Boolean,
    onRandomStudyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp),
        shape = RoundedCornerShape(AppRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.study_mode_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xD9071221),
                                Color(0xA61A2433),
                                Color(0x401A2433),
                            ),
                        ),
                    ),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                // Grupo 1: texto del banner con animación de entrada desde la izquierda
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 120)) +
                        slideInHorizontally(tween(520, delayMillis = 120)) { -it / 4 },
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = strings.library.libraryBannerLine1,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.graphicsLayer { rotationZ = -4f },
                        )
                        Text(
                            text = strings.library.libraryBannerLine2,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.92f),
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        Text(
                            text = strings.library.libraryBannerLine3,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.graphicsLayer { rotationZ = 2f },
                        )
                    }
                }

                // Grupo 2: botón de estudio aleatorio con mayor retraso para efecto escalonado
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 240)) +
                        slideInHorizontally(tween(560, delayMillis = 240)) { -it / 5 },
                ) {
                    UDarkButton(
                        text = strings.home.homeRandomStudy,
                        onClick = onRandomStudyClick,
                        size = UButtonSize.Tiny,
                        fullWidth = false,
                        enabled = enabled,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }
    }
}

@UPreview
@Composable
private fun LibraryModeBannerPreview() {
    UTheme {
        Box(
            modifier = Modifier
                .background(BrandNavy)
                .padding(16.dp),
        ) {
            LibraryModeBanner(
                visible = true,
                enabled = true,
                onRandomStudyClick = {},
            )
        }
    }
}
