package com.uquiz.android.data.stats.repository

import com.uquiz.android.data.attempts.dao.AttemptAnswerDao
import com.uquiz.android.data.attempts.dao.AttemptDao
import com.uquiz.android.data.attempts.entity.AttemptEntity
import com.uquiz.android.data.attempts.query.UserDifficultyStatsRow
import com.uquiz.android.data.attempts.query.UserPackStatsQueryRow
import com.uquiz.android.data.attempts.query.UserQuestionInsightRow
import com.uquiz.android.data.stats.dao.PackStatsDao
import com.uquiz.android.data.stats.dao.QuestionStatsDao
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.enums.AttemptStatus
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.domain.stats.enums.UserStatsModeFilter
import com.uquiz.android.domain.stats.enums.UserStatsPeriodFilter
import com.uquiz.android.domain.stats.projection.UserAccuracyTrendPoint
import com.uquiz.android.domain.stats.projection.UserAnswerSplit
import com.uquiz.android.domain.stats.projection.UserDifficultyStats
import com.uquiz.android.domain.stats.projection.UserModeStats
import com.uquiz.android.domain.stats.projection.UserPackStatsRow
import com.uquiz.android.domain.stats.projection.UserQuestionInsight
import com.uquiz.android.domain.stats.projection.UserStatsDashboard
import com.uquiz.android.domain.stats.projection.UserStatsSnapshot
import com.uquiz.android.domain.stats.projection.UserStatsSummary
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import java.util.TimeZone
import kotlin.math.roundToInt

