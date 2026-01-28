package com.uquiz.android.data.repository

import com.uquiz.android.data.local.dao.AttemptAnswerDao
import com.uquiz.android.data.local.dao.AttemptDao
import com.uquiz.android.data.local.dao.AttemptPackDao
import com.uquiz.android.data.local.entity.AttemptPackEntity
import com.uquiz.android.data.local.enums.AttemptMode
import com.uquiz.android.data.local.mapper.AttemptAnswerMapper
import com.uquiz.android.data.local.mapper.AttemptMapper
import com.uquiz.android.domain.model.Attempt
import com.uquiz.android.domain.model.AttemptAnswer
import com.uquiz.android.domain.repository.AttemptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Implementation of AttemptRepository
 */
class AttemptRepositoryImpl(
    private val attemptDao: AttemptDao,
    private val attemptAnswerDao: AttemptAnswerDao,
    private val attemptPackDao: AttemptPackDao
) : AttemptRepository {

    override fun observeAllAttempts(): Flow<List<Attempt>> {
        return attemptDao.observeAll()
            .map { AttemptMapper.toModelList(it) }
    }

    override fun observeByMode(mode: AttemptMode): Flow<List<Attempt>> {
        return attemptDao.observeByMode(mode)
            .map { AttemptMapper.toModelList(it) }
    }

    override suspend fun getById(id: String): Attempt? {
        return attemptDao.getById(id)?.let { AttemptMapper.toModel(it) }
    }

    override fun observeById(id: String): Flow<Attempt?> {
        return attemptDao.observeById(id)
            .map { it?.let { AttemptMapper.toModel(it) } }
    }

    override suspend fun getAttemptWithAnswers(attemptId: String): Pair<Attempt, List<AttemptAnswer>>? {
        val attemptWithAnswers = attemptDao.getAttemptWithAnswers(attemptId) ?: return null
        return Pair(
            AttemptMapper.toModel(attemptWithAnswers.attempt),
            AttemptAnswerMapper.toModelList(attemptWithAnswers.answers)
        )
    }

    override suspend fun getIncompleteAttempts(): List<Attempt> {
        return AttemptMapper.toModelList(attemptDao.getIncompleteAttempts())
    }

    override suspend fun getRecentCompleted(limit: Int): List<Attempt> {
        return AttemptMapper.toModelList(attemptDao.getRecentCompleted(limit))
    }

    override suspend fun getAverageScore(mode: AttemptMode): Double? {
        return attemptDao.getAverageScore(mode)
    }

    override suspend fun getBestScore(mode: AttemptMode): Int? {
        return attemptDao.getBestScore(mode)
    }

    override suspend fun createAttempt(
        mode: AttemptMode,
        primaryPackId: String?,
        packIds: List<String>
    ): Attempt {
        val now = System.currentTimeMillis()
        val attemptId = UUID.randomUUID().toString()

        val attempt = Attempt(
            id = attemptId,
            mode = mode,
            startedAt = now,
            completedAt = null,
            durationMs = null,
            score = 0,
            primaryPackId = primaryPackId,
            totalQuestions = 0,
            correctAnswers = 0,
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

    override suspend fun updateAttempt(attempt: Attempt) {
        val updated = attempt.copy(updatedAt = System.currentTimeMillis())
        attemptDao.upsert(AttemptMapper.toEntity(updated))
    }

    override suspend fun completeAttempt(
        attemptId: String,
        score: Int,
        correctAnswers: Int,
        totalQuestions: Int
    ) {
        val attempt = attemptDao.getById(attemptId) ?: return
        val now = System.currentTimeMillis()
        val duration = now - attempt.startedAt

        val completed = attempt.copy(
            completedAt = now,
            durationMs = duration,
            score = score,
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            audit = attempt.audit.copy(updatedAt = now)
        )
        attemptDao.upsert(completed)
    }

    override suspend fun recordAnswer(
        attemptId: String,
        questionId: String,
        pickedOptionId: String?,
        isCorrect: Boolean,
        timeMs: Long,
        timeLimitMs: Long?
    ): AttemptAnswer {
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

    override suspend fun deleteAttempt(attempt: Attempt) {
        attemptDao.delete(AttemptMapper.toEntity(attempt))
    }

    override suspend fun deleteOlderThan(beforeTimestamp: Long) {
        attemptDao.deleteOlderThan(beforeTimestamp)
    }
}
