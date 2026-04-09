package com.uquiz.android.data.importexport.validator

import com.uquiz.android.data.importexport.dto.ImportFolderNode
import com.uquiz.android.data.importexport.dto.ImportPackNode
import com.uquiz.android.data.importexport.parser.ParsedUQuizImport
import com.uquiz.android.domain.importexport.error.BlankImportedFolderNameException
import com.uquiz.android.domain.importexport.error.BlankImportedPackTitleException
import com.uquiz.android.domain.importexport.error.DuplicateImportedFolderNamesException
import com.uquiz.android.domain.importexport.error.DuplicateImportedOptionLabelsException
import com.uquiz.android.domain.importexport.error.DuplicateImportedPackTitlesException
import com.uquiz.android.domain.importexport.error.EmptyImportedPackException
import com.uquiz.android.domain.importexport.error.EmptyImportedQuestionTextException
import com.uquiz.android.domain.importexport.error.ImportExportException
import com.uquiz.android.domain.importexport.error.ImportedQuestionNeedsSingleCorrectOptionException
import com.uquiz.android.domain.importexport.error.ImportedQuestionNeedsTwoOptionsException
import com.uquiz.android.domain.importexport.error.InvalidImportRootShapeException
import com.uquiz.android.domain.importexport.error.MissingRootFolderForLibraryImportException

/**
 * Validates semantic constraints for parsed `.uquiz` documents before persistence.
 */
class UQuizImportValidator {

    fun validateForLibrary(parsed: ParsedUQuizImport) {
        if (parsed.folder == null || parsed.pack != null) {
            throw MissingRootFolderForLibraryImportException()
        }
        validateFolder(parsed.folder)
    }

    fun validateForFolder(parsed: ParsedUQuizImport) {
        when {
            parsed.folder != null && parsed.pack == null -> validateFolder(parsed.folder)
            parsed.pack != null && parsed.folder == null -> validatePack(parsed.pack)
            else -> throw InvalidImportRootShapeException()
        }
    }

    private fun validateFolder(folder: ImportFolderNode) {
        if (folder.name.isBlank()) {
            throw BlankImportedFolderNameException()
        }

        ensureUnique(folder.folders.map { it.name }, DuplicateImportedFolderNamesException())
        ensureUnique(folder.packs.map { it.title }, DuplicateImportedPackTitlesException())

        folder.folders.forEach(::validateFolder)
        folder.packs.forEach(::validatePack)
    }

    private fun validatePack(pack: ImportPackNode) {
        if (pack.title.isBlank()) {
            throw BlankImportedPackTitleException()
        }
        if (pack.questions.isEmpty()) {
            throw EmptyImportedPackException()
        }

        pack.questions.forEachIndexed { index, question ->
            if (question.text.isBlank()) {
                throw EmptyImportedQuestionTextException(index + 1)
            }
            val validOptions = question.options.filter { it.text.isNotBlank() }
            if (validOptions.size < 2) {
                throw ImportedQuestionNeedsTwoOptionsException(index + 1)
            }
            if (validOptions.count { it.isCorrect } != 1) {
                throw ImportedQuestionNeedsSingleCorrectOptionException(index + 1)
            }
            ensureUnique(validOptions.map { it.label }, DuplicateImportedOptionLabelsException(index + 1))
        }
    }

    private fun ensureUnique(values: List<String>, error: ImportExportException) {
        val duplicates = values
            .map { it.trim().lowercase() }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
        if (duplicates.isNotEmpty()) {
            throw error
        }
    }
}
