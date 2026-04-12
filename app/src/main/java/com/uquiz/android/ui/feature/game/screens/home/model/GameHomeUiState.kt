package com.uquiz.android.ui.feature.game.screens.home.model

import com.uquiz.android.domain.stats.projection.PackGameCard

/**
 * ### GameHomeUiState
 *
 * Estado de UI de la pantalla principal del Game mode.
 *
 * Propiedades:
 * - [isLoading]: indica si la pantalla está cargando los datos iniciales.
 * - [searchQuery]: texto de búsqueda actual introducido por el usuario.
 * - [activeGames]: packs con una partida en progreso.
 * - [recentPacks]: últimos packs sin partida activa, limitados a 10 por orden de actualización.
 * - [searchResults]: resultados filtrados cuando [searchQuery] no está vacío.
 */
data class GameHomeUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val activeGames: List<PackGameCard> = emptyList(),
    val recentPacks: List<PackGameCard> = emptyList(),
    val searchResults: List<PackGameCard> = emptyList(),
    val hasPlayablePacks: Boolean = false,
)
