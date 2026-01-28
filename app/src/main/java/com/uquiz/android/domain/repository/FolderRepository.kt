package com.uquiz.android.domain.repository

import com.uquiz.android.domain.model.Folder
import com.uquiz.android.domain.model.Pack
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository interface for Folder operations
 */
interface FolderRepository {

    /**
     * Observe all root folders (no parent)
     */
    fun observeRootFolders(): Flow<List<Folder>>

    /**
     * Observe child folders of a parent
     */
    fun observeChildFolders(parentId: String): Flow<List<Folder>>

    /**
     * Get folder by ID
     */
    suspend fun getById(id: String): Folder?

    /**
     * Observe folder by ID
     */
    fun observeById(id: String): Flow<Folder?>

    /**
     * Get all folders (for hierarchy)
     */
    suspend fun getAll(): List<Folder>

    /**
     * Get packs in a folder
     */
    suspend fun getPacksInFolder(folderId: String): List<Pack>

    /**
     * Create new folder
     * @return Generated folder with ID
     */
    suspend fun createFolder(
        name: String,
        parentId: String? = null
    ): Folder

    /**
     * Update folder
     */
    suspend fun updateFolder(folder: Folder)

    /**
     * Delete folder (cascade to children and packs)
     */
    suspend fun deleteFolder(folder: Folder)
}
