package com.uquiz.android.ui.navigation.tree

import androidx.navigation.NavBackStackEntry
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.navigation.Routes

/**
 * Resuelve el contexto de navegación visible a partir de la entrada actual de la pila
 * y de datos de dominio.
 *
 * Recibe [originTopLevel] — el destino de nivel superior más reciente en la pila — para
 * que rutas accesibles desde varias pestañas (PackStats, Study, Game) reflejen la pestaña
 * desde la que el usuario llegó, en lugar de un valor fijo.
 *
 * Las rutas dinámicas derivan su título y cadena de migas de pan a partir de repositorios,
 * de modo que el shell puede reconstruirse tras una restauración de estado sin depender del
 * historial de clics.
 */
suspend fun resolveNavigationContext(
    entry: NavBackStackEntry?,
    originTopLevel: TopLevelDestination,
    strings: AppStrings,
    folderRepository: FolderRepository,
    packRepository: PackRepository,
    attemptRepository: AttemptRepository,
): NavigationContext {
    val route = entry?.destination?.route
    val args = entry?.arguments
    // Solo usado para rutas estáticas de nivel superior.
    val root = topLevelDestinationForRoute(route)
    val variant = chromeVariantForRoute(route)
    // La miga de pan raíz siempre refleja el origen real, no la ruta actual.
    val rootTrail = rootTrailItem(originTopLevel, strings)

    return when (route) {
        Routes.HOME,
        Routes.LIBRARY,
        Routes.GAME,
        Routes.STATS,
        -> {
            val title = fallbackTitleForRoute(route, strings)
            NavigationContext(
                root = root,
                title = title,
                trail = listOf(rootTrail),
                chromeVariant = variant,
            )
        }

        Routes.PREFERENCES -> {
            NavigationContext(
                root = originTopLevel,
                title = strings.preferences.preferencesTitle,
                trail =
                    listOf(
                        rootTrail,
                        NavigationTrailItem(null, strings.preferences.preferencesTitle, Routes.PREFERENCES),
                    ),
                chromeVariant = variant,
            )
        }

        Routes.Patterns.FOLDER -> {
            val folderId = args?.getString(Routes.ARG_FOLDER_ID)
            val folderPath: List<Folder> =
                if (folderId != null) {
                    folderRepository.getFolderPath(folderId)
                } else {
                    emptyList()
                }
            val title = folderPath.lastOrNull()?.name ?: strings.library.myLibrary
            val trail =
                listOf(rootTrail) +
                    folderPath.map {
                        NavigationTrailItem(it.id, it.name, Routes.folderRoute(it.id))
                    }
            NavigationContext(
                root = originTopLevel,
                title = title,
                trail = trail.ifEmpty { listOf(rootTrail) },
                chromeVariant = variant,
            )
        }

        Routes.Patterns.PACK -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail = buildPackTrail(rootTrail, packPath)
            val title = packPath?.first?.title ?: strings.library.myLibrary
            NavigationContext(
                root = originTopLevel,
                title = title,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.STUDY -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail =
                buildPackTrail(rootTrail, packPath) +
                    NavigationTrailItem(
                        id = packId,
                        label = strings.common.studyModeTitle,
                        route = packId?.let(Routes::studyRoute),
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.common.studyModeTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.STUDY_SESSION -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail =
                buildPackTrail(rootTrail, packPath) +
                    NavigationTrailItem(
                        id = packId,
                        label = strings.common.studyModeTitle,
                        route = packId?.let(Routes::studyRoute),
                    ) +
                    NavigationTrailItem(
                        id = null,
                        label = strings.common.studyContinueStudy,
                        route = packId?.let(Routes::studySessionRoute),
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.common.studyModeTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.STUDY_SUMMARY -> {
            val attemptId = args?.getString(Routes.ARG_ATTEMPT_ID)
            val packId =
                if (attemptId != null) {
                    attemptRepository.getById(attemptId)?.primaryPackId
                } else {
                    null
                }
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail =
                buildPackTrail(rootTrail, packPath) +
                    NavigationTrailItem(
                        id = packId,
                        label = strings.common.studyModeTitle,
                        route = packId?.let(Routes::studyRoute),
                    ) +
                    NavigationTrailItem(
                        id = attemptId,
                        label = strings.common.studySummaryTitle,
                        route = attemptId?.let(Routes::studySummaryRoute),
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.common.studySummaryTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.GAME_INTRO,
        Routes.Patterns.GAME_SESSION,
        -> {
            // Las rutas de juego pueden alcanzarse desde GAME, HOME o LIBRARY.
            // originTopLevel determina cuál pestaña del bottom nav se resalta.
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail =
                buildPackTrail(rootTrail, packPath) +
                    NavigationTrailItem(
                        id = packId,
                        label = strings.common.gameModeTitle,
                        route = packId?.let(Routes::gameIntroRoute),
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.common.gameModeTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.GAME_SUMMARY -> {
            val attemptId = args?.getString(Routes.ARG_ATTEMPT_ID)
            val packId =
                if (attemptId != null) {
                    attemptRepository.getById(attemptId)?.primaryPackId
                } else {
                    null
                }
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail =
                buildPackTrail(rootTrail, packPath) +
                    NavigationTrailItem(
                        id = packId,
                        label = strings.common.gameModeTitle,
                        route = packId?.let(Routes::gameIntroRoute),
                    ) +
                    NavigationTrailItem(
                        id = attemptId,
                        label = strings.common.gameSummaryTitle,
                        route = null,
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.common.gameSummaryTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.QUESTION_CREATE -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail =
                buildPackTrail(rootTrail, packPath) +
                    NavigationTrailItem(
                        id = null,
                        label = strings.question.newQuestionTitle,
                        route = packId?.let(Routes::questionCreateRoute),
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.question.newQuestionTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.QUESTION_EDIT -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val questionId = args?.getString(Routes.ARG_QUESTION_ID)
            val packPath: Pair<Pack, List<Folder>>? =
                if (packId != null) {
                    packRepository.getPackPath(packId)
                } else {
                    null
                }
            val trail =
                buildPackTrail(rootTrail, packPath) +
                    NavigationTrailItem(
                        id = questionId,
                        label = strings.question.editQuestionTitle,
                        route =
                            if (packId != null && questionId != null) {
                                Routes.questionEditRoute(packId, questionId)
                            } else {
                                null
                            },
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.question.editQuestionTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.PACK_STATS -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val trail =
                listOf(rootTrail) +
                    NavigationTrailItem(
                        id = packId,
                        label = strings.statsPack.packStatsTitle,
                        route = packId?.let(Routes::packStatsRoute),
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.statsPack.packStatsTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        Routes.Patterns.PACK_STATS_HELP -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val trail =
                listOf(rootTrail) +
                    NavigationTrailItem(
                        id = packId,
                        label = strings.statsPack.packStatsTitle,
                        route = packId?.let(Routes::packStatsRoute),
                    ) +
                    NavigationTrailItem(
                        id = null,
                        label = strings.statsPack.packStatsHelpTitle,
                        route = packId?.let(Routes::packStatsHelpRoute),
                    )
            NavigationContext(
                root = originTopLevel,
                title = strings.statsPack.packStatsHelpTitle,
                trail = trail,
                chromeVariant = variant,
            )
        }

        else -> {
            fallbackNavigationContext(route, strings)
        }
    }
}

/**
 * Construye la cadena de migas de pan desde la raíz hasta el pack, incluyendo las
 * carpetas intermedias.
 */
private fun buildPackTrail(
    rootTrail: NavigationTrailItem,
    packPath: Pair<Pack, List<Folder>>?,
): List<NavigationTrailItem> {
    if (packPath == null) return listOf(rootTrail)
    val (pack, folders) = packPath
    return listOf(rootTrail) +
        folders.map { NavigationTrailItem(it.id, it.name, Routes.folderRoute(it.id)) } +
        NavigationTrailItem(pack.id, pack.title, Routes.packRoute(pack.id))
}
