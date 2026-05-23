package com.uquiz.android.ui.feature.game.utils

import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.Option
import com.uquiz.android.domain.content.model.Question
import com.uquiz.android.domain.content.model.QuestionWithOptions
import org.junit.Assert.assertEquals
import org.junit.Test

class GameUtilsTest {

    // --- computeAverageDifficulty ---

    @Test
    fun `lista vacia retorna MEDIUM`() {
        assertEquals(DifficultyLevel.MEDIUM, computeAverageDifficulty(emptyList()))
    }

    @Test
    fun `todas EASY retorna EASY`() {
        val questions = List(3) { makeQWO(DifficultyLevel.EASY) }
        assertEquals(DifficultyLevel.EASY, computeAverageDifficulty(questions))
    }

    @Test
    fun `todas EXPERT retorna EXPERT`() {
        val questions = List(3) { makeQWO(DifficultyLevel.EXPERT) }
        assertEquals(DifficultyLevel.EXPERT, computeAverageDifficulty(questions))
    }

    @Test
    fun `mix EASY y HARD retorna MEDIUM`() {
        // ordinals 0 y 2 → avg = 1.0 → MEDIUM
        val questions = listOf(makeQWO(DifficultyLevel.EASY), makeQWO(DifficultyLevel.HARD))
        assertEquals(DifficultyLevel.MEDIUM, computeAverageDifficulty(questions))
    }

    @Test
    fun `mayoria EASY redondea a EASY`() {
        // ordinals (0, 0, 1) → avg ≈ 0.33 → EASY
        val questions = listOf(
            makeQWO(DifficultyLevel.EASY),
            makeQWO(DifficultyLevel.EASY),
            makeQWO(DifficultyLevel.MEDIUM),
        )
        assertEquals(DifficultyLevel.EASY, computeAverageDifficulty(questions))
    }

    @Test
    fun `una sola pregunta retorna su propia dificultad`() {
        assertEquals(DifficultyLevel.HARD, computeAverageDifficulty(listOf(makeQWO(DifficultyLevel.HARD))))
    }

    // --- formatGameDuration ---

    @Test
    fun `cero ms formatea como 00 00`() {
        assertEquals("00:00", formatGameDuration(0L))
    }

    @Test
    fun `30 segundos formatea como 00 30`() {
        assertEquals("00:30", formatGameDuration(30_000L))
    }

    @Test
    fun `60 segundos formatea como 01 00`() {
        assertEquals("01:00", formatGameDuration(60_000L))
    }

    @Test
    fun `90 segundos formatea como 01 30`() {
        assertEquals("01:30", formatGameDuration(90_000L))
    }

    @Test
    fun `1 hora formatea como 60 00`() {
        assertEquals("60:00", formatGameDuration(3_600_000L))
    }

    @Test
    fun `ms parciales se truncan al segundo`() {
        assertEquals("00:45", formatGameDuration(45_999L))
    }

    // --- formatExpectedPlayTime ---

    @Test
    fun `cero ms formatea como al menos 1 segundo`() {
        assertEquals("~1s", formatExpectedPlayTime(0L))
    }

    @Test
    fun `menos de un minuto muestra solo segundos`() {
        assertEquals("~45s", formatExpectedPlayTime(45_000L))
    }

    @Test
    fun `exactamente un minuto muestra solo minutos`() {
        assertEquals("~1m", formatExpectedPlayTime(60_000L))
    }

    @Test
    fun `un minuto y treinta segundos`() {
        assertEquals("~1m 30s", formatExpectedPlayTime(90_000L))
    }

    @Test
    fun `dos minutos sin segundos restantes`() {
        assertEquals("~2m", formatExpectedPlayTime(120_000L))
    }

    @Test
    fun `dos minutos y treinta segundos`() {
        assertEquals("~2m 30s", formatExpectedPlayTime(150_000L))
    }

    // --- Helpers ---

    private fun makeQWO(difficulty: DifficultyLevel): QuestionWithOptions {
        val question = Question(id = "q", text = "Sample question", difficulty = difficulty, createdAt = 0L, updatedAt = 0L)
        val options = listOf(
            Option(id = "o1", questionId = "q", label = "A", text = "Option A", isCorrect = true, createdAt = 0L, updatedAt = 0L),
            Option(id = "o2", questionId = "q", label = "B", text = "Option B", isCorrect = false, createdAt = 0L, updatedAt = 0L),
        )
        return QuestionWithOptions(question = question, options = options)
    }
}
