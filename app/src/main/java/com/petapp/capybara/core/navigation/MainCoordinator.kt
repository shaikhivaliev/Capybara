package com.petapp.capybara.core.navigation

import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import javax.inject.Inject

class MainCoordinator @Inject constructor(
    private val router: AppRouter
) : IMainCoordinator {

    override fun back() {
        return router.exit()
    }

    override fun openSurvey(survey: Survey?) {
        router.navigateTo(Screens.SurveyScreen(survey))
    }

    override fun openSurveys(typeId: Long) {
        router.navigateTo(Screens.SurveysScreen(typeId))
    }

    override fun openProfile(profile: Profile?) {
        router.navigateTo(Screens.ProfileScreen(profile))
    }

    override fun openProfiles() {
        router.navigateTo(Screens.ProfilesScreen())
    }

    override fun openHealthDiary(profileId: Long?) {
        router.navigateTo(Screens.HealthDiaryScreen(profileId))
    }

    override fun openTypes() {
        router.navigateTo(Screens.TypesScreen())
    }

    override fun openCalendar() {
        router.navigateTo(Screens.CalendarScreen())
    }

    override fun openSettings() {
        router.navigateTo(Screens.SettingsScreen())
    }

}
