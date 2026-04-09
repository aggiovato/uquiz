package com.uquiz.android.domain.content.model

/**
 * ### Pack
 *
 * Modelo de dominio que representa un conjunto de preguntas agrupadas dentro
 * de la jerarquía de contenido de la aplicación.
 *
 * Un `Pack` se utiliza para reunir y organizar preguntas bajo una misma unidad
 * lógica, permitiendo su clasificación dentro de una carpeta y su identificación
 * visual mediante propiedades opcionales como icono y color.
 *
 * Propiedades:
 * - [id]: identificador único del pack.
 * - [title]: título visible del pack, definido por el usuario.
 * - [description]: descripción opcional del pack. Puede utilizarse para aportar
 *   contexto adicional sobre su contenido.
 * - [folderId]: identificador de la carpeta a la que pertenece el pack.
 * - [icon]: clave o identificador del icono asociado al pack. Puede ser `null`
 *   si no se ha definido ningún icono.
 * - [colorHex]: color asociado visualmente al pack en formato hexadecimal.
 *   Puede ser `null` si no se ha definido un color.
 * - [createdAt]: marca temporal de creación del pack.
 * - [updatedAt]: marca temporal de la última actualización realizada sobre el pack.
 */
data class Pack(
    val id: String,
    val title: String,
    val description: String? = null,
    val folderId: String,
    val icon: String? = null,
    val colorHex: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
