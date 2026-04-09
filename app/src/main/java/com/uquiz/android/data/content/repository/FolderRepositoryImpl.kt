package com.uquiz.android.data.content.repository

import com.uquiz.android.data.content.dao.FolderDao
import com.uquiz.android.data.content.mapper.FolderMapper
import com.uquiz.android.domain.content.enums.AllowedUColor
import com.uquiz.android.domain.content.enums.AllowedUIcon
import com.uquiz.android.domain.content.error.CONTENT_NAME_MAX_LENGTH
import com.uquiz.android.domain.content.error.DuplicateFolderNameException
import com.uquiz.android.domain.content.error.FolderNameTooLongException
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.projection.FolderWithCounts
import com.uquiz.android.domain.content.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FolderRepositoryImpl(
    private val folderDao: FolderDao
) : FolderRepository {

    override fun observeByParent(parentId: String?): Flow<List<Folder>> =
        folderDao.observeByParent(parentId).map { FolderMapper.toModelList(it) }

    override fun observeByParentWithCounts(parentId: String?): Flow<List<FolderWithCounts>> =
        folderDao.observeByParentWithCounts(parentId).map { list -> list.map(FolderMapper::toModelWithCounts) }

    override fun observeById(id: String): Flow<Folder?> =
        folderDao.observeById(id).map { it?.let(FolderMapper::toModel) }

    override suspend fun getById(id: String): Folder? =
        folderDao.getById(id)?.let(FolderMapper::toModel)

    override suspend fun getAll(): List<Folder> =
        FolderMapper.toModelList(folderDao.getAll())

    override suspend fun getFolderPath(folderId: String): List<Folder> {
        val foldersById = getAll().associateBy { it.id }
        val path = mutableListOf<Folder>()
        var current = foldersById[folderId]
        while (current != null) {
            path.add(current)
            current = current.parentId?.let(foldersById::get)
        }
        return path.asReversed()
    }

    override suspend fun createFolder(
        name: String,
        parentId: String?,
        colorHex: String?,
        icon: String?
    ): Folder {
        val normalizedName = name.trim()
        if (normalizedName.length > CONTENT_NAME_MAX_LENGTH) throw FolderNameTooLongException()
        if (folderDao.existsSiblingWithName(parentId = parentId, name = normalizedName)) {
            throw DuplicateFolderNameException(normalizedName)
        }
        val validatedColorHex = colorHex?.also { AllowedUColor.fromHexOrThrow(it) }
        val validatedIcon = icon?.also { AllowedUIcon.fromKeyOrThrow(it) }
        val now = System.currentTimeMillis()
        val folder = Folder(
            id = UUID.randomUUID().toString(),
            name = normalizedName,
            parentId = parentId,
            colorHex = validatedColorHex,
            icon = validatedIcon,
            createdAt = now,
            updatedAt = now
        )
        folderDao.upsert(FolderMapper.toEntity(folder))
        return folder
    }

    override suspend fun updateFolder(folder: Folder) {
        val normalizedName = folder.name.trim()
        if (normalizedName.length > CONTENT_NAME_MAX_LENGTH) throw FolderNameTooLongException()
        if (folderDao.existsSiblingWithName(parentId = folder.parentId, name = normalizedName, excludeId = folder.id)) {
            throw DuplicateFolderNameException(normalizedName)
        }
        folder.colorHex?.let { AllowedUColor.fromHexOrThrow(it) }
        folder.icon?.let { AllowedUIcon.fromKeyOrThrow(it) }
        val updated = folder.copy(name = normalizedName, updatedAt = System.currentTimeMillis())
        folderDao.upsert(FolderMapper.toEntity(updated))
    }

    override suspend fun deleteFolder(folderId: String) {
        folderDao.deleteById(folderId)
    }

    override suspend fun moveFolder(folderId: String, newParentId: String) {
        val entity = folderDao.getById(folderId) ?: return
        folderDao.upsert(entity.copy(parentId = newParentId))
    }

    override suspend fun existsSiblingFolderName(
        parentId: String?,
        name: String,
        excludeId: String?
    ): Boolean = folderDao.existsSiblingWithName(parentId = parentId, name = name.trim(), excludeId = excludeId)
}
