package com.uquiz.android.domain.attempts.enums

/**
 * ### AttemptStatus
 *
 * Enum que representa el estado de ciclo de vida de un intento.
 *
 * Este valor permite distinguir entre sesiones activas, finalizadas y abandonadas,
 * facilitando tanto la lógica de negocio como la persistencia y las estadísticas.
 *
 * Estados disponibles:
 * - [IN_PROGRESS]: el intento sigue activo y puede reanudarse.
 * - [COMPLETED]: el intento terminó correctamente.
 * - [ABANDONED]: el intento se dio por abandonado antes de completarse.
 */
enum class AttemptStatus {
    IN_PROGRESS,
    COMPLETED,
    ABANDONED,
}
