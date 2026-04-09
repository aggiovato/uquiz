package com.uquiz.android.data.user.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.uquiz.android.domain.user.enums.AppThemeMode
import com.uquiz.android.domain.user.model.UserPreferences
import com.uquiz.android.domain.user.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override fun observePreferences(): Flow<UserPreferences> {
        return dataStore.data.map(::toModel)
    }

    override suspend fun getPreferences(): UserPreferences {
        return observePreferences().first()
    }

    override suspend fun updateLanguage(code: String) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE_CODE_KEY] = code
        }
    }

    override suspend fun updateThemeMode(mode: AppThemeMode) {
        dataStore.edit { prefs ->
            prefs[THEME_MODE_KEY] = mode.name
        }
    }

    override suspend fun setReminderEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[REMINDER_ENABLED_KEY] = enabled
        }
    }

    override suspend fun setReminderTime(hour: Int, minute: Int) {
        dataStore.edit { prefs ->
            prefs[REMINDER_HOUR_KEY] = hour
            prefs[REMINDER_MINUTE_KEY] = minute
        }
    }

    override suspend fun setReminderDays(days: Set<String>) {
        dataStore.edit { prefs ->
            prefs[REMINDER_DAYS_KEY] = days
        }
    }

    private fun toModel(preferences: Preferences): UserPreferences {
        return UserPreferences(
            languageCode = preferences[LANGUAGE_CODE_KEY] ?: "en",
            themeMode = preferences[THEME_MODE_KEY]
                ?.let { runCatching { AppThemeMode.valueOf(it) }.getOrNull() }
                ?: AppThemeMode.LIGHT,
            practiceReminderEnabled = preferences[REMINDER_ENABLED_KEY] ?: false,
            practiceReminderHour = preferences[REMINDER_HOUR_KEY] ?: 20,
            practiceReminderMinute = preferences[REMINDER_MINUTE_KEY] ?: 0,
            practiceReminderDays = preferences[REMINDER_DAYS_KEY] ?: emptySet()
        )
    }

    private companion object {
        val LANGUAGE_CODE_KEY = stringPreferencesKey("language_code")
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        val REMINDER_ENABLED_KEY = booleanPreferencesKey("practice_reminder_enabled")
        val REMINDER_HOUR_KEY = intPreferencesKey("practice_reminder_hour")
        val REMINDER_MINUTE_KEY = intPreferencesKey("practice_reminder_minute")
        val REMINDER_DAYS_KEY = stringSetPreferencesKey("practice_reminder_days")
    }
}
