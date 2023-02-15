package com.petapp.capybara.healthdiary.di

import com.petapp.capybara.core.di.CoreComponentHolder

object HealthDiaryComponentHolder {
    var componentHealthDiary: HealthDiaryComponent? = null

    fun getComponent(): HealthDiaryComponent? {
        if (componentHealthDiary == null) {
            componentHealthDiary = DaggerHealthDiaryComponent
                .builder()
                .bindCoreComponent(CoreComponentHolder.coreComponent)
                .build()
        }
        return componentHealthDiary
    }

    fun clearComponent() {
        componentHealthDiary = null
    }
}
