package com.uquiz.android.domain.user.repository

import com.uquiz.android.domain.user.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * ### UserProfileRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar el perfil visible
 * del usuario actual.
 *
 * Esta interfaz abstrae la fuente de datos concreta utilizada para mantener el
 * perfil del usuario, incluyendo su nombre y avatar, y ofrece un contrato estable
 * para lectura y actualización desde el dominio.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar el perfil actual de forma reactiva,
 * - recuperar el perfil actual mediante lectura puntual,
 * - asegurar que exista un perfil inicial válido,
 * - actualizar nombre y avatar del usuario.
 */
interface UserProfileRepository {
    /**
     * ### Reactive reads
     *
     * - observeCurrent()
     */
    fun observeCurrent(): Flow<UserProfile>

    /**
     * ### One-shot reads
     *
     * - getCurrent()
     * - ensureInitialProfile()
     */
    suspend fun getCurrent(): UserProfile
    suspend fun ensureInitialProfile(): UserProfile

    /**
     * ### Commands
     *
     * - updateDisplayName(name)
     * - updateAvatarIcon(icon)
     * - updateAvatarImageUri(uri)
     */
    suspend fun updateDisplayName(name: String)
    suspend fun updateAvatarIcon(icon: String?)
    suspend fun updateAvatarImageUri(uri: String?)
}
