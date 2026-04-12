package com.uquiz.android.data.ranking.repository

import com.uquiz.android.data.ranking.dao.UserRankDao
import com.uquiz.android.data.ranking.mapper.UserRankMapper
import com.uquiz.android.domain.ranking.model.UserRankState
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

/**
 * Implementación de [UserRankRepository]. Combina [UserRankDao] con [CurrentUserRepository]
 * para reactive reads con filtro por usuario activo. Cuando no existe fila en `user_rank`,
 * devuelve un estado por defecto con MMR inicial en lugar de propagar `null`.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserRankRepositoryImpl(
    private val userRankDao: UserRankDao,
    private val currentUserRepository: CurrentUserRepository
) : UserRankRepository {

    override suspend fun getCurrent(): UserRankState {
        val userId = currentUserRepository.getCurrentUserId()
            ?: error("No current user — ensure AppBootstrapper has run before calling getCurrent()")
        return userRankDao.getByUserId(userId)?.let(UserRankMapper::toModel)
            ?: defaultRankState(userId)
    }

    override fun observeCurrent(): Flow<UserRankState> =
        currentUserRepository.observeCurrentUserId()
            // Espera hasta que el bootstrap establezca un usuario, igual que getCurrent()
            .filterNotNull()
            .flatMapLatest { userId ->
                // El map queda dentro del flatMapLatest para tener acceso al userId real
                // y así construir el estado por defecto con el id correcto si no hay entidad
                userRankDao.observeByUserId(userId)
                    .map { entity ->
                        entity?.let(UserRankMapper::toModel) ?: defaultRankState(userId)
                    }
            }

    override suspend fun upsert(rank: UserRankState) {
        userRankDao.upsert(UserRankMapper.toEntity(rank))
    }

    /** Crea un estado de ranking inicial válido para [userId] cuando todavía no existe en la BD. */
    private fun defaultRankState(userId: String) = UserRankState(
        userId = userId,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}
