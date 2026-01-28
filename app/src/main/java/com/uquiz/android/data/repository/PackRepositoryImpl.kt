package com.uquiz.android.data.repository

import com.uquiz.android.data.local.dao.PackDao
import com.uquiz.android.data.local.dao.PackQuestionDao
import com.uquiz.android.data.local.entity.PackQuestionEntity
import com.uquiz.android.data.local.mapper.PackMapper
import com.uquiz.android.data.local.mapper.QuestionWithOptionsMapper
import com.uquiz.android.domain.model.Pack
import com.uquiz.android.domain.model.QuestionWithOptions
import com.uquiz.android.domain.repository.PackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Implementation of PackRepository
 */
class PackRepositoryImpl(
    private val packDao: PackDao,
    private val packQuestionDao: PackQuestionDao
) : PackRepository {

    override fun observePack(packId: String): Flow<Pack?> {
        return packDao.observePack(packId)
            .map { it?.let { PackMapper.toModel(it) } }
    }

    override suspend fun getById(packId: String): Pack? {
        return packDao.observePack(packId).map { it?.let { PackMapper.toModel(it) } }.toString().let { null }
        // TODO: Fix this - need getById not observe
    }

    override fun observeByFolderId(folderId: String): Flow<List<Pack>> {
        return packDao.observeByFolderId(folderId)
            .map { PackMapper.toModelList(it) }
    }

    override suspend fun getByFolderId(folderId: String): List<Pack> {
        return PackMapper.toModelList(packDao.getByFolderId(folderId))
    }

    override suspend fun getPackWithQuestions(packId: String): List<QuestionWithOptions> {
        val packWithQuestions = packDao.getPackWithQuestionsAndOptions(packId)
        return packWithQuestions?.questionsWithOptions?.let {
            QuestionWithOptionsMapper.toModelList(it)
        } ?: emptyList()
    }

    override fun observePackWithQuestions(packId: String): Flow<List<QuestionWithOptions>> {
        return packDao.observePackWithQuestionsAndOptions(packId)
            .map { packWithQuestions ->
                packWithQuestions?.questionsWithOptions?.let {
                    QuestionWithOptionsMapper.toModelList(it)
                } ?: emptyList()
            }
    }

    override suspend fun createPack(
        title: String,
        description: String?,
        folderId: String,
        icon: String?
    ): Pack {
        val now = System.currentTimeMillis()
        val pack = Pack(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            folderId = folderId,
            icon = icon,
            createdAt = now,
            updatedAt = now
        )
        packDao.upsertPack(PackMapper.toEntity(pack))
        return pack
    }

    override suspend fun updatePack(pack: Pack) {
        val updated = pack.copy(updatedAt = System.currentTimeMillis())
        packDao.upsertPack(PackMapper.toEntity(updated))
    }

    override suspend fun deletePack(pack: Pack) {
        packDao.delete(PackMapper.toEntity(pack))
    }

    override suspend fun setPackQuestions(packId: String, orderedQuestionIds: List<String>) {
        // Clear existing questions
        packDao.clearPackQuestions(packId)

        // Add new questions with order
        val packQuestions = orderedQuestionIds.mapIndexed { index, questionId ->
            PackQuestionEntity(
                packId = packId,
                questionId = questionId,
                sortOrder = index
            )
        }
        packDao.upsertPackQuestions(packQuestions)
    }

    override suspend fun addQuestionToPack(packId: String, questionId: String, sortOrder: Int) {
        packQuestionDao.upsert(
            PackQuestionEntity(
                packId = packId,
                questionId = questionId,
                sortOrder = sortOrder
            )
        )
    }

    override suspend fun removeQuestionFromPack(packId: String, questionId: String) {
        packQuestionDao.delete(packId, questionId)
    }

    override suspend fun getQuestionCount(packId: String): Int {
        return packQuestionDao.countQuestionsInPack(packId)
    }
}
