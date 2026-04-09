package com.uquiz.android.ui.navigation

import androidx.navigation.NavBackStackEntry
import com.uquiz.android.domain.content.model.Folder
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.ui.i18n.AppStrings

/**
 * Resolves the visible app navigation context from a back stack entry and domain data.
 *
 * Dynamic routes derive their title and breadcrumb trail from repository data so the
 * shell can be reconstructed after state restoration without relying on click history.
 */
suspend fun resolveNavigationContext(
    entry: NavBackStackEntry?,
    strings: AppStrings,
    folderRepository: FolderRepository,
    packRepository: PackRepository,
    attemptRepository: AttemptRepository
): NavigationContext {
    val route = entry?.destination?.route
    val args = entry?.arguments
    val root = topLevelDestinationForRoute(route)
    val variant = chromeVariantForRoute(route)
    val rootTrail = rootTrailItem(root, strings)

    return when (route) {
        Routes.HOME,
        Routes.LIBRARY,
        Routes.GAME,
        Routes.STATS -> {
            val title = fallbackTitleForRoute(route, strings)
            NavigationContext(
                root = root,
                title = title,
                trail = listOf(rootTrail),
                chromeVariant = variant
            )
        }

        Routes.PREFERENCES -> {
            NavigationContext(
                root = TopLevelDestination.HOME,
                title = strings.preferencesTitle,
                trail = listOf(
                    rootTrailItem(TopLevelDestination.HOME, strings),
                    NavigationTrailItem(null, strings.preferencesTitle, Routes.PREFERENCES)
                ),
                chromeVariant = variant
            )
        }

        folderRoutePattern -> {
            val folderId = args?.getString(Routes.ARG_FOLDER_ID)
            val folderPath: List<Folder> = if (folderId != null) {
                folderRepository.getFolderPath(folderId)
            } else {
                emptyList()
            }
            val title = folderPath.lastOrNull()?.name ?: strings.myLibrary
            val trail = listOf(rootTrail) + folderPath.map {
                NavigationTrailItem(it.id, it.name, Routes.folderRoute(it.id))
            }
            NavigationContext(
                root = TopLevelDestination.LIBRARY,
                title = title,
                trail = trail.ifEmpty { listOf(rootTrail) },
                chromeVariant = variant
            )
        }

        packRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? = if (packId != null) {
                packRepository.getPackPath(packId)
            } else {
                null
            }
            val trail = buildLibraryPackTrail(rootTrail, packPath)
            val title = packPath?.first?.title ?: strings.myLibrary
            NavigationContext(
                root = TopLevelDestination.LIBRARY,
                title = title,
                trail = trail,
                chromeVariant = variant
            )
        }

        studyRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? = if (packId != null) {
                packRepository.getPackPath(packId)
            } else {
                null
            }
            val trail = buildLibraryPackTrail(rootTrail, packPath) + NavigationTrailItem(
                id = packId,
                label = strings.studyModeTitle,
                route = packId?.let(Routes::studyRoute)
            )
            NavigationContext(
                root = TopLevelDestination.LIBRARY,
                title = strings.studyModeTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        studySessionRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? = if (packId != null) {
                packRepository.getPackPath(packId)
            } else {
                null
            }
            val trail = buildLibraryPackTrail(rootTrail, packPath) +
                NavigationTrailItem(
                    id = packId,
                    label = strings.studyModeTitle,
                    route = packId?.let(Routes::studyRoute)
                ) +
                NavigationTrailItem(
                    id = null,
                    label = strings.studyContinueStudy,
                    route = packId?.let(Routes::studySessionRoute)
                )
            NavigationContext(
                root = TopLevelDestination.LIBRARY,
                title = strings.studyModeTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        studySummaryRoutePattern -> {
            val attemptId = args?.getString(Routes.ARG_ATTEMPT_ID)
            val packId = if (attemptId != null) {
                attemptRepository.getById(attemptId)?.primaryPackId
            } else {
                null
            }
            val packPath: Pair<Pack, List<Folder>>? = if (packId != null) {
                packRepository.getPackPath(packId)
            } else {
                null
            }
            val trail = buildLibraryPackTrail(rootTrail, packPath) +
                NavigationTrailItem(
                    id = packId,
                    label = strings.studyModeTitle,
                    route = packId?.let(Routes::studyRoute)
                ) +
                NavigationTrailItem(
                    id = attemptId,
                    label = strings.studySummaryTitle,
                    route = attemptId?.let(Routes::studySummaryRoute)
                )
            NavigationContext(
                root = TopLevelDestination.LIBRARY,
                title = strings.studySummaryTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        quickGameRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val pack: Pack? = if (packId != null) {
                packRepository.getById(packId)
            } else {
                null
            }
            val trail = listOf(rootTrail) +
                listOfNotNull(
                    pack?.let { NavigationTrailItem(it.id, it.title, Routes.packRoute(it.id)) },
                    NavigationTrailItem(null, strings.quickGameTitle, packId?.let(Routes::quickGameRoute))
                )
            NavigationContext(
                root = TopLevelDestination.GAME,
                title = strings.quickGameTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        questionCreateRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val packPath: Pair<Pack, List<Folder>>? = if (packId != null) {
                packRepository.getPackPath(packId)
            } else {
                null
            }
            val trail = buildLibraryPackTrail(rootTrail, packPath) + NavigationTrailItem(
                id = null,
                label = strings.newQuestionTitle,
                route = packId?.let(Routes::questionCreateRoute)
            )
            NavigationContext(
                root = TopLevelDestination.LIBRARY,
                title = strings.newQuestionTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        questionEditRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val questionId = args?.getString(Routes.ARG_QUESTION_ID)
            val packPath: Pair<Pack, List<Folder>>? = if (packId != null) {
                packRepository.getPackPath(packId)
            } else {
                null
            }
            val trail = buildLibraryPackTrail(rootTrail, packPath) + NavigationTrailItem(
                id = questionId,
                label = strings.editQuestionTitle,
                route = if (packId != null && questionId != null) Routes.questionEditRoute(packId, questionId) else null
            )
            NavigationContext(
                root = TopLevelDestination.LIBRARY,
                title = strings.editQuestionTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        packStatsHelpRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val trail = listOf(rootTrailItem(TopLevelDestination.STATS, strings)) + NavigationTrailItem(
                id = packId,
                label = strings.packStatsTitle,
                route = packId?.let(Routes::packStatsRoute)
            ) + NavigationTrailItem(
                id = null,
                label = strings.packStatsHelpTitle,
                route = packId?.let(Routes::packStatsHelpRoute)
            )
            NavigationContext(
                root = TopLevelDestination.STATS,
                title = strings.packStatsHelpTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        packStatsRoutePattern -> {
            val packId = args?.getString(Routes.ARG_PACK_ID)
            val trail = listOf(rootTrailItem(TopLevelDestination.STATS, strings)) + NavigationTrailItem(
                id = packId,
                label = strings.packStatsTitle,
                route = packId?.let(Routes::packStatsRoute)
            )
            NavigationContext(
                root = TopLevelDestination.STATS,
                title = strings.packStatsTitle,
                trail = trail,
                chromeVariant = variant
            )
        }

        else -> fallbackNavigationContext(route, strings)
    }
}

private fun buildLibraryPackTrail(
    rootTrail: NavigationTrailItem,
    packPath: Pair<Pack, List<Folder>>?
): List<NavigationTrailItem> {
    if (packPath == null) return listOf(rootTrail)
    val (pack, folders) = packPath
    return listOf(rootTrail) +
        folders.map { NavigationTrailItem(it.id, it.name, Routes.folderRoute(it.id)) } +
        NavigationTrailItem(pack.id, pack.title, Routes.packRoute(pack.id))
}
