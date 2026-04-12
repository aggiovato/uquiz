package com.uquiz.android.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.uquiz.android.core.analytics.usecase.ComputeSessionPerformanceUseCase
import com.uquiz.android.core.analytics.usecase.FinalizeAttemptAnalyticsUseCase
import com.uquiz.android.core.analytics.usecase.UpdatePackStatsFromAttemptUseCase
import com.uquiz.android.core.analytics.usecase.UpdateQuestionStatsFromAttemptUseCase
import com.uquiz.android.core.analytics.usecase.UpdateUserRankFromAttemptUseCase
import com.uquiz.android.core.game.usecase.BuildGameAttemptPlanUseCase
import com.uquiz.android.core.game.usecase.CalculateQuestionTimeBudgetUseCase
import com.uquiz.android.core.game.usecase.ComputeGameScoreUseCase
import com.uquiz.android.core.game.usecase.FinalizeGameAttemptUseCase
import com.uquiz.android.core.preferences.PreferencesModule
import com.uquiz.android.core.user.AppBootstrapper
import com.uquiz.android.data.attempts.repository.AttemptRepositoryImpl
import com.uquiz.android.data.content.repository.FolderRepositoryImpl
import com.uquiz.android.data.content.repository.PackRepositoryImpl
import com.uquiz.android.data.content.repository.QuestionRepositoryImpl
import com.uquiz.android.data.importexport.repository.ImportExportRepositoryImpl
import com.uquiz.android.data.importexport.repository.UQuizExportAssembler
import com.uquiz.android.data.importexport.repository.UQuizImportApplier
import com.uquiz.android.data.local.db.UQuizDatabase
import com.uquiz.android.data.ranking.repository.UserRankRepositoryImpl
import com.uquiz.android.data.stats.repository.PackStatsRepositoryImpl
import com.uquiz.android.data.stats.repository.QuestionStatsRepositoryImpl
import com.uquiz.android.data.stats.repository.UserStatsRepositoryImpl
import com.uquiz.android.data.user.repository.CurrentUserRepositoryImpl
import com.uquiz.android.data.user.repository.UserPreferencesRepositoryImpl
import com.uquiz.android.data.user.repository.UserProfileRepositoryImpl
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.content.repository.QuestionRepository
import com.uquiz.android.domain.importexport.repository.ImportExportRepository
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.domain.stats.repository.PackStatsRepository
import com.uquiz.android.domain.stats.repository.QuestionStatsRepository
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.domain.user.repository.CurrentUserRepository
import com.uquiz.android.domain.user.repository.UserPreferencesRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import kotlinx.serialization.json.Json

data class AppRepositories(
    val folderRepository: FolderRepository,
    val packRepository: PackRepository,
    val attemptRepository: AttemptRepository,
    val questionRepository: QuestionRepository,
    val importExportRepository: ImportExportRepository,
    val currentUserRepository: CurrentUserRepository,
    val userProfileRepository: UserProfileRepository,
    val userPreferencesRepository: UserPreferencesRepository,
    val questionStatsRepository: QuestionStatsRepository,
    val packStatsRepository: PackStatsRepository,
    val userStatsRepository: UserStatsRepository,
    val userRankRepository: UserRankRepository,
    val finalizeAttemptAnalyticsUseCase: FinalizeAttemptAnalyticsUseCase,
    val buildGameAttemptPlanUseCase: BuildGameAttemptPlanUseCase,
    val computeGameScoreUseCase: ComputeGameScoreUseCase,
    val finalizeGameAttemptUseCase: FinalizeGameAttemptUseCase,
    val appBootstrapper: AppBootstrapper,
)

