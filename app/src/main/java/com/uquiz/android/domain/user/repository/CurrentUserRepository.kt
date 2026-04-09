package com.uquiz.android.domain.user.repository

import kotlinx.coroutines.flow.Flow

/**
 * ### CurrentUserRepository
 *
 * Contrato de repositorio del dominio encargado de exponer y persistir la identidad
 * del usuario actualmente activo en la aplicación.
 *
 * Esta interfaz abstrae el mecanismo usado para recordar qué usuario está activo,
 * permitiendo que el resto de capas trabaje con un contrato simple e independiente
 * de preferencias, base de datos o almacenamiento local.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar de forma reactiva el identificador del usuario actual,
 * - recuperar el identificador activo mediante lectura puntual,
 * - actualizar el usuario activo cuando cambie el contexto de sesión.
 */
interface CurrentUserRepository {
    /**
     * ### Reactive reads
     *
     * - observeCurrentUserId()
     */
    fun observeCurrentUserId(): Flow<String?>

    /**
     * ### One-shot reads
     *
     * - getCurrentUserId()
     */
    suspend fun getCurrentUserId(): String?

    /**
     * ### Commands
     *
     * - setCurrentUserId(userId)
     */
    suspend fun setCurrentUserId(userId: String)
}
