package com.petapp.capybara.di

import android.content.Context
import com.petapp.capybara.calendar.presentation.CalendarFragment
import com.petapp.capybara.di.module.DatabaseModule
import com.petapp.capybara.di.module.NavigationModule
import com.petapp.capybara.di.module.RepositoryModule
import com.petapp.capybara.di.module.ViewModelModule
import com.petapp.capybara.main.AppActivity
import com.petapp.capybara.profiles.presentation.profile.ProfileFragment
import com.petapp.capybara.profiles.presentation.profiles.ProfilesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, DatabaseModule::class, ViewModelModule::class, NavigationModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(fragment: ProfileFragment)
    fun inject(fragment: ProfilesFragment)
    fun inject(fragment: CalendarFragment)
    fun inject(activity: AppActivity)
}