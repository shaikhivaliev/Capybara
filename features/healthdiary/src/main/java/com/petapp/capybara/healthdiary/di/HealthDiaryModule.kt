package com.petapp.capybara.healthdiary.di

import com.petapp.capybara.core.data.repository.HealthDiaryRepository
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.healthdiary.presentation.HealthDiaryVm
import dagger.Module
import dagger.Provides

@Module
class HealthDiaryModule {

    @Provides
    @HealthDiaryScope
    fun provideHealthDiaryVmFactory(
        healthDiaryRepository: HealthDiaryRepository,
        profileRepository: ProfileRepository
    ): HealthDiaryVm = HealthDiaryVm(healthDiaryRepository, profileRepository)
}
