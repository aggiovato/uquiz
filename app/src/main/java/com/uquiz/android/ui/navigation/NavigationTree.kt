package com.uquiz.android.ui.navigation

import com.uquiz.android.ui.navigation.components.NavigationChromeVariant
import com.uquiz.android.ui.i18n.AppStrings

const val folderRoutePattern = "${Routes.FOLDER}/{${Routes.ARG_FOLDER_ID}}"
const val packRoutePattern = "${Routes.PACK}/{${Routes.ARG_PACK_ID}}"
const val studyRoutePattern = "${Routes.STUDY}/{${Routes.ARG_PACK_ID}}"
const val studySessionRoutePattern = "${Routes.STUDY_SESSION}/{${Routes.ARG_PACK_ID}}"
const val studySummaryRoutePattern = "${Routes.STUDY_SUMMARY}/{${Routes.ARG_ATTEMPT_ID}}"
const val quickGameRoutePattern = "${Routes.QUICK_GAME}/{${Routes.ARG_PACK_ID}}"
const val questionCreateRoutePattern = "${Routes.QUESTION_CREATE}/{${Routes.ARG_PACK_ID}}"
const val questionEditRoutePattern = "${Routes.QUESTION_EDIT}/{${Routes.ARG_PACK_ID}}/{${Routes.ARG_QUESTION_ID}}"
const val packStatsRoutePattern = "${Routes.PACK_STATS}/{${Routes.ARG_PACK_ID}}"
const val packStatsHelpRoutePattern = "${Routes.PACK_STATS_HELP}/{${Routes.ARG_PACK_ID}}"

val topLevelRoutes = setOf(Routes.HOME, Routes.LIBRARY, Routes.GAME, Routes.STATS)
val studyRoutePatterns = setOf(studyRoutePattern, studySessionRoutePattern, studySummaryRoutePattern)

/** Returns the root branch that should be highlighted for the provided route pattern. */
fun topLevelDestinationForRoute(route: String?): TopLevelDestination = when (route) {
    Routes.HOME -> TopLevelDestination.HOME
    Routes.GAME, quickGameRoutePattern -> TopLevelDestination.GAME
    Routes.STATS, packStatsRoutePattern, packStatsHelpRoutePattern -> TopLevelDestination.STATS
    else -> TopLevelDestination.LIBRARY
}

/** Whether the route pattern points to one of the static top-level roots. */
fun isTopLevelRoute(route: String?): Boolean = route in topLevelRoutes

/** Default chrome variant used by the visible shell for the provided route pattern. */
fun chromeVariantForRoute(route: String?): NavigationChromeVariant =
    if (route in studyRoutePatterns) NavigationChromeVariant.TransparentLight
    else NavigationChromeVariant.Default

/** Root breadcrumb item used as the first visible item in every app trail. */
fun rootTrailItem(root: TopLevelDestination, strings: AppStrings): NavigationTrailItem = when (root) {
    TopLevelDestination.HOME -> NavigationTrailItem(
        id = null,
        label = strings.navHome,
        route = Routes.HOME
    )
    TopLevelDestination.LIBRARY -> NavigationTrailItem(
        id = null,
        label = strings.myLibrary,
        route = Routes.LIBRARY
    )
    TopLevelDestination.GAME -> NavigationTrailItem(
        id = null,
        label = strings.navGame,
        route = Routes.GAME
    )
    TopLevelDestination.STATS -> NavigationTrailItem(
        id = null,
        label = strings.navStats,
        route = Routes.STATS
    )
}

/** Fallback title used while dynamic navigation context is being resolved. */
fun fallbackTitleForRoute(route: String?, strings: AppStrings): String = when (route) {
    Routes.HOME -> strings.navHome
    Routes.LIBRARY -> strings.myLibrary
    Routes.GAME -> strings.navGame
    Routes.STATS -> strings.navStats
    studyRoutePattern, studySessionRoutePattern -> strings.studyModeTitle
    studySummaryRoutePattern -> strings.studySummaryTitle
    quickGameRoutePattern -> strings.quickGameTitle
    questionCreateRoutePattern -> strings.newQuestionTitle
    questionEditRoutePattern -> strings.editQuestionTitle
    packStatsRoutePattern -> strings.packStatsTitle
    packStatsHelpRoutePattern -> strings.packStatsHelpTitle
    Routes.PREFERENCES -> strings.preferencesTitle
    else -> strings.myLibrary
}

/** Minimal fallback context used before dynamic data-backed resolution completes. */
fun fallbackNavigationContext(route: String?, strings: AppStrings): NavigationContext {
    val root = topLevelDestinationForRoute(route)
    return NavigationContext(
        root = root,
        title = fallbackTitleForRoute(route, strings),
        trail = listOf(rootTrailItem(root, strings)),
        chromeVariant = chromeVariantForRoute(route)
    )
}
