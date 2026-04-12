package com.uquiz.android.data.user.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.user.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * ### UserProfileDao
 *
 * Acceso reactivo y puntual a la tabla `user_profiles`.
 */
@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profiles WHERE id = :id")
    suspend fun getById(id: String): UserProfileEntity?

    @Query("SELECT * FROM user_profiles WHERE id = :id")
    fun observeById(id: String): Flow<UserProfileEntity?>

    /** Devuelve cualquier perfil almacenado. Útil para recuperar el usuario activo en apps de un solo usuario. */
    @Query("SELECT * FROM user_profiles LIMIT 1")
    suspend fun getAny(): UserProfileEntity?

    @Upsert
    suspend fun upsert(profile: UserProfileEntity)
}
