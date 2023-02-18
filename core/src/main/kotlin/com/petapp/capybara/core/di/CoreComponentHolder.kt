package com.petapp.capybara.core.di

import android.app.Application

object CoreComponentHolder : BaseComponentHolder<CoreComponent>() {
    override val component: CoreComponent
        get() = _component ?: throw IllegalStateException("CoreComponent must be initialized")

    fun initComponent(
        application: Application
    ): CoreComponent {
        return _component ?: DaggerCoreComponent
            .builder()
            .bindApplication(application)
            .build()
            .also { commonComponent ->
                _component = commonComponent
            }
    }
}
