package com.uquiz.android.ui.feature.game.screens.summary.model

import com.uquiz.android.domain.ranking.enums.UserRank

/**
 * ### GameSummaryUiState
 *
 * Estado de UI de la pantalla de resumen al finalizar una sesión del Game mode.
 *
 * Propiedades:
 * - [isLoading]: indica si el resumen está cargando los datos del intento.
 * - [packId]: identificador del pack jugado. Puede ser null si no se pudo resolver.
 * - [sessionScore]: puntuación visible de la sesión (puede ser negativa).
 * - [correctAnswers]: número de respuestas correctas.
 * - [incorrectAnswers]: número de respuestas incorrectas o por tiempo.
 * - [accuracyPercent]: porcentaje de acierto sobre el total de preguntas.
 * - [durationMs]: duración total de la sesión en milisegundos.
 * - [xpGained]: XP total obtenida durante la sesión.
 * - [previousRank]: rango del usuario antes de esta sesión.
 * - [currentRank]: rango del usuario después de aplicar esta sesión.
 * - [rankChanged]: indica si el rango cambió como consecuencia de la sesión.
 * - [previousMmr]: MMR antes de la sesión, punto de inicio de la animación del arco.
 * - [mmr]: valor de MMR actual (tras la sesión), punto final de la animación del arco.
 */
data class GameSummaryUiState(
    val isLoading: Boolean = true,
    val packId: String? = null,
    val sessionScore: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val accuracyPercent: Int = 0,
    val durationMs: Long = 0L,
    val xpGained: Long = 0L,
    val previousRank: UserRank = UserRank.INITIATE,
    val currentRank: UserRank = UserRank.INITIATE,
    val rankChanged: Boolean = false,
    val previousMmr: Float = 600f,
    val mmr: Float = 600f,
)
