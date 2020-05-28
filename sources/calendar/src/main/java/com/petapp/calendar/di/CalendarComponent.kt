package com.petapp.calendar.di

import com.petapp.calendar.presentation.CalendarFragment
import com.petapp.core_api.ProvidersFacade
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [CalendarModule::class],
    dependencies = [ProvidersFacade::class]
)
interface CalendarComponent {

    companion object {

        fun create(providersFacade: ProvidersFacade): CalendarComponent {
            return DaggerCalendarComponent
                .builder()
                .providersFacade(providersFacade)
                .build()
        }
    }

    fun inject(fragment: CalendarFragment)
}