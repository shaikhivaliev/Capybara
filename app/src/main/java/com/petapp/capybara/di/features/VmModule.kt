package com.petapp.capybara.di.features

import com.petapp.capybara.core.data.repository.HealthDiaryRepository
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.data.repository.SurveysRepository
import com.petapp.capybara.core.data.repository.TypesRepository
import com.petapp.capybara.navigation.MainNavigatorImpl
import com.petapp.capybara.presentation.calendar.CalendarVmFactory
import com.petapp.capybara.presentation.healthDiary.HealthDiaryVmFactory
import com.petapp.capybara.presentation.profile.ProfileVmFactory
import com.petapp.capybara.presentation.profiles.ProfilesVmFactory
import com.petapp.capybara.presentation.survey.SurveyVmFactory
import com.petapp.capybara.presentation.surveys.SurveysVmFactory
import com.petapp.capybara.presentation.types.TypesStore
import com.petapp.capybara.presentation.types.TypesVmFactory
import dagger.Module
import dagger.Provides

@Module
class VmModule {

    @Provides
    @FeaturesScope
    fun provideCalendarVmFactory(
        mainNavigator: MainNavigatorImpl,
        profileRepository: ProfileRepository,
        surveysRepository: SurveysRepository
    ): CalendarVmFactory = CalendarVmFactory(mainNavigator, profileRepository, surveysRepository)

    @Provides
    @FeaturesScope
    fun provideHealthDiaryVmFactory(
        mainNavigator: MainNavigatorImpl,
        healthDiaryRepository: HealthDiaryRepository,
        profileRepository: ProfileRepository
    ): HealthDiaryVmFactory = HealthDiaryVmFactory(mainNavigator, healthDiaryRepository, profileRepository)

    @Provides
    @FeaturesScope
    fun provideProfileVmFactory(
        mainNavigator: MainNavigatorImpl,
        profileRepository: ProfileRepository
    ): ProfileVmFactory = ProfileVmFactory(mainNavigator, profileRepository)

    @Provides
    @FeaturesScope
    fun provideProfilesVmFactory(
        mainNavigator: MainNavigatorImpl,
        profileRepository: ProfileRepository
    ): ProfilesVmFactory = ProfilesVmFactory(mainNavigator, profileRepository)

    @Provides
    @FeaturesScope
    fun provideSurveyVmFactory(
        mainNavigator: MainNavigatorImpl,
        surveysRepository: SurveysRepository,
        typesRepository: TypesRepository,
        profileRepository: ProfileRepository
    ): SurveyVmFactory = SurveyVmFactory(mainNavigator, surveysRepository, typesRepository, profileRepository)

    @Provides
    @FeaturesScope
    fun provideSurveysVmFactory(
        mainNavigator: MainNavigatorImpl,
        surveysRepository: SurveysRepository,
        profileRepository: ProfileRepository
    ): SurveysVmFactory = SurveysVmFactory(mainNavigator, surveysRepository, profileRepository)

    @Provides
    @FeaturesScope
    fun provideTypesVmFactory(
        mainNavigator: MainNavigatorImpl,
        typesRepository: TypesRepository,
        store: TypesStore
    ): TypesVmFactory = TypesVmFactory(mainNavigator, typesRepository, store)
}
