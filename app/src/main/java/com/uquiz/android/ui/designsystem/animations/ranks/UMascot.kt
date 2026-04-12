package com.uquiz.android.ui.designsystem.animations.ranks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UMascot
 *
 * Componente público que combina la mascota animada y el badge de rango del usuario.
 * Resuelve automáticamente los visuales correspondientes al [rank] indicado a través
 * de [RankVisuals], delegando la animación del badge a [URankBadge] y la mascota
 * al composable interno del mapeo.
 *
 * @param rank Rango del usuario que determina la mascota y el badge mostrados.
 * @param mascotSize Tamaño total de la mascota animada.
 * @param badgeSize Tamaño del badge de rango.
 */
@Composable
fun UMascot(
    rank: UserRank,
    mascotSize: Dp = 155.dp,
    badgeSize: Dp = 100.dp,
    modifier: Modifier = Modifier,
) {
    val visuals = remember(rank) { rank.rankVisuals() }

    Box(
        modifier = modifier.height(160.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        visuals.mascot(Modifier.size(mascotSize))
        URankBadge(
            badge = visuals.badge,
            badgeSize = badgeSize,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 120.dp),
        )
    }
}

@UPreview
@Composable
private fun UMascotPreview() {
    UTheme {
        UMascot(rank = UserRank.INITIATE)
    }
}
