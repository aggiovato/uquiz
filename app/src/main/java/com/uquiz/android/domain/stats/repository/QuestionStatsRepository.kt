package com.uquiz.android.domain.stats.repository

import com.uquiz.android.domain.stats.model.QuestionStats
import kotlinx.coroutines.flow.Flow

/**
 * ### QuestionStatsRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar las estadísticas
 * acumuladas de preguntas.
 *
 * Esta interfaz define operaciones de lectura reactiva, consulta puntual y
 * persistencia de agregados [QuestionStats], permitiendo a la aplicación trabajar
 * con el progreso y dominio por pregunta sin depender de la implementación concreta
 * de almacenamiento o recálculo.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar estadísticas de preguntas por pack,
 * - consultar contadores de preguntas dominadas,
 * - recuperar una estadística puntual por pregunta,
 * - persistir o actualizar el agregado estadístico correspondiente.
 *
 * Al tratarse de un contrato de dominio, su definición no depende de detalles
 * de implementación específicos como Room, red, caché o workers internos.
 */
interface QuestionStatsRepository {
    /**
     * ### Reactive reads
     *
     * - observeByPack(packId)
     * - observeMasteredCount(packId)
     */
    fun observeByPack(packId: String): Flow<List<QuestionStats>>
    fun observeMasteredCount(packId: String): Flow<Int>

    /**
     * ### One-shot reads
     *
     * - getByQuestion(questionId)
     * - countMasteredByPack(packId)
     */
    suspend fun getByQuestion(questionId: String): QuestionStats?
    suspend fun countMasteredByPack(packId: String): Int

    /**
     * ### Commands
     *
     * - upsert(stats)
     */
    suspend fun upsert(stats: QuestionStats)
}
