package com.uquiz.android.ui.feature.home.model

import com.uquiz.android.domain.ranking.enums.UserRank

data class ContinuePackUiModel(
    val packId: String,
    val title: String,
    val answeredCount: Int,
    val totalQuestions: Int,
    val progressFraction: Float,
    val icon: String?,
    val colorHex: String?
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val displayName: String = "",
    val avatarIcon: String? = null,
    val avatarImageUri: String? = null,
    val currentRank: UserRank = UserRank.INITIATE,
    val dayStreak: Int = 0,
    val totalPoints: Int = 0,
    val continuePlaying: List<ContinuePackUiModel> = emptyList(),
    val continueStudying: List<ContinuePackUiModel> = emptyList()
)
