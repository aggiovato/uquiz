package com.uquiz.android.domain.content.repository

import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.Option
import com.uquiz.android.domain.content.model.Question
import com.uquiz.android.domain.content.model.QuestionWithOptions
import kotlinx.coroutines.flow.Flow

/**
 * ### QuestionRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar las operaciones
 * relacionadas con [Question] y sus opciones de respuesta asociadas.
 *
 * Esta interfaz define el acceso a lectura, consulta puntual y modificación de
 * preguntas dentro de la aplicación, abstrayendo la fuente de datos concreta
 * utilizada por la capa de datos.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - consultar preguntas de forma reactiva,
 * - recuperar preguntas de forma puntual cuando se necesita una lectura inmediata,
 * - obtener una pregunta enriquecida junto con sus opciones mediante [QuestionWithOptions],
 * - crear y actualizar preguntas con su contenido principal y configuración asociada,
 * - gestionar el ciclo de vida de las opciones de respuesta de cada pregunta.
 *
 * Además de exponer el modelo de dominio [Question], este repositorio también
 * permite trabajar con estructuras compuestas que agrupan la pregunta con sus
 * posibles respuestas, facilitando así su uso en casos de uso y en la capa de presentación.
 *
 * Al tratarse de un contrato de dominio, su definición no depende de detalles
 * de implementación específicos como Room, red, caché o cualquier otro mecanismo
 * de persistencia.
 */
interface QuestionRepository {
    /**
     * ### Reactive reads
     *
     * - observeById(questionId)
     * - observeWithOptions(questionId)
     */
    fun observeById(questionId: String): Flow<Question?>

    fun observeWithOptions(questionId: String): Flow<QuestionWithOptions?>

    /**
     * ### One-shot reads
     *
     * - getById(questionId)
     * - getWithOptions(questionId)
     */
    suspend fun getById(questionId: String): Question?

    suspend fun getWithOptions(questionId: String): QuestionWithOptions?

    /**
     * ### Commands
     *
     * - createQuestion(text, explanation, difficulty, options)
     * - updateQuestion(question)
     * - updateOptions(questionId, options)
     * - deleteQuestion(questionId)
     */
    suspend fun createQuestion(
        text: String,
        explanation: String? = null,
        difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
        options: List<CreateOptionData>,
    ): Question

    suspend fun updateQuestion(question: Question)

    suspend fun updateOptions(
        questionId: String,
        options: List<Option>,
    )

    suspend fun deleteQuestion(questionId: String)
}
