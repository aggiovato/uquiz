package com.uquiz.android.ui.feature.library.model

import com.uquiz.android.ui.designsystem.components.feedback.UToastTone

/** Eventos de una sola emisión que la pantalla de biblioteca debe consumir. */
sealed interface LibraryUiEvent {
    data object OpenImportPicker : LibraryUiEvent
    data class OpenRandomStudy(val packId: String) : LibraryUiEvent
    data class ShowToast(val message: String, val tone: UToastTone) : LibraryUiEvent
}
