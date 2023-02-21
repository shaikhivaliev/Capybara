package com.petapp.capybara.core.navigation

import androidx.navigation.NamedNavArgument

interface NavigationDirection {
    val route: String
    val typeArg: String?
        get() = null
    val routeWithArgs: String?
        get() = null
    val arguments: List<NamedNavArgument>
        get() = emptyList()
}
