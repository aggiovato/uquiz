package com.uquiz.android.domain.stats.enums

import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel.LEARNING
import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel.MASTERED
import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel.NEW
import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel.PRACTICED

/**
 * ### QuestionMasteryLevel
 *
 * Enum que representa el nivel de dominio o familiaridad alcanzado por el usuario
 * sobre una `Question` dentro de su proceso de estudio.
 *
 * Este valor permite clasificar el estado de aprendizaje de una pregunta en función
 * de su práctica previa, y puede utilizarse para seguimiento de progreso, estadísticas,
 * priorización de repaso o futuras mecánicas adaptativas de estudio.
 *
 * Niveles disponibles:
 * - [NEW]: la pregunta aún no ha sido trabajada o no tiene suficiente historial.
 * - [LEARNING]: la pregunta está en proceso de aprendizaje y todavía requiere práctica frecuente.
 * - [PRACTICED]: la pregunta ya ha sido practicada varias veces y muestra un nivel aceptable de familiaridad.
 * - [MASTERED]: la pregunta se considera dominada por el usuario.
 */
enum class QuestionMasteryLevel {
    NEW,
    LEARNING,
    PRACTICED,
    MASTERED,
}
