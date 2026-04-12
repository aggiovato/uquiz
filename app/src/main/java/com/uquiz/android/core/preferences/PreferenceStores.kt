package com.uquiz.android.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory

/**
 * Singleton de DataStore para las preferencias del usuario.
 *
 * DataStore lanza [IllegalStateException] si se crean dos instancias activas para el mismo
 * fichero. Este objeto garantiza que solo existe una instancia por proceso, con alcance ligado
 * al ciclo de vida del proceso (no de la Activity).
 */
object PreferencesModule {
    @Volatile
    private var INSTANCE: DataStore<Preferences>? = null

    /**
     * Devuelve la instancia singleton de [DataStore]. El [context] debe ser el contexto de
     * aplicación para evitar fugas de memoria.
     */
    fun getDataStore(context: Context): DataStore<Preferences> =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: PreferenceDataStoreFactory.create(
                produceFile = {
                    context.applicationContext.preferencesDataStoreFile("user_preferences")
                },
            ).also { INSTANCE = it }
        }
}
