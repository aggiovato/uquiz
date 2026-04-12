package com.uquiz.android.ui.feature.game.screens.intro.model

import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * ### GameIntroUiState
 *
 * Estado de UI de la pantalla de introducción al Game mode para un pack concreto.
 *
 * Propiedades:
 * - [isLoading]: indica si la pantalla está cargando los datos del pack.
 * - [packId]: identificador del pack seleccionado.
 * - [packTitle]: título visible del pack.
 * - [questionCount]: número total de preguntas en el pack.
 * - [averageDifficulty]: dificultad media estimada del pack.
 * - [expectedPlayTimeMs]: tiempo estimado de partida en milisegundos.
 * - [hasActiveAttempt]: indica si existe una partida en progreso para este pack.
 * - [answeredCount]: número de preguntas ya respondidas en la partida activa.
 */
data class GameIntroUiState(
    val isLoading: Boolean = true,
    val packId: String = "",
    val packTitle: String = "",
    val questionCount: Int = 0,
    val averageDifficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val expectedPlayTimeMs: Long = 0L,
    val hasActiveAttempt: Boolean = false,
    val answeredCount: Int = 0,
)
