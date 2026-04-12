package com.uquiz.android.ui.feature.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.user.repository.UserPreferencesRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import com.uquiz.android.ui.feature.preferences.model.AvatarSource
import com.uquiz.android.ui.feature.preferences.model.PreferencesEditableState
import com.uquiz.android.ui.feature.preferences.model.PreferencesUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de preferencias.
 *
 * Combina el perfil actual y las preferencias persistidas en un único
 * [PreferencesUiState]. Mantiene un borrador local para permitir edición
 * diferida y expone side effects para el agendado del recordatorio.
 */
class PreferencesViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesUiState())
    val uiState: StateFlow<PreferencesUiState> = _uiState.asStateFlow()

    /** Flujo de acciones que delega en la UI la interacción con el programador de recordatorios. */
    private val _reminderSideEffect = MutableSharedFlow<ReminderAction>(extraBufferCapacity = 1)
    val reminderSideEffect: SharedFlow<ReminderAction> = _reminderSideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                userProfileRepository.observeCurrent(),
                userPreferencesRepository.observePreferences()
            ) { profile, prefs -> profile to prefs }
                .collect { (profile, prefs) ->
                    val persisted = PreferencesEditableState(
                        displayName = profile.displayName,
                        avatarSource = if (profile.avatarImageUri.isNullOrBlank()) AvatarSource.Icon else AvatarSource.Image,
                        avatarIcon = profile.avatarIcon,
                        avatarImageUri = profile.avatarImageUri,
                        themeMode = prefs.themeMode,
                        languageCode = sanitizeLanguageCode(prefs.languageCode),
                        reminderEnabled = prefs.practiceReminderEnabled,
                        reminderHour = prefs.practiceReminderHour,
                        reminderMinute = prefs.practiceReminderMinute,
                        reminderDays = prefs.practiceReminderDays
                    )

                    val current = _uiState.value
                    val nextDraft = if (current.isLoading || !current.hasPendingChanges) {
                        persisted
                    } else {
                        current.draft
                    }
                    _uiState.value = PreferencesUiState(
                        isLoading = false,
                        persisted = persisted,
                        draft = nextDraft,
                        hasPendingChanges = nextDraft != persisted
                    )
                }
        }
    }

    /** Actualiza el nombre visible del borrador actual. */
    fun updateDraftDisplayName(name: String) = updateDraft { copy(displayName = name) }

    /** Actualiza el icono del avatar y fuerza la fuente de avatar a icono. */
    fun updateDraftAvatarIcon(icon: String?) = updateDraft {
        copy(avatarSource = AvatarSource.Icon, avatarIcon = icon)
    }

    /** Actualiza la imagen del avatar usando una uri persistible. */
    fun updateDraftAvatarImage(uri: String?) = updateDraft {
        copy(avatarSource = AvatarSource.Image, avatarImageUri = uri)
    }

    /** Limpia la imagen del avatar y vuelve el borrador al modo icono. */
    fun clearDraftAvatarImage() = updateDraft {
        copy(avatarSource = AvatarSource.Icon, avatarImageUri = null)
    }

    /** Cambia explícitamente la fuente activa del avatar. */
    fun switchAvatarSource(source: AvatarSource) = updateDraft {
        copy(avatarSource = source)
    }

    /** Actualiza el tema preferido en el borrador. */
    fun updateDraftThemeMode(mode: com.uquiz.android.domain.user.enums.AppThemeMode) = updateDraft {
        copy(themeMode = mode)
    }

    /** Actualiza el idioma visible en el borrador. */
    fun updateDraftLanguage(code: String) = updateDraft {
        copy(languageCode = sanitizeLanguageCode(code))
    }

    /** Activa o desactiva el recordatorio en el borrador. */
    fun updateDraftReminderEnabled(enabled: Boolean) = updateDraft {
        copy(reminderEnabled = enabled)
    }

    /** Actualiza la hora objetivo del recordatorio. */
    fun updateDraftReminderTime(hour: Int, minute: Int) = updateDraft {
        copy(reminderHour = hour, reminderMinute = minute)
    }

    /** Sustituye el conjunto completo de días seleccionados para el recordatorio. */
    fun updateDraftReminderDays(days: Set<String>) = updateDraft {
        copy(reminderDays = days)
    }

    /** Descarta el borrador y restaura el último estado persistido. */
    fun discardChanges() {
        val persisted = _uiState.value.persisted
        _uiState.value = PreferencesUiState(
            isLoading = false,
            persisted = persisted,
            draft = persisted,
            hasPendingChanges = false
        )
    }

    /** Persiste el borrador actual y emite el side effect de recordatorio si cambió su configuración. */
    fun saveChanges() {
        val current = _uiState.value
        if (current.isLoading || !current.hasPendingChanges) return

        val persisted = current.persisted
        val draft = current.draft.sanitized(persisted)

        viewModelScope.launch {
            userProfileRepository.updateDisplayName(draft.displayName)
            userProfileRepository.updateAvatarIcon(draft.avatarIcon)
            userProfileRepository.updateAvatarImageUri(
                if (draft.avatarSource == AvatarSource.Image) draft.avatarImageUri else null
            )
            userPreferencesRepository.updateThemeMode(draft.themeMode)
            userPreferencesRepository.updateLanguage(draft.languageCode)
            userPreferencesRepository.setReminderEnabled(draft.reminderEnabled)
            userPreferencesRepository.setReminderTime(draft.reminderHour, draft.reminderMinute)
            userPreferencesRepository.setReminderDays(draft.reminderDays)

            _uiState.value = PreferencesUiState(
                isLoading = false,
                persisted = draft,
                draft = draft,
                hasPendingChanges = false
            )

            if (reminderConfigurationChanged(persisted, draft)) {
                if (draft.reminderEnabled) {
                    _reminderSideEffect.emit(ReminderAction.Schedule(draft.reminderHour, draft.reminderMinute))
                } else {
                    _reminderSideEffect.emit(ReminderAction.Cancel)
                }
            }
        }
    }

    private fun updateDraft(transform: PreferencesEditableState.() -> PreferencesEditableState) {
        val current = _uiState.value
        if (current.isLoading) return
        val nextDraft = current.draft.transform()
        _uiState.value = current.copy(
            draft = nextDraft,
            hasPendingChanges = nextDraft != current.persisted
        )
    }

    /** Factory que resuelve las dependencias requeridas por [PreferencesViewModel]. */
    class Factory(
        private val userProfileRepository: UserProfileRepository,
        private val userPreferencesRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PreferencesViewModel(userProfileRepository, userPreferencesRepository) as T
    }
}

