package com.uquiz.android.core.game.model

/**
 * Resultado del cálculo de score para una sesión de Game mode.
 *
 * @param questionScores Lista de puntos obtenidos por cada pregunta (puede contener valores negativos).
 * @param sessionScore   Suma total de todos los scores de pregunta.
 */
data class GameScoreResult(
    val questionScores: List<Int>,
    val sessionScore: Int,
)
