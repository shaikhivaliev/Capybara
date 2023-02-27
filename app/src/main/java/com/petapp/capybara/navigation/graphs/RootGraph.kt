package com.petapp.capybara.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.petapp.capybara.profile.navigation.ProfilesNavigation

@Composable
fun RootGraph(
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = ProfilesNavigation.route
    ) {
        settingsGraph(context)
        profileGraph(navController)
        calendarGraph(navController)
        surveyGraph(navController)
    }
}
