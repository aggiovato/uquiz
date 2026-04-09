package com.uquiz.android.domain.content.model

/**
 * ### Option
 *
 * Modelo de dominio que representa una opción de respuesta asociada a una
 * pregunta dentro de la aplicación.
 *
 * Una `Option` forma parte del conjunto de respuestas posibles de una `Question`
 * y contiene tanto el contenido visible para el usuario como la información
 * necesaria para determinar si dicha opción es correcta.
 *
 * Propiedades:
 * - [id]: identificador único de la opción.
 * - [questionId]: identificador de la pregunta a la que pertenece esta opción.
 * - [label]: etiqueta visible de la opción, normalmente utilizada para distinguirla
 *   dentro de la interfaz (por ejemplo: `A`, `B`, `C`, `D`).
 * - [text]: contenido textual de la opción, mostrado al usuario como posible respuesta.
 * - [isCorrect]: indica si esta opción es la respuesta correcta de la pregunta.
 * - [createdAt]: marca temporal de creación de la opción.
 * - [updatedAt]: marca temporal de la última actualización realizada sobre la opción.
 */
data class Option(
    val id: String,
    val questionId: String,
    val label: String,
    val text: String,
    val isCorrect: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)
