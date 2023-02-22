package com.petapp.capybara.profile.di

import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.profile.ProfileVm
import com.petapp.capybara.profile.presentation.ProfilesVm
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    @ProfileScope
    fun provideProfileVmFactory(
        profileRepository: ProfileRepository,
    ): ProfileVm = ProfileVm(profileRepository)

    @Provides
    @ProfileScope
    fun provideProfilesVmFactory(
        profileRepository: ProfileRepository,
    ): ProfilesVm = ProfilesVm(profileRepository)
}
