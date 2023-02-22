package com.petapp.capybara.healthdiary.di

import com.petapp.capybara.core.di.BaseComponent
import com.petapp.capybara.core.di.CoreComponent
import com.petapp.capybara.healthdiary.presentation.HealthDiaryVm
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [HealthDiaryModule::class]
)
@HealthDiaryScope
interface HealthDiaryComponent : BaseComponent {

    @Component.Builder
    interface Builder {
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        fun build(): HealthDiaryComponent
    }

    fun provideViewModel(): HealthDiaryVm
}
