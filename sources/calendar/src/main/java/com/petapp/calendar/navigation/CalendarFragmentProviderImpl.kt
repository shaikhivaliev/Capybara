package com.petapp.calendar.navigation

import androidx.fragment.app.Fragment
import com.petapp.calendar.presentation.CalendarFragment
import com.petapp.core_api.navigation.FragmentProvider
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class CalendarFragmentProviderImpl @Inject constructor() : FragmentProvider {

    override fun createScreen(): Screen {

        return object : SupportAppScreen() {
            override fun getFragment(): Fragment? {
                return CalendarFragment()
            }
        }
    }
}
