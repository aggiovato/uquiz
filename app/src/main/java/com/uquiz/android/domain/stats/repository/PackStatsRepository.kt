package com.uquiz.android.domain.stats.repository

import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.domain.stats.projection.PackPracticeStats
import com.uquiz.android.domain.stats.projection.PackStudyProgress
import kotlinx.coroutines.flow.Flow

/**
 * ### PackStatsRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar lecturas y refresco
 * de estadísticas agregadas de packs.
 *
 * Esta interfaz abstrae la fuente de datos concreta utilizada para construir
 * proyecciones detalladas de rendimiento por pack, de modo que la capa de dominio
 * y la capa de presentación trabajen con un contrato estable e independiente de
 * detalles de persistencia o cálculo.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar estadísticas detalladas de un pack de forma reactiva,
 * - forzar el recálculo o refresco de dichas estadísticas cuando sea necesario.
 *
 * Al tratarse de un contrato de dominio, su definición no depende de detalles
 * de implementación específicos como Room, red, caché o jobs de sincronización.
 */
interface PackStatsRepository {
    /**
     * ### Reactive reads
     *
     * - observeDetailed(packId)
     * - observePackPracticeStats(packId)
     * - observeActiveStudyProgress()
     */
    fun observeDetailed(packId: String): Flow<PackDetailedStats>

    fun observePackPracticeStats(packId: String): Flow<PackPracticeStats>

    fun observeActiveStudyProgress(): Flow<List<PackStudyProgress>>

    /**
     * ### Commands
     *
     * - refresh(packId)
     */
    suspend fun refresh(packId: String)
}
