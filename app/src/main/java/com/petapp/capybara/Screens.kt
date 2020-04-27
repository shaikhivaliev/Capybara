package com.petapp.capybara

import androidx.fragment.app.FragmentActivity
import com.petapp.capybara.ui.allsurveys.AllSurveysFragment
import com.petapp.capybara.ui.calendar.CalendarFragment
import com.petapp.capybara.ui.main.MainFragment
import com.petapp.capybara.ui.newprofile.NewProfileFragment
import com.petapp.capybara.ui.types.TypesFragment
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    private val cicerone = Cicerone.create()
    private val router = cicerone.router
    private val navigatorHolder = cicerone.navigatorHolder
    fun navigateTo(context: FragmentActivity, screen: Screen, container: Int) {
        val navigator = SupportAppNavigator(context, container)
        navigatorHolder.setNavigator(navigator)
        router.navigateTo(screen)
    }

    object Main : SupportAppScreen() {
        override fun getFragment() = MainFragment()
    }

    object NewProfile : SupportAppScreen() {
        override fun getFragment() = NewProfileFragment()
    }

    object Calendar : SupportAppScreen() {
        override fun getFragment() = CalendarFragment()
    }

    object Types : SupportAppScreen() {
        override fun getFragment() = TypesFragment()
    }

    object AllSurvey : SupportAppScreen() {
        override fun getFragment() = AllSurveysFragment()
    }
}