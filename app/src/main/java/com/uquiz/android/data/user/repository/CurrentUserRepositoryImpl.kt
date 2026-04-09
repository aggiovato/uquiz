package com.uquiz.android.data.user.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class CurrentUserRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : CurrentUserRepository {

    override fun observeCurrentUserId(): Flow<String?> {
        return dataStore.data.map { it[CURRENT_USER_ID_KEY] }
    }

    override suspend fun getCurrentUserId(): String? {
        return dataStore.data.map { it[CURRENT_USER_ID_KEY] }.firstOrNull()
    }

    override suspend fun setCurrentUserId(userId: String) {
        dataStore.edit { prefs ->
            prefs[CURRENT_USER_ID_KEY] = userId
        }
    }

    private companion object {
        val CURRENT_USER_ID_KEY = stringPreferencesKey("current_user_id")
    }
}
