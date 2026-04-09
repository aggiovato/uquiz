package com.uquiz.android.data.stats.repository

import com.uquiz.android.data.attempts.dao.AttemptDao
import com.uquiz.android.data.attempts.entity.AttemptEntity
import com.uquiz.android.domain.attempts.enums.AttemptStatus
import com.uquiz.android.domain.stats.projection.UserStatsSnapshot
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class UserStatsRepositoryImpl(
    private val attemptDao: AttemptDao,
    private val currentUserRepository: CurrentUserRepository
) : UserStatsRepository {

    override fun observeSnapshot(): Flow<UserStatsSnapshot> =
        currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(UserStatsSnapshot())
                else attemptDao.observeAll(userId).map { entities ->
                    val completed = entities.filter { it.status == AttemptStatus.COMPLETED }
                    val totalAnswered = completed.sumOf { it.totalQuestions }
                    val totalCorrect = completed.sumOf { it.correctAnswers }
                    UserStatsSnapshot(
                        dayStreak = calculateDayStreak(completed),
                        totalPoints = completed.sumOf { it.score.toLong() },
                        completedSessions = completed.size,
                        accuracyPercent = if (totalAnswered > 0) {
                            ((totalCorrect.toFloat() / totalAnswered.toFloat()) * 100f).toInt()
                        } else null,
                        totalCorrect = totalCorrect,
                        totalAnswered = totalAnswered,
                    )
                }
            }

    private fun calculateDayStreak(completed: List<AttemptEntity>): Int {
        if (completed.isEmpty()) return 0
        val uniqueDays = completed
            .mapNotNull { it.completedAt }
            .map { timestamp ->
                val offset = TimeZone.getDefault().getOffset(timestamp).toLong()
                (timestamp + offset) / MILLIS_PER_DAY
            }
            .distinct()
            .sortedDescending()

        if (uniqueDays.isEmpty()) return 0
        var streak = 1
        for (index in 1 until uniqueDays.size) {
            if (uniqueDays[index - 1] - uniqueDays[index] == 1L) streak++ else break
        }
        return streak
    }

    private companion object {
        const val MILLIS_PER_DAY = 24L * 60L * 60L * 1000L
    }
}
