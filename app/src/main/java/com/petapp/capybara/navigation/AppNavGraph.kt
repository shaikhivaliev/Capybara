package com.petapp.capybara.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.petapp.capybara.calendar.CalendarScreen
import com.petapp.capybara.calendar.navigation.CalendarNavigationScreen
import com.petapp.capybara.healthdiary.HealthDiaryScreen
import com.petapp.capybara.healthdiary.navigation.HealthDiaryNavigationScreen
import com.petapp.capybara.profile.ProfileScreen
import com.petapp.capybara.profile.ProfilesScreen
import com.petapp.capybara.profile.navigation.ProfileNavigationScreen
import com.petapp.capybara.profile.navigation.ProfilesNavigationScreen
import com.petapp.capybara.setting.SettingsScreen
import com.petapp.capybara.setting.navigation.SettingNavigationScreen
import com.petapp.capybara.survey.SurveyScreen
import com.petapp.capybara.survey.SurveysScreen
import com.petapp.capybara.survey.navigation.SurveyNavigationScreen
import com.petapp.capybara.survey.navigation.SurveysNavigationScreen
import com.petapp.capybara.types.TypesScreen
import com.petapp.capybara.types.navigation.TypesNavigationScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ProfilesNavigationScreen.route
    ) {
        composable(CalendarNavigationScreen.route) {
            CalendarScreen(
                openNewSurveyScreen = {},
                openProfileScreen = {}
            )
        }
        composable(HealthDiaryNavigationScreen.route) {
            HealthDiaryScreen()
        }
        composable(ProfileNavigationScreen.route) {
            ProfileScreen(
                openProfilesScreen = {}
            )
        }
        composable(ProfilesNavigationScreen.route) {
            ProfilesScreen(
                openNewProfile = {},
                openProfileScreen = {}
            )
        }
        composable(SettingNavigationScreen.route) {
            SettingsScreen()
        }

        composable(SurveyNavigationScreen.route) {
            SurveyScreen()
        }

        composable(SurveysNavigationScreen.route) {
            SurveysScreen(
                openNewSurveyScreen = {},
                openSurveyScreen = {}
            )
        }

        composable(TypesNavigationScreen.route) {
            TypesScreen(
                openHealthDiary = {},
                openSurveysScreen = {}
            )
        }
    }
}
