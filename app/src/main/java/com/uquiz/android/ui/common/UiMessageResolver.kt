package com.uquiz.android.ui.common

import com.uquiz.android.domain.content.error.DuplicateFolderNameException
import com.uquiz.android.domain.content.error.DuplicatePackTitleException
import com.uquiz.android.domain.content.error.FolderNameTooLongException
import com.uquiz.android.domain.content.error.InvalidContentColorException
import com.uquiz.android.domain.content.error.InvalidContentIconException
import com.uquiz.android.domain.content.error.PackDescriptionTooLongException
import com.uquiz.android.domain.content.error.PackTitleTooLongException
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
import com.uquiz.android.domain.importexport.error.InvalidImportExtensionException
import com.uquiz.android.domain.importexport.error.InvalidImportRootShapeException
import com.uquiz.android.domain.importexport.error.InvalidUQuizContentException
import com.uquiz.android.domain.importexport.error.InvalidUQuizFormatException
import com.uquiz.android.domain.importexport.error.MissingRootFolderForLibraryImportException
import com.uquiz.android.ui.i18n.AppErrors

/**
 * Traduce cualquier excepción de dominio en un mensaje localizado listo para mostrarse al usuario.
 *
 * Solo necesita [AppErrors] — no depende de toda la superficie de [com.uquiz.android.ui.i18n.AppStrings] — porque
 * su única responsabilidad es el mapeo excepción → texto de error.
 */
fun Throwable.toUiMessage(errors: AppErrors): String =
    when (this) {
        is FolderNameTooLongException -> {
            errors.folderNameTooLongMessage
        }

        is PackTitleTooLongException -> {
            errors.packTitleTooLongMessage
        }

        is PackDescriptionTooLongException -> {
            errors.packDescriptionTooLongMessage
        }

        is InvalidContentColorException -> {
            errors.invalidContentColorMessage
        }

        is InvalidContentIconException -> {
            errors.invalidContentIconMessage
        }

        is DuplicateFolderNameException -> {
            errors.duplicateFolderNameMessage(folderName)
        }

        is DuplicatePackTitleException -> {
            errors.duplicatePackTitleMessage(packTitle)
        }

        is InvalidImportExtensionException -> {
            errors.invalidImportExtensionMessage
        }

        is InvalidUQuizFormatException -> {
            errors.invalidUQuizFormatMessage
        }

        is MissingRootFolderForLibraryImportException -> {
            errors.missingRootFolderImportMessage
        }

        is InvalidImportRootShapeException -> {
            errors.invalidImportRootShapeMessage
        }

        is BlankImportedFolderNameException -> {
            errors.blankImportedFolderNameMessage
        }

        is BlankImportedPackTitleException -> {
            errors.blankImportedPackTitleMessage
        }

        is EmptyImportedPackException -> {
            errors.emptyImportedPackMessage
        }

        is EmptyImportedQuestionTextException -> {
            errors.emptyImportedQuestionTextMessage(questionIndex)
        }

        is ImportedQuestionNeedsTwoOptionsException -> {
            errors.importedQuestionNeedsTwoOptionsMessage(questionIndex)
        }

        is ImportedQuestionNeedsSingleCorrectOptionException -> {
            errors.importedQuestionNeedsSingleCorrectOptionMessage(
                questionIndex,
            )
        }

        is DuplicateImportedFolderNamesException -> {
            errors.duplicateImportedFolderNamesMessage
        }

        is DuplicateImportedPackTitlesException -> {
            errors.duplicateImportedPackTitlesMessage
        }

        is DuplicateImportedOptionLabelsException -> {
            errors.duplicateImportedOptionLabelsMessage(questionIndex)
        }

        is InvalidUQuizContentException -> {
            message ?: errors.importFailedMessage
        }

        is ImportExportException -> {
            message ?: errors.importFailedMessage
        }

        else -> {
            message ?: errors.importFailedMessage
        }
    }
