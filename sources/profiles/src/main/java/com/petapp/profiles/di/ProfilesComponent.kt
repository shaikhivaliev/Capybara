package com.petapp.profiles.di

import com.petapp.core_api.ProvidersFacade
import com.petapp.profiles.presentation.profile.ProfileFragment
import com.petapp.profiles.presentation.profiles.ProfilesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ProfilesModule::class],
    dependencies = [ProvidersFacade::class]
)
interface ProfilesComponent {

    companion object {

        fun create(providersFacade: ProvidersFacade): ProfilesComponent {
            return DaggerProfilesComponent
                .builder()
                .providersFacade(providersFacade)
                .build()
        }
    }

    fun inject(fragment: ProfileFragment)
    fun inject(fragment: ProfilesFragment)

}