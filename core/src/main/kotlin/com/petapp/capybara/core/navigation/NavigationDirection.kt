package com.petapp.capybara.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController


interface NavigationDirection {
    val route: String
}
interface NavigationDirectionWithArgs: NavigationDirection {
    val typeArg: String
    val routeWithArgs: String
    val arguments: List<NamedNavArgument>
}

fun NavHostController.navigateWithPopUp(
    toRoute: String,
    fromRoute: String
) {
    this.navigate(toRoute) {
        popUpTo(fromRoute) {
            inclusive = false
        }
    }
}
