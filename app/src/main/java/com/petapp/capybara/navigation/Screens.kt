package com.petapp.capybara.navigation

import com.petapp.capybara.calendar.CalendarFragment
import com.petapp.capybara.main.MainFragment
import com.petapp.capybara.profiles.presentation.profile.ProfileFragment
import com.petapp.capybara.profiles.presentation.profiles.ProfilesFragment
import com.petapp.capybara.surveys.SurveysFragment
import com.petapp.capybara.types.TypesFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object Main : SupportAppScreen() {
        override fun getFragment() = MainFragment()
    }

    object Profiles : SupportAppScreen() {
        override fun getFragment() = ProfilesFragment()
    }

    data class Profile(
        val profileId: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProfileFragment.create(profileId)
    }


    object Calendar : SupportAppScreen() {
        override fun getFragment() = CalendarFragment()
    }

    object Types : SupportAppScreen() {
        override fun getFragment() = TypesFragment()
    }

    object Surveys : SupportAppScreen() {
        override fun getFragment() = SurveysFragment()
    }
}