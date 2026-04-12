package com.uquiz.android.data.attempts.mapper

import com.uquiz.android.data.attempts.entity.AttemptQuestionPlanEntity
import com.uquiz.android.domain.attempts.model.AttemptQuestionPlan

/**
 * Mapeo bidireccional entre [com.uquiz.android.data.attempts.entity.AttemptQuestionPlanEntity] y
 * [com.uquiz.android.domain.attempts.model.AttemptQuestionPlan].
 */
internal object AttemptQuestionPlanMapper {

    /** Convierte una entidad de Room al modelo de dominio. */
    fun toModel(entity: AttemptQuestionPlanEntity): AttemptQuestionPlan =
        AttemptQuestionPlan(
            attemptId = entity.attemptId,
            questionId = entity.questionId,
            orderIndex = entity.orderIndex,
            timeLimitMs = entity.timeLimitMs,
        )

    /** Convierte una lista de entidades al modelo de dominio. */
    fun toModelList(entities: List<AttemptQuestionPlanEntity>): List<AttemptQuestionPlan> =
        entities.map { toModel(it) }

    /** Convierte un modelo de dominio a entidad de Room. */
    fun toEntity(model: AttemptQuestionPlan): AttemptQuestionPlanEntity =
        AttemptQuestionPlanEntity(
            attemptId = model.attemptId,
            questionId = model.questionId,
            orderIndex = model.orderIndex,
            timeLimitMs = model.timeLimitMs,
        )

    /** Convierte una lista de modelos de dominio a entidades de Room. */
    fun toEntityList(models: List<AttemptQuestionPlan>): List<AttemptQuestionPlanEntity> =
        models.map { toEntity(it) }
}
