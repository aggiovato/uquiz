package com.uquiz.android.data.importexport.repository

/**
 * Genera nombres de archivo para exportaciones en formato `.uquiz`.
 */
internal object UQuizFileNamer {

    /**
     * Construye el nombre de archivo a partir del nombre del contenido exportado.
     *
     * @param name Nombre del pack o carpeta a exportar.
     * @return Nombre de archivo con extensión `.uquiz`.
     */
    fun buildFileName(name: String): String = "${slugify(name)}.uquiz"

    /** Convierte un nombre arbitrario en un slug válido para nombres de archivo. */
    private fun slugify(value: String): String =
        value.lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .ifBlank { "uquiz-export" }
}
