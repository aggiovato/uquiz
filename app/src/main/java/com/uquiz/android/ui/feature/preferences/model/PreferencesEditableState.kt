package com.uquiz.android.ui.feature.preferences.model

import com.uquiz.android.domain.user.enums.AppThemeMode

/** Fuente elegida para representar el avatar del usuario en preferencias. */
enum class AvatarSource {
    Icon,
    Image
}

/** Estado editable de preferencias antes de persistir los cambios. */
data class PreferencesEditableState(
    val displayName: String = "",
    val avatarSource: AvatarSource = AvatarSource.Icon,
    val avatarIcon: String? = null,
    val avatarImageUri: String? = null,
    val themeMode: AppThemeMode = AppThemeMode.LIGHT,
    val languageCode: String = "en",
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val reminderDays: Set<String> = emptySet()
)
