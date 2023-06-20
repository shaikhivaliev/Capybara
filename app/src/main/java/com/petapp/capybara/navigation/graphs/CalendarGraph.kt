package com.petapp.capybara.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.petapp.capybara.calendar.navigation.CalendarNavigation
import com.petapp.capybara.calendar.presentation.CalendarScreen
import com.petapp.capybara.core.navigation.navigateWithPopUp
import com.petapp.capybara.profile.navigation.ProfilesNavigation
import com.petapp.capybara.survey.navigation.SurveyNavigation

fun NavGraphBuilder.calendarGraph(
    navController: NavHostController
) {
    composable(CalendarNavigation.route) {
        CalendarScreen(
            openNewSurveyScreen = {
                navController.navigate(SurveyNavigation.route)
            },
            openProfilesScreen = {
                navController.navigate(ProfilesNavigation.route)
            }
        )
    }
}
