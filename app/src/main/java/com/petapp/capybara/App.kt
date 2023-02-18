package com.petapp.capybara

import android.app.Application
import com.facebook.stetho.Stetho
import com.petapp.capybara.core.di.CoreComponentHolder

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initComponents()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initComponents() {
        CoreComponentHolder.initComponent(
            application = this
        )
    }
}
