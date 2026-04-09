package com.uquiz.android.domain.importexport.repository

import com.uquiz.android.domain.importexport.projection.ExportedUQuizFile
import com.uquiz.android.domain.importexport.projection.ImportResult

/**
 * ### ImportExportRepository
 *
 * Contrato de repositorio del dominio encargado de coordinar las operaciones de
 * importación y exportación de contenido UQuiz.
 *
 * Esta interfaz abstrae la construcción de archivos exportables y la importación
 * transaccional de árboles de contenido, permitiendo que la capa de dominio
 * trabaje con un contrato estable sin depender de detalles de serialización,
 * almacenamiento o persistencia.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - exportar packs o subárboles de carpetas a un archivo `.uquiz`,
 * - importar contenido en la biblioteca raíz o dentro de una carpeta concreta,
 * - devolver resultados estructurados sobre la operación realizada.
 */
interface ImportExportRepository {
    /**
     * ### One-shot reads
     *
     * - exportPack(packId)
     * - exportFolderSubtree(folderId)
     */
    suspend fun exportPack(packId: String): ExportedUQuizFile
    suspend fun exportFolderSubtree(folderId: String): ExportedUQuizFile

    /**
     * ### Commands
     *
     * - importIntoLibrary(content)
     * - importIntoFolder(parentFolderId, content)
     */
    suspend fun importIntoLibrary(content: String): ImportResult
    suspend fun importIntoFolder(parentFolderId: String, content: String): ImportResult
}
