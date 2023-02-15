package com.petapp.capybara.calendar.di

import com.petapp.capybara.core.di.CoreComponentHolder

object CalendarComponentHolder {
    var componentCalendar: CalendarComponent? = null

    fun getComponent(): CalendarComponent? {
        if (componentCalendar == null) {
            componentCalendar = DaggerCalendarComponent
                .builder()
                .bindCoreComponent(CoreComponentHolder.coreComponent)
                .build()
        }
        return componentCalendar
    }

    fun clearComponent() {
        componentCalendar = null
    }
}
