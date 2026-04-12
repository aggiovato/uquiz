package com.uquiz.android.data.attempts.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.attempts.entity.AttemptQuestionPlanEntity

/**
 * ### AttemptQuestionPlanDao
 *
 * Acceso puntual a la tabla `attempt_question_plan` (orden de preguntas planificado para cada intento).
 */
@Dao
interface AttemptQuestionPlanDao {

    /**
     * Inserta o actualiza todos los slots del plan de preguntas de un intento.
     */
    @Upsert
    suspend fun upsertAll(plans: List<AttemptQuestionPlanEntity>)

    /**
     * Devuelve el plan ordenado de un intento. Retorna lista vacía si no existe plan guardado.
     */
    @Query("SELECT * FROM attempt_question_plan WHERE attemptId = :attemptId ORDER BY orderIndex ASC")
    suspend fun getByAttemptId(attemptId: String): List<AttemptQuestionPlanEntity>

    /**
     * Elimina el plan asociado a un intento (p.ej. al finalizarlo).
     */
    @Query("DELETE FROM attempt_question_plan WHERE attemptId = :attemptId")
    suspend fun deleteByAttemptId(attemptId: String)
}
