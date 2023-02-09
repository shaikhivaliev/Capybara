package com.petapp.capybara.navigation

import androidx.navigation.NavController
import com.petapp.capybara.R
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey

class MainNavigatorImpl(
    private val navControllerProvider: NavControllerProvider
) : MainNavigator {

    private val navController: NavController
        get() = navControllerProvider.getNavController(R.id.nav_host_home)

    override fun back(): Boolean {
        return navController.popBackStack()
    }

    override fun openSurvey(survey: Survey) {
        navController.navigate(R.id.to_survey, SurveyNavDto(survey))
    }

    override fun openNewSurvey() {
        navController.navigate(R.id.to_survey)
    }

    override fun openSurveys(typeId: Long) {
        navController.navigate(R.id.to_surveys, LongNavDto(typeId))
    }

    override fun openProfile(profile: Profile) {
        navController.navigate(R.id.to_profile, ProfileNavDto(profile))
    }

    override fun openNewProfile() {
        navController.navigate(R.id.to_profile)
    }

    override fun openProfiles() {
        navController.navigate(R.id.to_profiles)
    }

    override fun openHealthDiary(profileId: Long) {
        navController.navigate(R.id.to_health_diary, LongNavDto(profileId))
    }

    override fun openTypes() {
        navController.navigate(R.id.to_types)
    }
}
