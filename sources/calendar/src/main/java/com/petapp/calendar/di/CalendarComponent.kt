package com.petapp.calendar.di

import com.petapp.calendar.presentation.CalendarFragment
import com.petapp.core.CoreProvidersFactory
import com.petapp.core_api.ProvidersFacade
import com.petapp.core_api.viewmodel.ViewModelsProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [CalendarModule::class],
    dependencies = [ProvidersFacade::class, ViewModelsProvider::class]
)
interface CalendarComponent: ViewModelsProvider {

    companion object {

        fun create(providersFacade: ProvidersFacade): CalendarComponent {
            return DaggerCalendarComponent
                .builder()
                .providersFacade(providersFacade)
                .viewModelsProvider(CoreProvidersFactory.createViewModelBuilder())
                .build()
        }
    }

    fun inject(fragment: CalendarFragment)
}