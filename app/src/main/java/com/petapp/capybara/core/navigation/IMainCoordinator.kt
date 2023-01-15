package com.petapp.capybara.core.navigation

import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey

interface IMainCoordinator {
    fun back()
    fun openSurvey(survey: Survey?)
    fun openSurveys(typeId: Long)
    fun openProfile(profile: Profile?)
    fun openProfiles()
    fun openHealthDiary(profileId: Long?)
    fun openTypes()
    fun openCalendar()
    fun openSettings()
}
