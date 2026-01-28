package com.uquiz.android.data.repository

import com.uquiz.android.data.local.dao.FolderDao
import com.uquiz.android.data.local.dao.PackDao
import com.uquiz.android.data.local.mapper.FolderMapper
import com.uquiz.android.data.local.mapper.PackMapper
import com.uquiz.android.domain.model.Folder
import com.uquiz.android.domain.model.Pack
import com.uquiz.android.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Implementation of FolderRepository
 */
class FolderRepositoryImpl(
    private val folderDao: FolderDao,
    private val packDao: PackDao
) : FolderRepository {

    override fun observeRootFolders(): Flow<List<Folder>> {
        return folderDao.observeRootFolders()
            .map { FolderMapper.toModelList(it) }
    }

    override fun observeChildFolders(parentId: String): Flow<List<Folder>> {
        return folderDao.observeChildFolders(parentId)
            .map { FolderMapper.toModelList(it) }
    }

    override suspend fun getById(id: String): Folder? {
        return folderDao.getById(id)?.let { FolderMapper.toModel(it) }
    }

    override fun observeById(id: String): Flow<Folder?> {
        return folderDao.observeById(id)
            .map { it?.let { FolderMapper.toModel(it) } }
    }

    override suspend fun getAll(): List<Folder> {
        return FolderMapper.toModelList(folderDao.getAll())
    }

    override suspend fun getPacksInFolder(folderId: String): List<Pack> {
        return PackMapper.toModelList(packDao.getByFolderId(folderId))
    }

    override suspend fun createFolder(name: String, parentId: String?): Folder {
        val now = System.currentTimeMillis()
        val folder = Folder(
            id = UUID.randomUUID().toString(),
            name = name,
            parentId = parentId,
            createdAt = now,
            updatedAt = now
        )
        folderDao.upsert(FolderMapper.toEntity(folder))
        return folder
    }

    override suspend fun updateFolder(folder: Folder) {
        val updated = folder.copy(updatedAt = System.currentTimeMillis())
        folderDao.upsert(FolderMapper.toEntity(updated))
    }

    override suspend fun deleteFolder(folder: Folder) {
        folderDao.delete(FolderMapper.toEntity(folder))
    }
}
