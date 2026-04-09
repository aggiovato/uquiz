package com.uquiz.android.domain.content.model

import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * ### Question
 *
 * Modelo de dominio que representa una pregunta dentro de la aplicación.
 *
 * Una `Question` contiene el enunciado principal que será mostrado al usuario
 * durante una sesión de estudio o juego, junto con una explicación opcional y
 * un nivel de dificultad asociado.
 *
 * Propiedades:
 * - [id]: identificador único de la pregunta.
 * - [text]: enunciado o contenido principal de la pregunta.
 * - [explanation]: explicación opcional asociada a la pregunta. Puede utilizarse
 *   para mostrar retroalimentación, aclaraciones o contexto adicional tras responder.
 * - [difficulty]: nivel de dificultad asignado a la pregunta. Por defecto se
 *   considera [DifficultyLevel.MEDIUM].
 * - [createdAt]: marca temporal de creación de la pregunta.
 * - [updatedAt]: marca temporal de la última actualización realizada sobre la pregunta.
 */
data class Question(
    val id: String,
    val text: String,
    val explanation: String? = null,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val createdAt: Long,
    val updatedAt: Long,
)
