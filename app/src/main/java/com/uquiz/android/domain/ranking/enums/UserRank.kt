package com.uquiz.android.domain.ranking.enums

import com.uquiz.android.domain.ranking.enums.UserRank.ACOLYTE
import com.uquiz.android.domain.ranking.enums.UserRank.ADEPT
import com.uquiz.android.domain.ranking.enums.UserRank.ARCHON
import com.uquiz.android.domain.ranking.enums.UserRank.DISCIPLE
import com.uquiz.android.domain.ranking.enums.UserRank.INITIATE
import com.uquiz.android.domain.ranking.enums.UserRank.NEOPHYTE
import com.uquiz.android.domain.ranking.enums.UserRank.PARAGON
import com.uquiz.android.domain.ranking.enums.UserRank.VIRTUOSO

/**
 * ### UserRank
 *
 * Enum que representa el rango competitivo o de progresión del usuario dentro
 * del sistema de ranking.
 *
 * Este valor permite clasificar al usuario en un tramo de habilidad a partir
 * de su MMR, facilitando la presentación de progreso, insignias y estados
 * visuales asociados al rendimiento acumulado.
 *
 * Rangos disponibles:
 * - [INITIATE]
 * - [NEOPHYTE]
 * - [ACOLYTE]
 * - [DISCIPLE]
 * - [ADEPT]
 * - [VIRTUOSO]
 * - [ARCHON]
 * - [PARAGON]
 */
enum class UserRank(
    /** Límite inferior de MMR necesario para pertenecer a este rango. */
    val lowerBoundMmr: Float,
) {
    INITIATE(0f),
    NEOPHYTE(400f),
    ACOLYTE(700f),
    DISCIPLE(1000f),
    ADEPT(1300f),
    VIRTUOSO(1650f),
    ARCHON(2000f),
    PARAGON(2500f),
    ;

    companion object {
        /**
         * Franja de histéresis de promoción (en puntos de MMR).
         *
         * El usuario necesita superar `nextRank.lowerBoundMmr + PROMOTION_HYSTERESIS`
         * para ser promovido. Este valor se reutiliza en [effectiveUpperMmr] y en
         * [com.uquiz.android.core.analytics.usecase.UpdateUserRankFromAttemptUseCase] para garantizar consistencia.
         */
        const val PROMOTION_HYSTERESIS = 50f

        /**
         * Devuelve el rango correspondiente al valor de MMR recibido.
         *
         * @param mmr Valor de rating usado para clasificar al usuario.
         */
        fun fromMmr(mmr: Float): UserRank =
            when {
                mmr < 400f -> INITIATE
                mmr < 700f -> NEOPHYTE
                mmr < 1000f -> ACOLYTE
                mmr < 1300f -> DISCIPLE
                mmr < 1650f -> ADEPT
                mmr < 2000f -> VIRTUOSO
                mmr < 2500f -> ARCHON
                else -> PARAGON
            }
    }
}

/** Devuelve el siguiente rango desbloqueable o `null` si ya está en el máximo. */
fun UserRank.nextRank(): UserRank? = UserRank.entries.getOrNull(ordinal + 1)

/**
 * Límite superior efectivo del tramo actual, incluyendo la franja de histéresis.
 *
 * Representa el MMR real necesario para ser promovido al siguiente rango y se usa
 * como techo honesto en [progressFraction] y [mmrToNextRank], evitando la zona muerta
 * donde el progreso visual llega al 100 % antes de que ocurra el cambio de rango.
 */
fun UserRank.effectiveUpperMmr(): Float {
    val next = nextRank() ?: return lowerBoundMmr
    return next.lowerBoundMmr + UserRank.PROMOTION_HYSTERESIS
}

/** Devuelve el MMR que falta para alcanzar el siguiente rango (usando el umbral efectivo con histéresis). */
fun UserRank.mmrToNextRank(mmr: Float): Int {
    if (nextRank() == null) return 0
    return (effectiveUpperMmr() - mmr).coerceAtLeast(0f).toInt()
}

/** Calcula el progreso dentro del tramo actual de rango usando el umbral efectivo con histéresis como techo. */
fun UserRank.progressFraction(mmr: Float): Float {
    val next = nextRank() ?: return 1f
    val span = effectiveUpperMmr() - lowerBoundMmr
    return ((mmr - lowerBoundMmr) / span).coerceIn(0f, 1f)
}
