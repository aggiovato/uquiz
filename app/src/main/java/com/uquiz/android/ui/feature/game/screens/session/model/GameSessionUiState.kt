package com.uquiz.android.ui.feature.game.screens.session.model

/**
 * ### GameSessionUiState
 *
 * Estado de UI de la pantalla de sesión activa del Game mode.
 *
 * Propiedades:
 * - [isLoading]: indica si la sesión está cargando el plan de preguntas.
 * - [packTitle]: título del pack que se está jugando.
 * - [attemptId]: identificador del intento activo.
 * - [questions]: lista de preguntas en el orden del plan de juego.
 * - [currentIndex]: índice de la pregunta visible en pantalla.
 * - [elapsedMs]: tiempo transcurrido desde que apareció la pregunta actual.
 * - [selectedOptionId]: opción seleccionada por el usuario antes de que se registre la respuesta.
 * - [isAnswered]: true cuando la respuesta ha sido registrada (incluye timeout).
 * - [feedbackVisible]: true mientras se muestra el overlay de feedback.
 * - [feedbackPositive]: true si el feedback es positivo (respuesta correcta).
 * - [lastQuestionScore]: puntos obtenidos en la última pregunta respondida.
 * - [runningScore]: puntuación acumulada durante la sesión.
 */
data class GameSessionUiState(
    val isLoading: Boolean = true,
    val packTitle: String = "",
    val attemptId: String? = null,
    val questions: List<GameQuestionUiModel> = emptyList(),
    val currentIndex: Int = 0,
    val elapsedMs: Long = 0L,
    val selectedOptionId: String? = null,
    val isAnswered: Boolean = false,
    val feedbackVisible: Boolean = false,
    val feedbackPositive: Boolean = true,
    val lastQuestionScore: Int = 0,
    val runningScore: Int = 0,
) {
    val currentQuestion: GameQuestionUiModel?
        get() = questions.getOrNull(currentIndex)
}
