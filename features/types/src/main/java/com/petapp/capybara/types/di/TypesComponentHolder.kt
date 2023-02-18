package com.petapp.capybara.types.di

import com.petapp.capybara.core.di.BaseComponentHolder
import com.petapp.capybara.core.di.CoreComponentHolder

object TypesComponentHolder : BaseComponentHolder<TypesComponent>() {
    override val component: TypesComponent
        get() = _component ?: DaggerTypesComponent
            .builder()
            .bindCoreComponent(CoreComponentHolder.component)
            .build()
            .also { commonComponent ->
                _component = commonComponent
            }
}
