package com.petapp.capybara.core.di

import android.app.Application
import android.content.Context
import com.petapp.capybara.core.data.repository.HealthDiaryRepository
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.data.repository.SurveysRepository
import com.petapp.capybara.core.data.repository.TypesRepository
import com.petapp.capybara.core.database.AppDao
import com.petapp.capybara.core.di.module.AppModule
import com.petapp.capybara.core.di.module.DataModule
import com.petapp.capybara.core.di.module.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [AppModule::class, DatabaseModule::class, DataModule::class]
)
@Singleton
interface CoreComponent: BaseComponent {

    @Component.Builder
    interface Builder {
        fun build(): CoreComponent

        @BindsInstance
        fun bindApplication(application: Application): Builder
    }

    fun provideAppContext(): Context
    fun provideAppDao(): AppDao
    fun provideProfileRepository(): ProfileRepository
    fun provideTypesRepository(): TypesRepository
    fun provideSurveysRepository(): SurveysRepository
    fun provideHealthDiaryRepository(): HealthDiaryRepository
}
