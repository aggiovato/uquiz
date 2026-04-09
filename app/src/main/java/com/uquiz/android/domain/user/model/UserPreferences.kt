package com.uquiz.android.domain.user.model

import com.uquiz.android.domain.user.enums.AppThemeMode

/**
 * ### UserPreferences
 *
 * Modelo de dominio que representa el conjunto de preferencias configurables del
 * usuario en la aplicación.
 *
 * Esta estructura agrupa ajustes relacionados con idioma, apariencia y recordatorios
 * de práctica, de forma que la capa de dominio y la capa de presentación puedan
 * trabajar con una única fuente coherente de configuración.
 *
 * Propiedades:
 * - [languageCode]: código de idioma actualmente seleccionado.
 * - [themeMode]: modo de tema visual preferido por el usuario.
 * - [practiceReminderEnabled]: indica si los recordatorios de práctica están activos.
 * - [practiceReminderHour]: hora configurada para el recordatorio diario.
 * - [practiceReminderMinute]: minuto configurado para el recordatorio diario.
 * - [practiceReminderDays]: conjunto de días en los que el recordatorio debe activarse.
 */
data class UserPreferences(
    val languageCode: String = "en",
    val themeMode: AppThemeMode = AppThemeMode.LIGHT,
    val practiceReminderEnabled: Boolean = false,
    val practiceReminderHour: Int = 20,
    val practiceReminderMinute: Int = 0,
    val practiceReminderDays: Set<String> = emptySet(),
)
