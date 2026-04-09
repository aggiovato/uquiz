package com.uquiz.android.data.user.repository

import com.uquiz.android.core.user.LOCAL_DEFAULT_USER_ID
import com.uquiz.android.core.user.LOCAL_DEFAULT_USER_NAME
import com.uquiz.android.data.user.dao.UserProfileDao
import com.uquiz.android.data.user.mapper.UserProfileMapper
import com.uquiz.android.domain.user.model.UserProfile
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileRepositoryImpl(
    private val userProfileDao: UserProfileDao,
    private val currentUserRepository: CurrentUserRepository
) : UserProfileRepository {

    override suspend fun ensureInitialProfile(): UserProfile {
        val existing = userProfileDao.getAny()?.let(UserProfileMapper::toModel)
        val profile = existing ?: createDefaultProfile()
        currentUserRepository.setCurrentUserId(profile.id)
        return profile
    }

    override suspend fun getCurrent(): UserProfile {
        val userId = currentUserRepository.getCurrentUserId()
        val entity = if (userId != null) {
            userProfileDao.getById(userId)
        } else {
            null
        } ?: userProfileDao.getAny()
        return entity?.let(UserProfileMapper::toModel) ?: ensureInitialProfile()
    }

    override fun observeCurrent(): Flow<UserProfile> =
        currentUserRepository.observeCurrentUserId()
            .flatMapLatest { userId ->
                userProfileDao.observeById(userId ?: LOCAL_DEFAULT_USER_ID)
                    .map { entity ->
                        // Si el perfil no existe aún (bootstrap pendiente o primera ejecución),
                        // se crea y se establece como usuario actual. El DAO volverá a emitir
                        // con el perfil creado, por lo que esta rama solo se ejecuta una vez.
                        entity?.let(UserProfileMapper::toModel) ?: ensureInitialProfile()
                    }
            }

    override suspend fun updateDisplayName(name: String) {
        val current = getCurrent()
        val updated = current.copy(
            displayName = name.trim().ifBlank { current.displayName },
            updatedAt = System.currentTimeMillis()
        )
        userProfileDao.upsert(UserProfileMapper.toEntity(updated))
    }

    override suspend fun updateAvatarIcon(icon: String?) {
        val current = getCurrent()
        val updated = current.copy(
            avatarIcon = icon,
            updatedAt = System.currentTimeMillis()
        )
        userProfileDao.upsert(UserProfileMapper.toEntity(updated))
    }

    override suspend fun updateAvatarImageUri(uri: String?) {
        val current = getCurrent()
        val updated = current.copy(
            avatarImageUri = uri,
            updatedAt = System.currentTimeMillis()
        )
        userProfileDao.upsert(UserProfileMapper.toEntity(updated))
    }

    private suspend fun createDefaultProfile(): UserProfile {
        val now = System.currentTimeMillis()
        val profile = UserProfile(
            id = LOCAL_DEFAULT_USER_ID,
            displayName = LOCAL_DEFAULT_USER_NAME,
            avatarIcon = null,
            avatarImageUri = null,
            createdAt = now,
            updatedAt = now
        )
        userProfileDao.upsert(UserProfileMapper.toEntity(profile))
        return profile
    }
}
