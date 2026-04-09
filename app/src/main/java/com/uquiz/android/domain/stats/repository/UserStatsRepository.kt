package com.uquiz.android.domain.stats.repository

import com.uquiz.android.domain.stats.projection.UserStatsSnapshot
import kotlinx.coroutines.flow.Flow

/**
 * ### UserStatsRepository
 *
 * Contrato de repositorio del dominio encargado de exponer la instantánea global
 * de estadísticas del usuario.
 *
 * Esta interfaz abstrae la composición de métricas agregadas transversales para
 * que la capa de presentación pueda observar una visión unificada del estado del
 * usuario sin depender de cómo se calculan o almacenan internamente.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar de forma reactiva la instantánea global de estadísticas del usuario.
 *
 * Al tratarse de un contrato de dominio, su definición no depende de detalles
 * de implementación específicos como Room, red, caché o pipelines de agregación.
 */
interface UserStatsRepository {
    /**
     * ### Reactive reads
     *
     * - observeSnapshot()
     */
    fun observeSnapshot(): Flow<UserStatsSnapshot>
}
