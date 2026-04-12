package com.uquiz.android.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uquiz.android.R
import com.uquiz.android.ui.AppRepositories
import com.uquiz.android.ui.designsystem.components.feedback.LocalToastController
import com.uquiz.android.ui.designsystem.components.feedback.UToastHost
import com.uquiz.android.ui.designsystem.components.feedback.rememberUToastController
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.feature.home.HomeRoute
import com.uquiz.android.ui.feature.library.LibraryRoute
import com.uquiz.android.ui.feature.preferences.PreferencesRoute
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.i18n.stringsFor
import com.uquiz.android.ui.navigation.chrome.navigationChromeStateForRoute
import com.uquiz.android.ui.navigation.components.AppBottomNavBar
import com.uquiz.android.ui.navigation.components.UNavigationTrailBar
import com.uquiz.android.ui.navigation.components.UTopBar
import com.uquiz.android.ui.navigation.graph.addGameGraph
import com.uquiz.android.ui.navigation.graph.addLibraryGraph
import com.uquiz.android.ui.navigation.graph.addStatsGraph
import com.uquiz.android.ui.navigation.graph.addStudyGraph
import com.uquiz.android.ui.navigation.tree.TopLevelDestination
import com.uquiz.android.ui.navigation.tree.fallbackNavigationContext
import com.uquiz.android.ui.navigation.tree.gameRoutePatterns
import com.uquiz.android.ui.navigation.tree.isTopLevelRoute
import com.uquiz.android.ui.navigation.tree.resolveNavigationContext
import com.uquiz.android.ui.navigation.tree.studyRoutePatterns
import com.uquiz.android.ui.navigation.tree.topLevelDestinationForRoute
import com.uquiz.android.ui.navigation.tree.topLevelRoutes
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
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

    // Estado compartido con los sub-grafos para mostrar el diálogo de salida
    // cuando el usuario toca el top bar o el bottom nav durante una sesión activa.
    val studyExitState = remember { mutableStateOf(false) }
    val gameExitState = remember { mutableStateOf(false) }

    val strings = remember(preferences.languageCode) { stringsFor(preferences.languageCode) }


    // Mantiene el último contexto resuelto: se usa como valor inicial de produceState
    // al cambiar de ruta, evitando que el título muestre el texto de fallback genérico
    // ("Mi biblioteca") mientras se espera el nombre real de la carpeta o el pack.
    var lastResolvedContext by remember {
        mutableStateOf(fallbackNavigationContext(currentRoute, strings))
    }

    val navigationContext by produceState(
        initialValue = lastResolvedContext,
        key1 = currentEntry?.id,
        key2 = preferences.languageCode,
    ) {
        // El destino de nivel superior más reciente en la pila determina cuál pestaña
        // del bottom nav se resalta, independientemente de cómo se llegó a la ruta actual.
        val originTopLevel =
            navController.currentBackStack.value
                .lastOrNull { it.destination.route in topLevelRoutes }
                ?.destination
                ?.route
                ?.let { topLevelDestinationForRoute(it) }
                ?: TopLevelDestination.HOME
        val resolved =
            resolveNavigationContext(
                entry = currentEntry,
                originTopLevel = originTopLevel,
                strings = strings,
                folderRepository = repositories.folderRepository,
                packRepository = repositories.packRepository,
                attemptRepository = repositories.attemptRepository,
            )
        lastResolvedContext = resolved
        value = resolved
    }
    val selectedDestination = navigationContext.root
    val isTopLevel = isTopLevelRoute(currentRoute)
    val chromeState =
        remember(currentRoute) {
            navigationChromeStateForRoute(
                route = currentRoute,
                studyRoutes = studyRoutePatterns,
                gameRoutes = gameRoutePatterns,
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
                            topLevelDestination = if (isTopLevel) selectedDestination else null,
                            onBackClick = {
                                when {
                                    currentRoute?.contains(Routes.STUDY_SESSION) == true -> {
                                        studyExitState.value = true
                                    }

                                    currentRoute?.contains(Routes.GAME_SESSION) == true -> {
                                        gameExitState.value = true
                                    }

                                    else -> {
                                        navController.popBackStack()
                                    }
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
                                when {
                                    currentRoute?.contains(Routes.STUDY_SESSION) == true -> {
                                        studyExitState.value = true
                                    }

                                    currentRoute?.contains(Routes.GAME_SESSION) == true -> {
                                        gameExitState.value = true
                                    }

                                    dest == selectedDestination -> {
                                        // No-op: ya estamos en la pestaña seleccionada.
                                    }

                                    else -> {
                                        val route =
                                            when (dest) {
                                                TopLevelDestination.HOME -> Routes.HOME
                                                TopLevelDestination.LIBRARY -> Routes.LIBRARY
                                                TopLevelDestination.GAME -> Routes.GAME
                                                TopLevelDestination.STATS -> Routes.STATS
                                            }
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            },
                        )
                    },
                ) { paddingValues ->
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
                                    // Fade sincronizado con la transición del chrome (260 ms):
                                    // así el contenido y la barra/breadcrumb cambian a la vez.
                                    enterTransition = { fadeIn(animationSpec = tween(220)) },
                                    exitTransition = { fadeOut(animationSpec = tween(220)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(220)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(220)) },
                                ) {
                                    composable(Routes.HOME) {
                                        HomeRoute(
                                            userProfileRepository = repositories.userProfileRepository,
                                            userRankRepository = repositories.userRankRepository,
                                            userStatsRepository = repositories.userStatsRepository,
                                            attemptRepository = repositories.attemptRepository,
                                            packRepository = repositories.packRepository,
                                            onContinuePlayingClick = {
                                                navController.navigate(Routes.gameIntroRoute(it))
                                            },
                                            onContinueStudyingClick = {
                                                navController.navigate(Routes.studyRoute(it))
                                            },
                                            onProfileClick = {
                                                navController.navigate(Routes.PREFERENCES)
                                            },
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
                                            onFolderClick = { id, _ ->
                                                navController.navigate(Routes.folderRoute(id))
                                            },
                                            onPackClick = { id, _ ->
                                                navController.navigate(Routes.packRoute(id))
                                            },
                                            onRandomStudyClick = { packId ->
                                                navController.navigate(Routes.studyRoute(packId))
                                            },
                                        )
                                    }

                                    addLibraryGraph(navController, repositories)
                                    addStudyGraph(navController, repositories, studyExitState)
                                    addGameGraph(navController, repositories, gameExitState)
                                    addStatsGraph(navController, repositories)
                                }
                            }
                        }
                    }
                }

                UToastHost(manager = toastController)
            }
        }
    }
}
