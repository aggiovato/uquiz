package com.uquiz.android.ui.feature.preferences.model

/** Estado de UI de la pantalla de preferencias. */
data class PreferencesUiState(
    val isLoading: Boolean = true,
    val persisted: PreferencesEditableState = PreferencesEditableState(),
    val draft: PreferencesEditableState = PreferencesEditableState(),
    val hasPendingChanges: Boolean = false
)
