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
 * Backing ViewModel for the preferences screen.
 *
 * Combines the current user profile (display name, avatar) with the stored
 * preferences (theme, language, reminder settings) into a single [PreferencesUiState].
 * Preferences are edited through a local draft and only persisted when
 * [saveChanges] is called.
 *
 * Side effects (scheduling/cancelling the reminder alarm) are emitted via
 * [reminderSideEffect] so the composable can perform the platform call with a
 * Context while keeping the ViewModel free of Android framework dependencies.
 */
class PreferencesViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesUiState())
    val uiState: StateFlow<PreferencesUiState> = _uiState.asStateFlow()

    /** Emitted when the caller must schedule (true) or cancel (false) the reminder alarm. */
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

    fun updateDraftDisplayName(name: String) = updateDraft { copy(displayName = name) }

    fun updateDraftAvatarIcon(icon: String?) = updateDraft {
        copy(avatarSource = AvatarSource.Icon, avatarIcon = icon)
    }

    fun updateDraftAvatarImage(uri: String?) = updateDraft {
        copy(avatarSource = AvatarSource.Image, avatarImageUri = uri)
    }

    fun clearDraftAvatarImage() = updateDraft {
        copy(avatarSource = AvatarSource.Icon, avatarImageUri = null)
    }

    fun switchAvatarSource(source: AvatarSource) = updateDraft {
        copy(avatarSource = source)
    }

    fun updateDraftThemeMode(mode: com.uquiz.android.domain.user.enums.AppThemeMode) = updateDraft {
        copy(themeMode = mode)
    }

    fun updateDraftLanguage(code: String) = updateDraft {
        copy(languageCode = sanitizeLanguageCode(code))
    }

    fun updateDraftReminderEnabled(enabled: Boolean) = updateDraft {
        copy(reminderEnabled = enabled)
    }

    fun updateDraftReminderTime(hour: Int, minute: Int) = updateDraft {
        copy(reminderHour = hour, reminderMinute = minute)
    }

    fun updateDraftReminderDays(days: Set<String>) = updateDraft {
        copy(reminderDays = days)
    }

    fun discardChanges() {
        val persisted = _uiState.value.persisted
        _uiState.value = PreferencesUiState(
            isLoading = false,
            persisted = persisted,
            draft = persisted,
            hasPendingChanges = false
        )
    }

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

/** Action emitted by [PreferencesViewModel] to ask the composable to interact with AlarmManager. */
sealed interface ReminderAction {
    data class Schedule(val hour: Int, val minute: Int) : ReminderAction
    data object Cancel : ReminderAction
}
