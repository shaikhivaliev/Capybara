package com.petapp.capybara.survey.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.petapp.capybara.core.navigation.NavigationDirection

object SurveyNavigation : NavigationDirection {
    override val route = "survey"
    override val typeArg = "survey_type"
    override val routeWithArgs = "${route}/{$typeArg}"
    override val arguments = listOf(
        navArgument(typeArg) { type = NavType.LongType }
    )
}

object SurveysNavigation : NavigationDirection {
    override val route = "surveys"
    override val typeArg = "surveys_type"
    override val routeWithArgs = "${route}/{$typeArg}"
    override val arguments = listOf(
        navArgument(typeArg) { this.type = NavType.LongType }
    )
}

