package com.uquiz.android.domain.attempts.repository

import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.model.Attempt
import com.uquiz.android.domain.attempts.model.AttemptAnswer
import com.uquiz.android.domain.attempts.projection.ActivePackProgress
import kotlinx.coroutines.flow.Flow

/**
 * ### AttemptRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar el ciclo de vida de
 * los intentos y sus respuestas asociadas.
 *
 * Esta interfaz define operaciones de lectura reactiva, consulta puntual y
 * modificación sobre sesiones de estudio o juego, abstrayendo la fuente de datos
 * concreta utilizada para persistir intentos, respuestas y proyecciones derivadas.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar intentos y proyecciones activas de progreso,
 * - recuperar intentos y respuestas mediante lecturas puntuales,
 * - crear, reanudar, completar o abandonar sesiones,
 * - registrar respuestas y mantener métricas básicas del intento.
 *
 * Al tratarse de un contrato de dominio, su definición no depende de detalles
 * de implementación específicos como Room, red, caché o jobs internos.
 */
interface AttemptRepository {
    /**
     * ### Reactive reads
     *
     * - observeAll()
     * - observeByMode(mode)
     * - observeById(id)
     * - observeActivePackProgress(mode)
     */
    fun observeAll(): Flow<List<Attempt>>

    fun observeByMode(mode: AttemptMode): Flow<List<Attempt>>

    fun observeById(id: String): Flow<Attempt?>

    fun observeActivePackProgress(mode: AttemptMode): Flow<List<ActivePackProgress>>

    /**
     * ### One-shot reads
     *
     * - getById(id)
     * - getWithAnswers(attemptId)
     * - getIncomplete()
     * - getActiveStudyAttempt(packId)
     * - getAnswers(attemptId)
     * - getRecentCompleted(limit)
     * - getAverageScore(mode)
     * - getBestScore(mode)
     */
    suspend fun getById(id: String): Attempt?

    suspend fun getWithAnswers(attemptId: String): Pair<Attempt, List<AttemptAnswer>>?

    suspend fun getIncomplete(): List<Attempt>

    suspend fun getActiveStudyAttempt(packId: String): Attempt?

    suspend fun getAnswers(attemptId: String): List<AttemptAnswer>

    suspend fun getRecentCompleted(limit: Int = 10): List<Attempt>

    suspend fun getAverageScore(mode: AttemptMode): Double?

    suspend fun getBestScore(mode: AttemptMode): Int?

    /**
     * ### Commands
     *
     * - createAttempt(mode, primaryPackId, packIds, totalQuestions)
     * - startOrResumeStudyAttempt(packId, totalQuestions)
     * - updateAttempt(attempt)
     * - updateCurrentQuestionIndex(attemptId, index)
     * - completeAttempt(attemptId, score, correctAnswers, totalQuestions, durationMs)
     * - abandonAttempt(attemptId)
     * - recordAnswer(attemptId, questionId, pickedOptionId, isCorrect, timeMs, timeLimitMs)
     * - deleteAttempt(attemptId)
     * - deleteOlderThan(beforeTimestamp)
     */
    suspend fun createAttempt(
        mode: AttemptMode,
        primaryPackId: String? = null,
        packIds: List<String> = emptyList(),
        totalQuestions: Int = 0,
    ): Attempt

    suspend fun startOrResumeStudyAttempt(
        packId: String,
        totalQuestions: Int,
    ): Attempt

    suspend fun updateAttempt(attempt: Attempt)

    suspend fun updateCurrentQuestionIndex(
        attemptId: String,
        index: Int,
    )

    suspend fun completeAttempt(
        attemptId: String,
        score: Int,
        correctAnswers: Int,
        totalQuestions: Int,
        durationMs: Long? = null,
    )

    suspend fun abandonAttempt(attemptId: String)

    suspend fun recordAnswer(
        attemptId: String,
        questionId: String,
        pickedOptionId: String?,
        isCorrect: Boolean,
        timeMs: Long,
        timeLimitMs: Long? = null,
    ): AttemptAnswer

    suspend fun deleteAttempt(attemptId: String)

    suspend fun deleteOlderThan(beforeTimestamp: Long)
}
