package com.petapp.capybara.profile.di

import com.petapp.capybara.core.di.BaseComponent
import com.petapp.capybara.core.di.CoreComponent
import com.petapp.capybara.profile.presentation.ProfileVm
import com.petapp.capybara.profile.presentation.ProfilesVm
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [ProfileModule::class]
)
@ProfileScope
interface ProfileComponent : BaseComponent {

    @Component.Builder
    interface Builder {
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        fun build(): ProfileComponent
    }

    fun provideProfilesVm(): ProfilesVm
    fun provideProfileVm(): ProfileVm
}