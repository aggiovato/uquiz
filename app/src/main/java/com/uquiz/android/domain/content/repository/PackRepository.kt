package com.uquiz.android.domain.content.repository

import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.model.QuestionWithOptions
import com.uquiz.android.domain.content.projection.PackWithQuestionCount
import kotlinx.coroutines.flow.Flow

/**
 * ### PackRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar las operaciones
 * relacionadas con [Pack] y su vínculo con las preguntas asociadas.
 *
 * Esta interfaz define el acceso a lectura, consulta puntual, modificación y
 * validación de packs dentro de la jerarquía de contenido de la aplicación,
 * abstrayendo la fuente de datos concreta utilizada por la capa de datos.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - consultar packs de forma reactiva,
 * - recuperar información puntual cuando se necesita una lectura inmediata,
 * - trabajar con proyecciones orientadas a UI, como [PackWithQuestionCount],
 * - obtener la composición completa de un pack junto con sus preguntas y opciones,
 * - gestionar la asociación y el orden de las preguntas dentro de cada pack,
 * - aplicar validaciones de negocio relacionadas con la unicidad y organización del contenido.
 *
 * Además de exponer modelos de dominio puros como [Pack], este repositorio también
 * permite acceder a estructuras enriquecidas como [QuestionWithOptions], necesarias
 * para casos de uso donde un pack debe resolverse con to-do su contenido listo para
 * ser mostrado o procesado.
 *
 * Al tratarse de un contrato de dominio, su definición no depende de detalles
 * de implementación específicos como Room, red, caché o cualquier otro mecanismo
 * de persistencia.
 */
interface PackRepository {
    /**
     * ### Reactive reads
     *
     * - observeAll()
     * - observeAllWithQuestionCounts()
     * - observeById(packId)
     * - observeByFolder(folderId)
     * - observeByFolderWithQuestionCounts(folderId)
     * - observeWithQuestions(packId)
     */
    fun observeAll(): Flow<List<Pack>>

    fun observeAllWithQuestionCounts(): Flow<List<PackWithQuestionCount>>

    fun observeById(packId: String): Flow<Pack?>

    fun observeByFolder(folderId: String): Flow<List<Pack>>

    fun observeByFolderWithQuestionCounts(folderId: String): Flow<List<PackWithQuestionCount>>

    fun observeWithQuestions(packId: String): Flow<List<QuestionWithOptions>>

    /**
     * ### One-shot reads
     *
     * - getById(packId)
     * - getByFolder(folderId)
     * - getPackPath(packId)
     * - getWithQuestions(packId)
     * - getQuestionCount(packId)
     */
    suspend fun getById(packId: String): Pack?

    suspend fun getByFolder(folderId: String): List<Pack>

    suspend fun getPackPath(packId: String): Pair<Pack, List<Folder>>?

    suspend fun getWithQuestions(packId: String): List<QuestionWithOptions>

    suspend fun getQuestionCount(packId: String): Int

    /**
     * ### Commands
     *
     * - createPack(title, description, folderId, icon, colorHex)
     * - updatePack(pack)
     * - deletePack(packId)
     * - setPackQuestions(packId, orderedQuestionIds)
     * - reorderQuestions(packId, orderedQuestionIds)
     * - addQuestionToPack(packId, questionId, sortOrder)
     * - removeQuestionFromPack(packId, questionId)
     */
    suspend fun createPack(
        title: String,
        description: String? = null,
        folderId: String,
        icon: String? = null,
        colorHex: String? = null,
    ): Pack

    suspend fun updatePack(pack: Pack)

    suspend fun deletePack(packId: String)

    suspend fun setPackQuestions(
        packId: String,
        orderedQuestionIds: List<String>,
    )

    suspend fun reorderQuestions(
        packId: String,
        orderedQuestionIds: List<String>,
    ) = setPackQuestions(packId, orderedQuestionIds)

    suspend fun addQuestionToPack(
        packId: String,
        questionId: String,
        sortOrder: Int,
    )

    suspend fun removeQuestionFromPack(
        packId: String,
        questionId: String,
    )

    /**
     * ### Business validation
     *
     * - existsPackTitleInFolder(folderId, title, excludeId)
     */
    suspend fun existsPackTitleInFolder(
        folderId: String,
        title: String,
        excludeId: String? = null,
    ): Boolean
}
