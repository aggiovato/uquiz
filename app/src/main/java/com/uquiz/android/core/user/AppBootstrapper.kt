package com.uquiz.android.core.user

import com.uquiz.android.domain.user.repository.UserProfileRepository

class AppBootstrapper(
    private val userProfileRepository: UserProfileRepository
) {
    suspend fun ensureReady() {
        userProfileRepository.ensureInitialProfile()
    }
}
