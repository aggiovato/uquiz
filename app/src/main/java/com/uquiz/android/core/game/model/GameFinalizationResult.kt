package com.uquiz.android.core.game.model

import com.uquiz.android.domain.ranking.enums.UserRank

/**
 * Resultado de finalizar una sesión de Game mode.
 *
 * @param sessionScore  Score visible de la sesión (puede ser negativo).
 * @param xpGained      XP obtenida por la sesión (siempre ≥ 0).
 * @param previousRank  Rango del usuario antes de la sesión.
 * @param currentRank   Rango del usuario después de aplicar la sesión.
 * @param rankChanged   Indica si el rango cambió como consecuencia de la sesión.
 */
data class GameFinalizationResult(
    val sessionScore: Int,
    val xpGained: Long,
    val previousRank: UserRank,
    val currentRank: UserRank,
    val rankChanged: Boolean,
    /** MMR del usuario antes de aplicar la sesión, usado para animar el arco de progreso. */
    val previousMmr: Float,
)
