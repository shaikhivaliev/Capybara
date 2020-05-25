package com.petapp.capybara.di.module

import android.content.Context
import com.petapp.capybara.database.AppDao
import com.petapp.capybara.database.DatabaseProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): DatabaseProvider {
        return DatabaseProvider(context)
    }

    @Singleton
    @Provides
    fun provideDao(dbProvider: DatabaseProvider): AppDao {
        return dbProvider.appDao()
    }

}