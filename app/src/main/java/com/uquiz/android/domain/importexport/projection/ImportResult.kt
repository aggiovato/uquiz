package com.uquiz.android.domain.importexport.projection

/**
 * ### ImportResult
 *
 * Modelo de proyección que resume una operación de importación completada con éxito.
 *
 * Esta estructura permite devolver a la capa de presentación un resultado listo
 * para mostrarse al usuario, incluyendo el nombre raíz importado y los contadores
 * de entidades creadas durante el proceso.
 *
 * Propiedades:
 * - [rootName]: nombre de la carpeta o pack importado en el nivel raíz.
 * - [createdFolderCount]: número de carpetas creadas durante la importación.
 * - [createdPackCount]: número de packs creados durante la importación.
 * - [createdQuestionCount]: número de preguntas creadas durante la importación.
 */
data class ImportResult(
    val rootName: String,
    val createdFolderCount: Int,
    val createdPackCount: Int,
    val createdQuestionCount: Int,
)
