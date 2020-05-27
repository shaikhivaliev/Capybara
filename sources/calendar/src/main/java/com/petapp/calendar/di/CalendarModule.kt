package com.petapp.calendar.di

import androidx.lifecycle.ViewModel
import com.petapp.calendar.data.CalendarDataRepository
import com.petapp.calendar.data.CalendarEntityMapper
import com.petapp.calendar.presentation.CalendarViewModel
import com.petapp.capybara.calendar.domain.CalendarRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CalendarModule.RepositoryBinds::class])
class CalendarModule {

    @Provides
    @Singleton
    fun provideAppViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
        repository: CalendarRepository
    ): ViewModel = CalendarViewModel(repository).also {
        map[CalendarViewModel::class.java] = it
    }


    @Singleton
    @Provides
    fun providersCalendarMapper() = CalendarEntityMapper()

    @Module
    interface RepositoryBinds {
        @Binds
        fun bindsCalendarRepository(repository: CalendarDataRepository): CalendarRepository
    }

}

