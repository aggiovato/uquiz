package com.uquiz.android.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.navigation.components.NavigationChromeVariant
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Navy50
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.LocalStrings

enum class TopLevelDestination { HOME, LIBRARY, GAME, STATS }

@Composable
fun AppBottomNavBar(
    selected: TopLevelDestination,
    onDestinationClick: (TopLevelDestination) -> Unit,
    variant: NavigationChromeVariant = NavigationChromeVariant.Default
) {
    val strings = LocalStrings.current
    val containerColor = when (variant) {
        NavigationChromeVariant.Default -> Color.White
        NavigationChromeVariant.TransparentLight -> Color.Transparent
    }
    val animatedContainerColor = animateColorAsState(
        targetValue = containerColor,
        animationSpec = tween(durationMillis = 260),
        label = "bottomBarContainer"
    )
    val animatedSelectedIconColor = animateColorAsState(
        targetValue = when (variant) {
            NavigationChromeVariant.Default -> BrandNavy
            NavigationChromeVariant.TransparentLight -> Color.White
        },
        animationSpec = tween(durationMillis = 260),
        label = "bottomBarSelectedIcon"
    )
    val animatedSelectedTextColor = animateColorAsState(
        targetValue = when (variant) {
            NavigationChromeVariant.Default -> BrandNavy
            NavigationChromeVariant.TransparentLight -> Color.White
        },
        animationSpec = tween(durationMillis = 260),
        label = "bottomBarSelectedText"
    )
    val animatedIndicatorColor = animateColorAsState(
        targetValue = when (variant) {
            NavigationChromeVariant.Default -> Navy50
            NavigationChromeVariant.TransparentLight -> Color.White.copy(alpha = 0.14f)
        },
        animationSpec = tween(durationMillis = 260),
        label = "bottomBarIndicator"
    )
    val animatedUnselectedIconColor = animateColorAsState(
        targetValue = when (variant) {
            NavigationChromeVariant.Default -> Neutral500
            NavigationChromeVariant.TransparentLight -> Color.White.copy(alpha = 0.62f)
        },
        animationSpec = tween(durationMillis = 260),
        label = "bottomBarUnselectedIcon"
    )
    val animatedUnselectedTextColor = animateColorAsState(
        targetValue = when (variant) {
            NavigationChromeVariant.Default -> Neutral500
            NavigationChromeVariant.TransparentLight -> Color.White.copy(alpha = 0.62f)
        },
        animationSpec = tween(durationMillis = 260),
        label = "bottomBarUnselectedText"
    )
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = animatedSelectedIconColor.value,
        selectedTextColor = animatedSelectedTextColor.value,
        indicatorColor = animatedIndicatorColor.value,
        unselectedIconColor = animatedUnselectedIconColor.value,
        unselectedTextColor = animatedUnselectedTextColor.value
    )

    NavigationBar(containerColor = animatedContainerColor.value, tonalElevation = 0.dp) {
        NavigationBarItem(
            selected = selected == TopLevelDestination.HOME,
            onClick = { onDestinationClick(TopLevelDestination.HOME) },
            icon = {
                Icon(
                    painter = painterResource(UIcons.Menu.Home),
                    contentDescription = null
                )
            },
            label = { Text(strings.navHome) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selected == TopLevelDestination.LIBRARY,
            onClick = { onDestinationClick(TopLevelDestination.LIBRARY) },
            icon = {
                Icon(
                    painter = painterResource(UIcons.Menu.Library),
                    contentDescription = null
                )
            },
            label = { Text(strings.navLibrary) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selected == TopLevelDestination.GAME,
            onClick = { onDestinationClick(TopLevelDestination.GAME) },
            icon = {
                Icon(
                    painter = painterResource(UIcons.Menu.Game),
                    contentDescription = null
                )
            },
            label = { Text(strings.navGame) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selected == TopLevelDestination.STATS,
            onClick = { onDestinationClick(TopLevelDestination.STATS) },
            icon = {
                Icon(
                    painter = painterResource(UIcons.Menu.Stats),
                    contentDescription = null
                )
            },
            label = { Text(strings.navStats) },
            colors = itemColors
        )
    }
}
