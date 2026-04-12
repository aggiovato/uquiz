package com.uquiz.android.ui.feature.home.model

/**
 * Eventos efímeros emitidos desde Home para acciones de navegación.
 */
sealed interface HomeUiEvent {
    /** Solicita abrir el flujo Game para un pack aleatorio. */
    data class OpenRandomGamePack(val packId: String) : HomeUiEvent

    /** Solicita abrir el flujo Study para un pack aleatorio. */
    data class OpenRandomStudyPack(val packId: String) : HomeUiEvent
}
