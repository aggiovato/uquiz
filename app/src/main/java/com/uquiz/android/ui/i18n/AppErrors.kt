package com.uquiz.android.ui.i18n

/**
 * Contiene todos los mensajes de error localizados de la aplicación.
 *
 * Es la única fuente de verdad que consume [com.uquiz.android.ui.common.toUiMessage]
 * para traducir excepciones de dominio en texto visible para el usuario.
 * Se accede vía [AppStrings.errors] desde cualquier punto de la UI.
 */
data class AppErrors(
    // Content validation
    val folderNameTooLongMessage: String,
    val packTitleTooLongMessage: String,
    val packDescriptionTooLongMessage: String,
    val duplicateFolderNameMessage: (String) -> String,
    val duplicatePackTitleMessage: (String) -> String,
    // Color/icon validation — default en inglés para idiomas sin traducción explícita
    val invalidContentColorMessage: String = "Invalid color.",
    val invalidContentIconMessage: String = "Invalid icon.",
    // Import structure validation
    val invalidImportExtensionMessage: String,
    val invalidUQuizFormatMessage: String,
    val missingRootFolderImportMessage: String,
    val invalidImportRootShapeMessage: String,
    val blankImportedFolderNameMessage: String,
    val blankImportedPackTitleMessage: String,
    val emptyImportedPackMessage: String,
    val emptyImportedQuestionTextMessage: (Int) -> String,
    val importedQuestionNeedsTwoOptionsMessage: (Int) -> String,
    val importedQuestionNeedsSingleCorrectOptionMessage: (Int) -> String,
    val duplicateImportedFolderNamesMessage: String,
    val duplicateImportedPackTitlesMessage: String,
    val duplicateImportedOptionLabelsMessage: (Int) -> String,
    // Generic operation failures
    val importReadErrorMessage: String,
    val exportWriteErrorMessage: String,
    val importFailedMessage: String,
    val exportFailedMessage: String,
)
