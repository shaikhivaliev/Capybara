package com.petapp.capybara.di.features

import com.petapp.capybara.di.app.AppComponentHolder
import com.petapp.capybara.di.core.CoreComponentHolder
import com.petapp.capybara.presentation.main.MainActivity

object FeaturesComponentHolder {
    var featuresComponent: FeaturesComponent? = null

    fun getComponent(mainActivity: MainActivity): FeaturesComponent? {
        if (featuresComponent == null) {
            featuresComponent = DaggerFeaturesComponent
                .builder()
                .bindAppComponent(AppComponentHolder.appComponent)
                .bindCoreComponent(CoreComponentHolder.coreComponent)
                .bindMainActivity(mainActivity)
                .build()
        }
        return featuresComponent
    }

    fun clearComponent() {
        featuresComponent = null
    }
}
