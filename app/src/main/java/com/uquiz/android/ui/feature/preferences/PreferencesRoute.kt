package com.uquiz.android.ui.feature.preferences

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.core.reminder.scheduler.ReminderScheduler
import com.uquiz.android.domain.user.repository.UserPreferencesRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import com.uquiz.android.ui.designsystem.components.actionsheet.UActionsSheetFab
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.designsystem.components.inputs.UTextField
import com.uquiz.android.ui.navigation.components.uAppLanguages
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.feature.preferences.components.AvatarSourceSelector
import com.uquiz.android.ui.feature.preferences.components.DayOfWeekSelector
import com.uquiz.android.ui.feature.preferences.components.PreferencesSection
import com.uquiz.android.ui.feature.preferences.components.ThemeModeSelector
import com.uquiz.android.ui.feature.preferences.components.TimePickerRow
import com.uquiz.android.ui.feature.preferences.model.AvatarSource
import com.uquiz.android.ui.feature.preferences.model.PreferencesUiState
import com.uquiz.android.ui.i18n.LocalStrings

@Composable
fun PreferencesRoute(
    userProfileRepository: UserProfileRepository,
    userPreferencesRepository: UserPreferencesRepository
) {
    val context = LocalContext.current
    val reminderScheduler = remember(context) { ReminderScheduler(context) }
    val viewModel: PreferencesViewModel = viewModel(
        factory = PreferencesViewModel.Factory(
            userProfileRepository = userProfileRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    )
    val uiState by viewModel.uiState.collectAsState()
    var pendingReminderSchedule by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        pendingReminderSchedule?.let { (hour, minute) ->
            if (granted) {
                reminderScheduler.scheduleDaily(hour, minute)
            }
        }
        pendingReminderSchedule = null
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        viewModel.updateDraftAvatarImage(uri.toString())
    }

    LaunchedEffect(Unit) {
        viewModel.reminderSideEffect.collect { action ->
            when (action) {
                is ReminderAction.Schedule -> {
                    val requiresNotificationPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED

                    if (requiresNotificationPermission) {
                        pendingReminderSchedule = action.hour to action.minute
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        reminderScheduler.scheduleDaily(action.hour, action.minute)
                    }
                }

                ReminderAction.Cancel -> reminderScheduler.cancel()
            }
        }
    }

    PreferencesScreen(
        uiState = uiState,
        onDisplayNameChange = viewModel::updateDraftDisplayName,
        onAvatarIconSelect = viewModel::updateDraftAvatarIcon,
        onAvatarImagePick = { photoPickerLauncher.launch(arrayOf("image/*")) },
        onAvatarUseIcon = {
            viewModel.clearDraftAvatarImage()
            viewModel.switchAvatarSource(AvatarSource.Icon)
        },
        onThemeModeSelect = viewModel::updateDraftThemeMode,
        onLanguageSelect = viewModel::updateDraftLanguage,
        onReminderToggle = viewModel::updateDraftReminderEnabled,
        onReminderTimeChange = viewModel::updateDraftReminderTime,
        onReminderDaysChange = viewModel::updateDraftReminderDays,
        onSave = viewModel::saveChanges,
        onCancel = viewModel::discardChanges
    )
}

@Composable
private fun PreferencesScreen(
    uiState: PreferencesUiState,
    onDisplayNameChange: (String) -> Unit,
    onAvatarIconSelect: (String?) -> Unit,
    onAvatarImagePick: () -> Unit,
    onAvatarUseIcon: () -> Unit,
    onThemeModeSelect: (com.uquiz.android.domain.user.enums.AppThemeMode) -> Unit,
    onLanguageSelect: (String) -> Unit,
    onReminderToggle: (Boolean) -> Unit,
    onReminderTimeChange: (Int, Int) -> Unit,
    onReminderDaysChange: (Set<String>) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val strings = LocalStrings.current
    val draft = uiState.draft

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            PreferencesSection(
                title = strings.profileSectionTitle,
                subtitle = strings.profileSectionSubtitle
            ) {
                UTextField(
                    value = draft.displayName,
                    onValueChange = onDisplayNameChange,
                    label = strings.displayNameLabel,
                    placeholder = strings.displayNameHint
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = strings.avatarLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = Neutral500
                )
                AvatarSourceSelector(
                    displayName = draft.displayName,
                    avatarSource = draft.avatarSource,
                    avatarIcon = draft.avatarIcon,
                    avatarImageUri = draft.avatarImageUri,
                    onChoosePhotoClick = onAvatarImagePick,
                    onUseIconClick = onAvatarUseIcon,
                    onIconSelect = onAvatarIconSelect
                )
            }

            PreferencesSection(
                title = strings.appearanceSectionTitle,
                subtitle = strings.appearanceSectionSubtitle
            ) {
                ThemeModeSelector(
                    selected = draft.themeMode,
                    onSelect = onThemeModeSelect
                )
            }

            PreferencesSection(
                title = strings.languageSectionTitle,
                subtitle = strings.languageSectionSubtitle
            ) {
                LanguageOptionGrid(
                    selectedCode = draft.languageCode,
                    onSelect = onLanguageSelect
                )
            }

            PreferencesSection(
                title = strings.reminderSectionTitle,
                subtitle = strings.reminderSectionSubtitle
            ) {
                ReminderToggleRow(
                    label = strings.reminderEnabledLabel,
                    checked = draft.reminderEnabled,
                    onCheckedChange = onReminderToggle
                )
                if (draft.reminderEnabled) {
                    TimePickerRow(
                        hour = draft.reminderHour,
                        minute = draft.reminderMinute,
                        onConfirm = onReminderTimeChange
                    )
                    Text(
                        text = strings.reminderDaysLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = Neutral500
                    )
                    DayOfWeekSelector(
                        selectedDays = draft.reminderDays,
                        onToggle = { day ->
                            val updated = draft.reminderDays.toMutableSet()
                            if (day in updated) updated.remove(day) else updated.add(day)
                            onReminderDaysChange(updated)
                        }
                    )
                }
            }

            Spacer(Modifier.height(96.dp))
        }

        UActionsSheetFab(
            actions = listOf(
                UFabActionItem(
                    id = "save_preferences",
                    label = strings.save,
                    description = strings.savePreferencesActionDescription,
                    iconRes = UIcons.Actions.Settings,
                    enabled = uiState.hasPendingChanges,
                    containerColor = Navy500,
                    onClick = onSave
                ),
                UFabActionItem(
                    id = "cancel_preferences",
                    label = strings.cancel,
                    description = strings.discardPreferencesActionDescription,
                    iconRes = UIcons.Actions.Close,
                    enabled = uiState.hasPendingChanges,
                    containerColor = Neutral500,
                    onClick = onCancel
                )
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun LanguageOptionGrid(
    selectedCode: String,
    onSelect: (String) -> Unit
) {
    val strings = LocalStrings.current
    val chunked = uAppLanguages.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { lang ->
                    val isSelected = lang.code == selectedCode
                    Surface(
                        onClick = { onSelect(lang.code) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppRadius),
                        color = if (isSelected) BrandNavy else Color.White,
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) BrandNavy else Neutral100
                        ),
                        tonalElevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Image(
                                painter = painterResource(lang.flagRes),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = lang.name(strings),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Ink950,
                                maxLines = 1
                            )
                        }
                    }
                }
                repeat(3 - rowItems.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ReminderToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(AppRadius),
        color = Color.White,
        border = BorderStroke(1.dp, Neutral100),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (checked) Ink950 else Neutral400
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Navy500,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Neutral100,
                    uncheckedBorderColor = Neutral400
                )
            )
        }
    }
}
