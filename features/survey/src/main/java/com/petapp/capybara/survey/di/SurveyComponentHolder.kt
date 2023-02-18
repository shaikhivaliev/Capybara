package com.petapp.capybara.survey.di

import com.petapp.capybara.core.di.BaseComponentHolder
import com.petapp.capybara.core.di.CoreComponentHolder

object SurveyComponentHolder : BaseComponentHolder<SurveyComponent>() {
    override val component: SurveyComponent
        get() = _component ?: DaggerSurveyComponent
            .builder()
            .bindCoreComponent(CoreComponentHolder.component)
            .build()
            .also { commonComponent ->
                _component = commonComponent
            }
}
