package com.petapp.capybara.core.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.presentation.calendar.CalendarFragment
import com.petapp.capybara.presentation.healthDiary.HealthDiaryFragment
import com.petapp.capybara.presentation.profile.ProfileFragment
import com.petapp.capybara.presentation.profiles.ProfilesFragment
import com.petapp.capybara.presentation.settings.SettingsFragment
import com.petapp.capybara.presentation.survey.SurveyFragment
import com.petapp.capybara.presentation.surveys.SurveysFragment
import com.petapp.capybara.presentation.types.TypesFragment

class Screens {

    class TypesScreen : BaseScreen() {
        override fun getFragmentInstance(): Fragment = TypesFragment()
    }

    class CalendarScreen : BaseScreen() {
        override fun getFragmentInstance(): Fragment = CalendarFragment()
    }

    class ProfilesScreen : BaseScreen() {
        override fun getFragmentInstance(): Fragment = ProfilesFragment()
    }

    class SettingsScreen : BaseScreen() {
        override fun getFragmentInstance(): Fragment = SettingsFragment()
    }

    class HealthDiaryScreen(profileId: Long?) : BaseScreen() {
        init {
            args = bundleOf(
                EXTRA_NAVIGATION_DTO to profileId
            )
        }
        override fun getFragmentInstance(): Fragment = HealthDiaryFragment()
    }

    class ProfileScreen(profile: Profile?) : BaseScreen() {
        init {
            args = bundleOf(
                EXTRA_NAVIGATION_DTO to profile
            )
        }
        override fun getFragmentInstance(): Fragment = ProfileFragment()
    }

    class SurveyScreen(survey: Survey?) : BaseScreen() {
        init {
            args = bundleOf(
                EXTRA_NAVIGATION_DTO to survey
            )
        }
        override fun getFragmentInstance(): Fragment = SurveyFragment()
    }

    class SurveysScreen(typeId: Long) : BaseScreen() {
        init {
            args = bundleOf(
                EXTRA_NAVIGATION_DTO to typeId
            )
        }
        override fun getFragmentInstance(): Fragment = SurveysFragment()
    }
}
