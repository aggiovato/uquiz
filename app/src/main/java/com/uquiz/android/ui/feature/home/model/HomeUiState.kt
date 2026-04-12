package com.uquiz.android.ui.feature.home.model

import com.uquiz.android.domain.ranking.enums.UserRank

/**
 * Modelo de UI usado para renderizar packs con progreso resumido en Home.
 */
data class ContinuePackUiModel(
    val packId: String,
    val title: String,
    val answeredCount: Int,
    val totalQuestions: Int,
    val progressFraction: Float,
    val icon: String?,
    val colorHex: String?
)

/**
 * Estado de UI de la pantalla principal.
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val displayName: String = "",
    val avatarIcon: String? = null,
    val avatarImageUri: String? = null,
    val currentRank: UserRank = UserRank.INITIATE,
    val dayStreak: Int = 0,
    /** MMR del usuario, representa el score competitivo. */
    val score: Int = 0,
    /** MMR exacto usado para calcular el progreso dentro del rango actual. */
    val scoreMmr: Float = 0f,
    /** XP total acumulada del usuario (nunca disminuye). */
    val totalXp: Long = 0L,
    val hasPlayablePacks: Boolean = false,
    val continuePlaying: List<ContinuePackUiModel> = emptyList(),
    val continueStudying: List<ContinuePackUiModel> = emptyList()
)
