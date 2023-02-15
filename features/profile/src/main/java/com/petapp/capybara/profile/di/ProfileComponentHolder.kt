package com.petapp.capybara.profile.di

import com.petapp.capybara.core.di.CoreComponentHolder

object ProfileComponentHolder {
    var componentProfile: ProfileComponent? = null

    fun getComponent(): ProfileComponent? {
        if (componentProfile == null) {
            componentProfile = DaggerProfileComponent
                .builder()
                .bindCoreComponent(CoreComponentHolder.coreComponent)
                .build()
        }
        return componentProfile
    }

    fun clearComponent() {
        componentProfile = null
    }
}
