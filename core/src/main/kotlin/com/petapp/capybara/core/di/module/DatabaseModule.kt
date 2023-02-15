package com.petapp.capybara.core.di.module

import android.content.Context
import com.petapp.capybara.core.database.AppDao
import com.petapp.capybara.core.database.DatabaseProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseProvider(context: Context): DatabaseProvider = DatabaseProvider(context)

    @Provides
    @Singleton
    fun provideAppDao(databaseProvider: DatabaseProvider): AppDao = databaseProvider.appDao()
}
