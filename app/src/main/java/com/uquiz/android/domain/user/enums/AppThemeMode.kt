package com.uquiz.android.domain.user.enums

/**
 * ### AppThemeMode
 *
 * Enum que representa el modo de tema visual seleccionado por el usuario.
 *
 * Este valor permite persistir la preferencia de apariencia de la aplicación y
 * decidir si la UI debe mostrarse en modo claro, oscuro o seguir la configuración
 * del sistema operativo.
 *
 * Modos disponibles:
 * - [LIGHT]: fuerza la interfaz en modo claro.
 * - [DARK]: fuerza la interfaz en modo oscuro.
 * - [SYSTEM]: delega la elección al tema activo del sistema.
 */
enum class AppThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
}