/**
 * Implementación de [UserStatsRepository]. Agrega datos de múltiples DAOs
 * ([AttemptDao], [AttemptAnswerDao], [PackStatsDao], [QuestionStatsDao], [UserRankRepository])
 * para construir los modelos de dashboard y snapshot de estadísticas del usuario.
 * Cuando no hay usuario activo, emite valores por defecto vacíos sin producir errores.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserStatsRepositoryImpl(
    private val attemptDao: AttemptDao,
    private val attemptAnswerDao: AttemptAnswerDao,
    private val packStatsDao: PackStatsDao,
    private val questionStatsDao: QuestionStatsDao,
    private val userRankRepository: UserRankRepository,
    private val currentUserRepository: CurrentUserRepository,
) : UserStatsRepository {
    override fun observeDashboard(
        modeFilter: UserStatsModeFilter,
        periodFilter: UserStatsPeriodFilter,
    ): Flow<UserStatsDashboard> =
        currentUserRepository
            .observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) {
                    flowOf(UserStatsDashboard())
                } else {
                    attemptDao.observeAll(userId).mapLatest { attempts ->
                        buildDashboard(
                            userId = userId,
                            attempts = attempts,
                            modeFilter = modeFilter,
                            periodFilter = periodFilter,
                        )
                    }
                }
            }

    override fun observeSnapshot(): Flow<UserStatsSnapshot> =
        currentUserRepository
            .observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) {
                    flowOf(UserStatsSnapshot())
                } else {
                    combine(
                        attemptDao.observeAll(userId),
                        userRankRepository.observeCurrent(),
                    ) { entities, rankState ->
                        val completed = entities.filter { it.status == AttemptStatus.COMPLETED }
                        val totalAnswered = completed.sumOf { it.totalQuestions }
                        val totalCorrect = completed.sumOf { it.correctAnswers }
                        UserStatsSnapshot(
                            dayStreak = calculateDayStreak(completed),
                            // totalPoints viene del XP acumulado en el ranking, no de la suma de scores.
                            totalPoints = rankState.totalXp,
                            completedSessions = completed.size,
                            accuracyPercent = accuracyPercent(totalCorrect, totalAnswered),
                            totalCorrect = totalCorrect,
                            totalAnswered = totalAnswered,
                        )
                    }
                }
            }

    private suspend fun buildDashboard(
        userId: String,
        attempts: List<AttemptEntity>,
        modeFilter: UserStatsModeFilter,
        periodFilter: UserStatsPeriodFilter,
    ): UserStatsDashboard =
        coroutineScope {
            val startedAfter = periodFilter.toStartedAfter()
            val selectedMode = modeFilter.toAttemptMode()
            val periodCompleted = attempts.completedAfter(startedAfter)
            val filteredCompleted = periodCompleted.filterByMode(selectedMode)
            val gameAttempts = periodCompleted.filterByMode(AttemptMode.GAME)
            val answeredQuestions = filteredCompleted.sumOf { it.totalQuestions }
            val correctAnswers = filteredCompleted.sumOf { it.correctAnswers }

            // Las 7 queries de base de datos son independientes entre sí: se lanzan en paralelo.
            val packCompletionDeferred = async { packStatsDao.getUserPackCompletion(userId) }
            val masteryDeferred = async { questionStatsDao.getUserMasterySummary(userId) }
            val avgAnswerTimeDeferred =
                async {
                    attemptAnswerDao.getAverageAnswerTime(
                        userId = userId,
                        mode = selectedMode,
                        startedAfter = startedAfter,
                    )
                }
            val packRowsDeferred =
                async {
                    attemptDao.getUserPackStatsRows(
                        userId = userId,
                        mode = selectedMode,
                        startedAfter = startedAfter,
                    )
                }
            val difficultyDeferred =
                async {
                    attemptAnswerDao.getDifficultyStats(
                        userId = userId,
                        mode = selectedMode,
                        startedAfter = startedAfter,
                    )
                }
            val fastestDeferred =
                async {
                    attemptAnswerDao.getFastestQuestion(
                        userId = userId,
                        mode = selectedMode,
                        startedAfter = startedAfter,
                    )
                }
            val mostFailedDeferred =
                async {
                    attemptAnswerDao.getMostFailedQuestion(
                        userId = userId,
                        mode = selectedMode,
                        startedAfter = startedAfter,
                    )
                }

            val packCompletion = packCompletionDeferred.await()
            val mastery = masteryDeferred.await()

            UserStatsDashboard(
                summary =
                    UserStatsSummary(
                        totalSessions = filteredCompleted.size,
                        answeredQuestions = answeredQuestions,
                        accuracyPercent = accuracyPercent(correctAnswers, answeredQuestions),
                        totalStudyTimeMs = filteredCompleted.sumOf { it.durationMs ?: 0L },
                        averageAnswerTimeMs = avgAnswerTimeDeferred.await(),
                        completedPacks = packCompletion.completedPacks,
                        inProgressPacks = packCompletion.inProgressPacks,
                    ),
                modeStats =
                    UserModeStats(
                        studyAccuracyPercent = periodCompleted.filterByMode(AttemptMode.STUDY).accuracyPercent(),
                        gameAccuracyPercent = gameAttempts.accuracyPercent(),
                        bestGameScore = gameAttempts.maxOfOrNull { it.score },
                        averageGameScore = gameAttempts.map { it.score }.averageOrNull()?.roundToInt(),
                        masteredQuestionPercent = accuracyPercent(mastery.masteredQuestions, mastery.trackedQuestions),
                    ),
                answerSplit =
                    UserAnswerSplit(
                        correctAnswers = correctAnswers,
                        incorrectAnswers = (answeredQuestions - correctAnswers).coerceAtLeast(0),
                    ),
                accuracyTrend = filteredCompleted.toAccuracyTrend(),
                packRows = packRowsDeferred.await().map(UserPackStatsQueryRow::toDomain),
                difficultyStats = difficultyDeferred.await().map(UserDifficultyStatsRow::toDomain),
                fastestQuestion = fastestDeferred.await()?.toDomainValue { value -> value.toReadableDurationLabel() },
                mostFailedQuestion = mostFailedDeferred.await()?.toDomainValue { value -> "${value}x" },
            )
        }

    private fun calculateDayStreak(completed: List<AttemptEntity>): Int {
        if (completed.isEmpty()) return 0
        val uniqueDays =
            completed
                .mapNotNull { it.completedAt }
                .map { timestamp ->
                    val offset = TimeZone.getDefault().getOffset(timestamp).toLong()
                    (timestamp + offset) / MILLIS_PER_DAY
                }.distinct()
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

private fun UserStatsModeFilter.toAttemptMode(): AttemptMode? =
    when (this) {
        UserStatsModeFilter.ALL -> null
        UserStatsModeFilter.STUDY -> AttemptMode.STUDY
        UserStatsModeFilter.GAME -> AttemptMode.GAME
    }

private fun UserStatsPeriodFilter.toStartedAfter(): Long? {
    val now = System.currentTimeMillis()
    return when (this) {
        UserStatsPeriodFilter.ALL -> null
        UserStatsPeriodFilter.LAST_7_DAYS -> now - (7L * USER_STATS_MILLIS_PER_DAY)
        UserStatsPeriodFilter.LAST_30_DAYS -> now - (30L * USER_STATS_MILLIS_PER_DAY)
    }
}

private fun List<AttemptEntity>.completedAfter(startedAfter: Long?): List<AttemptEntity> =
    filter { attempt ->
        val completedAt = attempt.completedAt
        attempt.status == AttemptStatus.COMPLETED &&
            completedAt != null &&
            (startedAfter == null || completedAt >= startedAfter)
    }

private fun List<AttemptEntity>.filterByMode(mode: AttemptMode?): List<AttemptEntity> =
    if (mode == null) this else filter { it.mode == mode }

private fun List<AttemptEntity>.accuracyPercent(): Int? =
    accuracyPercent(
        correct = sumOf { it.correctAnswers },
        total = sumOf { it.totalQuestions },
    )

private fun List<AttemptEntity>.toAccuracyTrend(): List<UserAccuracyTrendPoint> =
    sortedByDescending { it.completedAt }
        .filter { it.totalQuestions > 0 && it.completedAt != null }
        .take(USER_STATS_TREND_LIMIT)
        .sortedBy { it.completedAt }
        .mapNotNull { attempt ->
            val timestamp = attempt.completedAt ?: return@mapNotNull null
            UserAccuracyTrendPoint(
                timestamp = timestamp,
                accuracyPercent = accuracyPercent(attempt.correctAnswers, attempt.totalQuestions) ?: 0,
            )
        }

private fun UserPackStatsQueryRow.toDomain(): UserPackStatsRow =
    UserPackStatsRow(
        packId = packId,
        title = title,
        sessions = sessions,
        accuracyPercent = accuracyPercent,
        averageDurationMs = averageDurationMs,
        progressPercent = progressPercent,
    )

private fun UserDifficultyStatsRow.toDomain(): UserDifficultyStats =
    UserDifficultyStats(
        difficulty = difficulty,
        answeredCount = answeredCount,
        accuracyPercent = accuracyPercent(correctAnswers, answeredCount),
        averageTimeMs = averageTimeMs,
    )

private fun UserQuestionInsightRow.toDomainValue(formatValue: (Long) -> String): UserQuestionInsight =
    UserQuestionInsight(
        questionId = questionId,
        questionText = questionText,
        valueLabel = formatValue(value),
    )

private fun accuracyPercent(
    correct: Int,
    total: Int,
): Int? = if (total > 0) ((correct.toFloat() / total.toFloat()) * 100f).roundToInt() else null

private fun Iterable<Int>.averageOrNull(): Double? = if (none()) null else average()

private fun Long.toReadableDurationLabel(): String {
    val totalSeconds = (this / 1000L).coerceAtLeast(0L)
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return if (minutes > 0) "${minutes}m ${seconds}s" else "${seconds}s"
}

private const val USER_STATS_MILLIS_PER_DAY = 24L * 60L * 60L * 1000L
private const val USER_STATS_TREND_LIMIT = 10
