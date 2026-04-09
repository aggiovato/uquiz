package com.uquiz.android.ui.feature.library.model

import com.uquiz.android.ui.designsystem.components.feedback.UToastTone

sealed interface LibraryUiEvent {
    data object OpenImportPicker : LibraryUiEvent
    data class ShowToast(val message: String, val tone: UToastTone) : LibraryUiEvent
}
