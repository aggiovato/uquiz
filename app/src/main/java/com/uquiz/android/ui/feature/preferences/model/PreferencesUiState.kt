package com.uquiz.android.ui.feature.preferences.model

data class PreferencesUiState(
    val isLoading: Boolean = true,
    val persisted: PreferencesEditableState = PreferencesEditableState(),
    val draft: PreferencesEditableState = PreferencesEditableState(),
    val hasPendingChanges: Boolean = false
)
