package com.uquiz.android.ui.navigation.graph

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uquiz.android.ui.AppRepositories
import com.uquiz.android.ui.feature.game.screens.home.GameHomeRoute
import com.uquiz.android.ui.feature.game.screens.intro.GameIntroRoute
import com.uquiz.android.ui.feature.game.screens.session.GameSessionRoute
import com.uquiz.android.ui.feature.game.screens.summary.GameSummaryRoute
import com.uquiz.android.ui.navigation.Routes

/**
 * Registra las rutas del flujo de Game en el [NavGraphBuilder] receptor.
 *
 * Cubre: GAME (home), GAME_INTRO, GAME_SESSION, GAME_SUMMARY.
 *
 * @param exitState Estado compartido con el shell para mostrar el diálogo de salida cuando
 *   el usuario toca el top bar o el bottom nav durante una sesión activa.
 */
fun NavGraphBuilder.addGameGraph(
    navController: NavController,
    repositories: AppRepositories,
    exitState: MutableState<Boolean>,
) {
    composable(Routes.GAME) {
        GameHomeRoute(
            packRepository = repositories.packRepository,
            attemptRepository = repositories.attemptRepository,
            onPackClick = { packId ->
                navController.navigate(Routes.gameIntroRoute(packId))
            },
        )
    }

    composable(
        route = Routes.Patterns.GAME_INTRO,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
            ?: return@composable
        GameIntroRoute(
            packId = packId,
            packRepository = repositories.packRepository,
            attemptRepository = repositories.attemptRepository,
            onBack = { navController.popBackStack() },
            onStartGame = { navController.navigate(Routes.gameSessionRoute(packId)) },
        )
    }

    composable(
        route = Routes.Patterns.GAME_SESSION,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
            ?: return@composable
        GameSessionRoute(
            packId = packId,
            buildGameAttemptPlanUseCase = repositories.buildGameAttemptPlanUseCase,
            computeGameScoreUseCase = repositories.computeGameScoreUseCase,
            finalizeGameAttemptUseCase = repositories.finalizeGameAttemptUseCase,
            attemptRepository = repositories.attemptRepository,
            packRepository = repositories.packRepository,
            externalExitRequested = exitState.value,
            onExternalExitConsumed = { exitState.value = false },
            onExitToGame = {
                // Si hay una pantalla de Game en la pila, vuelve a ella;
                // si no (viene de Home u otro contexto), saca la sesión de la pila.
                if (!navController.popBackStack(Routes.GAME, inclusive = false)) {
                    navController.popBackStack()
                }
            },
            onFinished = { attemptId, xpGained, prevRankOrdinal, currRankOrdinal, prevMmrInt ->
                navController.navigate(
                    Routes.gameSummaryRoute(
                        attemptId = attemptId,
                        xpGained = xpGained,
                        previousRankOrdinal = prevRankOrdinal,
                        currentRankOrdinal = currRankOrdinal,
                        previousMmrInt = prevMmrInt,
                    ),
                ) {
                    popUpTo(Routes.gameIntroRoute(packId)) { inclusive = true }
                    launchSingleTop = true
                }
            },
        )
    }

    composable(
        route = Routes.Patterns.GAME_SUMMARY,
        arguments = listOf(
            navArgument(Routes.ARG_ATTEMPT_ID) { type = NavType.StringType },
            navArgument(Routes.ARG_XP_GAINED) { type = NavType.LongType },
            navArgument(Routes.ARG_PREVIOUS_RANK_ORDINAL) { type = NavType.IntType },
            navArgument(Routes.ARG_CURRENT_RANK_ORDINAL) { type = NavType.IntType },
            navArgument(Routes.ARG_PREVIOUS_MMR_INT) { type = NavType.IntType },
        ),
    ) { backStackEntry ->
        val attemptId = backStackEntry.arguments?.getString(Routes.ARG_ATTEMPT_ID)
            ?: return@composable
        val xpGained = backStackEntry.arguments?.getLong(Routes.ARG_XP_GAINED) ?: 0L
        val prevRankOrdinal = backStackEntry.arguments?.getInt(Routes.ARG_PREVIOUS_RANK_ORDINAL) ?: 0
        val currRankOrdinal = backStackEntry.arguments?.getInt(Routes.ARG_CURRENT_RANK_ORDINAL) ?: 0
        val prevMmrInt = backStackEntry.arguments?.getInt(Routes.ARG_PREVIOUS_MMR_INT) ?: 60000
        GameSummaryRoute(
            attemptId = attemptId,
            xpGained = xpGained,
            previousRankOrdinal = prevRankOrdinal,
            currentRankOrdinal = currRankOrdinal,
            previousMmrInt = prevMmrInt,
            attemptRepository = repositories.attemptRepository,
            userRankRepository = repositories.userRankRepository,
            onPlayAgain = { packId ->
                navController.navigate(Routes.gameIntroRoute(packId)) {
                    // Elimina GAME_SUMMARY de la pila independientemente de dónde vino el usuario
                    // (HOME, GAME tab, LIBRARY…). Sin esto, si GAME no está en la pila el
                    // popUpTo anterior era un no-op y Back retornaba a la pantalla de resumen.
                    popUpTo(Routes.Patterns.GAME_SUMMARY) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() },
        )
    }
}
