package com.petapp.capybara.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.petapp.capybara.calendar.navigation.CalendarNavigationScreen
import com.petapp.capybara.calendar.presentation.CalendarScreen
import com.petapp.capybara.profile.navigation.ProfilesNavigationScreen
import com.petapp.capybara.survey.navigation.SurveyNavigationScreen

fun NavGraphBuilder.calendarGraph(
    navController: NavHostController
) {
    composable(CalendarNavigationScreen.route) {
        CalendarScreen(
            openNewSurveyScreen = {
                navController.navigate(SurveyNavigationScreen.route)
            },
            openProfilesScreen = {
                navController.navigate(ProfilesNavigationScreen.route)
            }
        )
    }
}
