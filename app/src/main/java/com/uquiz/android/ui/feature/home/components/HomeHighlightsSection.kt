package com.uquiz.android.ui.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.ui.designsystem.animations.ranks.UMascot
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### HomeHighlightsSection
 *
 * Resumen visual del rango, racha diaria y XP total del usuario en Home.
 *
 * @param currentRank Rango actual visible en el badge principal.
 * @param totalXp XP total acumulada del usuario.
 * @param dayStreak Racha diaria mostrada en la tarjeta inferior.
 * @param visible Controla la entrada secuencial de las tarjetas de métricas.
 */
@Composable
fun HomeHighlightsSection(
    currentRank: UserRank,
    totalXp: Long,
    dayStreak: Int,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        UMascot(
            rank = currentRank,
            modifier = Modifier.weight(1f),
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HomeAnimatedStatCard(visible = visible, delayMillis = 120) {
                HomeHeroStatCard(
                    value = dayStreak.toString(),
                    label = strings.home.homeDayStreakLabel,
                    iconRes = UIcons.Cards.Session,
                    containerColor = Teal700,
                    iconTint = Orange500,
                )
            }
            HomeAnimatedStatCard(visible = visible, delayMillis = 220) {
                HomeHeroStatCard(
                    value = totalXp.toString(),
                    label = strings.home.homeTotalXpLabel,
                    iconRes = UIcons.Menu.Stats,
                    containerColor = Navy500,
                    iconTint = Orange500,
                )
            }
        }
    }
}

@Composable
private fun HomeAnimatedStatCard(
    visible: Boolean,
    delayMillis: Int,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter =
            fadeIn(tween(420, delayMillis = delayMillis)) +
                slideInHorizontally(tween(460, delayMillis = delayMillis)) { it / 4 },
    ) {
        content()
    }
}

@UPreview
@Composable
private fun HomeHighlightsSectionPreview() {
    UTheme {
        HomeHighlightsSection(
            currentRank = UserRank.INITIATE,
            totalXp = 1280L,
            dayStreak = 7,
        )
    }
}
