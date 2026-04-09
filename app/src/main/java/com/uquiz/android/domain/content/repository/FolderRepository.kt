package com.uquiz.android.domain.content.repository

import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.projection.FolderWithCounts
import kotlinx.coroutines.flow.Flow

/**
 * ### FolderRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar las operaciones
 * relacionadas con [Folder].
 *
 * Esta interfaz define el acceso a lectura, consulta puntual, modificación y
 * validación de carpetas dentro de la jerarquía de contenido de la aplicación,
 * abstrayendo la fuente de datos concreta utilizada por la capa de datos.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - consultar carpetas de forma reactiva,
 * - recuperar datos puntuales cuando se necesita una lectura inmediata,
 * - ejecutar comandos de creación, actualización, eliminación y movimiento,
 * - aplicar validaciones de negocio relacionadas con la estructura de carpetas.
 *
 * Además, este repositorio permite trabajar tanto con modelos de dominio puros
 * ([Folder]) como con proyecciones orientadas a UI, como [FolderWithCounts],
 * cuando la capa de presentación necesita información agregada.
 *
 * Al tratarse de un contrato de dominio, su definición no depende de detalles
 * de implementación específicos como Room, red, caché o cualquier otra fuente
 * de persistencia.
 */
interface FolderRepository {
    /**
     * ### Reactive reads
     *
     * - observeByParent(parentId)  — parentId = null → root folders
     * - observeByParentWithCounts(parentId)
     * - observeById(id)
     */
    fun observeByParent(parentId: String?): Flow<List<Folder>>

    fun observeByParentWithCounts(parentId: String?): Flow<List<FolderWithCounts>>

    fun observeById(id: String): Flow<Folder?>

    /**
     * ### One-shot reads
     *
     * - getById(id)
     * - getAll()
     * - getFolderPath(folderId)
     */
    suspend fun getById(id: String): Folder?

    suspend fun getAll(): List<Folder>

    suspend fun getFolderPath(folderId: String): List<Folder>

    /**
     * ### Commands
     *
     * - createFolder(name, parentId, colorHex, icon)
     * - updateFolder(folder)
     * - deleteFolder(folderId)
     * - moveFolder(folderId, newParentId)
     */
    suspend fun createFolder(
        name: String,
        parentId: String? = null,
        colorHex: String? = null,
        icon: String? = null,
    ): Folder

    suspend fun updateFolder(folder: Folder)

    suspend fun deleteFolder(folderId: String)

    suspend fun moveFolder(
        folderId: String,
        newParentId: String,
    )

    /**
     * ### Business validation
     *
     * - existsSiblingFolderName(parentId, name, excludeId)
     */
    suspend fun existsSiblingFolderName(
        parentId: String?,
        name: String,
        excludeId: String? = null,
    ): Boolean
}
