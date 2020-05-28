package com.petapp.capybara

import com.petapp.calendar.navigation.CalendarFragmentProviderImpl
import com.petapp.core_api.navigation.FragmentProvider
import com.petapp.main.navigation.MainFragmentProviderImpl
import com.petapp.profiles.navigation.ProfileFragmentProviderImpl
import com.petapp.profiles.navigation.ProfilesFragmentProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class NavigationMediator {

    @Binds
    @Named("main")
    abstract fun createMainFragment(fragment: MainFragmentProviderImpl): FragmentProvider

    @Binds
    @Named("profile")
    abstract fun createProfileFragment(fragment: ProfileFragmentProviderImpl): FragmentProvider

    @Binds
    @Named("profiles")
    abstract fun createProfilesFragment(fragment: ProfilesFragmentProviderImpl): FragmentProvider

    @Binds
    @Named("calendar")
    abstract fun createCalendarFragment(fragment: CalendarFragmentProviderImpl): FragmentProvider

}