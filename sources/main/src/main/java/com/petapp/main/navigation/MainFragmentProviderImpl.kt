package com.petapp.main.navigation

import androidx.fragment.app.Fragment
import com.petapp.core_api.navigation.FragmentProvider
import com.petapp.main.presentation.MainFragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFragmentProviderImpl: FragmentProvider {

    override fun createScreen(): Screen {

        return object : SupportAppScreen() {
            override fun getFragment(): Fragment? {
                return MainFragment()
            }
        }
    }
}
