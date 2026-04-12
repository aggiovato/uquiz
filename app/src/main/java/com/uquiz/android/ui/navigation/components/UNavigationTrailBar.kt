package com.uquiz.android.ui.navigation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.navigation.chrome.NavigationChromeVariant
import com.uquiz.android.ui.navigation.tree.NavigationTrailItem

private sealed interface UTrailToken {
    data class Item(
        val item: NavigationTrailItem,
    ) : UTrailToken

    data object Ellipsis : UTrailToken
}

/**
 * Muestra la ruta de navegación actual en la cabecera compartida.
 *
 * @param trail Lista ordenada desde la raíz hasta el destino actual.
 * @param variant Variante visual del chrome de navegación.
 * @param modifier Modifier aplicado al contenedor.
 */
@Composable
fun UNavigationTrailBar(
    trail: List<NavigationTrailItem>,
    variant: NavigationChromeVariant = NavigationChromeVariant.Default,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        when (variant) {
            NavigationChromeVariant.Default -> BrandNavy
            NavigationChromeVariant.TransparentLight -> Color.Transparent
        }
    val primaryColor =
        when (variant) {
            NavigationChromeVariant.Default -> Color.White
            NavigationChromeVariant.TransparentLight -> Color.White.copy(alpha = 0.95f)
        }
    val secondaryColor = primaryColor.copy(alpha = 0.6f)
    val dividerColor = primaryColor.copy(alpha = 0.4f)
    val ellipsisBackground = primaryColor.copy(alpha = 0.12f)
    val animatedBackgroundColor =
        animateColorAsState(
            targetValue = backgroundColor,
            animationSpec = tween(durationMillis = 260),
            label = "trailBackground",
        )
    val animatedPrimaryColor =
        animateColorAsState(
            targetValue = primaryColor,
            animationSpec = tween(durationMillis = 260),
            label = "trailPrimary",
        )
    val animatedSecondaryColor =
        animateColorAsState(
            targetValue = secondaryColor,
            animationSpec = tween(durationMillis = 260),
            label = "trailSecondary",
        )
    val animatedDividerColor =
        animateColorAsState(
            targetValue = dividerColor,
            animationSpec = tween(durationMillis = 260),
            label = "trailDivider",
        )
    val animatedEllipsisBackground =
        animateColorAsState(
            targetValue = ellipsisBackground,
            animationSpec = tween(durationMillis = 260),
            label = "trailEllipsis",
        )
    val visibleTrail =
        when {
            trail.size <= 3 -> {
                trail.map { UTrailToken.Item(it) }
            }

            else -> {
                listOf(
                    UTrailToken.Item(trail.first()),
                    UTrailToken.Ellipsis,
                    UTrailToken.Item(trail[trail.lastIndex - 1]),
                    UTrailToken.Item(trail.last()),
                )
            }
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(animatedBackgroundColor.value)
                .padding(bottom = 10.dp)
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visibleTrail.forEachIndexed { index, token ->
            if (index > 0) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = animatedDividerColor.value,
                    modifier = Modifier.size(14.dp),
                )
            }
            when (token) {
                UTrailToken.Ellipsis -> {
                    Box(
                        modifier =
                            Modifier
                                .background(animatedEllipsisBackground.value, RoundedCornerShape(999.dp))
                                .padding(horizontal = 5.dp, vertical = 0.dp),
                    ) {
                        Text(
                            text = "...",
                            style = MaterialTheme.typography.labelSmall.copy(lineHeight = 10.sp),
                            color = animatedPrimaryColor.value.copy(alpha = 0.9f),
                        )
                    }
                }

                is UTrailToken.Item -> {
                    val isCurrent = token.item == trail.lastOrNull()
                    Text(
                        text = token.item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isCurrent) animatedPrimaryColor.value else animatedSecondaryColor.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = if (isCurrent) 110.dp else 84.dp),
                    )
                }
            }
        }
    }
}
