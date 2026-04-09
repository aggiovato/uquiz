package com.uquiz.android.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uquiz.android.R
import com.uquiz.android.ui.AppRepositories
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastHost
import com.uquiz.android.ui.designsystem.components.feedback.rememberUToastController
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.folder.FolderRoute
import com.uquiz.android.ui.feature.game.GameRoute
import com.uquiz.android.ui.feature.help.PackStatsHelpRoute
import com.uquiz.android.ui.feature.home.HomeRoute
import com.uquiz.android.ui.feature.library.LibraryRoute
import com.uquiz.android.ui.feature.pack.PackRoute
import com.uquiz.android.ui.feature.preferences.PreferencesRoute
import com.uquiz.android.ui.feature.question.QuestionRoute
import com.uquiz.android.ui.feature.quickgame.QuickGameRoute
import com.uquiz.android.ui.feature.stats.screens.pack.PackStatsRoute
import com.uquiz.android.ui.feature.stats.screens.stats.StatsRoute
import com.uquiz.android.ui.feature.study.screens.intro.StudyIntroRoute
import com.uquiz.android.ui.feature.study.screens.session.StudySessionRoute
import com.uquiz.android.ui.feature.study.screens.summary.StudySummaryRoute
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.i18n.stringsFor
import com.uquiz.android.ui.navigation.components.UNavigationTrailBar
import com.uquiz.android.ui.navigation.components.UTopBar
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavGraph(repositories: AppRepositories) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val toastController = rememberUToastController(scope)
    val preferences by repositories.userPreferencesRepository
        .observePreferences()
        .collectAsState(
            initial =
                com.uquiz.android.domain.user.model
                    .UserPreferences(),
        )

    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    // Señal para que StudySessionRoute muestre el diálogo de salida cuando el back
    // llega desde el top bar o el bottom nav (no desde el gesto del sistema).
    var studySessionExitRequest by remember { mutableStateOf(false) }
    val strings = remember(preferences.languageCode) { stringsFor(preferences.languageCode) }
    val navigationContext by produceState(
        initialValue = fallbackNavigationContext(currentRoute, strings),
        key1 = currentEntry?.id,
        key2 = preferences.languageCode,
    ) {
        value =
            resolveNavigationContext(
                entry = currentEntry,
                strings = strings,
                folderRepository = repositories.folderRepository,
                packRepository = repositories.packRepository,
                attemptRepository = repositories.attemptRepository,
            )
    }
    val selectedDestination = navigationContext.root
    val isTopLevel = isTopLevelRoute(currentRoute)
    val chromeState =
        remember(currentRoute) {
            navigationChromeStateForRoute(
                route = currentRoute,
                studyRoutes = studyRoutePatterns,
            )
        }
    val chromeTransition = updateTransition(targetState = chromeState, label = "navigationChrome")
    val rootBackgroundColor =
        chromeTransition.animateColor(
            transitionSpec = { tween(durationMillis = 320) },
            label = "rootBackgroundColor",
        ) { it.rootBackgroundColor }
    val backgroundImageAlpha =
        chromeTransition.animateFloat(
            transitionSpec = { tween(durationMillis = 260) },
            label = "backgroundImageAlpha",
        ) { it.backgroundImageAlpha }

    UTheme(themeMode = preferences.themeMode) {
        CompositionLocalProvider(
            LocalStrings provides strings,
            LocalToastController provides toastController,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(rootBackgroundColor.value),
            ) {
                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        UTopBar(
                            title = navigationContext.title,
                            canNavigateBack = !isTopLevel,
                            onBackClick = {
                                if (currentRoute?.contains(Routes.STUDY_SESSION) == true) {
                                    studySessionExitRequest = true
                                } else {
                                    navController.popBackStack()
                                }
                            },
                            selectedLang = preferences.languageCode,
                            onLangSelect = { code ->
                                scope.launch {
                                    repositories.userPreferencesRepository.updateLanguage(code)
                                }
                            },
                            variant = chromeState.variant,
                        )
                    },
                    bottomBar = {
                        AppBottomNavBar(
                            selected = selectedDestination,
                            variant = chromeState.variant,
                            onDestinationClick = { dest ->
                                if (currentRoute?.contains(Routes.STUDY_SESSION) == true) {
                                    studySessionExitRequest = true
                                } else {
                                    val route =
                                        when (dest) {
                                            TopLevelDestination.HOME -> Routes.HOME
                                            TopLevelDestination.LIBRARY -> Routes.LIBRARY
                                            TopLevelDestination.GAME -> Routes.GAME
                                            TopLevelDestination.STATS -> Routes.STATS
                                        }
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        )
                    },
                ) { paddingValues ->
                    val onFolderClick: (String, String) -> Unit = { id, _ ->
                        navController.navigate(Routes.folderRoute(id))
                    }
                    val onPackClick: (String, String) -> Unit = { id, _ ->
                        navController.navigate(Routes.packRoute(id))
                    }
                    val onFolderDeleted: () -> Unit = {
                        navController.popBackStack()
                    }

                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(
                                    PaddingValues(
                                        top = paddingValues.calculateTopPadding(),
                                        bottom = paddingValues.calculateBottomPadding(),
                                    ),
                                ),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.bck_image),
                            contentDescription = null,
                            alpha = backgroundImageAlpha.value,
                            modifier =
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .scale(1.8f)
                                    .fillMaxSize(0.75f)
                                    .offset(x = 40.dp, y = (-40).dp)
                                    .rotate(-10f),
                        )

                        Column(modifier = Modifier.fillMaxSize()) {
                            if (chromeState.showBreadcrumbs) {
                                UNavigationTrailBar(
                                    trail = navigationContext.trail,
                                    variant = chromeState.variant,
                                )
                            }

                            Box(modifier = Modifier.weight(1f)) {
                                NavHost(
                                    navController = navController,
                                    startDestination = Routes.HOME,
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    composable(Routes.HOME) {
                                        HomeRoute(
                                            userProfileRepository = repositories.userProfileRepository,
                                            userRankRepository = repositories.userRankRepository,
                                            userStatsRepository = repositories.userStatsRepository,
                                            attemptRepository = repositories.attemptRepository,
                                            packRepository = repositories.packRepository,
                                            onContinuePlayingClick = {
                                                navController.navigate(
                                                    Routes.quickGameRoute(it),
                                                )
                                            },
                                            onContinueStudyingClick = { navController.navigate(Routes.studyRoute(it)) },
                                            onProfileClick = { navController.navigate(Routes.PREFERENCES) },
                                        )
                                    }

                                    composable(Routes.PREFERENCES) {
                                        PreferencesRoute(
                                            userProfileRepository = repositories.userProfileRepository,
                                            userPreferencesRepository = repositories.userPreferencesRepository,
                                        )
                                    }

                                    composable(Routes.LIBRARY) {
                                        LibraryRoute(
                                            folderRepository = repositories.folderRepository,
                                            packRepository = repositories.packRepository,
                                            packStatsRepository = repositories.packStatsRepository,
                                            importExportRepository = repositories.importExportRepository,
                                            onFolderClick = onFolderClick,
                                            onPackClick = onPackClick,
                                        )
                                    }

                                    composable(Routes.GAME) {
                                        GameRoute()
                                    }

                                    composable(Routes.STATS) {
                                        StatsRoute(
                                            userStatsRepository = repositories.userStatsRepository,
                                        )
                                    }

                                    composable(
                                        route = "${Routes.FOLDER}/{${Routes.ARG_FOLDER_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_FOLDER_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) { backStackEntry ->
                                        val folderId =
                                            backStackEntry.arguments?.getString(Routes.ARG_FOLDER_ID)
                                                ?: return@composable
                                        FolderRoute(
                                            folderId = folderId,
                                            folderRepository = repositories.folderRepository,
                                            packRepository = repositories.packRepository,
                                            packStatsRepository = repositories.packStatsRepository,
                                            importExportRepository = repositories.importExportRepository,
                                            onFolderClick = onFolderClick,
                                            onPackClick = onPackClick,
                                            onFolderDeleted = onFolderDeleted,
                                        )
                                    }

                                    composable(
                                        route = "${Routes.PACK}/{${Routes.ARG_PACK_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) { backStackEntry ->
                                        val packId =
                                            backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
                                                ?: return@composable
                                        PackRoute(
                                            packId = packId,
                                            packRepository = repositories.packRepository,
                                            packStatsRepository = repositories.packStatsRepository,
                                            importExportRepository = repositories.importExportRepository,
                                            onPackTitleResolved = { },
                                            onStudyModeClick = { navController.navigate(Routes.studyRoute(it)) },
                                            onQuickGameClick = { navController.navigate(Routes.quickGameRoute(it)) },
                                            onDetailedStatsClick = {
                                                navController.navigate(
                                                    Routes.packStatsRoute(it),
                                                )
                                            },
                                            onCreateQuestionClick = {
                                                navController.navigate(
                                                    Routes.questionCreateRoute(it),
                                                )
                                            },
                                            onQuestionClick = { currentPackId, questionId ->
                                                navController.navigate(
                                                    Routes.questionEditRoute(currentPackId, questionId),
                                                )
                                            },
                                            onPackDeleted = { navController.popBackStack() },
                                        )
                                    }

                                    composable(
                                        route = "${Routes.PACK_STATS}/{${Routes.ARG_PACK_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) { backStackEntry ->
                                        val packId =
                                            backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
                                                ?: return@composable
                                        PackStatsRoute(
                                            packId = packId,
                                            packRepository = repositories.packRepository,
                                            packStatsRepository = repositories.packStatsRepository,
                                            onBackToPack = {
                                                navController.popBackStack(Routes.packRoute(packId), false)
                                            },
                                            onHelpClick = { navController.navigate(Routes.packStatsHelpRoute(packId)) },
                                        )
                                    }

                                    composable(
                                        route = "${Routes.PACK_STATS_HELP}/{${Routes.ARG_PACK_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) {
                                        PackStatsHelpRoute()
                                    }

                                    composable(
                                        route = "${Routes.STUDY}/{${Routes.ARG_PACK_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) { backStackEntry ->
                                        val packId =
                                            backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
                                                ?: return@composable
                                        StudyIntroRoute(
                                            packId = packId,
                                            packRepository = repositories.packRepository,
                                            attemptRepository = repositories.attemptRepository,
                                            onBack = { navController.popBackStack() },
                                            onOpenSession = {
                                                navController.navigate(Routes.studySessionRoute(packId))
                                            },
                                        )
                                    }

                                    composable(
                                        route = "${Routes.STUDY_SESSION}/{${Routes.ARG_PACK_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) { backStackEntry ->
                                        val packId =
                                            backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
                                                ?: return@composable
                                        StudySessionRoute(
                                            packId = packId,
                                            packRepository = repositories.packRepository,
                                            attemptRepository = repositories.attemptRepository,
                                            finalizeAttemptAnalyticsUseCase = repositories.finalizeAttemptAnalyticsUseCase,
                                            externalExitRequested = studySessionExitRequest,
                                            onExternalExitConsumed = { studySessionExitRequest = false },
                                            onExitToPack = {
                                                navController.popBackStack(Routes.packRoute(packId), false)
                                            },
                                            onFinished = { attemptId ->
                                                navController.navigate(Routes.studySummaryRoute(attemptId)) {
                                                    popUpTo(Routes.studyRoute(packId)) { inclusive = false }
                                                    launchSingleTop = true
                                                }
                                            },
                                        )
                                    }

                                    composable(
                                        route = "${Routes.STUDY_SUMMARY}/{${Routes.ARG_ATTEMPT_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_ATTEMPT_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) { backStackEntry ->
                                        val attemptId =
                                            backStackEntry.arguments?.getString(Routes.ARG_ATTEMPT_ID)
                                                ?: return@composable
                                        StudySummaryRoute(
                                            attemptId = attemptId,
                                            attemptRepository = repositories.attemptRepository,
                                            packRepository = repositories.packRepository,
                                            onBackToPack = { packId ->
                                                navController.popBackStack(Routes.packRoute(packId), false)
                                            },
                                        )
                                    }

                                    composable(
                                        route = "${Routes.QUICK_GAME}/{${Routes.ARG_PACK_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) {
                                        QuickGameRoute()
                                    }

                                    composable(
                                        route = "${Routes.QUESTION_CREATE}/{${Routes.ARG_PACK_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) {
                                                    type = NavType.StringType
                                                },
                                            ),
                                    ) { backStackEntry ->
                                        val packId =
                                            backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
                                                ?: return@composable
                                        QuestionRoute(
                                            packId = packId,
                                            questionId = null,
                                            questionRepository = repositories.questionRepository,
                                            packRepository = repositories.packRepository,
                                            onDone = { navController.popBackStack() },
                                        )
                                    }

                                    composable(
                                        route = "${Routes.QUESTION_EDIT}/{${Routes.ARG_PACK_ID}}/{${Routes.ARG_QUESTION_ID}}",
                                        arguments =
                                            listOf(
                                                navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
                                                navArgument(Routes.ARG_QUESTION_ID) { type = NavType.StringType },
                                            ),
                                    ) { backStackEntry ->
                                        val packId =
                                            backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
                                                ?: return@composable
                                        val questionId =
                                            backStackEntry.arguments?.getString(Routes.ARG_QUESTION_ID)
                                                ?: return@composable
                                        QuestionRoute(
                                            packId = packId,
                                            questionId = questionId,
                                            questionRepository = repositories.questionRepository,
                                            packRepository = repositories.packRepository,
                                            onDone = { navController.popBackStack() },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                UToastHost(manager = toastController)
            }
        }
    } // UTheme
}
