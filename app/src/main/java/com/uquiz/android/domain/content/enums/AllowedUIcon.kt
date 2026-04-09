package com.uquiz.android.domain.content.enums

import com.uquiz.android.domain.content.enums.AllowedUIcon.Companion.fromKeyOrNull
import com.uquiz.android.domain.content.enums.AllowedUIcon.Companion.fromKeyOrThrow
import com.uquiz.android.domain.content.error.InvalidContentIconException

/**
 * ### AllowedUIcon
 *
 * Enum que define el conjunto cerrado de iconos permitidos dentro del dominio
 * para representar visualmente entidades de contenido como `folders` y `packs`.
 *
 * Cada constante expone una clave textual mediante la propiedad [key], que actúa
 * como identificador estable del icono dentro de la aplicación.
 *
 * Iconos permitidos:
 * `FOLDER`, `FILE`, `HISTORY`, `GLOBE`, `BRAIN`, `IDEA`, `MATH`, `MOLECULE`,
 * `BACTERIA`, `QUERY`, `SHELF`, `BOOK`, `LANG`, `CALCULATE`, `CODE`, `SCIENCE`,
 * `SCHOOL`
 *
 * El uso de este enum permite:
 * - restringir los iconos válidos a un catálogo controlado,
 * - evitar claves arbitrarias o inconsistentes en la capa de dominio,
 * - mantener una correspondencia clara entre datos persistidos y recursos visuales,
 * - facilitar validaciones al crear, editar, importar o reconstruir contenido.
 *
 * El `companion object` funciona como punto de acceso estático para convertir una
 * clave externa en su correspondiente valor de [AllowedUIcon].
 *
 * Proporciona dos utilidades:
 * - [fromKeyOrNull]: devuelve el icono si la clave es válida; si la clave es `null`,
 *   vacía o no existe dentro del conjunto permitido, devuelve `null`.
 * - [fromKeyOrThrow]: devuelve el icono si la clave es válida, o lanza
 *   [InvalidContentIconException] cuando el valor recibido no corresponde a ningún
 *   icono permitido.
 *
 * Esto es especialmente útil en procesos de validación, mapeo de datos, lectura
 * desde base de datos, importación/exportación y transformación de modelos externos
 * hacia modelos de dominio seguros.
 */
enum class AllowedUIcon(
    val key: String,
) {
    FOLDER("folder"),
    FILE("file"),
    HISTORY("history"),
    GLOBE("globe"),
    BRAIN("brain"),
    IDEA("idea"),
    MATH("math"),
    MOLECULE("molecule"),
    BACTERIA("bacteria"),
    QUERY("query"),
    SHELF("shelf"),
    BOOK("book"),
    LANG("lang"),
    CALCULATE("calculate"),
    CODE("code"),
    SCIENCE("science"),
    SCHOOL("school"),
    ;

    companion object {
        fun fromKeyOrNull(key: String?): AllowedUIcon? {
            if (key.isNullOrBlank()) return null
            return entries.firstOrNull { it.key == key }
        }

        fun fromKeyOrThrow(key: String?): AllowedUIcon = fromKeyOrNull(key) ?: throw InvalidContentIconException(key)
    }
}
