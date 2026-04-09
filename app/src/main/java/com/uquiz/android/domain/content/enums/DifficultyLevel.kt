package com.uquiz.android.domain.content.enums

import com.uquiz.android.domain.content.enums.DifficultyLevel.EASY
import com.uquiz.android.domain.content.enums.DifficultyLevel.EXPERT
import com.uquiz.android.domain.content.enums.DifficultyLevel.HARD
import com.uquiz.android.domain.content.enums.DifficultyLevel.MEDIUM

/**
 * ### DifficultyLevel
 *
 * Enum que representa el nivel de dificultad asignado a una `Question`
 * dentro del dominio de la aplicación.
 *
 * Este valor permite clasificar las preguntas según su complejidad estimada,
 * facilitando su organización, filtrado, presentación en la UI y futuras
 * mecánicas de estudio o juego.
 *
 * Niveles disponibles:
 * - [EASY]: pregunta de dificultad baja, pensada para contenido básico o de introducción.
 * - [MEDIUM]: dificultad intermedia, adecuada para práctica general.
 * - [HARD]: dificultad alta, orientada a contenidos que requieren mayor dominio.
 * - [EXPERT]: dificultad muy alta, reservada para preguntas avanzadas o especialmente complejas.
 */
enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD,
    EXPERT,
}
