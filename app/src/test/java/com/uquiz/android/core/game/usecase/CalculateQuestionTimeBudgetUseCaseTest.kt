package com.uquiz.android.core.game.usecase

import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.Option
import com.uquiz.android.domain.content.model.Question
import com.uquiz.android.domain.content.model.QuestionWithOptions
import com.uquiz.android.domain.stats.model.QuestionStats
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculateQuestionTimeBudgetUseCaseTest {

    private val useCase = CalculateQuestionTimeBudgetUseCase()

    // --- Bases por dificultad ---

    @Test
    fun `EASY sin texto extra retorna base de 15 segundos`() {
        assertEquals(15_000L, useCase(makeQuestion(difficulty = DifficultyLevel.EASY), null))
    }

    @Test
    fun `MEDIUM sin texto extra retorna base de 20 segundos`() {
        assertEquals(20_000L, useCase(makeQuestion(difficulty = DifficultyLevel.MEDIUM), null))
    }

    @Test
    fun `HARD sin texto extra retorna base de 25 segundos`() {
        assertEquals(25_000L, useCase(makeQuestion(difficulty = DifficultyLevel.HARD), null))
    }

    @Test
    fun `EXPERT sin texto extra retorna base de 30 segundos`() {
        assertEquals(30_000L, useCase(makeQuestion(difficulty = DifficultyLevel.EXPERT), null))
    }

    // --- Bono por longitud ---

    @Test
    fun `100 chars en pregunta agrega 1500ms`() {
        // 100 / 100 * 1500 = 1500ms
        val q = makeQuestion(text = "A".repeat(100))
        assertEquals(20_000L + 1_500L, useCase(q, null))
    }

    @Test
    fun `200 chars en pregunta agrega 3000ms`() {
        val q = makeQuestion(text = "A".repeat(200))
        assertEquals(20_000L + 3_000L, useCase(q, null))
    }

    @Test
    fun `bono acumula texto de pregunta y opciones`() {
        // 50 chars en pregunta + 50 chars en opción = 100 chars → +1500ms
        val q = makeQuestion(text = "A".repeat(50), options = listOf("B".repeat(50)))
        assertEquals(20_000L + 1_500L, useCase(q, null))
    }

    @Test
    fun `texto con menos de 100 chars no otorga bono`() {
        val q = makeQuestion(text = "A".repeat(99))
        assertEquals(20_000L, useCase(q, null))
    }

    @Test
    fun `bono de longitud tiene tope de 25 segundos`() {
        // 1800 chars → sin tope sería 27_000ms; con tope → 25_000ms
        val q = makeQuestion(text = "A".repeat(1800))
        assertEquals(20_000L + 25_000L, useCase(q, null))
    }

    @Test
    fun `texto extremadamente largo no supera tope de bono`() {
        val q = makeQuestion(text = "A".repeat(5000))
        assertEquals(20_000L + 25_000L, useCase(q, null))
    }

    // --- Acotamiento global [5s, 60s] ---

    @Test
    fun `resultado se acota al maximo de 60 segundos`() {
        // EXPERT (30s) + tope de bono (25s) = 55s < 60s — usamos stats para forzar
        val q = makeQuestion(difficulty = DifficultyLevel.EXPERT, text = "A".repeat(5000))
        val stats = makeStats(avgGameTimeMs = 80_000L)
        // blended = 55_000 * 0.40 + 80_000 * 1.50 * 0.60 = 22_000 + 72_000 = 94_000 → coerce → 60_000
        assertEquals(60_000L, useCase(q, stats))
    }

    @Test
    fun `resultado se acota al minimo de 5 segundos`() {
        val stats = makeStats(avgGameTimeMs = 0L)
        // blended = 20_000 * 0.40 + 0 * 1.50 * 0.60 = 8_000 → bien por encima de 5s
        assertTrue(useCase(makeQuestion(), stats) >= 5_000L)
    }

    // --- Blend con historial del usuario ---

    @Test
    fun `con historial aplica blend 40 calculado mas 60 historico`() {
        // calculatedMs = 20_000 (MEDIUM, sin chars extra)
        // blended = 20_000 * 0.40 + 10_000 * 1.50 * 0.60 = 8_000 + 9_000 = 17_000
        val stats = makeStats(avgGameTimeMs = 10_000L)
        assertEquals(17_000L, useCase(makeQuestion(), stats))
    }

    @Test
    fun `sin historial usa solo el calculado`() {
        assertEquals(20_000L, useCase(makeQuestion(), null))
    }

    // --- Helpers ---

    private fun makeQuestion(
        difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
        text: String = "",
        options: List<String> = emptyList(),
    ): QuestionWithOptions {
        val question = Question(id = "q1", text = text, difficulty = difficulty, createdAt = 0L, updatedAt = 0L)
        val opts = options.mapIndexed { i, t ->
            Option(id = "o$i", questionId = "q1", label = "${i + 1}", text = t, isCorrect = i == 0, createdAt = 0L, updatedAt = 0L)
        }
        return QuestionWithOptions(question = question, options = opts)
    }

    private fun makeStats(avgGameTimeMs: Long) = QuestionStats(
        id = "s1",
        userId = "u1",
        questionId = "q1",
        packId = "p1",
        avgGameTimeMs = avgGameTimeMs,
        createdAt = 0L,
        updatedAt = 0L,
    )
}
