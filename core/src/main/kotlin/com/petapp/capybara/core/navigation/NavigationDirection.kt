package com.petapp.capybara.core.navigation

import androidx.navigation.NamedNavArgument


interface NavigationDirection {
    val route: String
}
interface NavigationDirectionWithArgs: NavigationDirection {
    val typeArg: String
    val routeWithArgs: String
    val arguments: List<NamedNavArgument>
}
