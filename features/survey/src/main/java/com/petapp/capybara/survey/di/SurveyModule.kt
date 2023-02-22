package com.petapp.capybara.survey.di

import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.data.repository.SurveysRepository
import com.petapp.capybara.core.data.repository.TypesRepository
import com.petapp.capybara.survey.presentation.SurveyVm
import com.petapp.capybara.survey.presentation.SurveysVm
import dagger.Module
import dagger.Provides

@Module
class SurveyModule {

    @Provides
    @SurveyScope
    fun provideSurveyVmFactory(
        surveysRepository: SurveysRepository,
        typesRepository: TypesRepository,
        profileRepository: ProfileRepository
    ): SurveyVm = SurveyVm(surveysRepository, typesRepository, profileRepository)

    @Provides
    @SurveyScope
    fun provideSurveysVmFactory(
        surveysRepository: SurveysRepository,
        profileRepository: ProfileRepository
    ): SurveysVm = SurveysVm(surveysRepository, profileRepository)

}
