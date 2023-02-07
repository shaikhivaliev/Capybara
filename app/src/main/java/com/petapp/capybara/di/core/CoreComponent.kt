package com.petapp.capybara.di.core

import com.petapp.capybara.database.AppDao
import com.petapp.capybara.di.app.AppComponent
import dagger.Component

@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        DatabaseModule::class
    ]
)
@CoreScope
interface CoreComponent {

    @Component.Builder
    interface Builder {
        fun bindAppComponent(appComponent: AppComponent): Builder
        fun build(): CoreComponent
    }

    fun provideAppDao(): AppDao
}
