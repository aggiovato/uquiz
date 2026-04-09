package com.uquiz.android.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.projection.ActivePackProgress
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.projection.PackWithQuestionCount
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import com.uquiz.android.ui.feature.home.model.ContinuePackUiModel
import com.uquiz.android.ui.feature.home.model.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    userProfileRepository: UserProfileRepository,
    userRankRepository: UserRankRepository,
    userStatsRepository: UserStatsRepository,
    attemptRepository: AttemptRepository,
    packRepository: PackRepository
) : ViewModel() {

    private val activeProgress = combine(
        attemptRepository.observeActivePackProgress(AttemptMode.GAME),
        attemptRepository.observeActivePackProgress(AttemptMode.STUDY),
        packRepository.observeAllWithQuestionCounts()
    ) { activeGame, activeStudy, packs ->
        Triple(activeGame, activeStudy, packs)
    }

    val uiState = combine(
        userProfileRepository.observeCurrent(),
        userRankRepository.observeCurrent(),
        userStatsRepository.observeSnapshot(),
        activeProgress
    ) { profile, rank, stats, progress ->
        val (activeGame, activeStudy, packs) = progress
        val packsById = packs.associateBy { it.pack.id }
        HomeUiState(
            isLoading = false,
            displayName = profile.displayName,
            avatarIcon = profile.avatarIcon,
            avatarImageUri = profile.avatarImageUri,
            currentRank = rank.currentRank,
            dayStreak = stats.dayStreak,
            totalPoints = stats.totalPoints.toInt(),
            continuePlaying = activeGame.asContinuePackModels(packsById),
            continueStudying = activeStudy.asContinuePackModels(packsById)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeUiState()
    )

    class Factory(
        private val userProfileRepository: UserProfileRepository,
        private val userRankRepository: UserRankRepository,
        private val userStatsRepository: UserStatsRepository,
        private val attemptRepository: AttemptRepository,
        private val packRepository: PackRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                userProfileRepository = userProfileRepository,
                userRankRepository = userRankRepository,
                userStatsRepository = userStatsRepository,
                attemptRepository = attemptRepository,
                packRepository = packRepository
            ) as T
        }
    }
}

private fun List<ActivePackProgress>.asContinuePackModels(
    packsById: Map<String, PackWithQuestionCount>
): List<ContinuePackUiModel> {
    return sortedByDescending { it.startedAt }
        .filter { it.answeredCount > 0 }
        .distinctBy { it.packId }
        .mapNotNull { progress ->
            val pack = packsById[progress.packId] ?: return@mapNotNull null
            val totalQuestions = pack.questionCount
            if (totalQuestions <= 0) return@mapNotNull null
            ContinuePackUiModel(
                packId = pack.pack.id,
                title = pack.pack.title,
                answeredCount = progress.answeredCount.coerceAtMost(totalQuestions),
                totalQuestions = totalQuestions,
                progressFraction = (progress.answeredCount.toFloat() / totalQuestions.toFloat())
                    .coerceIn(0f, 1f),
                icon = pack.pack.icon,
                colorHex = pack.pack.colorHex
            )
        }
}
