package com.uquiz.android.ui.feature.preferences.model

import com.uquiz.android.domain.user.enums.AppThemeMode

enum class AvatarSource {
    Icon,
    Image
}

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
