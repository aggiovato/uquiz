package com.uquiz.android.data.attempts.repository

import com.uquiz.android.data.attempts.dao.AttemptAnswerDao
import com.uquiz.android.data.attempts.dao.AttemptDao
import com.uquiz.android.data.attempts.dao.AttemptPackDao
import com.uquiz.android.data.attempts.dao.AttemptQuestionPlanDao
import com.uquiz.android.data.attempts.entity.AttemptPackEntity
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.enums.AttemptStatus
import com.uquiz.android.data.attempts.mapper.AttemptAnswerMapper
import com.uquiz.android.data.attempts.mapper.AttemptMapper
import com.uquiz.android.data.attempts.mapper.AttemptQuestionPlanMapper
import com.uquiz.android.domain.attempts.projection.ActivePackProgress
import com.uquiz.android.domain.attempts.model.Attempt
import com.uquiz.android.domain.attempts.model.AttemptAnswer
import com.uquiz.android.domain.attempts.model.AttemptQuestionPlan
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Implementation of AttemptRepository
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AttemptRepositoryImpl(
    private val attemptDao: AttemptDao,
    private val attemptAnswerDao: AttemptAnswerDao,
    private val attemptPackDao: AttemptPackDao,
    private val attemptQuestionPlanDao: AttemptQuestionPlanDao,
    private val currentUserRepository: CurrentUserRepository,
) : AttemptRepository {

    override fun observeAll(): Flow<List<Attempt>> {
        return currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(emptyList())
                else attemptDao.observeAll(userId)
            }
            .map { AttemptMapper.toModelList(it) }
    }

    override fun observeByMode(mode: AttemptMode): Flow<List<Attempt>> {
        return currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(emptyList())
                else attemptDao.observeByMode(userId, mode)
            }
            .map { AttemptMapper.toModelList(it) }
    }

    override suspend fun getById(id: String): Attempt? {
        return attemptDao.getById(id)?.let { AttemptMapper.toModel(it) }
    }

    override fun observeById(id: String): Flow<Attempt?> {
        return attemptDao.observeById(id)
            .map { it?.let { AttemptMapper.toModel(it) } }
    }

    override suspend fun getWithAnswers(attemptId: String): Pair<Attempt, List<AttemptAnswer>>? {
        val attemptWithAnswers = attemptDao.getAttemptWithAnswers(attemptId) ?: return null
        return Pair(
            AttemptMapper.toModel(attemptWithAnswers.attempt),
            AttemptAnswerMapper.toModelList(attemptWithAnswers.answers)
        )
    }

    override suspend fun getIncomplete(): List<Attempt> {
        val userId = requireCurrentUserId()
        return AttemptMapper.toModelList(attemptDao.getIncompleteAttempts(userId))
    }

    override suspend fun getActiveStudyAttempt(packId: String): Attempt? {
        return attemptDao.getActiveAttemptForPack(requireCurrentUserId(), packId)?.let { AttemptMapper.toModel(it) }
    }

    override suspend fun getActiveGameAttempt(packId: String): Attempt? {
        return attemptDao.getActiveGameAttemptForPack(requireCurrentUserId(), packId)?.let { AttemptMapper.toModel(it) }
    }

    override suspend fun getQuestionPlan(attemptId: String): List<AttemptQuestionPlan> {
        return AttemptQuestionPlanMapper.toModelList(attemptQuestionPlanDao.getByAttemptId(attemptId))
    }

    override suspend fun getAnswers(attemptId: String): List<AttemptAnswer> {
        return AttemptAnswerMapper.toModelList(attemptAnswerDao.getByAttemptId(attemptId))
    }

    override suspend fun getRecentCompleted(limit: Int): List<Attempt> {
        return AttemptMapper.toModelList(attemptDao.getRecentCompleted(requireCurrentUserId(), limit))
    }

    override suspend fun getAverageScore(mode: AttemptMode): Double? {
        return attemptDao.getAverageScore(requireCurrentUserId(), mode)
    }

    override suspend fun getBestScore(mode: AttemptMode): Int? {
        return attemptDao.getBestScore(requireCurrentUserId(), mode)
    }

    override fun observeActivePackProgress(mode: AttemptMode): Flow<List<ActivePackProgress>> {
        return currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(emptyList())
                else attemptDao.observeActivePackProgress(userId, mode)
            }
            .map { progress ->
                progress.map {
                    ActivePackProgress(
                        attemptId = it.attemptId,
                        packId = it.packId,
                        mode = it.mode,
                        answeredCount = it.answeredCount,
                        startedAt = it.startedAt
                    )
                }
            }
    }

    override suspend fun createAttempt(
        mode: AttemptMode,
        primaryPackId: String?,
        packIds: List<String>,
        totalQuestions: Int
    ): Attempt {
        val now = System.currentTimeMillis()
        val attemptId = UUID.randomUUID().toString()

        val attempt = Attempt(
            id = attemptId,
            userId = requireCurrentUserId(),
            mode = mode,
            status = AttemptStatus.IN_PROGRESS,
            startedAt = now,
            completedAt = null,
            durationMs = null,
            score = 0,
            primaryPackId = primaryPackId,
            totalQuestions = totalQuestions,
            correctAnswers = 0,
            currentQuestionIndex = 0,
            createdAt = now,
            updatedAt = now
        )
        attemptDao.upsert(AttemptMapper.toEntity(attempt))

        // Link packs to attempt (for Game mode with multiple packs)
        if (packIds.isNotEmpty()) {
            val attemptPacks = packIds.map { packId ->
                AttemptPackEntity(attemptId = attemptId, packId = packId)
            }
            attemptPackDao.upsertAll(attemptPacks)
        }

        return attempt
    }

    override suspend fun startOrResumeStudyAttempt(
        packId: String,
        totalQuestions: Int
    ): Attempt {
        return getActiveStudyAttempt(packId) ?: createAttempt(
            mode = AttemptMode.STUDY,
            primaryPackId = packId,
            totalQuestions = totalQuestions
        )
    }

    override suspend fun updateAttempt(attempt: Attempt) {
        val updated = attempt.copy(updatedAt = System.currentTimeMillis())
        attemptDao.upsert(AttemptMapper.toEntity(updated))
    }

    override suspend fun updateCurrentQuestionIndex(attemptId: String, index: Int) {
        val attempt = attemptDao.getById(attemptId) ?: return
        attemptDao.upsert(
            attempt.copy(
                currentQuestionIndex = index,
                audit = attempt.audit.copy(updatedAt = System.currentTimeMillis())
            )
        )
    }

    override suspend fun completeAttempt(
        attemptId: String,
        score: Int,
        correctAnswers: Int,
        totalQuestions: Int,
        durationMs: Long?
    ) {
        val attempt = attemptDao.getById(attemptId) ?: return
        val now = System.currentTimeMillis()
        val duration = durationMs ?: (now - attempt.startedAt)

        val completed = attempt.copy(
            status = AttemptStatus.COMPLETED,
            completedAt = now,
            durationMs = duration,
            score = score,
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            audit = attempt.audit.copy(updatedAt = now)
        )
        attemptDao.upsert(completed)
    }

    override suspend fun abandonAttempt(attemptId: String) {
        val attempt = attemptDao.getById(attemptId) ?: return
        val now = System.currentTimeMillis()
        attemptDao.upsert(
            attempt.copy(
                status = AttemptStatus.ABANDONED,
                completedAt = now,
                audit = attempt.audit.copy(updatedAt = now)
            )
        )
    }

    override suspend fun recordAnswer(
        attemptId: String,
        questionId: String,
        pickedOptionId: String?,
        isCorrect: Boolean,
        timeMs: Long,
        timeLimitMs: Long?
    ): AttemptAnswer {
        attemptAnswerDao.getAnswer(attemptId, questionId)?.let {
            return AttemptAnswerMapper.toModel(it)
        }

        val now = System.currentTimeMillis()

        val answer = AttemptAnswer(
            id = UUID.randomUUID().toString(),
            attemptId = attemptId,
            questionId = questionId,
            pickedOptionId = pickedOptionId,
            isCorrect = isCorrect,
            timeMs = timeMs,
            timeLimitMs = timeLimitMs,
            createdAt = now,
            updatedAt = now
        )
        attemptAnswerDao.upsert(AttemptAnswerMapper.toEntity(answer))
        return answer
    }

    override suspend fun saveQuestionPlan(plan: List<AttemptQuestionPlan>) {
        attemptQuestionPlanDao.upsertAll(AttemptQuestionPlanMapper.toEntityList(plan))
    }

    override suspend fun deleteQuestionPlan(attemptId: String) {
        attemptQuestionPlanDao.deleteByAttemptId(attemptId)
    }

    override suspend fun deleteAttempt(attemptId: String) {
        attemptDao.deleteById(attemptId)
    }

    override suspend fun deleteOlderThan(beforeTimestamp: Long) {
        attemptDao.deleteOlderThan(beforeTimestamp)
    }

    private suspend fun requireCurrentUserId(): String {
        return currentUserRepository.getCurrentUserId()
            ?: error("No current user available.")
    }
}
