package com.petapp.capybara.calendar.di

import com.petapp.capybara.calendar.presentation.CalendarVm
import com.petapp.capybara.core.di.BaseComponent
import com.petapp.capybara.core.di.CoreComponent
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [CalendarModule::class]
)
@CalendarScope
interface CalendarComponent : BaseComponent {

    @Component.Builder
    interface Builder {
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        fun build(): CalendarComponent
    }

    fun provideViewModel(): CalendarVm
}
