package com.uquiz.android.core.game.usecase

import com.uquiz.android.core.game.model.GameAnswerInput
import com.uquiz.android.domain.content.enums.DifficultyLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class ComputeGameScoreUseCaseTest {

    private val useCase = ComputeGameScoreUseCase()

    // --- Respuestas correctas ---

    @Test
    fun `respuesta correcta instantanea obtiene puntuacion maxima`() {
        // speedScore = 1 - 0/20000 = 1.0 → speedMultiplier = 2.0 → 10 × 2.0 × 1.0 = 20
        val result = useCase(listOf(mediumAnswer(correct = true, timeMs = 0L, limitMs = 20_000L)))
        assertEquals(20, result.questionScores[0])
        assertEquals(20, result.sessionScore)
    }

    @Test
    fun `respuesta correcta al limite obtiene puntuacion minima`() {
        // speedScore = 0.0 → speedMultiplier = 0.5 → 10 × 0.5 × 1.0 = 5
        val result = useCase(listOf(mediumAnswer(correct = true, timeMs = 20_000L, limitMs = 20_000L)))
        assertEquals(5, result.questionScores[0])
    }

    @Test
    fun `respuesta correcta a mitad de tiempo`() {
        // speedScore = 0.5 → speedMultiplier = 1.25 → 10 × 1.25 × 1.0 = 12 (truncado de 12.5)
        val result = useCase(listOf(mediumAnswer(correct = true, timeMs = 10_000L, limitMs = 20_000L)))
        assertEquals(12, result.questionScores[0])
    }

    @Test
    fun `sin limite de tiempo speedScore es maximo`() {
        // timeLimitMs = null → speedScore = 1.0 → score = 20
        val result = useCase(listOf(mediumAnswer(correct = true, timeMs = 99_000L, limitMs = null)))
        assertEquals(20, result.questionScores[0])
    }

    // --- Timeout ---

    @Test
    fun `timeout aplica penalizacion correcta`() {
        // timeMs >= timeLimitMs → isTimeout = true → -(8 × 1.0) = -8
        val result = useCase(listOf(mediumAnswer(correct = false, timeMs = 20_000L, limitMs = 20_000L)))
        assertEquals(-8, result.questionScores[0])
    }

    @Test
    fun `timeout supera el limite con margen`() {
        // timeMs > timeLimitMs también cuenta como timeout
        val result = useCase(listOf(mediumAnswer(correct = false, timeMs = 30_000L, limitMs = 20_000L)))
        assertEquals(-8, result.questionScores[0])
    }

    // --- Respuestas incorrectas ---

    @Test
    fun `respuesta incorrecta antes del limite aplica penalizacion`() {
        // timeMs < timeLimitMs, no es timeout → -(5 × 1.0) = -5
        val result = useCase(listOf(mediumAnswer(correct = false, timeMs = 5_000L, limitMs = 20_000L)))
        assertEquals(-5, result.questionScores[0])
    }

    // --- Pesos por dificultad ---

    @Test
    fun `dificultad EASY aplica peso 0_90`() {
        // 10 × 2.0 × 0.90 = 18
        val input = GameAnswerInput(isCorrect = true, timeMs = 0L, timeLimitMs = 15_000L, difficulty = DifficultyLevel.EASY)
        assertEquals(18, useCase(listOf(input)).questionScores[0])
    }

    @Test
    fun `dificultad HARD aplica peso 1_15 en timeout`() {
        // -(8 × 1.15) = -9.2 → truncado a -9
        val input = GameAnswerInput(isCorrect = false, timeMs = 25_000L, timeLimitMs = 25_000L, difficulty = DifficultyLevel.HARD)
        assertEquals(-9, useCase(listOf(input)).questionScores[0])
    }

    @Test
    fun `dificultad EXPERT aplica peso 1_30 en respuesta correcta rapida`() {
        // 10 × 2.0 × 1.30 = 26
        val input = GameAnswerInput(isCorrect = true, timeMs = 0L, timeLimitMs = 30_000L, difficulty = DifficultyLevel.EXPERT)
        assertEquals(26, useCase(listOf(input)).questionScores[0])
    }

    // --- Sesión completa ---

    @Test
    fun `multiples respuestas acumulan el score de sesion correctamente`() {
        val answers = listOf(
            mediumAnswer(correct = true, timeMs = 0L, limitMs = 20_000L),    // +20
            mediumAnswer(correct = false, timeMs = 5_000L, limitMs = 20_000L), // -5
            mediumAnswer(correct = false, timeMs = 20_000L, limitMs = 20_000L), // -8 (timeout)
        )
        val result = useCase(answers)
        assertEquals(3, result.questionScores.size)
        assertEquals(7, result.sessionScore) // 20 - 5 - 8
    }

    @Test
    fun `lista vacia produce score cero`() {
        val result = useCase(emptyList())
        assertEquals(0, result.sessionScore)
        assertEquals(0, result.questionScores.size)
    }

    // --- Helpers ---

    private fun mediumAnswer(correct: Boolean, timeMs: Long, limitMs: Long?) = GameAnswerInput(
        isCorrect = correct,
        timeMs = timeMs,
        timeLimitMs = limitMs,
        difficulty = DifficultyLevel.MEDIUM,
    )
}
