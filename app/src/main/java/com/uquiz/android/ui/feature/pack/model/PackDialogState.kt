package com.uquiz.android.ui.feature.pack.model

import com.uquiz.android.domain.content.model.Pack

sealed interface PackDialogState {
    data object None : PackDialogState
    data class EditPack(val pack: Pack) : PackDialogState
    data class DeletePack(val pack: Pack) : PackDialogState
}
