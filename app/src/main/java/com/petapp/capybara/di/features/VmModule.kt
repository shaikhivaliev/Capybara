package com.petapp.capybara.di.features

import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.data.IHealthDiaryRepository
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.ISurveysRepository
import com.petapp.capybara.data.ITypesRepository
import com.petapp.capybara.presentation.calendar.CalendarVmFactory
import com.petapp.capybara.presentation.healthDiary.HealthDiaryVmFactory
import com.petapp.capybara.presentation.profile.ProfileVmFactory
import com.petapp.capybara.presentation.profiles.ProfilesVmFactory
import com.petapp.capybara.presentation.survey.SurveyVmFactory
import com.petapp.capybara.presentation.surveys.SurveysVmFactory
import com.petapp.capybara.presentation.types.TypesVmFactory
import dagger.Module
import dagger.Provides

@Module
class VmModule {

    @Provides
    @FeaturesScope
    fun provideCalendarVmFactory(
        mainNavigator: IMainNavigator,
        profileRepository: IProfileRepository,
        surveysRepository: ISurveysRepository
    ): CalendarVmFactory = CalendarVmFactory(mainNavigator, profileRepository, surveysRepository)

    @Provides
    @FeaturesScope
    fun provideHealthDiaryVmFactory(
        mainNavigator: IMainNavigator,
        healthDiaryRepository: IHealthDiaryRepository,
        profileRepository: IProfileRepository
    ): HealthDiaryVmFactory = HealthDiaryVmFactory(mainNavigator, healthDiaryRepository, profileRepository)

    @Provides
    @FeaturesScope
    fun provideProfileVmFactory(
        mainNavigator: IMainNavigator,
        profileRepository: IProfileRepository,
        healthDiaryRepository: IHealthDiaryRepository
    ): ProfileVmFactory = ProfileVmFactory(mainNavigator, profileRepository, healthDiaryRepository)

    @Provides
    @FeaturesScope
    fun provideProfilesVmFactory(
        mainNavigator: IMainNavigator,
        profileRepository: IProfileRepository
    ): ProfilesVmFactory = ProfilesVmFactory(mainNavigator, profileRepository)

    @Provides
    @FeaturesScope
    fun provideSurveyVmFactory(
        mainNavigator: IMainNavigator,
        surveysRepository: ISurveysRepository,
        typesRepository: ITypesRepository,
        profileRepository: IProfileRepository
    ): SurveyVmFactory = SurveyVmFactory(mainNavigator, surveysRepository, typesRepository, profileRepository)

    @Provides
    @FeaturesScope
    fun provideSurveysVmFactory(
        mainNavigator: IMainNavigator,
        surveysRepository: ISurveysRepository,
        profileRepository: IProfileRepository
    ): SurveysVmFactory = SurveysVmFactory(mainNavigator, surveysRepository, profileRepository)

    @Provides
    @FeaturesScope
    fun provideTypesVmFactory(
        mainNavigator: IMainNavigator,
        typesRepository: ITypesRepository
    ): TypesVmFactory = TypesVmFactory(mainNavigator, typesRepository)
}
