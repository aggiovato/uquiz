package com.uquiz.android.ui.navigation.tree

import com.uquiz.android.ui.i18n.AppStrings
import com.uquiz.android.ui.navigation.Routes
import com.uquiz.android.ui.navigation.chrome.NavigationChromeVariant

/** Rutas que corresponden a los destinos raíz del bottom navigation bar. */
val topLevelRoutes = setOf(Routes.HOME, Routes.LIBRARY, Routes.GAME, Routes.STATS)

/** Patrones de ruta del flujo de estudio, usados para detectar rutas inmersivas. */
val studyRoutePatterns =
    setOf(
        Routes.Patterns.STUDY,
        Routes.Patterns.STUDY_SESSION,
        Routes.Patterns.STUDY_SUMMARY,
    )

/** Patrones de ruta del flujo de juego, usados para detectar rutas inmersivas. */
val gameRoutePatterns =
    setOf(
        Routes.Patterns.GAME_INTRO,
        Routes.Patterns.GAME_SESSION,
        Routes.Patterns.GAME_SUMMARY,
    )

/** Devuelve la pestaña raíz que debe resaltarse para la ruta estática proporcionada. */
fun topLevelDestinationForRoute(route: String?): TopLevelDestination =
    when (route) {
        Routes.HOME -> TopLevelDestination.HOME

        Routes.GAME,
        Routes.Patterns.GAME_INTRO,
        Routes.Patterns.GAME_SESSION,
        Routes.Patterns.GAME_SUMMARY,
        -> TopLevelDestination.GAME

        Routes.STATS,
        Routes.Patterns.PACK_STATS,
        Routes.Patterns.PACK_STATS_HELP,
        -> TopLevelDestination.STATS

        else -> TopLevelDestination.LIBRARY
    }

/** Indica si el patrón de ruta corresponde a uno de los destinos raíz estáticos. */
fun isTopLevelRoute(route: String?): Boolean = route in topLevelRoutes

/** Variante de chrome por defecto para el shell compartido en la ruta indicada. */
fun chromeVariantForRoute(route: String?): NavigationChromeVariant =
    if (route in studyRoutePatterns || route in gameRoutePatterns) {
        NavigationChromeVariant.TransparentLight
    } else {
        NavigationChromeVariant.Default
    }

/** Elemento raíz de la cadena de migas de pan, asociado al destino de nivel superior activo. */
fun rootTrailItem(
    root: TopLevelDestination,
    strings: AppStrings,
): NavigationTrailItem =
    when (root) {
        TopLevelDestination.HOME -> {
            NavigationTrailItem(
                id = null,
                label = strings.nav.navHome,
                route = Routes.HOME,
            )
        }

        TopLevelDestination.LIBRARY -> {
            NavigationTrailItem(
                id = null,
                label = strings.library.myLibrary,
                route = Routes.LIBRARY,
            )
        }

        TopLevelDestination.GAME -> {
            NavigationTrailItem(
                id = null,
                label = strings.nav.navGame,
                route = Routes.GAME,
            )
        }

        TopLevelDestination.STATS -> {
            NavigationTrailItem(
                id = null,
                label = strings.nav.navStats,
                route = Routes.STATS,
            )
        }
    }

/** Título de fallback mostrado mientras se resuelve el contexto de navegación dinámico. */
fun fallbackTitleForRoute(
    route: String?,
    strings: AppStrings,
): String =
    when (route) {
        // Las pantallas raíz usan títulos distintos a sus etiquetas de breadcrumb para
        // evitar la repetición visual entre top bar y barra de migas de pan.
        Routes.HOME -> "UQuiz"

        Routes.LIBRARY -> strings.common.studyModeTitle

        Routes.GAME -> strings.common.gameModeTitle

        Routes.STATS -> strings.nav.personalStats

        Routes.Patterns.STUDY, Routes.Patterns.STUDY_SESSION -> strings.common.studyModeTitle

        Routes.Patterns.STUDY_SUMMARY -> strings.common.studySummaryTitle

        Routes.Patterns.GAME_INTRO,
        Routes.Patterns.GAME_SESSION,
        -> strings.common.gameModeTitle

        Routes.Patterns.GAME_SUMMARY,
        -> strings.common.gameSummaryTitle

        Routes.Patterns.QUESTION_CREATE -> strings.question.newQuestionTitle

        Routes.Patterns.QUESTION_EDIT -> strings.question.editQuestionTitle

        Routes.Patterns.PACK_STATS -> strings.statsPack.packStatsTitle

        Routes.Patterns.PACK_STATS_HELP -> strings.statsPack.packStatsHelpTitle

        Routes.PREFERENCES -> strings.preferences.preferencesTitle

        else -> strings.library.myLibrary
    }

/** Contexto mínimo de fallback usado antes de que la resolución basada en datos termine. */
fun fallbackNavigationContext(
    route: String?,
    strings: AppStrings,
): NavigationContext {
    val root = topLevelDestinationForRoute(route)
    return NavigationContext(
        root = root,
        title = fallbackTitleForRoute(route, strings),
        trail = listOf(rootTrailItem(root, strings)),
        chromeVariant = chromeVariantForRoute(route),
    )
}
