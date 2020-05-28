package com.petapp.calendar.di

import android.widget.CalendarView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petapp.calendar.data.CalendarDataRepository
import com.petapp.calendar.data.CalendarEntityMapper
import com.petapp.calendar.presentation.CalendarViewModel
import com.petapp.capybara.calendar.domain.CalendarRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [CalendarModule.RepositoryBinds::class, CalendarModule.ViewModelsBinds::class])
class CalendarModule {


    @Singleton
    @Provides
    fun providersCalendarMapper() = CalendarEntityMapper()

    @Module
    interface RepositoryBinds {
        @Binds
        fun bindsCalendarRepository(repository: CalendarDataRepository): CalendarRepository
    }

    @Module
    interface ViewModelsBinds {
        @Binds
        @IntoMap
        @ViewModelKey(CalendarViewModel::class)
        fun bindMainViewModel(viewModel: CalendarViewModel): ViewModel

        @Binds
        fun bindFactory(factory: ViewModelFactory): ViewModelProvider.Factory
    }

}