private fun PreferencesEditableState.sanitized(
    fallback: PreferencesEditableState
): PreferencesEditableState {
    val trimmedName = displayName.trim().ifBlank { fallback.displayName }
    val sanitizedSource = if (avatarSource == AvatarSource.Image && avatarImageUri.isNullOrBlank()) {
        AvatarSource.Icon
    } else {
        avatarSource
    }

    return copy(
        displayName = trimmedName,
        avatarSource = sanitizedSource,
        languageCode = sanitizeLanguageCode(languageCode),
        avatarImageUri = avatarImageUri?.takeIf { it.isNotBlank() }
    )
}

private fun reminderConfigurationChanged(
    persisted: PreferencesEditableState,
    draft: PreferencesEditableState
): Boolean {
    return persisted.reminderEnabled != draft.reminderEnabled ||
        persisted.reminderHour != draft.reminderHour ||
        persisted.reminderMinute != draft.reminderMinute ||
        persisted.reminderDays != draft.reminderDays
}

private fun sanitizeLanguageCode(code: String): String {
    return if (VISIBLE_LANGUAGE_CODES.contains(code)) code else DEFAULT_LANGUAGE_CODE
}

private val VISIBLE_LANGUAGE_CODES = setOf("en", "es", "it", "ja")
private const val DEFAULT_LANGUAGE_CODE = "en"

/** Acción emitida por [PreferencesViewModel] para coordinar el recordatorio con la capa UI. */
sealed interface ReminderAction {
    data class Schedule(val hour: Int, val minute: Int) : ReminderAction
    data object Cancel : ReminderAction
}
