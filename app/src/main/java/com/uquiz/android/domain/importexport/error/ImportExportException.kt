package com.uquiz.android.domain.importexport.error

/**
 * Excepción base para errores de validación en importación y exportación.
 */
sealed class ImportExportException(message: String) : IllegalArgumentException(message)

/**
 * Se lanza cuando el archivo seleccionado no usa una extensión soportada.
 */
class InvalidImportExtensionException :
    ImportExportException("Selected file must use the .uquiz or .uqz extension.")

/**
 * Se lanza cuando el archivo seleccionado no puede interpretarse como un export válido.
 */
class InvalidUQuizFormatException :
    ImportExportException("The selected file is not a valid UQuiz document.")

/**
 * Se lanza al importar desde Library sin al menos una carpeta raíz en el archivo.
 */
class MissingRootFolderForLibraryImportException :
    ImportExportException("Imports from Library require a root folder in the file.")

/**
 * Se lanza cuando el contenido serializado es semánticamente inválido para UQuiz.
 */
class InvalidUQuizContentException(message: String) : ImportExportException(message)

/** Se lanza cuando la forma raíz del documento importado no coincide con la esperada. */
class InvalidImportRootShapeException :
    ImportExportException("Invalid root shape for import.")

/** Se lanza cuando una carpeta importada llega sin nombre válido. */
class BlankImportedFolderNameException :
    ImportExportException("Imported folder name cannot be blank.")

/** Se lanza cuando un pack importado llega sin título válido. */
class BlankImportedPackTitleException :
    ImportExportException("Imported pack title cannot be blank.")

/** Se lanza cuando un pack importado no contiene preguntas. */
class EmptyImportedPackException :
    ImportExportException("Imported pack must include at least one question.")

/** Se lanza cuando una pregunta importada no contiene texto. */
class EmptyImportedQuestionTextException(
    val questionIndex: Int,
) : ImportExportException("Imported question has no text.")

/** Se lanza cuando una pregunta importada no incluye al menos dos opciones. */
class ImportedQuestionNeedsTwoOptionsException(
    val questionIndex: Int,
) : ImportExportException("Imported question must include at least two options.")

/** Se lanza cuando una pregunta importada no tiene exactamente una opción correcta. */
class ImportedQuestionNeedsSingleCorrectOptionException(
    val questionIndex: Int,
) : ImportExportException("Imported question must include exactly one correct option.")

/** Se lanza cuando un mismo nivel importado contiene carpetas con nombres duplicados. */
class DuplicateImportedFolderNamesException :
    ImportExportException("Imported folder level contains duplicate names.")

/** Se lanza cuando un mismo nivel importado contiene packs con títulos duplicados. */
class DuplicateImportedPackTitlesException :
    ImportExportException("Imported folder level contains duplicate pack titles.")

/** Se lanza cuando una pregunta importada contiene etiquetas de opción duplicadas. */
class DuplicateImportedOptionLabelsException(
    val questionIndex: Int,
) : ImportExportException("Imported question contains duplicate option labels.")
