package com.uquiz.android.data.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.uquiz.android.data.content.entity.FolderEntity
import com.uquiz.android.data.content.query.FolderWithCountsRow
import com.uquiz.android.data.content.relations.FolderWithPacks
import kotlinx.coroutines.flow.Flow

/**
 * ### FolderDao
 *
 * Acceso reactivo y puntual a la tabla `folders`.
 * Las queries de conteo producen rows tipados definidos en `query/`.
 */
@Dao
interface FolderDao {
    /** Observa las carpetas hijas del [parentId] dado, o las raíz si es `null`. Ordenadas por nombre. */
    @Query(
        """
        SELECT * FROM folders
        WHERE (:parentId IS NULL AND parentId IS NULL) OR parentId = :parentId
        ORDER BY name ASC
    """,
    )
    fun observeByParent(parentId: String?): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folders WHERE id = :id")
    suspend fun getById(id: String): FolderEntity?

    @Query("SELECT * FROM folders WHERE id = :id")
    fun observeById(id: String): Flow<FolderEntity?>

    @Upsert
    suspend fun upsert(folder: FolderEntity)

    /** Elimina la carpeta por ID en cascada (subcarpetas y packs incluidos). */
    @Query("DELETE FROM folders WHERE id = :id")
    suspend fun deleteById(id: String)

    /** Devuelve todas las carpetas sin filtro, ordenadas por nombre. Útil para construir la jerarquía completa. */
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

    /** Observa todas las carpetas raíz con sus packs. */
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
    fun observeByParentWithCounts(parentId: String?): Flow<List<FolderWithCountsRow>>
}
