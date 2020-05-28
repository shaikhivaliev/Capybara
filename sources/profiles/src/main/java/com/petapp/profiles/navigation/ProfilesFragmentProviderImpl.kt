package com.petapp.profiles.navigation

import androidx.fragment.app.Fragment
import com.petapp.core_api.navigation.FragmentProvider
import com.petapp.profiles.presentation.profile.ProfileFragment
import com.petapp.profiles.presentation.profiles.ProfilesFragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class ProfilesFragmentProviderImpl @Inject constructor(): FragmentProvider {

    override fun createScreen(): Screen {

        return object : SupportAppScreen() {
            override fun getFragment(): Fragment? {
                return ProfilesFragment()
            }
        }
    }
}
