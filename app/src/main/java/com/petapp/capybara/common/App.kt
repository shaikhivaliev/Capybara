package com.petapp.capybara.common

import android.app.Application
import com.facebook.stetho.Stetho
import com.petapp.capybara.BuildConfig
import com.petapp.capybara.di.AppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        appComponent = DaggerAppComponent
            .factory()
            .create(this)

    }
}