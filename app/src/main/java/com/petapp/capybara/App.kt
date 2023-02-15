package com.petapp.capybara

import android.app.Application
import com.facebook.stetho.Stetho
import com.petapp.capybara.core.di.CoreComponentHolder
import com.petapp.capybara.core.di.DaggerCoreComponent

class App : Application() {

    companion object {
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
        coreComponentHolder.coreComponent = DaggerCoreComponent
            .builder()
            .bindApplication(this)
            .build()
    }
}
