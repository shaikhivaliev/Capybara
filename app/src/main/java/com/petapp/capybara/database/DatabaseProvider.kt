package com.petapp.capybara.database

import android.content.Context
import androidx.room.Room

class DatabaseProvider(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "database")
        .fallbackToDestructiveMigration()
        .build()

    fun appDao() = database.appDao()
}