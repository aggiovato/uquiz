package com.uquiz.android.domain.stats.projection

import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.Pack

/**
 * ### PackGameCard
 *
 * Proyección ensamblada que agrupa la información de un pack necesaria para
 * mostrar su card en la pantalla principal del modo Game.
 *
 * Combina datos del pack, conteo de preguntas, dificultad media estimada,
 * tiempo esperado de partida y el estado del intento activo, si existe.
 *
 * @param pack               Pack base con título, icono y color.
 * @param questionCount      Número de preguntas disponibles en el pack.
 * @param averageDifficulty  Dificultad media calculada a partir de las preguntas del pack.
 * @param expectedPlayTimeMs Estimación del tiempo total de partida en milisegundos.
 *                           Calculado como `questionCount × baseMsForDifficulty(averageDifficulty)`.
 * @param activeAttemptId    ID del intento de Game mode en curso, o null si no hay ninguno.
 * @param answeredCount      Número de preguntas ya respondidas en el intento activo (0 si no hay).
 */
data class PackGameCard(
    val pack: Pack,
    val questionCount: Int,
    val averageDifficulty: DifficultyLevel,
    val expectedPlayTimeMs: Long,
    val activeAttemptId: String? = null,
    val answeredCount: Int = 0,
) {
    /** Indica si hay una partida de Game mode en curso para este pack. */
    val hasActiveAttempt: Boolean get() = activeAttemptId != null
}
