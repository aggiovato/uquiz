package com.uquiz.android.domain.content.enums

import com.uquiz.android.domain.content.enums.AllowedUColor.Companion.fromHexOrNull
import com.uquiz.android.domain.content.enums.AllowedUColor.Companion.fromHexOrThrow
import com.uquiz.android.domain.content.error.InvalidContentColorException

/**
 * ### AllowedUColor
 *
 * Enum que centraliza los colores permitidos dentro del dominio para la creación
 * y personalización de entidades de contenido como `folders` y `packs`.
 *
 * Cada constante representa un color válido de la aplicación y expone su valor
 * hexadecimal asociado mediante la propiedad [hex].
 *
 * Colores permitidos:
 * - `NAVY`
 * - `TEAL`
 * - `GOLD`
 * - `RED`
 * - `NEUTRAL`
 *
 * El uso de este enum permite:
 * - restringir los colores aceptados a un conjunto controlado,
 * - evitar valores arbitrarios o inconsistentes en la capa de dominio,
 * - facilitar validaciones al importar, crear o actualizar contenido.
 *
 * El `companion object` actúa como punto de acceso estático para convertir un
 * valor hexadecimal externo en su correspondiente [AllowedUColor].
 *
 * Proporciona dos utilidades:
 * - [fromHexOrNull]: devuelve el color si el hex coincide con uno permitido,
 *   o `null` si no existe coincidencia.
 * - [fromHexOrThrow]: devuelve el color si es válido, o lanza
 *   [InvalidContentColorException] si el valor no pertenece al conjunto permitido.
 *
 * Esto resulta útil especialmente en procesos de validación, mapeo desde base de
 * datos, importación/exportación y transformación de DTOs hacia modelos de dominio.
 */
enum class AllowedUColor(
    val hex: String,
) {
    NAVY("#134C8F"),
    TEAL("#00CBA5"),
    GOLD("#F8D970"),
    RED("#E74C3C"),
    NEUTRAL("#60727F"),
    ;

    companion object {
        fun fromHexOrNull(hex: String?): AllowedUColor? = entries.firstOrNull { it.hex.equals(hex, ignoreCase = true) }

        fun fromHexOrThrow(hex: String?): AllowedUColor = fromHexOrNull(hex) ?: throw InvalidContentColorException(hex)
    }
}
