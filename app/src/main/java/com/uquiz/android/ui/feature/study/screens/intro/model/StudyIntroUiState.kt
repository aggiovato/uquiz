package com.uquiz.android.ui.feature.study.screens.intro.model

import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * ### StudyIntroUiState
 *
 * Modelo de estado de UI utilizado por la pantalla de introducción al modo
 * de estudio de un pack.
 *
 * Este estado concentra la información necesaria para renderizar la vista inicial
 * previa al comienzo de una sesión de estudio, incluyendo los datos básicos del
 * pack, un resumen de su contenido y la posible existencia de un intento activo.
 *
 * Propiedades:
 * - [isLoading]: indica si la pantalla sigue cargando la información necesaria.
 * - [packId]: identificador del pack que se va a estudiar.
 * - [packTitle]: título visible del pack.
 * - [questionCount]: cantidad total de preguntas incluidas en el pack.
 * - [averageDifficulty]: dificultad media estimada del pack. Puede ser `null`
 *   si todavía no se ha podido calcular o no hay datos suficientes.
 * - [hasActiveAttempt]: indica si existe un intento de estudio activo asociado
 *   al pack, lo que permite decidir si se ofrece continuar o empezar de nuevo.
 * - [activeProgress]: progreso actual del intento activo, normalmente expresado
 *   como número de preguntas ya recorridas o completadas.
 */
data class StudyIntroUiState(
    val isLoading: Boolean = true,
    val packId: String,
    val packTitle: String = "",
    val questionCount: Int = 0,
    val averageDifficulty: DifficultyLevel? = null,
    val hasActiveAttempt: Boolean = false,
    val activeProgress: Int = 0,
)
