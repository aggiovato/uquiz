package com.uquiz.android.ui.shared.model

import com.uquiz.android.domain.content.model.Pack

data class PackListItemUiModel(
    val pack: Pack,
    val questionCount: Int,
    val answeredCount: Int = 0,
    val progress: Float? = null
)
