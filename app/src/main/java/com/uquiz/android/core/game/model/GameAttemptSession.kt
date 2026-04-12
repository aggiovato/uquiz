package com.uquiz.android.core.game.model

import com.uquiz.android.domain.attempts.model.Attempt
import com.uquiz.android.domain.attempts.model.AttemptQuestionPlan

/**
 * Sesión de Game mode lista para iniciar o reanudar.
 *
 * @param attempt Intento activo (nuevo o reanudado).
 * @param plan    Lista ordenada de slots de pregunta con sus tiempos límite.
 */
data class GameAttemptSession(
    val attempt: Attempt,
    val plan: List<AttemptQuestionPlan>,
)
