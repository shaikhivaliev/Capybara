package com.petapp.capybara.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.petapp.capybara.profile.navigation.ProfilesNavigationScreen

@Composable
fun RootGraph(
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = ProfilesNavigationScreen.route
    ) {
        settingsGraph(navController, context)
        profileGraph(navController)
        calendarGraph(navController)
        surveyGraph(navController)
    }
}
