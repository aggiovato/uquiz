package com.uquiz.android.domain.attempts.enums

/**
 * ### AttemptMode
 *
 * Enum que representa el modo en el que se ejecuta un intento del usuario.
 *
 * Este valor permite diferenciar sesiones de estudio y juego, habilitando reglas
 * de negocio, estadísticas y flujos de UI específicos para cada contexto.
 *
 * Modos disponibles:
 * - [STUDY]: intento orientado a aprendizaje y repaso.
 * - [GAME]: intento orientado a juego, puntuación y rapidez.
 */
enum class AttemptMode {
    STUDY,
    GAME,
}
