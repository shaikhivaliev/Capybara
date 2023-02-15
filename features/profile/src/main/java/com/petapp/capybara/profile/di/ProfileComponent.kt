package com.petapp.capybara.profile.di

import com.petapp.capybara.core.di.CoreComponent
import com.petapp.capybara.profile.ProfileVm
import com.petapp.capybara.profile.ProfilesVm
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [ProfileModule::class]
)
@ProfileScope
interface ProfileComponent {

    @Component.Builder
    interface Builder {
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        fun build(): ProfileComponent
    }

    fun provideProfilesVm(): ProfilesVm
    fun provideProfileVm(): ProfileVm
}
