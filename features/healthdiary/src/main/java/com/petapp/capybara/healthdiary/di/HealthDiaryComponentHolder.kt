package com.petapp.capybara.healthdiary.di

import com.petapp.capybara.core.di.BaseComponentHolder
import com.petapp.capybara.core.di.CoreComponentHolder

object HealthDiaryComponentHolder : BaseComponentHolder<HealthDiaryComponent>() {
    override val component: HealthDiaryComponent
        get() = _component ?: DaggerHealthDiaryComponent
            .builder()
            .bindCoreComponent(CoreComponentHolder.component)
            .build()
            .also { calendarComponent ->
                _component = calendarComponent
            }
}
