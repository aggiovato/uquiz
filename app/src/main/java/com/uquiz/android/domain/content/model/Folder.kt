package com.uquiz.android.domain.content.model

/**
 * ### Folder
 *
 * Modelo de dominio que representa una carpeta dentro de la jerarquía de contenido
 * de la aplicación.
 *
 * Un `Folder` se utiliza para organizar contenido de forma estructurada y anidable,
 * permitiendo crear árboles de carpetas mediante la referencia opcional a una carpeta
 * padre a través de [parentId].
 *
 * Propiedades:
 * - [id]: identificador único de la carpeta.
 * - [name]: nombre visible de la carpeta, definido por el usuario.
 * - [parentId]: identificador de la carpeta padre. Si es `null`, la carpeta se
 *   considera de nivel raíz.
 * - [colorHex]: color asociado visualmente a la carpeta en formato hexadecimal.
 *   Puede ser `null` si no se ha definido un color.
 * - [icon]: clave o identificador del icono asociado a la carpeta. Puede ser `null`
 *   si no se ha definido ningún icono.
 * - [createdAt]: marca temporal de creación de la carpeta.
 * - [updatedAt]: marca temporal de la última actualización realizada sobre la carpeta.
 */
data class Folder(
    val id: String,
    val name: String,
    val parentId: String? = null,
    val colorHex: String? = null,
    val icon: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
