package com.uquiz.android.ui.navigation.graph

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uquiz.android.ui.AppRepositories
import com.uquiz.android.ui.feature.study.screens.intro.StudyIntroRoute
import com.uquiz.android.ui.feature.study.screens.session.StudySessionRoute
import com.uquiz.android.ui.feature.study.screens.summary.StudySummaryRoute
import com.uquiz.android.ui.navigation.Routes

/**
 * Registra las rutas del flujo de Study en el [NavGraphBuilder] receptor.
 *
 * Cubre: STUDY (intro), STUDY_SESSION, STUDY_SUMMARY.
 *
 * @param exitState Estado compartido con el shell para mostrar el diálogo de salida cuando
 *   el usuario toca el top bar o el bottom nav durante una sesión activa.
 */
fun NavGraphBuilder.addStudyGraph(
    navController: NavController,
    repositories: AppRepositories,
    exitState: MutableState<Boolean>,
) {
    composable(
        route = Routes.Patterns.STUDY,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
            ?: return@composable
        StudyIntroRoute(
            packId = packId,
            packRepository = repositories.packRepository,
            attemptRepository = repositories.attemptRepository,
            onBack = { navController.popBackStack() },
            onOpenSession = { navController.navigate(Routes.studySessionRoute(packId)) },
        )
    }

    composable(
        route = Routes.Patterns.STUDY_SESSION,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
            ?: return@composable
        StudySessionRoute(
            packId = packId,
            packRepository = repositories.packRepository,
            attemptRepository = repositories.attemptRepository,
            finalizeAttemptAnalyticsUseCase = repositories.finalizeAttemptAnalyticsUseCase,
            externalExitRequested = exitState.value,
            onExternalExitConsumed = { exitState.value = false },
            onExitToPack = {
                // Si hay un pack en la pila, vuelve a él; si no (viene de Home),
                // simplemente saca la sesión de la pila.
                if (!navController.popBackStack(Routes.packRoute(packId), inclusive = false)) {
                    navController.popBackStack()
                }
            },
            onFinished = { attemptId ->
                navController.navigate(Routes.studySummaryRoute(attemptId)) {
                    popUpTo(Routes.studyRoute(packId)) { inclusive = true }
                    launchSingleTop = true
                }
            },
        )
    }

    composable(
        route = Routes.Patterns.STUDY_SUMMARY,
        arguments = listOf(
            navArgument(Routes.ARG_ATTEMPT_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val attemptId = backStackEntry.arguments?.getString(Routes.ARG_ATTEMPT_ID)
            ?: return@composable
        StudySummaryRoute(
            attemptId = attemptId,
            attemptRepository = repositories.attemptRepository,
            packRepository = repositories.packRepository,
            onBack = { navController.popBackStack() },
        )
    }
}
