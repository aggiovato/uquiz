package com.uquiz.android.domain.importexport.projection

/**
 * ### ExportedUQuizFile
 *
 * Modelo de proyección que representa un archivo UQuiz exportado listo para ser
 * escrito en el destino elegido por el usuario.
 *
 * Esta estructura encapsula todos los datos necesarios para crear el documento
 * final desde la capa de UI o infraestructura, sin exponer detalles internos del
 * proceso de serialización.
 *
 * Propiedades:
 * - [fileName]: nombre de archivo sugerido incluyendo extensión.
 * - [mimeType]: tipo MIME usado al crear el documento destino.
 * - [content]: contenido serializado del archivo exportado.
 */
data class ExportedUQuizFile(
    val fileName: String,
    val mimeType: String,
    val content: String,
)
