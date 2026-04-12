package com.uquiz.android.ui.feature.preferences

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.reminder.scheduler.ReminderScheduler
import com.uquiz.android.domain.user.enums.AppThemeMode
import com.uquiz.android.domain.user.repository.UserPreferencesRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.designsystem.components.inputs.UTextField
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.preferences.components.AvatarEditor
import com.uquiz.android.ui.feature.preferences.components.DayOfWeekSelector
import com.uquiz.android.ui.feature.preferences.components.LanguageOptionGrid
import com.uquiz.android.ui.feature.preferences.components.PreferencesSection
import com.uquiz.android.ui.feature.preferences.components.ReminderToggleRow
import com.uquiz.android.ui.feature.preferences.components.ThemeModeSelector
import com.uquiz.android.ui.feature.preferences.components.TimePickerRow
import com.uquiz.android.ui.feature.preferences.model.PreferencesEditableState
import com.uquiz.android.ui.feature.preferences.model.PreferencesUiState
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PreferencesRoute
 *
 * Entrada pública de la pantalla de preferencias.
 *
 * Resuelve el [PreferencesViewModel], coordina los side effects de permisos,
 * foto y recordatorios, y delega el renderizado puro a [PreferencesScreen].
 *
 * @param userProfileRepository Repositorio del perfil del usuario.
 * @param userPreferencesRepository Repositorio de preferencias persistidas.
 */
@Composable
fun PreferencesRoute(
    userProfileRepository: UserProfileRepository,
    userPreferencesRepository: UserPreferencesRepository,
) {
    val context = LocalContext.current
    val reminderScheduler = remember(context) { ReminderScheduler(context) }
    val viewModel: PreferencesViewModel =
        viewModel(
            factory =
                PreferencesViewModel.Factory(
                    userProfileRepository = userProfileRepository,
                    userPreferencesRepository = userPreferencesRepository,
                ),
        )
    val uiState by viewModel.uiState.collectAsState()
    var pendingReminderSchedule by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { granted ->
            pendingReminderSchedule?.let { (hour, minute) ->
                if (granted) {
                    reminderScheduler.scheduleDaily(hour, minute)
                }
            }
            pendingReminderSchedule = null
        }

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
        ) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            }
            viewModel.updateDraftAvatarImage(uri.toString())
        }

    LaunchedEffect(Unit) {
        viewModel.reminderSideEffect.collect { action ->
            when (action) {
                is ReminderAction.Schedule -> {
                    val requiresNotificationPermission =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS,
                            ) != PackageManager.PERMISSION_GRANTED

                    if (requiresNotificationPermission) {
                        pendingReminderSchedule = action.hour to action.minute
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        reminderScheduler.scheduleDaily(action.hour, action.minute)
                    }
                }

                ReminderAction.Cancel -> {
                    reminderScheduler.cancel()
                }
            }
        }
    }

    PreferencesScreen(
        uiState = uiState,
        onDisplayNameChange = viewModel::updateDraftDisplayName,
        onAvatarImagePick = { photoPickerLauncher.launch(arrayOf("image/*")) },
        onAvatarDeletePhoto = viewModel::clearDraftAvatarImage,
        onThemeModeSelect = viewModel::updateDraftThemeMode,
        onLanguageSelect = viewModel::updateDraftLanguage,
        onReminderToggle = viewModel::updateDraftReminderEnabled,
        onReminderTimeChange = viewModel::updateDraftReminderTime,
        onReminderDaysChange = viewModel::updateDraftReminderDays,
        onSave = viewModel::saveChanges,
        onCancel = viewModel::discardChanges,
    )
}

