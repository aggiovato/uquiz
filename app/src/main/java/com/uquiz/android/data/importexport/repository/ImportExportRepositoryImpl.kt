package com.uquiz.android.data.importexport.repository

import androidx.room.withTransaction
import com.uquiz.android.data.importexport.parser.UQuizJsonParser
import com.uquiz.android.data.importexport.validator.UQuizImportValidator
import com.uquiz.android.data.local.db.UQuizDatabase
import com.uquiz.android.domain.importexport.error.MissingRootFolderForLibraryImportException
import com.uquiz.android.domain.importexport.projection.ExportedUQuizFile
import com.uquiz.android.domain.importexport.projection.ImportResult
import com.uquiz.android.domain.importexport.repository.ImportExportRepository

/**
 * Implementación Room del repositorio de importación y exportación de archivos `.uquiz`.
 *
 * Coordina el flujo completo: parseo → validación → apertura de transacción →
 * delegación al ensamblador de exportación o al aplicador de importación.
 */
internal class ImportExportRepositoryImpl(
    private val database: UQuizDatabase,
    private val exportAssembler: UQuizExportAssembler,
    private val importApplier: UQuizImportApplier,
    private val parser: UQuizJsonParser = UQuizJsonParser(),
    private val validator: UQuizImportValidator = UQuizImportValidator(),
) : ImportExportRepository {

    override suspend fun exportPack(packId: String): ExportedUQuizFile =
        exportAssembler.assembleForPack(packId)

    override suspend fun exportFolderSubtree(folderId: String): ExportedUQuizFile =
        exportAssembler.assembleForFolder(folderId)

    override suspend fun importIntoLibrary(content: String): ImportResult {
        val parsed = parser.parse(content)
        validator.validateForLibrary(parsed)
        val root = parsed.folder ?: throw MissingRootFolderForLibraryImportException()
        return database.withTransaction {
            importApplier.applyFolderSubtree(root, parentId = null)
        }
    }

    override suspend fun importIntoFolder(parentFolderId: String, content: String): ImportResult {
        val parsed = parser.parse(content)
        validator.validateForFolder(parsed)
        return database.withTransaction {
            when {
                parsed.folder != null -> importApplier.applyFolderSubtree(parsed.folder, parentId = parentFolderId)
                parsed.pack != null -> importApplier.applyPackSubtree(parsed.pack, folderId = parentFolderId)
                else -> error("Validated import must contain a root node.")
            }
        }
    }
}
