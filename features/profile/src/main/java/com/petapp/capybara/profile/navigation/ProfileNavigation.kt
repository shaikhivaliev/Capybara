package com.petapp.capybara.profile.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.petapp.capybara.core.navigation.NavigationDirection


object ProfileNavigation : NavigationDirection {
    override val route = "profile"
    override val typeArg = "profile_type"
    override val routeWithArgs = "${route}/{$typeArg}"
    override val arguments = listOf(
        navArgument(typeArg) { type = NavType.LongType }
    )
}

object ProfilesNavigationScreen : NavigationDirection {
    override val route = "profiles"
}

