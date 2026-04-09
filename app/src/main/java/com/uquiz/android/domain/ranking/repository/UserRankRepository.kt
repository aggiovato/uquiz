package com.uquiz.android.domain.ranking.repository

import com.uquiz.android.domain.ranking.model.UserRankState
import kotlinx.coroutines.flow.Flow

/**
 * ### UserRankRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar el estado de ranking
 * del usuario actual.
 *
 * Esta interfaz abstrae la fuente de datos concreta usada para almacenar y exponer
 * el rating, rango y métricas históricas del usuario, de modo que el resto del
 * dominio trabaje con un contrato estable y explícito.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar el estado actual de ranking de forma reactiva,
 * - recuperar el estado actual mediante lectura puntual,
 * - persistir actualizaciones del agregado de ranking.
 */
interface UserRankRepository {
    /**
     * ### Reactive reads
     *
     * - observeCurrent()
     */
    fun observeCurrent(): Flow<UserRankState>

    /**
     * ### One-shot reads
     *
     * - getCurrent()
     */
    suspend fun getCurrent(): UserRankState

    /**
     * ### Commands
     *
     * - upsert(rank)
     */
    suspend fun upsert(rank: UserRankState)
}
