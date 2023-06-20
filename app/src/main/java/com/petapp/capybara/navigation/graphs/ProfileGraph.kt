package com.petapp.capybara.navigation.graphs

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.petapp.capybara.core.navigation.navigateWithPopUp
import com.petapp.capybara.profile.navigation.ProfileNavigation
import com.petapp.capybara.profile.navigation.ProfilesNavigation
import com.petapp.capybara.profile.presentation.ProfileScreen
import com.petapp.capybara.profile.presentation.ProfilesScreen


fun NavGraphBuilder.profileGraph(
    navController: NavHostController
) {
    composable(
        route = ProfileNavigation.routeWithArgs,
        arguments = ProfileNavigation.arguments
    ) { navBackStackEntry ->
        val profileId = navBackStackEntry.arguments?.getLong(ProfileNavigation.typeArg)
        ProfileScreen(
            profileId = profileId,
            openProfilesScreen = {
                navController.navigate(ProfilesNavigation.route)
            }
        )
    }
    composable(route = ProfileNavigation.route) {
        ProfileScreen(
            openProfilesScreen = {
                navController.navigate(ProfilesNavigation.route)
            }
        )
    }
    composable(route = ProfilesNavigation.route) {
        ProfilesScreen(
            openNewProfile = {
                navController.navigateWithPopUp(ProfileNavigation.route, ProfilesNavigation.route)
            },
            openProfileScreen = {
                navController.navigateWithPopUp("${ProfileNavigation.route}/${it}", ProfilesNavigation.route)
            }
        )
    }
}


