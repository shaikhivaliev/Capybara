package com.petapp.capybara.survey.di

import com.petapp.capybara.core.di.CoreComponentHolder

object SurveyComponentHolder {
    var componentSurvey: SurveyComponent? = null

    fun getComponent(): SurveyComponent? {
        if (componentSurvey == null) {
            componentSurvey = DaggerSurveyComponent
                .builder()
                .bindCoreComponent(CoreComponentHolder.coreComponent)
                .build()
        }
        return componentSurvey
    }

    fun clearComponent() {
        componentSurvey = null
    }
}
