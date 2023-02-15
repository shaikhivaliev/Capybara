package com.petapp.capybara.core

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable

interface NavigationScreen {
    val route: String
}

interface NavigationScreenWithArgument<T : NavigationArgument> : NavigationScreen {
    val navType: NavType<T>
}

const val NAVIGATION_ARGUMENT_KEY = "argument"

interface NavigationArgument : Parcelable

fun NavController.navigateTo(screen: NavigationScreen, navOptions: NavOptions? = null) {
    this.navigate(screen.route, navOptions)
}

fun NavController.popUpTo(screen: NavigationScreen) {
    val navOptions = NavOptions
        .Builder()
        .setPopUpTo(route = this.currentDestination?.route, inclusive = true)
        .build()

    this.navigateTo(screen, navOptions)
}

fun NavGraphBuilder.composable(
    navigationScreen: NavigationScreen,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    this.composable(
        route = navigationScreen.route,
        content = content
    )
}
