package com.uquiz.android.data.importexport.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.uquiz.android.data.importexport.dto.UQuizFileDto
import com.uquiz.android.data.importexport.dto.UQuizFolderDto
import com.uquiz.android.data.importexport.dto.UQuizOptionDto
import com.uquiz.android.data.importexport.dto.UQuizPackDto
import com.uquiz.android.data.importexport.dto.UQuizQuestionDto
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.model.QuestionWithOptions
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.importexport.projection.ExportedUQuizFile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

/**
 * Ensambla el árbol de DTOs necesario para serializar packs y carpetas en formato `.uquiz`.
 *
 * Es responsable de obtener los datos del dominio, construir la estructura exportable
 * y codificarla como texto. El resultado es un [ExportedUQuizFile] listo para escribir.
 */
internal class UQuizExportAssembler(
    private val folderRepository: FolderRepository,
    private val packRepository: PackRepository,
    private val json: Json,
) {
    /**
     * Ensambla el archivo exportado para un pack individual.
     *
     * @param packId Identificador del pack a exportar.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun assembleForPack(packId: String): ExportedUQuizFile {
        val pack = packRepository.getById(packId) ?: error("Pack $packId not found")
        val questions = packRepository.getWithQuestions(packId)
        val dto =
            UQuizFileDto(
                exportedAt = Instant.now().toString(),
                pack = pack.toPackDto(questions),
            )
        return ExportedUQuizFile(
            fileName = UQuizFileNamer.buildFileName(pack.title),
            mimeType = "application/octet-stream",
            content = json.encodeToString(dto),
        )
    }

    /**
     * Ensambla el archivo exportado para el subárbol de una carpeta.
     *
     * @param folderId Identificador de la carpeta raíz a exportar.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun assembleForFolder(folderId: String): ExportedUQuizFile {
        val allFolders = folderRepository.getAll()
        val folder = allFolders.firstOrNull { it.id == folderId } ?: error("Folder $folderId not found")
        val childrenByParent = allFolders.groupBy { it.parentId }
        val dto =
            UQuizFileDto(
                exportedAt = Instant.now().toString(),
                folder = buildFolderDto(folder, childrenByParent),
            )
        return ExportedUQuizFile(
            fileName = UQuizFileNamer.buildFileName(folder.name),
            mimeType = "application/octet-stream",
            content = json.encodeToString(dto),
        )
    }

    private suspend fun buildFolderDto(
        folder: Folder,
        childrenByParent: Map<String?, List<Folder>>,
    ): UQuizFolderDto {
        val childFolders = childrenByParent[folder.id].orEmpty().sortedBy { it.name }
        val packs = packRepository.getByFolder(folder.id).sortedBy { it.title }
        return UQuizFolderDto(
            name = folder.name,
            color = folder.colorHex,
            icon = folder.icon,
            folders = childFolders.map { buildFolderDto(it, childrenByParent) },
            packs = packs.map { pack -> pack.toPackDto(packRepository.getWithQuestions(pack.id)) },
        )
    }

    private fun Pack.toPackDto(questions: List<QuestionWithOptions>): UQuizPackDto =
        UQuizPackDto(
            title = title,
            description = description,
            color = colorHex,
            icon = icon,
            questions =
                questions.map { (question, options) ->
                    UQuizQuestionDto(
                        text = question.text,
                        explanation = question.explanation,
                        difficulty = question.difficulty.name,
                        options =
                            options.mapIndexed { index, option ->
                                UQuizOptionDto(
                                    label = option.label,
                                    text = option.text,
                                    isCorrect = option.isCorrect,
                                    position = index,
                                )
                            },
                    )
                },
        )
}
