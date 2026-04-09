package com.uquiz.android.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

fun createUserPreferencesDataStore(context: Context) =
    PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("user_preferences.preferences_pb") }
    )
