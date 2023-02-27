package com.petapp.capybara.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.petapp.capybara.healthdiary.navigation.HealthDiaryNavigation
import com.petapp.capybara.healthdiary.presentation.HealthDiaryScreen
import com.petapp.capybara.profile.navigation.ProfilesNavigation
import com.petapp.capybara.survey.navigation.SurveyNavigation
import com.petapp.capybara.survey.navigation.SurveysNavigation
import com.petapp.capybara.survey.presentation.SurveyScreen
import com.petapp.capybara.survey.presentation.SurveysScreen
import com.petapp.capybara.types.navigation.TypesNavigation
import com.petapp.capybara.types.presentation.TypesScreen

fun NavGraphBuilder.surveyGraph(
    navController: NavHostController
) {
    composable(HealthDiaryNavigation.route) {
        HealthDiaryScreen(
            openProfilesScreen = {
                navController.navigate(ProfilesNavigation.route)
            }
        )
    }

    composable(route = SurveyNavigation.route) {
        SurveyScreen(
            openTypes = {
                navController.navigate(TypesNavigation.route)
            }
        )
    }

    composable(
        route = SurveyNavigation.routeWithArgs,
        arguments = SurveyNavigation.arguments
    ) { navBackStackEntry ->
        val surveyId = navBackStackEntry.arguments?.getLong(SurveyNavigation.typeArg)
        SurveyScreen(
            surveyId = surveyId,
            openTypes = { navController.navigate(TypesNavigation.route) }
        )
    }

    composable(
        route = SurveysNavigation.routeWithArgs,
        arguments = SurveysNavigation.arguments
    ) { navBackStackEntry ->
        val typeId = navBackStackEntry.arguments?.getLong(SurveysNavigation.typeArg)
        SurveysScreen(
            typeId = typeId,
            openProfilesScreen = {
                navController.navigate(ProfilesNavigation.route)
            },
            openNewSurveyScreen = {
                navController.navigate(SurveyNavigation.route)
            },
            openSurveyScreen = {
                navController.navigate("${SurveyNavigation.route}/${it}")
            }
        )
    }

    composable(TypesNavigation.route) {
        TypesScreen(
            openHealthDiary = {
                navController.navigate(HealthDiaryNavigation.route)
            },
            openSurveysScreen = {
                navController.navigate("${SurveysNavigation.route}/${it}")
            }
        )
    }
}

