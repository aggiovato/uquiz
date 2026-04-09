package com.uquiz.android.domain.ranking.enums

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
enum class UserRank {
    INITIATE,
    NEOPHYTE,
    ACOLYTE,
    DISCIPLE,
    ADEPT,
    VIRTUOSO,
    ARCHON,
    PARAGON,
    ;

    companion object {
        /**
         * Devuelve el rango correspondiente al valor de MMR recibido.
         *
         * @param mmr Valor de rating usado para clasificar al usuario.
         */
        fun fromMmr(mmr: Float): UserRank = when {
            mmr < 800f -> INITIATE
            mmr < 1100f -> NEOPHYTE
            mmr < 1400f -> ACOLYTE
            mmr < 1700f -> DISCIPLE
            mmr < 2100f -> ADEPT
            mmr < 2500f -> VIRTUOSO
            mmr < 3000f -> ARCHON
            else -> PARAGON
        }
    }
}
