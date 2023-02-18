package com.petapp.capybara.calendar.di

import com.petapp.capybara.core.di.BaseComponentHolder
import com.petapp.capybara.core.di.CoreComponentHolder

object CalendarComponentHolder : BaseComponentHolder<CalendarComponent>() {
    override val component: CalendarComponent
        get() = _component ?: DaggerCalendarComponent
            .builder()
            .bindCoreComponent(CoreComponentHolder.component)
            .build()
            .also { calendarComponent ->
                _component = calendarComponent
            }
}
