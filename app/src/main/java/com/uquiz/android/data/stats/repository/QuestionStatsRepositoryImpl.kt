package com.uquiz.android.data.stats.repository

import com.uquiz.android.data.stats.dao.QuestionStatsDao
import com.uquiz.android.data.stats.mapper.QuestionStatsMapper
import com.uquiz.android.domain.stats.model.QuestionStats
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import com.uquiz.android.domain.stats.repository.QuestionStatsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionStatsRepositoryImpl(
    private val questionStatsDao: QuestionStatsDao,
    private val currentUserRepository: CurrentUserRepository
) : QuestionStatsRepository {

    override suspend fun getByQuestion(questionId: String): QuestionStats? {
        val userId = currentUserRepository.getCurrentUserId() ?: return null
        return questionStatsDao.getByQuestionId(userId, questionId)?.let(QuestionStatsMapper::toModel)
    }

    override fun observeByPack(packId: String): Flow<List<QuestionStats>> =
        currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(emptyList())
                else questionStatsDao.observeByPackId(userId, packId)
            }
            .map { list -> list.map(QuestionStatsMapper::toModel) }

    override fun observeMasteredCount(packId: String): Flow<Int> =
        currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(0)
                else questionStatsDao.observeMasterySummary(userId, packId).map { it.dominatedQuestions }
            }

    override suspend fun countMasteredByPack(packId: String): Int {
        val userId = currentUserRepository.getCurrentUserId() ?: return 0
        return questionStatsDao.countMasteredByPack(userId, packId)
    }

    override suspend fun upsert(stats: QuestionStats) {
        questionStatsDao.upsert(QuestionStatsMapper.toEntity(stats))
    }
}
