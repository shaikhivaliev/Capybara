package com.petapp.capybara.core.navigation

import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey

interface IMainNavigator {
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
