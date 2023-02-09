package com.petapp.capybara.navigation

import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey

interface MainNavigator {
    fun back(): Boolean
    fun openSurveys(typeId: Long)
    fun openSurvey(survey: Survey)
    fun openNewSurvey()
    fun openProfile(profile: Profile)
    fun openNewProfile()
    fun openProfiles()
    fun openHealthDiary(profileId: Long)
    fun openTypes()
}
