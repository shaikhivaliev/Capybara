package com.petapp.capybara.survey.di

import com.petapp.capybara.core.di.CoreComponent
import com.petapp.capybara.survey.SurveyVm
import com.petapp.capybara.survey.SurveysVm
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [SurveyModule::class]
)
@SurveyScope
interface SurveyComponent {

    @Component.Builder
    interface Builder {
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        fun build(): SurveyComponent
    }

    fun provideSurveyVm(): SurveyVm
    fun provideSurveysVm(): SurveysVm
}
