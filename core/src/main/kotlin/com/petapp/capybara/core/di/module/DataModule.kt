package com.petapp.capybara.core.di.module

import com.petapp.capybara.core.data.repository.*
import com.petapp.capybara.core.database.AppDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideProfileRepository(appDao: AppDao): ProfileRepository =
        ProfileRepositoryImpl(appDao)

    @Provides
    @Singleton
    fun provideTypesRepository(appDao: AppDao): TypesRepository =
        TypesRepositoryImpl(appDao)

    @Provides
    @Singleton
    fun provideSurveysRepository(appDao: AppDao): SurveysRepository =
        SurveysRepositoryImpl(appDao)

    @Provides
    @Singleton
    fun provideHealthDiaryRepository(appDao: AppDao): HealthDiaryRepository =
        HealthDiaryRepositoryImpl(appDao)
}
