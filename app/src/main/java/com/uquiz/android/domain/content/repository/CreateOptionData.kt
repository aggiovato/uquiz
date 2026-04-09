package com.uquiz.android.domain.content.repository

/**
 * ### CreateOptionData
 *
 * Modelo de entrada utilizado para definir los datos necesarios al crear una
 * nueva opción de respuesta asociada a una pregunta.
 *
 * Este objeto se utiliza principalmente en operaciones de creación dentro de
 * los repositorios o casos de uso, cuando todavía no existe una [com.uquiz.android.domain.content.model.Option]
 * persistida y solo se dispone de la información mínima requerida para construirla.
 *
 * A diferencia de [com.uquiz.android.domain.content.model.Option], este modelo no incluye identificadores ni marcas
 * temporales, ya que representa únicamente los datos iniciales proporcionados
 * para la creación de la opción.
 *
 * Propiedades:
 * - [label]: etiqueta visible de la opción, normalmente utilizada para distinguirla
 *   dentro de la interfaz (por ejemplo: `A`, `B`, `C`, `D`).
 * - [text]: contenido textual de la opción, mostrado al usuario como posible respuesta.
 * - [isCorrect]: indica si esta opción debe marcarse como correcta al momento de crearla.
 */
data class CreateOptionData(
    val label: String,
    val text: String,
    val isCorrect: Boolean,
)