@Composable
private fun PreferencesScreen(
    uiState: PreferencesUiState,
    onDisplayNameChange: (String) -> Unit,
    onAvatarImagePick: () -> Unit,
    onAvatarDeletePhoto: () -> Unit,
    onThemeModeSelect: (AppThemeMode) -> Unit,
    onLanguageSelect: (String) -> Unit,
    onReminderToggle: (Boolean) -> Unit,
    onReminderTimeChange: (Int, Int) -> Unit,
    onReminderDaysChange: (Set<String>) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val draft = uiState.draft

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            PreferencesSection(
                title = strings.preferences.profileSectionTitle,
                subtitle = strings.preferences.profileSectionSubtitle,
            ) {
                UTextField(
                    value = draft.displayName,
                    onValueChange = onDisplayNameChange,
                    label = strings.preferences.displayNameLabel,
                    placeholder = strings.preferences.displayNameHint,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = strings.preferences.avatarLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = Neutral500,
                )
                AvatarEditor(
                    avatarImageUri = draft.avatarImageUri,
                    onChoosePhotoClick = onAvatarImagePick,
                    onDeletePhotoClick = onAvatarDeletePhoto,
                )
            }

            PreferencesSection(
                title = strings.preferences.appearanceSectionTitle,
                subtitle = strings.preferences.appearanceSectionSubtitle,
            ) {
                ThemeModeSelector(
                    selected = draft.themeMode,
                    onSelect = onThemeModeSelect,
                )
            }

            PreferencesSection(
                title = strings.preferences.languageSectionTitle,
                subtitle = strings.preferences.languageSectionSubtitle,
            ) {
                LanguageOptionGrid(
                    selectedCode = draft.languageCode,
                    onSelect = onLanguageSelect,
                )
            }

            PreferencesSection(
                title = strings.preferences.reminderSectionTitle,
                subtitle = strings.preferences.reminderSectionSubtitle,
            ) {
                ReminderToggleRow(
                    label = strings.preferences.reminderEnabledLabel,
                    checked = draft.reminderEnabled,
                    onCheckedChange = onReminderToggle,
                )
                if (draft.reminderEnabled) {
                    TimePickerRow(
                        hour = draft.reminderHour,
                        minute = draft.reminderMinute,
                        onConfirm = onReminderTimeChange,
                    )
                    Text(
                        text = strings.preferences.reminderDaysLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = Neutral500,
                    )
                    DayOfWeekSelector(
                        selectedDays = draft.reminderDays,
                        onToggle = { day ->
                            val updated = draft.reminderDays.toMutableSet()
                            if (day in updated) updated.remove(day) else updated.add(day)
                            onReminderDaysChange(updated)
                        },
                    )
                }
            }

            Spacer(Modifier.height(96.dp))
        }

        UActionsSheetFab(
            actions =
                listOf(
                    UFabActionItem(
                        id = "save_preferences",
                        label = strings.common.save,
                        description = strings.preferences.savePreferencesActionDescription,
                        iconRes = UIcons.Actions.Settings,
                        enabled = uiState.hasPendingChanges,
                        containerColor = Navy500,
                        onClick = onSave,
                    ),
                    UFabActionItem(
                        id = "cancel_preferences",
                        label = strings.common.cancel,
                        description = strings.preferences.discardPreferencesActionDescription,
                        iconRes = UIcons.Actions.Close,
                        enabled = uiState.hasPendingChanges,
                        containerColor = Neutral500,
                        onClick = onCancel,
                    ),
                ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@UPreview
@Composable
private fun PreferencesScreenPreview() {
    UTheme {
        PreferencesScreen(
            uiState =
                PreferencesUiState(
                    isLoading = false,
                    persisted = previewPreferencesEditableState(),
                    draft = previewPreferencesEditableState(),
                    hasPendingChanges = false,
                ),
            onDisplayNameChange = {},
            onAvatarImagePick = {},
            onAvatarDeletePhoto = {},
            onThemeModeSelect = {},
            onLanguageSelect = {},
            onReminderToggle = {},
            onReminderTimeChange = { _, _ -> },
            onReminderDaysChange = {},
            onSave = {},
            onCancel = {},
        )
    }
}

private fun previewPreferencesEditableState() =
    PreferencesEditableState(
        displayName = "Ada",
        avatarImageUri = null,
        themeMode = AppThemeMode.SYSTEM,
        languageCode = "es",
        reminderEnabled = true,
        reminderHour = 20,
        reminderMinute = 30,
        reminderDays = setOf("MON", "WED", "FRI"),
    )
