package com.uquiz.android.ui.navigation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * Muestra la barra superior compartida de la aplicación.
 *
 * @param title Título actual de la pantalla.
 * @param canNavigateBack Indica si debe mostrarse la acción de volver atrás.
 * @param onBackClick Se invoca al pulsar el botón de volver.
 * @param selectedLang Código del idioma seleccionado.
 * @param onLangSelect Se invoca al seleccionar un idioma.
 * @param variant Variante visual del chrome de navegación.
 * @param modifier Modifier aplicado al contenedor.
 */
@Composable
fun UTopBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    selectedLang: String,
    onLangSelect: (String) -> Unit,
    variant: NavigationChromeVariant = NavigationChromeVariant.Default,
    modifier: Modifier = Modifier,
) {
    var showLanguageMenu by remember { mutableStateOf(false) }
    val strings = LocalStrings.current
    val currentLang = uAppLanguages.firstOrNull { it.code == selectedLang } ?: uAppLanguages.first()
    val density = LocalDensity.current
    val statusBarTop = WindowInsets.statusBars.getTop(density)
    val dropdownOffsetY = statusBarTop + with(density) { (56.dp + 23.dp).roundToPx() }
    val dropdownOffsetX = with(density) { (-5).dp.roundToPx() }
    val backgroundColor = when (variant) {
        NavigationChromeVariant.Default -> BrandNavy
        NavigationChromeVariant.TransparentLight -> Color.Transparent
    }
    val contentColor = when (variant) {
        NavigationChromeVariant.Default -> Color.White
        NavigationChromeVariant.TransparentLight -> Color.White.copy(alpha = 0.96f)
    }
    val animatedBackgroundColor = animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 260),
        label = "topBarBackground",
    )
    val animatedContentColor = animateColorAsState(
        targetValue = contentColor,
        animationSpec = tween(durationMillis = 260),
        label = "topBarContent",
    )

    Box {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(animatedBackgroundColor.value)
                .statusBarsPadding()
                .height(50.dp)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = strings.back,
                        tint = animatedContentColor.value,
                    )
                }
            } else {
                Spacer(Modifier.width(16.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = animatedContentColor.value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(UIcons.Actions.LightMode),
                    contentDescription = strings.toggleTheme,
                    tint = animatedContentColor.value.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp),
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(URadius))
                    .clickable { showLanguageMenu = !showLanguageMenu }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(currentLang.flagRes),
                    contentDescription = currentLang.name(strings),
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape),
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = currentLang.code.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = animatedContentColor.value,
                )
            }
        }

        if (showLanguageMenu) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(dropdownOffsetX, dropdownOffsetY),
                onDismissRequest = { showLanguageMenu = false },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(URadius))
                        .background(BrandNavy)
                        .padding(vertical = 4.dp),
                ) {
                    uAppLanguages.forEach { lang ->
                        val isSelected = lang.code == selectedLang
                        Row(
                            modifier = Modifier
                                .width(172.dp)
                                .clickable {
                                    onLangSelect(lang.code)
                                    showLanguageMenu = false
                                }
                                .background(if (isSelected) Color.White.copy(alpha = 0.12f) else Color.Transparent)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(lang.flagRes),
                                contentDescription = lang.name(strings),
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape),
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = lang.name(strings),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
