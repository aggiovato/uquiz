package com.uquiz.android.core.analytics.usecase

import com.uquiz.android.domain.stats.repository.PackStatsRepository

class UpdatePackStatsFromAttemptUseCase(
    private val packStatsRepository: PackStatsRepository,
) {
    suspend operator fun invoke(packId: String) {
        packStatsRepository.refresh(packId)
    }
}
