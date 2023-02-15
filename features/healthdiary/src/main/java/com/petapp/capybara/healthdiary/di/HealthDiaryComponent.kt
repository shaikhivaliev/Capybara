package com.petapp.capybara.healthdiary.di

import com.petapp.capybara.core.di.CoreComponent
import com.petapp.capybara.healthdiary.HealthDiaryVm
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [HealthDiaryModule::class]
)
@HealthDiaryScope
interface HealthDiaryComponent {

    @Component.Builder
    interface Builder {
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        fun build(): HealthDiaryComponent
    }

    fun provideViewModel(): HealthDiaryVm
}
