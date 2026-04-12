package com.uquiz.android.data.importexport.parser

import com.uquiz.android.core.content.ContentDefaults
import com.uquiz.android.data.importexport.dto.ImportFolderNode
import com.uquiz.android.data.importexport.dto.ImportOptionNode
import com.uquiz.android.data.importexport.dto.ImportPackNode
import com.uquiz.android.data.importexport.dto.ImportQuestionNode
import com.uquiz.android.data.importexport.dto.UQuizFileDto
import com.uquiz.android.data.importexport.dto.UQuizFolderDto
import com.uquiz.android.data.importexport.dto.UQuizPackDto
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.importexport.error.InvalidUQuizFormatException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

data class ParsedUQuizImport(
    val folder: ImportFolderNode? = null,
    val pack: ImportPackNode? = null,
)

/**
 * Parses raw `.uquiz` JSON into normalized import nodes with default visuals applied.
 */
class UQuizJsonParser(
    private val json: Json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        },
) {
    fun parse(content: String): ParsedUQuizImport {
        val file =
            try {
                json.decodeFromString<UQuizFileDto>(content)
            } catch (_: SerializationException) {
                throw InvalidUQuizFormatException()
            }

        if (file.format.lowercase() != "uquiz" || file.schemaVersion != 1) {
            throw InvalidUQuizFormatException()
        }
        if ((file.folder == null) == (file.pack == null)) {
            throw InvalidUQuizFormatException()
        }

        return ParsedUQuizImport(
            folder = file.folder?.toImportNode(),
            pack = file.pack?.toImportNode(),
        )
    }

    private fun UQuizFolderDto.toImportNode(): ImportFolderNode =
        ImportFolderNode(
            name = name.trim(),
            colorHex = color?.takeIf { it.isNotBlank() } ?: ContentDefaults.DEFAULT_FOLDER_COLOR_HEX,
            icon = icon?.takeIf { it.isNotBlank() } ?: ContentDefaults.DEFAULT_FOLDER_ICON_KEY,
            folders = folders.map { it.toImportNode() },
            packs = packs.map { it.toImportNode() },
        )

    private fun UQuizPackDto.toImportNode(): ImportPackNode =
        ImportPackNode(
            title = title.trim(),
            description = description?.trim()?.takeIf { it.isNotBlank() },
            colorHex = color?.takeIf { it.isNotBlank() } ?: ContentDefaults.DEFAULT_PACK_COLOR_HEX,
            icon = icon?.takeIf { it.isNotBlank() } ?: ContentDefaults.DEFAULT_PACK_ICON_KEY,
            questions =
                questions.map { question ->
                    ImportQuestionNode(
                        text = question.text.trim(),
                        explanation = question.explanation?.trim()?.takeIf { it.isNotBlank() },
                        difficulty =
                            DifficultyLevel.entries.firstOrNull {
                                it.name.equals(question.difficulty, ignoreCase = true)
                            } ?: DifficultyLevel.MEDIUM,
                        options =
                            question.options.sortedBy { it.position }.map { option ->
                                ImportOptionNode(
                                    label = option.label.trim(),
                                    text = option.text.trim(),
                                    isCorrect = option.isCorrect,
                                    position = option.position,
                                )
                            },
                    )
                },
        )
}
