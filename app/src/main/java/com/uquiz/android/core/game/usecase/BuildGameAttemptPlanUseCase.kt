package com.uquiz.android.core.game.usecase

import com.uquiz.android.core.game.model.GameAttemptSession
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.model.AttemptQuestionPlan
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.stats.repository.QuestionStatsRepository

/**
 * Construye o reanuda una sesión de Game mode para un pack.
 *
 * Si existe un intento activo con su plan ya guardado, lo devuelve tal cual para
 * permitir la reanudación real sin recalcular el orden ni los tiempos. Si no existe,
 * crea un nuevo intento, genera el plan (orden aleatorio + tiempo límite por pregunta)
 * y lo persiste antes de devolverlo.
 */
class BuildGameAttemptPlanUseCase(
    private val attemptRepository: AttemptRepository,
    private val packRepository: PackRepository,
    private val questionStatsRepository: QuestionStatsRepository,
    private val calculateQuestionTimeBudgetUseCase: CalculateQuestionTimeBudgetUseCase,
) {
    /**
     * @param packId Identificador del pack para el que se construye la sesión.
     * @return [GameAttemptSession] con el intento y el plan de preguntas ordenado.
     */
    suspend operator fun invoke(packId: String): GameAttemptSession {
        // Intenta reanudar un intento activo existente
        val existingAttempt = attemptRepository.getActiveGameAttempt(packId)
        if (existingAttempt != null) {
            val existingPlan = attemptRepository.getQuestionPlan(existingAttempt.id)
            if (existingPlan.isNotEmpty()) {
                return GameAttemptSession(attempt = existingAttempt, plan = existingPlan)
            }
        }

        // Crea un nuevo intento
        val questions = packRepository.getWithQuestions(packId).shuffled()
        val attempt =
            attemptRepository.createAttempt(
                mode = AttemptMode.GAME,
                primaryPackId = packId,
                packIds = listOf(packId),
                totalQuestions = questions.size,
            )

        // Construye el plan calculando el tiempo límite para cada pregunta
        val plan =
            questions.mapIndexed { index, questionWithOptions ->
                val stats = questionStatsRepository.getByQuestion(questionWithOptions.question.id)
                AttemptQuestionPlan(
                    attemptId = attempt.id,
                    questionId = questionWithOptions.question.id,
                    orderIndex = index,
                    timeLimitMs = calculateQuestionTimeBudgetUseCase(questionWithOptions, stats),
                )
            }

        attemptRepository.saveQuestionPlan(plan)
        return GameAttemptSession(attempt = attempt, plan = plan)
    }
}
