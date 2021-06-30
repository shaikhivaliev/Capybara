package com.petapp.capybara.di.features

import com.petapp.capybara.data.*
import com.petapp.capybara.database.AppDao
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @FeaturesScope
    fun provideProfileRepository(appDao: AppDao): IProfileRepository = ProfileRepository(appDao)

    @Provides
    @FeaturesScope
    fun provideTypesRepository(appDao: AppDao): ITypesRepository = TypesRepository(appDao)

    @Provides
    @FeaturesScope
    fun provideSurveysRepository(appDao: AppDao): ISurveysRepository = SurveysRepository(appDao)

    @Provides
    @FeaturesScope
    fun provideHealthDiaryRepository(appDao: AppDao): IHealthDiaryRepository = HealthDiaryRepository(appDao)

}
