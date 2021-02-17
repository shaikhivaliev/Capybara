package com.petapp.capybara.common

import android.app.Application
import com.facebook.stetho.Stetho
import com.petapp.capybara.BuildConfig
import com.petapp.capybara.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule))
        }
    }
}
