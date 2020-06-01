package com.petapp.core_impl

import android.content.Context
import androidx.room.Room
import com.petapp.core_api.database.AppDao
import com.petapp.core_api.database.DatabaseContract
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
class DatabaseModule {

    companion object {
        const val DATABASE_NAME = "database"
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): DatabaseContract {
        return Room.databaseBuilder(
            context,
            com.petapp.core_impl.AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }


    @Provides
    @Reusable
    fun provideDao(dbCreate: DatabaseContract): AppDao {
        return dbCreate.appDao()
    }
}

