package com.uquiz.android.ui.feature.game.screens.home.model

/** Eventos efímeros consumidos por la pantalla principal del Game mode. */
sealed interface GameHomeUiEvent {
    data class OpenRandomPack(val packId: String) : GameHomeUiEvent
}
