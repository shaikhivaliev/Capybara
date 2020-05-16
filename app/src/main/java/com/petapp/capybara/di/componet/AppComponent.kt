package com.petapp.capybara.di.componet

import android.content.Context
import androidx.fragment.app.Fragment
import com.petapp.capybara.calendar.CalendarFragment
import com.petapp.capybara.di.module.DatabaseModule
import com.petapp.capybara.di.module.RepositoryModule
import com.petapp.capybara.di.module.ViewModelModule
import com.petapp.capybara.profiles.presentation.ProfilesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, DatabaseModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(fragment: ProfilesFragment)
}