package com.petapp.capybara.profile.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.petapp.capybara.core.navigation.NavigationDirection
import com.petapp.capybara.core.navigation.NavigationDirectionWithArgs


object ProfileNavigation : NavigationDirectionWithArgs {
    override val route = "profile"
    override val typeArg = "profile_type"
    override val routeWithArgs = "${route}/{$typeArg}"
    override val arguments = listOf(
        navArgument(typeArg) { type = NavType.LongType }
    )
}

object ProfilesNavigation : NavigationDirection {
    override val route = "profiles"
}
