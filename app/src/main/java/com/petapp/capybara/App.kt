package com.petapp.capybara

import android.app.Application
import com.facebook.stetho.Stetho
import com.petapp.capybara.di.app.AppComponentHolder
import com.petapp.capybara.di.app.DaggerAppComponent
import com.petapp.capybara.di.core.CoreComponentHolder
import com.petapp.capybara.di.core.DaggerCoreComponent

class App : Application() {

    companion object {
        val appComponentHolder = AppComponentHolder
        val coreComponentHolder = CoreComponentHolder
    }

    override fun onCreate() {
        super.onCreate()
        initComponents()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initComponents() {
        appComponentHolder.appComponent = DaggerAppComponent
            .builder()
            .bindApplication(this)
            .build()

        coreComponentHolder.coreComponent = DaggerCoreComponent
            .builder()
            .bindAppComponent(appComponentHolder.appComponent)
            .build()

        appComponentHolder.appComponent.inject(this)
    }
}
