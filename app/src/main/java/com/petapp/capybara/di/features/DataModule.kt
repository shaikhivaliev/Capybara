package com.petapp.capybara.di.features

import com.petapp.capybara.core.data.repository.*
import com.petapp.capybara.core.database.AppDao
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @FeaturesScope
    fun provideProfileRepository(appDao: AppDao): ProfileRepository =
        ProfileRepositoryImpl(appDao)

    @Provides
    @FeaturesScope
    fun provideTypesRepository(appDao: AppDao): TypesRepository =
        TypesRepositoryImpl(appDao)

    @Provides
    @FeaturesScope
    fun provideSurveysRepository(appDao: AppDao): SurveysRepository =
        SurveysRepositoryImpl(appDao)

    @Provides
    @FeaturesScope
    fun provideHealthDiaryRepository(appDao: AppDao): HealthDiaryRepository =
        HealthDiaryRepositoryImpl(appDao)
}
