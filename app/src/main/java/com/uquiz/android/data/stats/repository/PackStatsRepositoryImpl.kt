package com.uquiz.android.data.stats.repository

import com.uquiz.android.data.attempts.dao.AttemptDao
import com.uquiz.android.data.content.dao.PackQuestionDao
import com.uquiz.android.data.stats.dao.PackStatsDao
import com.uquiz.android.data.stats.mapper.PackStatsMapper
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.stats.projection.PackBestPerformance
import com.uquiz.android.domain.stats.projection.PackDetailedStats
import com.uquiz.android.domain.stats.projection.PackModeStats
import com.uquiz.android.domain.stats.projection.PackPracticeStats
import com.uquiz.android.domain.stats.projection.PackRecentActivity
import com.uquiz.android.domain.stats.projection.PackStudyProgress
import com.uquiz.android.domain.stats.model.PackStats
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.domain.stats.repository.QuestionStatsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class PackStatsRepositoryImpl(
    private val packStatsDao: PackStatsDao,
    private val attemptDao: AttemptDao,
    private val packQuestionDao: PackQuestionDao,
    private val questionStatsRepository: QuestionStatsRepository,
    private val currentUserRepository: CurrentUserRepository
) : PackStatsRepository {

    override fun observeDetailed(packId: String): Flow<PackDetailedStats> =
        currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) {
                    flowOf(PackDetailedStats(packId = packId))
                } else {
                    combine(
                        packStatsDao.observeByPackId(userId, packId),
                        attemptDao.observeRecentCompletedByPack(userId, packId, limit = 8),
                        questionStatsRepository.observeMasteredCount(packId),
                        packQuestionDao.observeQuestionCount(packId)
                    ) { cached, recent, dominatedQuestions, totalQuestions ->
                        val recentActivity = recent.map {
                            PackRecentActivity(
                                attemptId = it.attemptId,
                                mode = it.mode,
                                completedAt = it.completedAt,
                                durationMs = it.durationMs,
                                score = it.score,
                                accuracyPercent = it.totalQuestions
                                    .takeIf { total -> total > 0 }
                                    ?.let { total ->
                                        ((it.correctAnswers * 100f) / total).toInt()
                                    }
                            )
                        }
                        val model = cached?.let(PackStatsMapper::toModel)
                        val progressPercent = if (totalQuestions > 0) {
                            ((dominatedQuestions * 100f) / totalQuestions).toInt()
                        } else {
                            0
                        }
                        val studyStats = PackModeStats(
                            mode = AttemptMode.STUDY,
                            sessions = model?.totalStudySessions ?: 0,
                            accuracyPercent = model?.averageStudyAccuracyPercent,
                            averageDurationMs = model?.averageStudyDurationMs
                        )
                        val gameStats = PackModeStats(
                            mode = AttemptMode.GAME,
                            sessions = model?.totalGameSessions ?: 0,
                            accuracyPercent = model?.averageGameAccuracyPercent,
                            averageDurationMs = model?.averageGameDurationMs
                        )
                        val bestPerformance = when {
                            (model?.bestGameScore ?: 0) > 0 -> PackBestPerformance(
                                mode = AttemptMode.GAME,
                                scoreLabel = "${model?.bestGameScore ?: 0}",
                                numericScore = model?.bestGameScore
                            )
                            model?.bestStudyAccuracyPercent != null -> PackBestPerformance(
                                mode = AttemptMode.STUDY,
                                scoreLabel = "${model.bestStudyAccuracyPercent}%",
                                numericScore = model.bestStudyAccuracyPercent
                            )
                            else -> null
                        }

                        PackDetailedStats(
                            packId = packId,
                            totalSessions = model?.totalSessions ?: 0,
                            averageAccuracyPercent = model?.averageAccuracyPercent,
                            averageDurationMs = model?.averageDurationMs,
                            progressPercent = progressPercent,
                            dominatedQuestions = dominatedQuestions,
                            totalQuestions = totalQuestions,
                            lastSessionAt = recentActivity.firstOrNull()?.completedAt ?: model?.lastSessionAt,
                            lastSessionMode = recentActivity.firstOrNull()?.mode ?: model?.lastSessionMode,
                            mostUsedMode = model?.mostUsedMode,
                            studyStats = studyStats,
                            gameStats = gameStats,
                            recentActivity = recentActivity,
                            bestPerformance = bestPerformance
                        )
                    }
                }
            }

    override fun observePackPracticeStats(packId: String): Flow<PackPracticeStats> =
        currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(null)
                else attemptDao.observePackPracticeStats(userId, packId)
            }
            .map { stats ->
                PackPracticeStats(
                    sessionsCount = stats?.sessionsCount?.takeIf { it > 0 },
                    accuracyPercent = stats?.accuracyPercent,
                )
            }

    override fun observeActiveStudyProgress(): Flow<List<PackStudyProgress>> =
        currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(emptyList())
                else attemptDao.observeActivePackProgress(userId, AttemptMode.STUDY)
            }
            .map { progress ->
                progress.map { PackStudyProgress(packId = it.packId, answeredCount = it.answeredCount) }
            }

    override suspend fun refresh(packId: String) {
        val userId = currentUserRepository.getCurrentUserId() ?: return
        val aggregate = attemptDao.getPackAggregateStats(userId, packId)
        val dominatedQuestions = questionStatsRepository.countMasteredByPack(packId)
        val totalQuestions = packQuestionDao.countQuestionsInPack(packId)
        val now = System.currentTimeMillis()
        val averageAccuracyPercent = aggregate?.totalQuestions
            ?.takeIf { it > 0 }
            ?.let { ((aggregate.totalCorrectAnswers * 100f) / it).toInt() }
        val averageStudyAccuracyPercent = aggregate?.totalStudyQuestions
            ?.takeIf { it > 0 }
            ?.let { ((aggregate.totalStudyCorrectAnswers * 100f) / it).toInt() }
        val averageGameAccuracyPercent = aggregate?.totalGameQuestions
            ?.takeIf { it > 0 }
            ?.let { ((aggregate.totalGameCorrectAnswers * 100f) / it).toInt() }
        val mostUsedMode = aggregate?.let {
            when {
                it.totalStudySessions == 0 && it.totalGameSessions == 0 -> null
                it.totalStudySessions >= it.totalGameSessions -> AttemptMode.STUDY
                else -> AttemptMode.GAME
            }
        }
        val progressPercent = if (totalQuestions > 0) {
            ((dominatedQuestions * 100f) / totalQuestions).toInt()
        } else {
            0
        }
        val current = packStatsDao.getByPackId(userId, packId)
        val stats = PackStats(
            id = current?.id ?: "$userId:$packId",
            userId = userId,
            packId = packId,
            totalSessions = aggregate?.totalSessions ?: 0,
            totalStudySessions = aggregate?.totalStudySessions ?: 0,
            totalGameSessions = aggregate?.totalGameSessions ?: 0,
            averageAccuracyPercent = averageAccuracyPercent,
            averageStudyAccuracyPercent = averageStudyAccuracyPercent,
            averageGameAccuracyPercent = averageGameAccuracyPercent,
            averageDurationMs = aggregate?.averageDurationMs,
            averageStudyDurationMs = aggregate?.averageStudyDurationMs,
            averageGameDurationMs = aggregate?.averageGameDurationMs,
            bestScore = aggregate?.bestGameScore ?: aggregate?.bestStudyAccuracyPercent,
            bestStudyAccuracyPercent = aggregate?.bestStudyAccuracyPercent,
            bestGameScore = aggregate?.bestGameScore,
            lastSessionAt = aggregate?.lastSessionAt,
            lastSessionMode = null,
            mostUsedMode = mostUsedMode,
            dominatedQuestions = dominatedQuestions,
            totalQuestionsSnapshot = totalQuestions,
            progressPercent = progressPercent,
            createdAt = current?.audit?.createdAt ?: now,
            updatedAt = now
        )
        packStatsDao.upsert(PackStatsMapper.toEntity(stats))
    }
}
