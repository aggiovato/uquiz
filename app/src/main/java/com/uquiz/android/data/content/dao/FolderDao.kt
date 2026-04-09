package com.uquiz.android.data.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.content.entity.FolderEntity
import com.uquiz.android.data.content.entity.FolderWithCountsEntity
import com.uquiz.android.data.content.relations.FolderWithChildren
import com.uquiz.android.data.content.relations.FolderWithPacks
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    /**
     * Observe folders by parent. parentId = null → root folders.
     * Ordered by name ASC.
     */
    @Query(
        """
        SELECT * FROM folders
        WHERE (:parentId IS NULL AND parentId IS NULL) OR parentId = :parentId
        ORDER BY name ASC
    """,
    )
    fun observeByParent(parentId: String?): Flow<List<FolderEntity>>

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
     * Delete folder by ID (cascades to children and packs)
     */
    @Query("DELETE FROM folders WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Get all folders (for hierarchy building)
     */
    @Query("SELECT * FROM folders ORDER BY name ASC")
    suspend fun getAll(): List<FolderEntity>

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM folders
            WHERE ((:parentId IS NULL AND parentId IS NULL) OR parentId = :parentId)
              AND LOWER(name) = LOWER(:name)
              AND (:excludeId IS NULL OR id != :excludeId)
        )
    """,
    )
    suspend fun existsSiblingWithName(
        parentId: String?,
        name: String,
        excludeId: String? = null,
    ): Boolean

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

    @Query(
        """
        SELECT f.*,
            (SELECT COUNT(*) FROM folders c WHERE c.parentId = f.id) AS childFolderCount,
            (SELECT COUNT(*) FROM packs p WHERE p.folderId = f.id) AS packCount
        FROM folders f
        WHERE (:parentId IS NULL AND f.parentId IS NULL) OR f.parentId = :parentId
        ORDER BY f.name ASC
    """,
    )
    fun observeByParentWithCounts(parentId: String?): Flow<List<FolderWithCountsEntity>>
}
