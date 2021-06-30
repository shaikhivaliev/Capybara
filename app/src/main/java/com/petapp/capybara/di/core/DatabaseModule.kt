package com.petapp.capybara.di.core

import android.content.Context
import com.petapp.capybara.database.AppDao
import com.petapp.capybara.database.DatabaseProvider
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @CoreScope
    fun provideDatabaseProvider(context: Context): DatabaseProvider = DatabaseProvider(context)

    @Provides
    @CoreScope
    fun provideAppDao(databaseProvider: DatabaseProvider): AppDao = databaseProvider.appDao()
}
