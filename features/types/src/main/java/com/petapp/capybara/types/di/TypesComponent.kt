package com.petapp.capybara.types.di

import com.petapp.capybara.core.di.BaseComponent
import com.petapp.capybara.core.di.CoreComponent
import com.petapp.capybara.types.presentation.TypesVm
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [TypesModule::class, StoreModule::class]
)
@TypesScope
interface TypesComponent : BaseComponent {

    @Component.Builder
    interface Builder {
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        fun build(): TypesComponent
    }

    fun provideTypesVm(): TypesVm
}
