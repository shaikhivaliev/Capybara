package com.petapp.capybara.calendar.di

import com.petapp.capybara.calendar.presentation.CalendarVm
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.data.repository.SurveysRepository
import dagger.Module
import dagger.Provides

@Module
class CalendarModule {

    @Provides
    @CalendarScope
    fun provideCalendarVm(
        profileRepository: ProfileRepository,
        surveysRepository: SurveysRepository
    ): CalendarVm = CalendarVm(profileRepository, surveysRepository)
}
