package com.petapp.capybara.types.di

import com.petapp.capybara.core.di.CoreComponentHolder

object TypesComponentHolder {
    var componentTypes: TypesComponent? = null

    fun getComponent(): TypesComponent? {
        if (componentTypes == null) {
            componentTypes = DaggerTypesComponent
                .builder()
                .bindCoreComponent(CoreComponentHolder.coreComponent)
                .build()
        }
        return componentTypes
    }

    fun clearComponent() {
        componentTypes = null
    }
}
