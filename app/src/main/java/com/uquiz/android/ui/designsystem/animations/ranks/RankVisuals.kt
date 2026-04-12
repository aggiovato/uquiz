package com.uquiz.android.ui.designsystem.animations.ranks

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.uquiz.android.domain.ranking.enums.UserRank
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### RankVisuals
 *
 * Fuente centralizada del mapeo `UserRank → (badge drawable + mascota composable)`.
 * Ambas piezas se resuelven en un único branch, evitando la divergencia entre
 * el drawable del label y el composable animado de la mascota.
 *
 * @param badge Recurso drawable del label/badge del rango.
 * @param mascot Lambda composable que renderiza la mascota animada correspondiente.
 */
@Immutable
data class RankVisuals(
    @param:DrawableRes val badge: Int,
    val mascot: @Composable (modifier: Modifier) -> Unit,
)

/**
 * Resuelve el par (badge, mascota) correspondiente al rango del usuario.
 *
 * Cuando se implemente una mascota específica para un rango (e.g. `UNeophyteMascot`),
 * basta con cambiar el lambda en ese branch — cero impacto en los callers.
 */
fun UserRank.rankVisuals(): RankVisuals =
    when (this) {
        UserRank.INITIATE -> RankVisuals(UIcons.Ranks.Initiate) { mod -> UInitiateMascot(modifier = mod) }
        UserRank.NEOPHYTE -> RankVisuals(UIcons.Ranks.Neophyte) { mod -> UInitiateMascot(modifier = mod) }
        UserRank.ACOLYTE -> RankVisuals(UIcons.Ranks.Acolyte) { mod -> UInitiateMascot(modifier = mod) }
        UserRank.DISCIPLE -> RankVisuals(UIcons.Ranks.Disciple) { mod -> UInitiateMascot(modifier = mod) }
        UserRank.ADEPT -> RankVisuals(UIcons.Ranks.Adept) { mod -> UInitiateMascot(modifier = mod) }
        UserRank.VIRTUOSO -> RankVisuals(UIcons.Ranks.Virtuoso) { mod -> UInitiateMascot(modifier = mod) }
        UserRank.ARCHON -> RankVisuals(UIcons.Ranks.Archon) { mod -> UInitiateMascot(modifier = mod) }
        UserRank.PARAGON -> RankVisuals(UIcons.Ranks.Paragon) { mod -> UInitiateMascot(modifier = mod) }
    }
