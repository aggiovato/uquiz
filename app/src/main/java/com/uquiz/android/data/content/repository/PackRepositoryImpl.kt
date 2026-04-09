package com.uquiz.android.data.content.repository

import com.uquiz.android.data.content.dao.FolderDao
import com.uquiz.android.data.content.dao.PackDao
import com.uquiz.android.data.content.dao.PackQuestionDao
import com.uquiz.android.data.content.entity.PackQuestionEntity
import com.uquiz.android.data.content.mapper.FolderMapper
import com.uquiz.android.data.content.mapper.PackMapper
import com.uquiz.android.data.content.mapper.QuestionWithOptionsMapper
import com.uquiz.android.domain.content.enums.AllowedUColor
import com.uquiz.android.domain.content.enums.AllowedUIcon
import com.uquiz.android.domain.content.error.CONTENT_NAME_MAX_LENGTH
import com.uquiz.android.domain.content.error.PACK_DESCRIPTION_MAX_LENGTH
import com.uquiz.android.domain.content.error.DuplicatePackTitleException
import com.uquiz.android.domain.content.error.PackDescriptionTooLongException
import com.uquiz.android.domain.content.error.PackTitleTooLongException
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.model.QuestionWithOptions
import com.uquiz.android.domain.content.projection.PackWithQuestionCount
import com.uquiz.android.domain.content.repository.PackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class PackRepositoryImpl(
    private val packDao: PackDao,
    private val folderDao: FolderDao,
    private val packQuestionDao: PackQuestionDao
) : PackRepository {

    override fun observeAll(): Flow<List<Pack>> =
        packDao.observeAll().map { PackMapper.toModelList(it) }

    override fun observeAllWithQuestionCounts(): Flow<List<PackWithQuestionCount>> =
        packDao.observeAllWithQuestionCounts().map { list ->
            list.map(PackMapper::toModelWithQuestionCount)
        }

    override fun observeById(packId: String): Flow<Pack?> =
        packDao.observePack(packId).map { it?.let(PackMapper::toModel) }

    override fun observeByFolder(folderId: String): Flow<List<Pack>> =
        packDao.observeByFolderId(folderId).map { PackMapper.toModelList(it) }

    override fun observeByFolderWithQuestionCounts(folderId: String): Flow<List<PackWithQuestionCount>> =
        packDao.observeByFolderIdWithQuestionCounts(folderId)
            .map { list -> list.map(PackMapper::toModelWithQuestionCount) }

    override fun observeWithQuestions(packId: String): Flow<List<QuestionWithOptions>> =
        packDao.observeOrderedQuestionWithOptions(packId)
            .map { QuestionWithOptionsMapper.toOrderedModelList(it) }

    override suspend fun getById(packId: String): Pack? =
        packDao.getById(packId)?.let(PackMapper::toModel)

    override suspend fun getByFolder(folderId: String): List<Pack> =
        PackMapper.toModelList(packDao.getByFolderId(folderId))

    override suspend fun getPackPath(packId: String): Pair<Pack, List<Folder>>? {
        val pack = getById(packId) ?: return null
        val foldersById = FolderMapper.toModelList(folderDao.getAll()).associateBy { it.id }
        val folderPath = mutableListOf<Folder>()
        var current = foldersById[pack.folderId]
        while (current != null) {
            folderPath.add(current)
            current = current.parentId?.let(foldersById::get)
        }
        return pack to folderPath.asReversed()
    }

    override suspend fun getWithQuestions(packId: String): List<QuestionWithOptions> {
        val packWithQuestions = packDao.getPackWithQuestionsAndOptions(packId)
        return packWithQuestions?.questionsWithOptions
            ?.let { QuestionWithOptionsMapper.toModelList(it) }
            ?: emptyList()
    }

    override suspend fun getQuestionCount(packId: String): Int =
        packQuestionDao.countQuestionsInPack(packId)

    override suspend fun createPack(
        title: String,
        description: String?,
        folderId: String,
        icon: String?,
        colorHex: String?
    ): Pack {
        val normalizedTitle = title.trim()
        if (normalizedTitle.length > CONTENT_NAME_MAX_LENGTH) throw PackTitleTooLongException()
        if ((description?.length ?: 0) > PACK_DESCRIPTION_MAX_LENGTH) throw PackDescriptionTooLongException()
        if (packDao.existsPackWithTitle(folderId = folderId, title = normalizedTitle)) {
            throw DuplicatePackTitleException(normalizedTitle)
        }
        val validatedColorHex = colorHex?.also { AllowedUColor.fromHexOrThrow(it) }
        val validatedIcon = icon?.also { AllowedUIcon.fromKeyOrThrow(it) }
        val now = System.currentTimeMillis()
        val pack = Pack(
            id = UUID.randomUUID().toString(),
            title = normalizedTitle,
            description = description,
            folderId = folderId,
            icon = validatedIcon,
            colorHex = validatedColorHex,
            createdAt = now,
            updatedAt = now
        )
        packDao.upsertPack(PackMapper.toEntity(pack))
        return pack
    }

    override suspend fun updatePack(pack: Pack) {
        val normalizedTitle = pack.title.trim()
        if (normalizedTitle.length > CONTENT_NAME_MAX_LENGTH) throw PackTitleTooLongException()
        if ((pack.description?.length ?: 0) > PACK_DESCRIPTION_MAX_LENGTH) throw PackDescriptionTooLongException()
        if (packDao.existsPackWithTitle(folderId = pack.folderId, title = normalizedTitle, excludeId = pack.id)) {
            throw DuplicatePackTitleException(normalizedTitle)
        }
        pack.colorHex?.let { AllowedUColor.fromHexOrThrow(it) }
        pack.icon?.let { AllowedUIcon.fromKeyOrThrow(it) }
        packDao.upsertPack(PackMapper.toEntity(pack.copy(title = normalizedTitle, updatedAt = System.currentTimeMillis())))
    }

    override suspend fun deletePack(packId: String) {
        packDao.deleteById(packId)
    }

    override suspend fun setPackQuestions(packId: String, orderedQuestionIds: List<String>) {
        packDao.clearPackQuestions(packId)
        val packQuestions = orderedQuestionIds.mapIndexed { index, questionId ->
            PackQuestionEntity(packId = packId, questionId = questionId, sortOrder = index)
        }
        packDao.upsertPackQuestions(packQuestions)
    }

    override suspend fun addQuestionToPack(packId: String, questionId: String, sortOrder: Int) {
        packQuestionDao.upsert(PackQuestionEntity(packId = packId, questionId = questionId, sortOrder = sortOrder))
    }

    override suspend fun removeQuestionFromPack(packId: String, questionId: String) {
        packQuestionDao.delete(packId, questionId)
    }

    override suspend fun existsPackTitleInFolder(
        folderId: String,
        title: String,
        excludeId: String?
    ): Boolean = packDao.existsPackWithTitle(folderId = folderId, title = title.trim(), excludeId = excludeId)
}
