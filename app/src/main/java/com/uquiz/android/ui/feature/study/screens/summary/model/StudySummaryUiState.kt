package com.uquiz.android.ui.feature.study.screens.summary.model

/**
 * ### StudySummaryUiState
 *
 * Modelo de estado de UI utilizado por la pantalla de resumen de una sesión
 * de estudio.
 *
 * Este estado concentra la información necesaria para renderizar el resultado
 * final de un intento de estudio, incluyendo el pack asociado, el total de
 * preguntas respondidas, el rendimiento obtenido y el tiempo efectivo empleado.
 *
 * Propiedades:
 * - [isLoading]: indica si la pantalla sigue cargando la información necesaria.
 * - [attemptId]: identificador del intento de estudio resumido en la pantalla.
 * - [packId]: identificador del pack asociado al intento. Puede ser `null`
 *   si no existe asociación directa o todavía no se ha resuelto.
 * - [packTitle]: título visible del pack asociado al intento.
 * - [totalQuestions]: cantidad total de preguntas incluidas en el intento.
 * - [correctAnswers]: cantidad de respuestas correctas registradas en el intento.
 * - [incorrectAnswers]: cantidad de respuestas incorrectas registradas en el intento.
 * - [accuracyPercent]: porcentaje de acierto obtenido en el intento.
 * - [effectiveTimeMs]: tiempo efectivo empleado durante la sesión, expresado en milisegundos.
 */
data class StudySummaryUiState(
    val isLoading: Boolean = true,
    val attemptId: String,
    val packId: String? = null,
    val packTitle: String = "",
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val accuracyPercent: Int = 0,
    val effectiveTimeMs: Long = 0L,
)
