package com.uquiz.android.domain.content.model

/**
 * ### QuestionWithOptions
 *
 * Modelo de dominio compuesto que agrupa una [Question] junto con la colección
 * de [Option] asociadas a ella.
 *
 * Este modelo resulta útil cuando la aplicación necesita trabajar con una pregunta
 * ya enriquecida con sus posibles respuestas, evitando tener que manejar ambos
 * elementos por separado en casos de uso, repositorios o capa de presentación.
 *
 * Propiedades:
 * - [question]: objeto principal de la pregunta.
 * - [options]: lista de opciones de respuesta asociadas a la pregunta.
 */
data class QuestionWithOptions(
    val question: Question,
    val options: List<Option>,
) {
    /**
     * Opción correcta asociada a la pregunta.
     *
     * Esta propiedad calculada devuelve la primera opción de [options] marcada
     * como correcta mediante [Option.isCorrect].
     *
     * Si ninguna opción ha sido marcada como correcta, el resultado será `null`.
     */
    val correctOption: Option?
        get() = options.firstOrNull { it.isCorrect }
}
