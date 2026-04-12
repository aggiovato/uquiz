package com.uquiz.android.ui.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uquiz.android.ui.AppRepositories
import com.uquiz.android.ui.feature.help.PackStatsHelpRoute
import com.uquiz.android.ui.feature.stats.screens.pack.PackStatsRoute
import com.uquiz.android.ui.feature.stats.screens.user.UserStatsRoute
import com.uquiz.android.ui.navigation.Routes

/**
 * Registra las rutas de estadísticas en el [NavGraphBuilder] receptor.
 *
 * Cubre: STATS (resumen global), PACK_STATS (estadísticas de pack), PACK_STATS_HELP.
 * PACK_STATS puede alcanzarse tanto desde la pestaña de Library (vía PackRoute) como desde
 * la pestaña de Stats. El contexto de navegación visible (pestaña resaltada y migas de pan)
 * se resuelve en base al origen real de la pila en NavigationContextResolver.
 */
fun NavGraphBuilder.addStatsGraph(
    navController: NavController,
    repositories: AppRepositories,
) {
    composable(Routes.STATS) {
        UserStatsRoute(userStatsRepository = repositories.userStatsRepository)
    }

    composable(
        route = Routes.Patterns.PACK_STATS,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
            ?: return@composable
        PackStatsRoute(
            packId = packId,
            packRepository = repositories.packRepository,
            packStatsRepository = repositories.packStatsRepository,
            onBackToPack = {
                // Si hay un pack en la pila (acceso desde Library), vuelve a él;
                // si no (acceso directo desde Stats), sale a la entrada anterior.
                if (!navController.popBackStack(Routes.packRoute(packId), inclusive = false)) {
                    navController.popBackStack()
                }
            },
            onHelpClick = { navController.navigate(Routes.packStatsHelpRoute(packId)) },
        )
    }

    composable(
        route = Routes.Patterns.PACK_STATS_HELP,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) {
        PackStatsHelpRoute()
    }
}
