package com.petapp.capybara.di.app

import android.app.Application
import android.content.Context
import com.petapp.capybara.core.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        AndroidInjectionModule::class
    ]
)
@Singleton
interface AppComponent: AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindApplication(application: Application): Builder
        fun build(): AppComponent
    }

    fun provideAppContext(): Context
}
