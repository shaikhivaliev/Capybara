package com.petapp.capybara

import android.app.Application
import com.facebook.stetho.Stetho
import com.petapp.core_api.AppWithFacade
import com.petapp.core_api.ProvidersFacade

class App : Application(), AppWithFacade {

    companion object {
        private var facadeComponent: FacadeComponent? = null
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    override fun getFacade(): ProvidersFacade {
        return facadeComponent ?: FacadeComponent.init(this).also {
            facadeComponent = it
        }
    }
}