@Composable
fun rememberAppRepositories(
    context: Context,
    database: UQuizDatabase,
): AppRepositories =
    remember(context, database) {
        val userPreferencesDataStore = PreferencesModule.getDataStore(context)
        val currentUserRepository = CurrentUserRepositoryImpl(userPreferencesDataStore)
        val userPreferencesRepository = UserPreferencesRepositoryImpl(userPreferencesDataStore)
        val userProfileRepository =
            UserProfileRepositoryImpl(
                database.userProfileDao(),
                currentUserRepository,
            )
        val folderRepository = FolderRepositoryImpl(database.folderDao())
        val packRepository =
            PackRepositoryImpl(
                database.packDao(),
                database.folderDao(),
                database.packQuestionDao(),
            )
        val questionRepository =
            QuestionRepositoryImpl(
                database.questionDao(),
                database.optionDao(),
            )
        val attemptRepository =
            AttemptRepositoryImpl(
                database.attemptDao(),
                database.attemptAnswerDao(),
                database.attemptPackDao(),
                database.attemptQuestionPlanDao(),
                currentUserRepository,
            )
        val questionStatsRepository =
            QuestionStatsRepositoryImpl(
                database.questionStatsDao(),
                currentUserRepository,
            )
        val packStatsRepository =
            PackStatsRepositoryImpl(
                database.packStatsDao(),
                database.attemptDao(),
                database.packQuestionDao(),
                questionStatsRepository,
                currentUserRepository,
            )
        val userRankRepository =
            UserRankRepositoryImpl(
                database.userRankDao(),
                currentUserRepository,
            )
        val userStatsRepository =
            UserStatsRepositoryImpl(
                database.attemptDao(),
                database.attemptAnswerDao(),
                database.packStatsDao(),
                database.questionStatsDao(),
                userRankRepository,
                currentUserRepository,
            )
        val computeSessionPerformanceUseCase = ComputeSessionPerformanceUseCase(questionRepository)
        val finalizeAttemptAnalyticsUseCase =
            FinalizeAttemptAnalyticsUseCase(
                attemptRepository = attemptRepository,
                updateQuestionStatsFromAttemptUseCase = UpdateQuestionStatsFromAttemptUseCase(questionStatsRepository),
                updatePackStatsFromAttemptUseCase = UpdatePackStatsFromAttemptUseCase(packStatsRepository),
                updateUserRankFromAttemptUseCase =
                    UpdateUserRankFromAttemptUseCase(
                        userRankRepository = userRankRepository,
                        computeSessionPerformanceUseCase = computeSessionPerformanceUseCase,
                    ),
            )
        val computeGameScoreUseCase = ComputeGameScoreUseCase()
        val buildGameAttemptPlanUseCase =
            BuildGameAttemptPlanUseCase(
                attemptRepository = attemptRepository,
                packRepository = packRepository,
                questionStatsRepository = questionStatsRepository,
                calculateQuestionTimeBudgetUseCase = CalculateQuestionTimeBudgetUseCase(),
            )
        AppRepositories(
            folderRepository = folderRepository,
            packRepository = packRepository,
            attemptRepository = attemptRepository,
            questionRepository = questionRepository,
            importExportRepository =
                run {
                    val json =
                        Json {
                            prettyPrint = true
                            encodeDefaults = true
                        }
                    ImportExportRepositoryImpl(
                        database = database,
                        exportAssembler =
                            UQuizExportAssembler(
                                folderRepository = folderRepository,
                                packRepository = packRepository,
                                json = json,
                            ),
                        importApplier =
                            UQuizImportApplier(
                                folderRepository = folderRepository,
                                packRepository = packRepository,
                                questionRepository = questionRepository,
                            ),
                    )
                },
            currentUserRepository = currentUserRepository,
            userProfileRepository = userProfileRepository,
            userPreferencesRepository = userPreferencesRepository,
            questionStatsRepository = questionStatsRepository,
            packStatsRepository = packStatsRepository,
            userStatsRepository = userStatsRepository,
            userRankRepository = userRankRepository,
            finalizeAttemptAnalyticsUseCase = finalizeAttemptAnalyticsUseCase,
            buildGameAttemptPlanUseCase = buildGameAttemptPlanUseCase,
            computeGameScoreUseCase = computeGameScoreUseCase,
            finalizeGameAttemptUseCase =
                FinalizeGameAttemptUseCase(
                    attemptRepository = attemptRepository,
                    questionRepository = questionRepository,
                    userRankRepository = userRankRepository,
                    computeGameScoreUseCase = computeGameScoreUseCase,
                    finalizeAttemptAnalyticsUseCase = finalizeAttemptAnalyticsUseCase,
                ),
            appBootstrapper = AppBootstrapper(userProfileRepository),
        )
    }
