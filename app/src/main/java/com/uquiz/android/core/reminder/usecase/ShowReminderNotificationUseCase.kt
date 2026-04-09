package com.uquiz.android.core.reminder.usecase

import android.content.Context
import com.uquiz.android.core.database.DatabaseModule
import com.uquiz.android.core.preferences.createUserPreferencesDataStore
import com.uquiz.android.core.reminder.builder.ReminderNotificationBuilder
import com.uquiz.android.core.reminder.provider.ReminderNotificationContextProvider
import com.uquiz.android.core.reminder.resolver.ReminderMessageResolver
import com.uquiz.android.core.user.AppBootstrapper
import com.uquiz.android.data.attempts.repository.AttemptRepositoryImpl
import com.uquiz.android.data.content.repository.PackRepositoryImpl
import com.uquiz.android.data.ranking.repository.UserRankRepositoryImpl
import com.uquiz.android.data.stats.repository.UserStatsRepositoryImpl
import com.uquiz.android.data.user.repository.CurrentUserRepositoryImpl
import com.uquiz.android.data.user.repository.UserPreferencesRepositoryImpl
import com.uquiz.android.data.user.repository.UserProfileRepositoryImpl

class ShowReminderNotificationUseCase private constructor(
    private val appBootstrapper: AppBootstrapper,
    private val contextProvider: ReminderNotificationContextProvider,
    private val messageResolver: ReminderMessageResolver,
    private val notificationBuilder: ReminderNotificationBuilder,
) {

    suspend operator fun invoke(): Boolean {
        appBootstrapper.ensureReady()
        val context = contextProvider.getContext()
        val message = messageResolver.resolve(context)
        return notificationBuilder.show(message)
    }

    companion object {
        fun create(context: Context): ShowReminderNotificationUseCase {
            val appContext = context.applicationContext
            val database = DatabaseModule.getDatabase(appContext)
            val dataStore = createUserPreferencesDataStore(appContext)
            val currentUserRepository = CurrentUserRepositoryImpl(dataStore)
            val userPreferencesRepository = UserPreferencesRepositoryImpl(dataStore)
            val userProfileRepository = UserProfileRepositoryImpl(
                database.userProfileDao(),
                currentUserRepository
            )
            val attemptRepository = AttemptRepositoryImpl(
                database.attemptDao(),
                database.attemptAnswerDao(),
                database.attemptPackDao(),
                currentUserRepository
            )
            val packRepository = PackRepositoryImpl(
                database.packDao(),
                database.folderDao(),
                database.packQuestionDao()
            )
            val userStatsRepository = UserStatsRepositoryImpl(
                database.attemptDao(),
                currentUserRepository
            )
            val userRankRepository = UserRankRepositoryImpl(
                database.userRankDao(),
                currentUserRepository
            )

            return ShowReminderNotificationUseCase(
                appBootstrapper = AppBootstrapper(userProfileRepository),
                contextProvider = ReminderNotificationContextProvider(
                    userPreferencesRepository = userPreferencesRepository,
                    userStatsRepository = userStatsRepository,
                    userRankRepository = userRankRepository,
                    attemptRepository = attemptRepository,
                    packRepository = packRepository
                ),
                messageResolver = ReminderMessageResolver(),
                notificationBuilder = ReminderNotificationBuilder(appContext)
            )
        }
    }
}
