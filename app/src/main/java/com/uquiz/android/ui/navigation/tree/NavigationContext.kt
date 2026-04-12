package com.uquiz.android.ui.navigation.tree

import com.uquiz.android.ui.navigation.chrome.NavigationChromeVariant

/**
 * Visible navigation shell state derived from the current destination.
 *
 * This model is the single source of truth for title, breadcrumb trail, and root
 * section selection in the shared app scaffold.
 */
data class NavigationContext(
    val root: TopLevelDestination,
    val title: String,
    val trail: List<NavigationTrailItem>,
    val chromeVariant: NavigationChromeVariant,
)

/**
 * Single breadcrumb token shown in the shared navigation trail.
 *
 * @property id Optional entity identifier associated with the breadcrumb.
 * @property label Visible breadcrumb label.
 * @property route Optional route that can be used for future breadcrumb navigation.
 */
data class NavigationTrailItem(
    val id: String?,
    val label: String,
    val route: String?,
)
