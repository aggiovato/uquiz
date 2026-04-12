package com.uquiz.android.ui.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uquiz.android.ui.AppRepositories
import com.uquiz.android.ui.feature.folder.FolderRoute
import com.uquiz.android.ui.feature.pack.PackRoute
import com.uquiz.android.ui.feature.question.QuestionRoute
import com.uquiz.android.ui.navigation.Routes

/**
 * Registra las rutas del árbol de librería en el [NavGraphBuilder] receptor.
 *
 * Cubre: FOLDER, PACK, QUESTION_CREATE, QUESTION_EDIT.
 * Las rutas de estadísticas de pack (PACK_STATS) se registran en [StatsNavGraph].
 */
fun NavGraphBuilder.addLibraryGraph(
    navController: NavController,
    repositories: AppRepositories,
) {
    val onFolderClick: (String, String) -> Unit = { id, _ ->
        navController.navigate(Routes.folderRoute(id))
    }
    val onPackClick: (String, String) -> Unit = { id, _ ->
        navController.navigate(Routes.packRoute(id))
    }

    composable(
        route = Routes.Patterns.FOLDER,
        arguments = listOf(
            navArgument(Routes.ARG_FOLDER_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val folderId = backStackEntry.arguments?.getString(Routes.ARG_FOLDER_ID)
            ?: return@composable
        FolderRoute(
            folderId = folderId,
            folderRepository = repositories.folderRepository,
            packRepository = repositories.packRepository,
            packStatsRepository = repositories.packStatsRepository,
            importExportRepository = repositories.importExportRepository,
            onFolderClick = onFolderClick,
            onPackClick = onPackClick,
            onFolderDeleted = { navController.popBackStack() },
        )
    }

    composable(
        route = Routes.Patterns.PACK,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
            ?: return@composable
        PackRoute(
            packId = packId,
            packRepository = repositories.packRepository,
            packStatsRepository = repositories.packStatsRepository,
            importExportRepository = repositories.importExportRepository,
            onPackTitleResolved = {},
            onStudyModeClick = { navController.navigate(Routes.studyRoute(it)) },
            onGameClick = { navController.navigate(Routes.gameIntroRoute(it)) },
            onDetailedStatsClick = { navController.navigate(Routes.packStatsRoute(it)) },
            onCreateQuestionClick = { navController.navigate(Routes.questionCreateRoute(it)) },
            onQuestionClick = { currentPackId, questionId ->
                navController.navigate(Routes.questionEditRoute(currentPackId, questionId))
            },
            onPackDeleted = { navController.popBackStack() },
        )
    }

    composable(
        route = Routes.Patterns.QUESTION_CREATE,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
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
        route = Routes.Patterns.QUESTION_EDIT,
        arguments = listOf(
            navArgument(Routes.ARG_PACK_ID) { type = NavType.StringType },
            navArgument(Routes.ARG_QUESTION_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val packId = backStackEntry.arguments?.getString(Routes.ARG_PACK_ID)
            ?: return@composable
        val questionId = backStackEntry.arguments?.getString(Routes.ARG_QUESTION_ID)
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
