package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.FolderEntity
import com.uquiz.android.data.local.relations.FolderWithChildren
import com.uquiz.android.data.local.relations.FolderWithPacks
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    /**
     * Observe all root folders (parentId is null)
     * Ordered by name ASC
     */
    @Query("""
        SELECT * FROM folders
        WHERE parentId IS NULL
        ORDER BY name ASC
    """)
    fun observeRootFolders(): Flow<List<FolderEntity>>

    /**
     * Observe child folders of a parent
     * Ordered by name ASC
     */
    @Query("""
        SELECT * FROM folders
        WHERE parentId = :parentId
        ORDER BY name ASC
    """)
    fun observeChildFolders(parentId: String): Flow<List<FolderEntity>>

    /**
     * Get folder by ID
     */
    @Query("SELECT * FROM folders WHERE id = :id")
    suspend fun getById(id: String): FolderEntity?

    /**
     * Observe folder by ID
     */
    @Query("SELECT * FROM folders WHERE id = :id")
    fun observeById(id: String): Flow<FolderEntity?>

    /**
     * Insert or update folder
     */
    @Upsert
    suspend fun upsert(folder: FolderEntity)

    /**
     * Delete folder (will cascade to children and packs)
     */
    @Delete
    suspend fun delete(folder: FolderEntity)

    /**
     * Get all folders (for hierarchy building)
     */
    @Query("SELECT * FROM folders ORDER BY name ASC")
    suspend fun getAll(): List<FolderEntity>

    /**
     * Get folder with its packs
     */
    @Transaction
    @Query("SELECT * FROM folders WHERE id = :folderId")
    suspend fun getFolderWithPacks(folderId: String): FolderWithPacks?

    /**
     * Get folder with its child folders
     */
    @Transaction
    @Query("SELECT * FROM folders WHERE id = :folderId")
    suspend fun getFolderWithChildren(folderId: String): FolderWithChildren?

    /**
     * Get all root folders with their packs
     */
    @Transaction
    @Query("SELECT * FROM folders WHERE parentId IS NULL ORDER BY name ASC")
    fun observeRootFoldersWithPacks(): Flow<List<FolderWithPacks>>
}
