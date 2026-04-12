package com.uquiz.android.ui.navigation.chrome

import androidx.compose.ui.graphics.Color
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Neutral50

/**
 * Visual configuration for the shared application chrome.
 *
 * The navigation shell owns this state and derives it from the current route.
 * Child composable should consume the values without making additional route-based
 * decisions so transitions stay synchronized across the whole scaffold.
 *
 * @property variant Chrome variant applied to top bar, breadcrumbs, and bottom bar.
 * @property rootBackgroundColor Background color behind the whole scaffold.
 * @property backgroundImageAlpha Alpha used for the standard app background illustration.
 * @property showBreadcrumbs Whether the breadcrumb bar should be visible for the current route.
 */
data class NavigationChromeState(
    val variant: NavigationChromeVariant,
    val rootBackgroundColor: Color,
    val backgroundImageAlpha: Float,
    val showBreadcrumbs: Boolean,
)

/**
 * Resolves the shell chrome state for the current route.
 *
 * Study and game flows reuse the same structural chrome as the rest of the app but
 * switch to a transparent light variant so the underlying mode-specific background
 * remains visible behind the scaffold.
 */
fun navigationChromeStateForRoute(
    route: String?,
    studyRoutes: Set<String>,
    gameRoutes: Set<String> = emptySet(),
): NavigationChromeState {
    val isImmersiveRoute = route in studyRoutes || route in gameRoutes
    return NavigationChromeState(
        variant =
            if (isImmersiveRoute) {
                NavigationChromeVariant.TransparentLight
            } else {
                NavigationChromeVariant.Default
            },
        rootBackgroundColor = if (isImmersiveRoute) BrandNavy else Neutral50,
        backgroundImageAlpha = if (isImmersiveRoute) 0f else 0.5f,
        showBreadcrumbs = true,
    )
}
