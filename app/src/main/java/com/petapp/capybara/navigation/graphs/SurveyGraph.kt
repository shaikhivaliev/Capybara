package com.petapp.capybara.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.petapp.capybara.healthdiary.navigation.HealthDiaryNavigationScreen
import com.petapp.capybara.healthdiary.presentation.HealthDiaryScreen
import com.petapp.capybara.profile.navigation.ProfilesNavigationScreen
import com.petapp.capybara.survey.navigation.SurveyNavigationScreen
import com.petapp.capybara.survey.navigation.SurveysNavigationScreen
import com.petapp.capybara.survey.presentation.SurveyScreen
import com.petapp.capybara.survey.presentation.SurveysScreen
import com.petapp.capybara.types.TypesScreen
import com.petapp.capybara.types.navigation.TypesNavigationScreen

fun NavGraphBuilder.surveyGraph(
    navController: NavHostController
) {
    composable(HealthDiaryNavigationScreen.route) {
        HealthDiaryScreen(
            openProfilesScreen = {
                navController.navigate(ProfilesNavigationScreen.route)
            }
        )
    }

    composable(SurveyNavigationScreen.route) {
        SurveyScreen()
    }

    composable(SurveysNavigationScreen.route) {
        SurveysScreen(
            openProfilesScreen = {
                navController.navigate(ProfilesNavigationScreen.route)
            },
            openNewSurveyScreen = {
                navController.navigate(SurveyNavigationScreen.route)
            },
            openSurveyScreen = {
                navController.navigate(SurveyNavigationScreen.route)
            }
        )
    }

    composable(TypesNavigationScreen.route) {
        TypesScreen(
            openHealthDiary = {
                navController.navigate(HealthDiaryNavigationScreen.route)
            },
            openSurveysScreen = {
                navController.navigate(SurveysNavigationScreen.route)
            }
        )
    }
}

