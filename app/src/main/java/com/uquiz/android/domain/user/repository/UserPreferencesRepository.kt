package com.uquiz.android.domain.user.repository

import com.uquiz.android.domain.user.enums.AppThemeMode
import com.uquiz.android.domain.user.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * ### UserPreferencesRepository
 *
 * Contrato de repositorio del dominio encargado de gestionar las preferencias del
 * usuario.
 *
 * Esta interfaz abstrae la lectura y actualización de ajustes persistidos como
 * idioma, tema y recordatorios, de modo que la aplicación no dependa del mecanismo
 * concreto usado para almacenar dicha configuración.
 *
 * Su responsabilidad principal es ofrecer una API coherente para:
 * - observar el estado completo de preferencias,
 * - recuperar las preferencias actuales mediante lectura puntual,
 * - actualizar cada grupo de preferencias mediante comandos explícitos.
 */
interface UserPreferencesRepository {
    /**
     * ### Reactive reads
     *
     * - observePreferences()
     */
    fun observePreferences(): Flow<UserPreferences>

    /**
     * ### One-shot reads
     *
     * - getPreferences()
     */
    suspend fun getPreferences(): UserPreferences

    /**
     * ### Commands
     *
     * - updateLanguage(code)
     * - updateThemeMode(mode)
     * - setReminderEnabled(enabled)
     * - setReminderTime(hour, minute)
     * - setReminderDays(days)
     */
    suspend fun updateLanguage(code: String)
    suspend fun updateThemeMode(mode: AppThemeMode)
    suspend fun setReminderEnabled(enabled: Boolean)
    suspend fun setReminderTime(hour: Int, minute: Int)
    suspend fun setReminderDays(days: Set<String>)
}
